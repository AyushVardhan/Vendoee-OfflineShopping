package vendoee.vvpvtltd.vendoee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class handloom extends Fragment {
    private RecyclerView recyclerView;
    private SaleItemsAdapter adapter;
    private List<SaleItems> saleItemsList;
    DatabaseHelper myDB;
    Cursor res;
    TextView notify;
    String city,state;
    boolean isInserted;
    View rootView;
    RelativeLayout load;
    ProgressDialog loading;
    String last_id;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GPSTracker gps; String parsedDistance;
    LinearLayout weak; Button weak1; private boolean loadin;
    private boolean checkForNext = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.activity_handloom,container,false);
        myDB = new DatabaseHelper(getContext());
        load = (RelativeLayout) rootView.findViewById(R.id.loadingPanel);
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view13);
        recyclerView.setHasFixedSize(true);
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        city = sharedpreferences.getString("Ccity", "");
        SharedPreferences sharedpreferences1 = getContext().getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state = sharedpreferences1.getString("state", "");
        gps = new GPSTracker(getContext());
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);

        weak = (LinearLayout)rootView.findViewById(R.id.weakConn);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         myDB.deleteData("Handlooms");

                                                         saleItemsList=new ArrayList<>();
                                                         adapter=new SaleItemsAdapter(rootView.getContext(), saleItemsList);
                                                         RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(rootView.getContext(), LinearLayout.VERTICAL,false);
                                                         recyclerView.setLayoutManager(layoutManager);
                                                         recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                         recyclerView.setAdapter(adapter);
                                                         adapter.notifyDataSetChanged();

                                                         if(recyclerView.getAdapter()!=null){
                                                             if(recyclerView.getAdapter().getItemCount()==0){
                                                                 load.setVisibility(View.VISIBLE);
                                                             }
                                                         }
                                                         weak.setVisibility(View.INVISIBLE);
                                                         loadOffers(city);
                                                         final Toast toast = Toast.makeText(rootView.getContext(), "Reloading..", Toast.LENGTH_SHORT);
                                                         toast.show();

                                                         Handler handler = new Handler();
                                                         handler.postDelayed(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 toast.cancel();
                                                             }
                                                         }, 400);
                                                         checkForNext = true;
                                                         mSwipeRefreshLayout.setRefreshing(false);
                                                     }
                                                 }
        );

        notify = (TextView)rootView.findViewById(R.id.note1);
        res = myDB.getItemDetails("Handlooms");
        int c5 = res.getCount();
        if(res.getCount()>0){
            RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view13);
            //Toast.makeText(getContext(),"Electronis-GotData",Toast.LENGTH_SHORT).show();
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(rootView.getContext(), saleItemsList);

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(rootView.getContext(), LinearLayout.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

            if(!res.moveToFirst()){
                //notify.setVisibility(View.VISIBLE);
            }

            res.moveToPrevious();

            while (res.moveToNext()){
                load.setVisibility(View.GONE);
                //+","+res.getString(1)+","+res.getString(2)+","+res.getString(3)+","+res.getString(4)+","+res.getString(5)+","+res.getString(6)+","+res.getString(7)+","+res.getString(8)+","+res.getString(9)+","+res.getString(10)+","+res.getString(11)
                //Toast.makeText(getContext(),res.getString(0),Toast.LENGTH_LONG).show();
                //Toast.makeText(getContext(),res.getString(2)+","+res.getDouble(6)+","+res.getDouble(5)+","+res.getString(7)+","+res.getString(8)+","+res.getString(10)+","+res.getInt(0)+","+res.getInt(1)+","+res.getString(3)+","+res.getString(4)+","+res.getString(9) ,Toast.LENGTH_SHORT).show();
                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                //Toast.makeText(getContext(),res.getString(11),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getContext(),res.getString(2),Toast.LENGTH_SHORT).show();
                SaleItemsAdapter sale=new SaleItemsAdapter();
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();

                Thread thread=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String response;
                        InputStream in = null;
                        try {
                            URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(res.getString(14)) + "," + Double.parseDouble(res.getString(15)) + "&sensor=false&units=metric&mode=driving");
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

                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                //Toast.makeText(getContext(),cash.toString(),Toast.LENGTH_SHORT).show();
                //SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(13),res.getString(14),res.getString(15));
                // saleItemsList.add(a);

                last_id = res.getString(0);
            }

            adapter.notifyDataSetChanged();
            if(c5==0){
                final Toast toast = Toast.makeText(rootView.getContext(), "No more sales!", Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 400);
            }
        }else{
            //Toast.makeText(getContext(),"NoData",Toast.LENGTH_SHORT).show();
            loadOffers(city);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getAdapter().getItemCount() >= 3) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if ( loadin == false && lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                    {
                        if(checkForNext==true){
                            saleItemsList.add(null); loadin = true;
                            adapter.notifyItemInserted(saleItemsList.size() - 1);
                        }

                        res = myDB.getItemDetailsfIX("Handlooms",last_id,2);
                        int c = res.getCount();
                        if(res.getCount()>0){
                            if(checkForNext==true){
                                saleItemsList.remove(saleItemsList.size() - 1);
                            }
                            while(res.moveToNext()){
                                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                SaleItemsAdapter sale=new SaleItemsAdapter();
                                String cash=res.getString(12);

                                final double latitude = gps.getLatitude();
                                final double longitude = gps.getLongitude();

                                Thread thread=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String response;
                                        InputStream in = null;
                                        try {
                                            URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(res.getString(14)) + "," + Double.parseDouble(res.getString(15)) + "&sensor=false&units=metric&mode=driving");
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

                                if(cash.equals("1"))
                                {
                                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cashless");
                                    saleItemsList.add(a);
                                    sale.setCheckFlag(1);
                                }else if (cash.equals("0")){
                                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cash");
                                    saleItemsList.add(a);
                                    sale.setCheckFlag(0);
                                }
                                last_id = res.getString(0);
                            }
                            adapter.notifyDataSetChanged();
                            if(checkForNext==true){
                                loadin = false;
                            }
                            if(c==0){
                                final Toast toast = Toast.makeText(rootView.getContext(), "No more sales!", Toast.LENGTH_SHORT);
                                toast.show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 400);
                            }
                        }else{
                            loading = new ProgressDialog(getContext());
                            //loading.setMessage("Please wait...");
                            //loading.setCanceledOnTouchOutside(false);
                            //loading.show();
                            int count = recyclerView.getAdapter().getItemCount();

                            loadOffersID(city,count);
                            //ooToast.makeText(getContext(),last_id,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });

        // prepareSales();
        return rootView;

    }

    private void loadOffersID(final String city, final int count) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/offerSales2EC.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "0";
                        String result = response;

                        if(checkForNext==true){
                            saleItemsList.remove(saleItemsList.size() - 1);
                        }

                        if (result.matches(str)) {
                            final Toast toast = Toast.makeText(rootView.getContext(), "No Results!", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 400);
                        } else {
                            StringTokenizer str1 = new StringTokenizer(result,";");
                            while (str1.hasMoreTokens()) {
                                //Toast.makeText(chooser.this, str1.nextToken(), Toast.LENGTH_SHORT).show();
                                StringTokenizer str2 = new StringTokenizer(str1.nextToken(),"||");

                                //Toast.makeText(MainActivity.this,"Size is: " +str2.countTokens(), Toast.LENGTH_SHORT).show();
                                String str3[] = new String[str2.countTokens()+1];int i =0;
                                while(str2.hasMoreTokens()){
                                    str3[i]=str2.nextToken();
                                    //Toast.makeText(MainActivity.this,i+" "+str3[i],Toast.LENGTH_SHORT).show();
                                    i++;
                                }
                                String id = str3[0]; String sid = str3[1]; String pname = str3[2]; String sellCat = str3[3];
                                String proCat = str3[4]; String price = str3[5]; String oprice = str3[6]; String sdate = str3[7];
                                String edate = str3[8]; String shop = str3[9]; String desc = str3[10]; String img = str3[11];
                                String dis = str3[12]; String lat = str3[13]; String lng = str3[14]; String contact = str3[15];
                                String nextCheck = str3[19];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }
                                isInserted = myDB.insertOffer(id,sid,pname,sellCat,proCat, Double.parseDouble(price),Double.parseDouble(oprice),sdate,edate,shop,desc,img,Double.parseDouble(dis),lat,lng,contact,str3[16],str3[17],str3[18]);
                            }
                            if(isInserted){
                                recyclerView.setAdapter(adapter);
                                res = myDB.getItemDetailsfIX("Handlooms",last_id,2);
                                int count1 = res.getCount();
                                if(res.getCount()>0){
                                    while(res.moveToNext()){
                                        byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                        SaleItemsAdapter sale=new SaleItemsAdapter();
                                        String cash=res.getString(12);

                                        final double latitude = gps.getLatitude();
                                        final double longitude = gps.getLongitude();

                                        Thread thread=new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                String response;
                                                InputStream in = null;
                                                try {
                                                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(res.getString(14)) + "," + Double.parseDouble(res.getString(15)) + "&sensor=false&units=metric&mode=driving");
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

                                        if(cash.equals("1"))
                                        {
                                            SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cashless");
                                            saleItemsList.add(a);
                                            sale.setCheckFlag(1);
                                        }else if (cash.equals("0")){
                                            SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cash");
                                            saleItemsList.add(a);
                                            sale.setCheckFlag(0);
                                        }
                                        last_id = res.getString(0);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                                if (count1 == 0) {
                                    final Toast toast = Toast.makeText(rootView.getContext(), "No more sales!", Toast.LENGTH_SHORT);
                                    toast.show();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            toast.cancel();
                                        }
                                    }, 400);
                                }
                                if(count1!=0){
                                    recyclerView.scrollToPosition(count - count1);
                                }else{
                                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-3);
                                }
                            }
                        }
                        //dismiss();
                        loadin = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loading.dismiss();
                        final Toast toast = Toast.makeText(rootView.getContext(), "Cant connect to server. Try again!", Toast.LENGTH_SHORT);
                        toast.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 400);
                        //Toast.makeText(getContext(),"ID Load Handloom- "+ error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city);
                params.put("state",state);
                params.put("lastid",last_id);
                params.put("catid","14");
                params.put("itemCount","3");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void loadOffers(final String city) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/offerSales2E.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        weak.setVisibility(View.INVISIBLE);
                        String str = "0";
                        String result = response;
                        if (result.isEmpty()) {
                            //Toast.makeText(getContext(), "No Sale in this Category", Toast.LENGTH_SHORT).show();
                            notify.setVisibility(View.VISIBLE);
                            load.setVisibility(View.GONE);
                        } else {
                            StringTokenizer str1 = new StringTokenizer(result,";");
                            int cnt = str1.countTokens();
                            while (str1.hasMoreTokens()) {
                                //Toast.makeText(chooser.this, str1.nextToken(), Toast.LENGTH_SHORT).show();
                                StringTokenizer str2 = new StringTokenizer(str1.nextToken(),"||");

                                //Toast.makeText(MainActivity.this,"Size is: " +str2.countTokens(), Toast.LENGTH_SHORT).show();
                                String str3[] = new String[str2.countTokens()+1];int i =0;
                                while(str2.hasMoreTokens()){
                                    str3[i]=str2.nextToken();
                                    //Toast.makeText(MainActivity.this,i+" "+str3[i],Toast.LENGTH_SHORT).show();
                                    i++;
                                }
                                String id = str3[0]; String sid = str3[1]; String pname = str3[2]; String sellCat = str3[3];
                                String proCat = str3[4]; String price = str3[5]; String oprice = str3[6]; String sdate = str3[7];
                                String edate = str3[8]; String shop = str3[9]; String desc = str3[10]; String img = str3[11];
                                String dis = str3[12]; String lat = str3[13]; String lng = str3[14]; String contact = str3[15];
                                String nextCheck = str3[19];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }
                                //Toast.makeText(MainActivity.this, id +" "+str3[17], Toast.LENGTH_SHORT).show();
                                //Toast.makeText(MainActivity.this, proCat +" "+price + " "+ oprice + " " + sdate + " ", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(MainActivity.this, edate +" "+shop + " "+ desc + " ", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(MainActivity.this, img, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(MainActivity.this, str3[16], Toast.LENGTH_SHORT).show();

                                isInserted = myDB.insertOffer(id,sid,pname,sellCat,proCat, Double.parseDouble(price),Double.parseDouble(oprice),sdate,edate,shop,desc,img,Double.parseDouble(dis),lat,lng,contact,str3[16],str3[17],str3[18]);

                            }
                            if(cnt>0){
                                load.setVisibility(View.GONE);
                                RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view13);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(rootView.getContext(), saleItemsList);

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(rootView.getContext(), LinearLayout.VERTICAL,false);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);

                                res = myDB.getItemDetails("Handlooms");
                                while (res.moveToNext()){

                                    //Toast.makeText(getContext(),res.getString(0)+","+res.getString(1)+","+res.getString(2)+","+res.getString(3)+","+res.getString(4)+","+res.getString(5)+","+res.getString(6)+","+res.getString(7)+","+res.getString(8)+","+res.getString(9)+","+res.getString(10)+","+res.getString(11),Toast.LENGTH_LONG).show();
                                    //Toast.makeText(getContext(),res.getString(2)+","+res.getDouble(6)+","+res.getDouble(5)+","+res.getString(7)+","+res.getString(8)+","+res.getString(10)+","+res.getInt(0)+","+res.getInt(1)+","+res.getString(3)+","+res.getString(4)+","+res.getString(9) ,Toast.LENGTH_SHORT).show();
                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    //Toast.makeText(getContext(),res.getString(11),Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getContext(),res.getString(2),Toast.LENGTH_SHORT).show();
                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();

                                    Thread thread=new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String response;
                                            InputStream in = null;
                                            try {
                                                URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(res.getString(14)) + "," + Double.parseDouble(res.getString(15)) + "&sensor=false&units=metric&mode=driving");
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

                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getActivity().getPackageName()),res.getString(17),String.valueOf(parsedDistance),res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }
                                    //Toast.makeText(getContext(),cash.toString(),Toast.LENGTH_SHORT).show();
                                    //SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(13),res.getString(14),res.getString(15));
                                    // saleItemsList.add(a);
                                    last_id = res.getString(0);
                                }
                                adapter.notifyDataSetChanged();

                            }/*
                            if(getContext()!=null){
                                Intent in = new Intent(getContext(),CustomerSales.class);
                                getActivity().finish();
                                in.putExtra("pgno","12");
                                startActivity(in);
                            }*/
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        weak.setVisibility(View.VISIBLE);
                        load.setVisibility(View.INVISIBLE);
                        notify.setVisibility(View.INVISIBLE);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city);
                params.put("state",state);
                params.put("catid","14");
                params.put("itemCount","3");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }
}

