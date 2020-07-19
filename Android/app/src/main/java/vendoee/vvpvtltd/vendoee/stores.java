package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


public class stores extends Fragment {
    View rootView;
    GPSTracker gps;
    String parsedDistance;
    TextView notify;
    private RecyclerView recyclerView; String state;
    private RetailersToCustomerAdapter adapter; DatabaseHelper myDB; Cursor res;
    private List<RetailersToCustomer> retailersToCustomerList; RetailersToCustomerAdapter shop;
    String maxid = "0" ;    RelativeLayout load; int t = 0;
    ShopBrandLayout sp;     ProgressDialog loading; private boolean loadin;
    private boolean checkForNext = true;
    Vector vs = new Vector(13, 5);

    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible){
            t = 1;
        }else{
            t = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_stores,container,false);

        gps = new GPSTracker(rootView.getContext());
        retailersToCustomerList=new ArrayList<>();
        notify = (TextView)rootView.findViewById(R.id.note1S);
        myDB = new DatabaseHelper(rootView.getContext());
        load = (RelativeLayout) rootView.findViewById(R.id.loadingPanelS);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.category_recyclerViewS);
        recyclerView.setHasFixedSize(true);
        loading = new ProgressDialog(rootView.getContext());

        SharedPreferences sharedpreferences1 = getContext().getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state = sharedpreferences1.getString("state", "");

            adapter=new RetailersToCustomerAdapter(rootView.getContext(), retailersToCustomerList);

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(rootView.getContext(), LinearLayout.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            shop =new RetailersToCustomerAdapter();
            // prepareSales();

            SharedPreferences sharedpreferences = rootView.getContext().getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
            String citty = sharedpreferences.getString("Ccity", "");
        //Toast.makeText(rootView.getContext(),"fragment shop "+maxid,Toast.LENGTH_SHORT).show();
        vs.clear();
        SearchShops(citty);

            adapter.notifyDataSetChanged();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getAdapter().getItemCount() >= 3) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (  loadin == false && lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                    {
                        if(checkForNext==true){
                            retailersToCustomerList.add(null);
                            adapter.notifyItemInserted(retailersToCustomerList.size() - 1);
                            loadin = true;
                        }

                        SharedPreferences sharedpreferences = rootView.getContext().getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                        String citty = sharedpreferences.getString("Ccity", "");
                        //Toast.makeText(rootView.getContext(),"fragment shop scroll"+maxid,Toast.LENGTH_SHORT).show();

                        SearchShops1(citty);

                    }
                }
            }

        });


        return rootView;
    }

    private void SearchShops1(final String citty) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getRetailerForCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(checkForNext==true){
                            retailersToCustomerList.remove(retailersToCustomerList.size() - 1);
                        }

                        String result = response;
                        if (result.isEmpty()) {
                            final Toast toast = Toast.makeText(rootView.getContext(), "No more Stores", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 400);
                        } else {
                            StringTokenizer str = new StringTokenizer(response,";");
                            try{
                                if(str.countTokens()>0){
                                    load.setVisibility(View.GONE);
                                    notify.setVisibility(View.INVISIBLE);
                                    while(str.hasMoreElements()){
                                        String str1 = str.nextToken();
                                        StringTokenizer str2 = new StringTokenizer(str1,"||");
                                        String str3[] = new String[str2.countTokens()]; int i=0;
                                        while(str2.hasMoreElements()){
                                            str3[i]=str2.nextToken();
                                            i++;
                                        }

                                        String name = str3[0]; String image = str3[1]; String sellid = str3[2];
                                        String contact = str3[3]; final String lat1 = str3[4]; final String lng1 = str3[5];
                                        String cash = str3[6];

                                        String nextCheck = str3[10];

                                        if(nextCheck.equals("0")){
                                            checkForNext = false;
                                        }else {
                                            checkForNext = true;
                                        }

                                        byte [] encodeByte= Base64.decode(image, Base64.DEFAULT);
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                        final double latitude = gps.getLatitude();
                                        final double longitude = gps.getLongitude();

                                        Thread thread=new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String response;
                                                InputStream in = null;
                                                try {
                                                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(lat1) + "," + Double.parseDouble(lng1) + "&sensor=false&units=metric&mode=driving");
                                                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                    conn.setRequestMethod("POST");
                                                    in = new BufferedInputStream(conn.getInputStream());
                                                    response = IOUtils.toString(in, "UTF-8");

                                                    JSONObject jsonObject = new JSONObject(response);
                                                    JSONArray array = jsonObject.getJSONArray("routes");
                                                    JSONObject routes = array.getJSONObject(0);
                                                    JSONArray legs = routes.getJSONArray("legs");
                                                    JSONObject steps = legs.getJSONObject(0);
                                                    JSONObject distance = steps.getJSONObject("distance");
                                                    parsedDistance=distance.getString("text");

                                                    if(parsedDistance.equals("null")){
                                                        parsedDistance = "NA";
                                                    }

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        thread.start();
                                        try {
                                            thread.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

//                                        Toast.makeText(getActivity(),str3[7],Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(),str3[8],Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(),str3[9],Toast.LENGTH_SHORT).show();
                                        vs.add(sellid);

                                        int c = Collections.frequency(vs,sellid);
                                        if(c==1){
                                            try{

                                                RetailersToCustomerAdapter sale=new RetailersToCustomerAdapter();
                                                RetailersToCustomer a=new RetailersToCustomer(name,bitmap,Integer.parseInt(sellid),contact,str3[7],str3[9],str3[8],parsedDistance);
                                                retailersToCustomerList.add(a);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                        //Toast.makeText(ShowStores.this,str3[0],Toast.LENGTH_SHORT).show();
                                        maxid = sellid;
                                    }
                                }
                            }catch (IllegalStateException e){
                                Log.i("HERE",e.toString());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        loadin = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Cant connect to server. Try again",Toast.LENGTH_SHORT).show();
                        //getActivity().onBackPressed();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",citty);
                params.put("state",state);
                params.put("mid",maxid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(rootView.getContext());
        requestQueue.add(stringRequest);
    }

    private void SearchShops(final String citty) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getRetailerForCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        String result = response;
                        if (result.isEmpty()) {
                            if(recyclerView.getAdapter().getItemCount()==0){
                                notify.setVisibility(View.VISIBLE);
                                load.setVisibility(View.GONE);
                            }
                        } else {
                            StringTokenizer str = new StringTokenizer(response,";");
                            try{
                                if(str.countTokens()>0){
                                    load.setVisibility(View.GONE);
                                    notify.setVisibility(View.INVISIBLE);
                                    while(str.hasMoreElements()){
                                        String str1 = str.nextToken();
                                        StringTokenizer str2 = new StringTokenizer(str1,"||");
                                        String str3[] = new String[str2.countTokens()]; int i=0;
                                        while(str2.hasMoreElements()){
                                            str3[i]=str2.nextToken();
                                            i++;
                                        }

                                        String name = str3[0]; String image = str3[1]; String sellid = str3[2];
                                        String contact = str3[3]; final String lat1 = str3[4]; final String lng1 = str3[5];
                                        String cash = str3[6];

                                        String nextCheck = str3[10];

                                        if(nextCheck.equals("0")){
                                            checkForNext = false;
                                        }else {
                                            checkForNext = true;
                                        }

                                        byte [] encodeByte= Base64.decode(image, Base64.DEFAULT);
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                        final double latitude = gps.getLatitude();
                                        final double longitude = gps.getLongitude();

                                        Thread thread=new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String response;
                                                InputStream in = null;
                                                try {
                                                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(lat1) + "," + Double.parseDouble(lng1) + "&sensor=false&units=metric&mode=driving");
                                                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                    conn.setRequestMethod("POST");
                                                    in = new BufferedInputStream(conn.getInputStream());
                                                    response = IOUtils.toString(in, "UTF-8");

                                                    JSONObject jsonObject = new JSONObject(response);
                                                    JSONArray array = jsonObject.getJSONArray("routes");
                                                    JSONObject routes = array.getJSONObject(0);
                                                    JSONArray legs = routes.getJSONArray("legs");
                                                    JSONObject steps = legs.getJSONObject(0);
                                                    JSONObject distance = steps.getJSONObject("distance");
                                                    parsedDistance=distance.getString("text");

                                                    if(parsedDistance.equals("null")){
                                                        parsedDistance = "NA";
                                                    }

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        thread.start();
                                        try {
                                            thread.join();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

//                                        Toast.makeText(getActivity(),str3[7],Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(),str3[8],Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getActivity(),str3[9],Toast.LENGTH_SHORT).show();
                                        vs.add(sellid);

                                        int c = Collections.frequency(vs,sellid);
                                        if(c==1){
                                            try{

                                                RetailersToCustomerAdapter sale=new RetailersToCustomerAdapter();
                                                RetailersToCustomer a=new RetailersToCustomer(name,bitmap,Integer.parseInt(sellid),contact,str3[7],str3[9],str3[8],parsedDistance);
                                                retailersToCustomerList.add(a);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                        //Toast.makeText(ShowStores.this,str3[0],Toast.LENGTH_SHORT).show();
                                        maxid = sellid;
                                    }
                                }else{
                                    Toast.makeText(getActivity(),"No more stores!",Toast.LENGTH_SHORT).show();
                                }
                            }catch (IllegalStateException e){
                                Log.i("HERE",e.toString());
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Cant connect to server. Try again",Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",citty);
                params.put("state",state);
                params.put("mid",maxid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(rootView.getContext());
        requestQueue.add(stringRequest);
    }
}
