package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
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

import dmax.dialog.SpotsDialog;

public class ViewMore extends AppCompatActivity {

    String oid; Bundle bundle;
    private boolean loadin;
    String cat;
    private List<SaleItems> saleItemsList;
    TextView notify;
    private RecyclerView recyclerView;
    private SaleItemsAdapter adapter; GPSTracker gps;
    DatabaseHelper myDB;String parsedDistance;
    Cursor res; SaleItemsAdapter sale;String maxid = "0" ;
    Vector vs = new Vector(13, 5);
    private boolean checkForNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);

        bundle = getIntent().getExtras();
        oid = bundle.getString("oid");
        cat = bundle.getString("prodname");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("More Products");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        gps = new GPSTracker(ViewMore.this);
        notify = (TextView)findViewById(R.id.note1);
        myDB = new DatabaseHelper(this);
        saleItemsList=new ArrayList<>();
        adapter=new SaleItemsAdapter(this, saleItemsList);
        recyclerView=(RecyclerView)findViewById(R.id.category_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        sale =new SaleItemsAdapter();

        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        final String citty = sharedpreferences.getString("Ccity", "");

        SharedPreferences sharedpreferences1 = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        final String state1 = sharedpreferences1.getString("state", "");
        vs.clear();
        SearchCategory(cat,citty,state1);

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
                            Toast.makeText(ViewMore.this,"show",Toast.LENGTH_SHORT).show();
                            saleItemsList.add(null);
                            adapter.notifyItemInserted(saleItemsList.size() - 1);
                            loadin = true;
                        }

                        SearchCategory1(cat,citty,state1);
                    }
                }
            }

        });
    }

    private void SearchCategory1(final String cat,final String city,final String state) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getSimilarProduct2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;

                        if(checkForNext==true){
                            saleItemsList.remove(saleItemsList.size() - 1);
                        }

                        if (result.isEmpty()) {
                            Toast.makeText(ViewMore.this,"Page ends",Toast.LENGTH_SHORT).show();
                        } else {
                            notify.setVisibility(View.INVISIBLE);
                            StringTokenizer str1 = new StringTokenizer(result,";");
                            int cnt = str1.countTokens();
                            while (str1.hasMoreTokens()) {
                                //Toast.makeText(chooser.this, str1.nextToken(), Toast.LENGTH_SHORT).show();
                                StringTokenizer str2 = new StringTokenizer(str1.nextToken(),"||");

                                //Toast.makeText(MainActivity.this,"Size is: " +str2.countTokens(), Toast.LENGTH_SHORT).show();
                                final String str3[] = new String[str2.countTokens()+1];int i =0;
                                while(str2.hasMoreTokens()){
                                    str3[i]=str2.nextToken();
                                    //Toast.makeText(MainActivity.this,i+" "+str3[i],Toast.LENGTH_SHORT).show();
                                    i++;
                                }
                                String id = str3[0]; String sid = str3[1]; String pname = str3[2]; String sellCat = str3[3];
                                String proCat = str3[4]; String price = str3[5]; String oprice = str3[6]; String sdate = str3[7];
                                String edate = str3[8]; String shop = str3[9]; String desc = str3[10]; String img = str3[11];
                                String dis = str3[12]; final String lat = str3[13]; final String lng = str3[14]; String contact = str3[15];
                                String nextCheck = str3[19];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }
                                //Toast.makeText(ViewMore.this, pname + nextCheck , Toast.LENGTH_SHORT).show();
                                byte [] encodeByte= Base64.decode(img, Base64.DEFAULT);
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                maxid = id;

                                final double latitude = gps.getLatitude();
                                final double longitude = gps.getLongitude();

                                Thread thread=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String response;
                                        InputStream in = null;
                                        try {
                                            URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(lat) + "," + Double.parseDouble(lng) + "&sensor=false&units=metric&mode=driving");
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
                                //Toast.makeText(ViewMore.this, pname+" "+str3[18] , Toast.LENGTH_SHORT).show();
                                SaleItemsAdapter sale=new SaleItemsAdapter();
                                String cash=str3[16];

                                vs.add(id);

                                int c = Collections.frequency(vs,id);
                                if(c==1){
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(pname,Double.parseDouble(oprice),Double.parseDouble(price),sdate,edate,desc,bitmap,Integer.parseInt(id),Integer.parseInt(sid),sellCat,proCat,shop,str3[13],str3[14],str3[15],getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),str3[17],parsedDistance,str3[18],"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(pname,Double.parseDouble(oprice),Double.parseDouble(price),sdate,edate,desc,bitmap,Integer.parseInt(id),Integer.parseInt(sid),sellCat,proCat,shop,str3[13],str3[14],str3[15],getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable",getPackageName()),str3[17],parsedDistance,str3[18],"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        loadin = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewMore.this,"Cant connect to server! Try again",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("cat",cat);
                params.put("city",city);
                params.put("offerid",oid);
                params.put("state",state);
                params.put("mid",maxid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SearchCategory(final String cat,final String city,final String state) {
        //Toast.makeText(ViewMore.this,"Cat",Toast.LENGTH_SHORT).show();
        final AlertDialog loading;
        loading = new SpotsDialog(ViewMore.this, R.style.Custom);
        loading.setMessage("Loading offers...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getSimilarProduct2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;
                        if (result.isEmpty()) {
                            notify.setVisibility(View.VISIBLE);
                        } else {
                            notify.setVisibility(View.INVISIBLE);
                            StringTokenizer str1 = new StringTokenizer(result,";");
                            int cnt = str1.countTokens();
                            while (str1.hasMoreTokens()) {
                                //Toast.makeText(chooser.this, str1.nextToken(), Toast.LENGTH_SHORT).show();
                                StringTokenizer str2 = new StringTokenizer(str1.nextToken(),"||");

                                //Toast.makeText(MainActivity.this,"Size is: " +str2.countTokens(), Toast.LENGTH_SHORT).show();
                                final String str3[] = new String[str2.countTokens()+1];int i =0;
                                while(str2.hasMoreTokens()){
                                    str3[i]=str2.nextToken();
                                    //Toast.makeText(MainActivity.this,i+" "+str3[i],Toast.LENGTH_SHORT).show();
                                    i++;
                                }
                                String id = str3[0]; String sid = str3[1]; String pname = str3[2]; String sellCat = str3[3];
                                String proCat = str3[4]; String price = str3[5]; String oprice = str3[6]; String sdate = str3[7];
                                String edate = str3[8]; String shop = str3[9]; String desc = str3[10]; String img = str3[11];
                                String dis = str3[12]; final String lat = str3[13]; final String lng = str3[14]; String contact = str3[15];

                                String nextCheck = str3[19];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }
                                //Toast.makeText(ViewMore.this, pname + nextCheck , Toast.LENGTH_SHORT).show();
                                maxid = id;

                                //Toast.makeText(CategorySearch.this, pname , Toast.LENGTH_SHORT).show(); str3[18]
                                byte [] encodeByte= Base64.decode(img, Base64.DEFAULT);
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                final double latitude = gps.getLatitude();
                                final double longitude = gps.getLongitude();

                                Thread thread=new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String response;
                                        InputStream in = null;
                                        try {
                                            URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + Double.parseDouble(lat) + "," + Double.parseDouble(lng) + "&sensor=false&units=metric&mode=driving");
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
                                //Toast.makeText(ViewMore.this, pname+" "+str3[18] , Toast.LENGTH_SHORT).show();
                                SaleItemsAdapter sale=new SaleItemsAdapter();
                                String cash=str3[16];
                                vs.add(id);

                                int c = Collections.frequency(vs,id);
                                if(c==1){
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(pname,Double.parseDouble(oprice),Double.parseDouble(price),sdate,edate,desc,bitmap,Integer.parseInt(id),Integer.parseInt(sid),sellCat,proCat,shop,str3[13],str3[14],str3[15],getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),str3[17],parsedDistance,str3[18],"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(pname,Double.parseDouble(oprice),Double.parseDouble(price),sdate,edate,desc,bitmap,Integer.parseInt(id),Integer.parseInt(sid),sellCat,proCat,shop,str3[13],str3[14],str3[15],getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable",getPackageName()),str3[17],parsedDistance,str3[18],"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewMore.this,"Cant connect to server! Try again",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("cat",cat);
                params.put("city",city);
                params.put("offerid",oid);
                params.put("state",state);
                params.put("mid",maxid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
