package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class shopInfoCustomer extends Fragment {

    View rootView; String sid; //RelativeLayout load;
    TextView Owner,address,category,phone,email,open,close,payment,delivery,type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_shop_info_customer,container,false);

        Owner = (TextView)rootView.findViewById(R.id.owner);
        address = (TextView)rootView.findViewById(R.id.address);
        category = (TextView)rootView.findViewById(R.id.category);
        phone = (TextView)rootView.findViewById(R.id.phone);
        email = (TextView)rootView.findViewById(R.id.email);
        open = (TextView)rootView.findViewById(R.id.open);
        close = (TextView)rootView.findViewById(R.id.close);
        payment = (TextView)rootView.findViewById(R.id.payment);
        delivery = (TextView)rootView.findViewById(R.id.delivery);
        type = (TextView)rootView.findViewById(R.id.shop_type);
        //load = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("Customer_Shop_Profile", Context.MODE_PRIVATE);
        sid = sharedpreferences.getString("ShopProfile", "");

        getRetailer(sid);
        return rootView;
    }

    public static final String JSON_URL1 = "http://www.vendoee.com/android-scripts/getRetailer.php";

    private void getRetailer(final String sid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //load.setVisibility(View.GONE);
                        StringTokenizer st = new StringTokenizer(response,";");
                        String[] str = new String[st.countTokens()];
                        int i =0;

                        while (st.hasMoreTokens()) {
                            str[i++]=st.nextToken();
                        }

                        String mobile = str[4]; String oname = str[2];  String emil = str[3]; String adress=str[5];
                        String ctegory=str[8]; String opn = str[9]; String clse = str[10];

                        Owner.setText(oname); address.setText(adress); category.setText(ctegory.substring(0,ctegory.length()-2));
                        phone.setText(mobile); email.setText(emil); open.setText(opn); close.setText(clse);

                        if(str[11].equals("0")){
                            payment.setText("Cash Only");
                        }else if(str[11].equals("1")){
                            payment.setText("Cash and Cashless");
                        }

                        if(str[12].equals("0")){
                            delivery.setText("Not Available");
                        }else if(str[12].equals("1")){
                            delivery.setText("Available");
                        }

                        if(str[13].equals("0")){
                            type.setText("Store");
                        }else if(str[13].equals("1")){
                            type.setText("Brand");
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //load.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(),"Error: "+ error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
