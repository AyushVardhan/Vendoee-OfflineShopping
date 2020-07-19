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

import dmax.dialog.SpotsDialog;

public class CategorySearch extends AppCompatActivity {

    Bundle bundle; String cat,state;
    private List<SaleItems> saleItemsList;
    TextView notify;
    private RecyclerView recyclerView;
    private SaleItemsAdapter adapter; GPSTracker gps;
    DatabaseHelper myDB;String parsedDistance;
    Cursor res; SaleItemsAdapter sale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SharedPreferences sharedpreferences1 = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state = sharedpreferences1.getString("state", "");

        gps = new GPSTracker(CategorySearch.this);
        notify = (TextView)findViewById(R.id.note1);
        myDB = new DatabaseHelper(this);
        saleItemsList=new ArrayList<>();
        adapter=new SaleItemsAdapter(this, saleItemsList);
        recyclerView=(RecyclerView)findViewById(R.id.category_recyclerView);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        sale =new SaleItemsAdapter();

        //res = myDB.getItemDetails("Electronics");

        bundle = getIntent().getExtras();
        cat = bundle.getString("SaleName");

        //Toast.makeText(CategorySearch.this,cat,Toast.LENGTH_SHORT).show();

        //myDB = new DatabaseHelper(this);
        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        String citty = sharedpreferences.getString("Ccity", "");
        SearchCategory(cat,citty);

        adapter.notifyDataSetChanged();
    }

    private void SearchCategory(final String cat,final String city) {
        final AlertDialog loading;
        loading = new SpotsDialog(CategorySearch.this, R.style.Custom);
        loading.setMessage("Loading profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getSuggestionList.php",
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

                                //Toast.makeText(CategorySearch.this, pname , Toast.LENGTH_SHORT).show();
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

                                SaleItemsAdapter sale=new SaleItemsAdapter();
                                String cash=str3[16];
                                if(cash.equals("1"))
                                {
                                    SaleItems a=new SaleItems(pname,Double.parseDouble(oprice),Double.parseDouble(price),sdate,edate,desc,bitmap,Integer.parseInt(id),Integer.parseInt(sid),sellCat,proCat,shop,str3[13],str3[14],str3[15],getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),str3[17],parsedDistance,str3[18],"Cashless");
                                    saleItemsList.add(a);
                                    sale.setCheckFlag(1);
                                }else if (cash.equals("0")){
                                    SaleItems a=new SaleItems(pname,Double.parseDouble(oprice),Double.parseDouble(price),sdate,edate,desc,bitmap,Integer.parseInt(id),Integer.parseInt(sid),sellCat,proCat,shop,str3[13],str3[14],str3[15],getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),str3[17],parsedDistance,str3[18],"Cash");
                                    saleItemsList.add(a);
                                    sale.setCheckFlag(0);
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

                        loading.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("cat",cat);
                params.put("city",city);
                params.put("state",state);
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
