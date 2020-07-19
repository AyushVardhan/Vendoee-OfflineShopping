package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class OffersShopProfileCustomer  extends Fragment {

    Bitmap pimage[]; int pid[]; String pname[]; String salePrice[]; String discount[];int size;


    private OfferShopProfileCustoAdapter adapter;
    private List<OfferShopProfileCustoModel> retailersToCustomerList;

    View rootView; RecyclerView offersListView; String sid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_offers_shop_profile_customer,container,false);
        offersListView=(RecyclerView) rootView.findViewById(R.id.offerslistview);

        retailersToCustomerList=new ArrayList<>();
        adapter=new OfferShopProfileCustoAdapter(rootView.getContext(), retailersToCustomerList);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(rootView.getContext(), LinearLayout.VERTICAL,false);
        offersListView.setLayoutManager(layoutManager);
        offersListView.setItemAnimator(new DefaultItemAnimator());
        offersListView.setAdapter(adapter);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Customer_Shop_Profile", Context.MODE_PRIVATE);
        sid = sharedpreferences.getString("ShopProfile", "");

        sendRequest(sid);
        adapter.notifyDataSetChanged();
        return rootView;
    }

    public static final String JSON_URL = "http://www.vendoee.com/android-scripts/offerRetailVerified.php";

    private void sendRequest(final String id){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        StringTokenizer st=new StringTokenizer(response,";");
                        String str[];

                        size = st.countTokens();
                        pimage = new Bitmap[st.countTokens()];
                        pid = new int[st.countTokens()];
                        pname = new String[st.countTokens()];
                        salePrice=new String[st.countTokens()];
                        discount=new String[st.countTokens()];

                        try{
                            int j = 0;
                            while(st.hasMoreTokens()){
                                int i=0;
                                String item1=st.nextToken();
                                //Log.v("Hells",item1);
                                StringTokenizer st1=new StringTokenizer(item1,"||");
                                str=new String[st1.countTokens()];
                                while (st1.hasMoreTokens()){
                                    str[i++]=st1.nextToken();
                                }

                                byte [] encodeByte= Base64.decode(str[9], Base64.DEFAULT);
                                pimage[j]= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                pid[j] = Integer.parseInt(str[0]);
                                pname[j] = str[1];
                                salePrice[j]=str[5];
                                discount[j]=str[10];

                                OfferShopProfileCustoModel a=new OfferShopProfileCustoModel(str[1],pimage[j],str[0],str[5],str[10],str[6],str[7]);
                                retailersToCustomerList.add(a);
                                j++;
                            }

                            adapter.notifyDataSetChanged();

                        }catch (IllegalStateException e){
                            Log.i("HERE",e.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

}
