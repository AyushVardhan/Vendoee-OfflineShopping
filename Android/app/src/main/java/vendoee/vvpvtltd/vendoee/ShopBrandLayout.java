package vendoee.vvpvtltd.vendoee;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

public class ShopBrandLayout extends AppCompatActivity {

    TabLayout tabLayout ;Context mContext; Dialog confirm; Spinner spinner;
    ViewPager viewPager ; Bundle bundle; String number;Button b;
    public FragmentAdapterClassShops fragmentAdapter ;
    String Categories[] = {"All Categories","Electronics","Appliances","Men","Women","Kids & Baby","Home & Furniture","Books & more","Grocery","Gift Hampers","Food & Restaurants","Other","Handlooms"};
    String citty;
    private RecyclerView recyclerView; String state;
    private RetailersToCustomerAdapter adapter; DatabaseHelper myDB; Cursor res;
    private List<RetailersToCustomer> retailersToCustomerList; RetailersToCustomerAdapter shop;
    static String maxid = "0" ;    RelativeLayout load; LinearLayout filter1;
    GPSTracker gps; static String maxidBrands = "0";
    String parsedDistance;
    TextView notify; private boolean loadin;
    private boolean checkForNext = true;
    Vector vs = new Vector(13, 5);
    Vector vb = new Vector(13, 5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_brand_layout);

        filter1 = (LinearLayout)findViewById(R.id.filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vendors");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        SharedPreferences sharedpreferences1 = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state = sharedpreferences1.getString("state", "");
        gps = new GPSTracker(ShopBrandLayout.this);
        mContext = ShopBrandLayout.this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_HOME| ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView1=new ImageView(actionBar.getThemedContext());
        imageView1.setClickable(true);
        imageView1.setScaleType(ImageView.ScaleType.CENTER);
        imageView1.setImageResource(R.drawable.filter);




        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm = new Dialog(mContext);
                confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                confirm.setContentView(R.layout.dialog_category);
                confirm.show();
                spinner = (Spinner) confirm.findViewById(R.id.spinner);
                b = (Button)confirm.findViewById(R.id.getCity);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShopBrandLayout.this,
                        android.R.layout.simple_spinner_item, Categories);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String cid = "0"; String cat = spinner.getSelectedItem().toString();
                        if(cat.contains("All")){
                            cid = "0";
                        }else if(cat.contains("Electronics")){
                            cid = "1";
                        }else if(cat.contains("Appliances")){
                            cid = "2";
                        }else if(cat.contains("Men")){
                            cid = "3";
                        }else if(cat.contains("Women")){
                            cid = "4";
                        }else if(cat.contains("Kids & Baby")){
                            cid = "5";
                        }else if(cat.contains("Home & Furniture")){
                            cid = "6";
                        }else if(cat.contains("Books & more")){
                            cid = "7";
                        }else if(cat.contains("Grocery")){
                            cid = "8";
                        }else if(cat.contains("Gift Hampers")){
                            cid = "9";
                        }else if(cat.contains("Tools & Hardware")){
                            cid = "10";
                        }else if(cat.contains("Electrical & Lighting")){
                            cid = "11";
                        }else if(cat.contains("Food & Restaurants")){
                            cid = "12";
                        }else if(cat.contains("Other")){
                            cid = "13";
                        }else if(cat.contains("Handlooms")){
                            cid = "14";
                        }

                        int n = viewPager.getCurrentItem();
                        if(n==0){
                            maxid="0";
                        }
                        if(n==1){
                            maxidBrands="0";
                        }

                        fetchShops(cid);

                        confirm.dismiss();
                    }
                });
            }
        });

        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        citty = sharedpreferences.getString("Ccity", "");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);
        //tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        tabLayout.addTab(tabLayout.newTab().setText("Stores"));
        tabLayout.addTab(tabLayout.newTab().setText("Brands"));

        fragmentAdapter = new FragmentAdapterClassShops(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.getAdapter().notifyDataSetChanged();
        bundle = getIntent().getExtras();
        number = bundle.getString("cat");

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab LayoutTab) {

                viewPager.setCurrentItem(LayoutTab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab LayoutTab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab LayoutTab) {

            }
        });

        if(number.matches("Shop")){
            viewPager.setCurrentItem(0);
        }else{
            viewPager.setCurrentItem(1);
        }
    }

    private void fetchShops(final String cid) {

        int n = viewPager.getCurrentItem();
        if(n==0){
            maxid="0";  vs.clear();
            recyclerView=(RecyclerView)findViewById(R.id.category_recyclerViewS);
            recyclerView.setHasFixedSize(true);

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
                                retailersToCustomerList.add(null);
                                adapter.notifyItemInserted(retailersToCustomerList.size() - 1);
                                loadin = true;
                            }
                            //load.setVisibility(View.VISIBLE);
                            //Toast.makeText(ShopBrandLayout.this,maxid+" shop",Toast.LENGTH_SHORT).show();
                            SearchShops1(citty,cid);
                        }
                    }
                }

            });

            retailersToCustomerList=new ArrayList<>();
            adapter=new RetailersToCustomerAdapter(ShopBrandLayout.this, retailersToCustomerList);

            load = (RelativeLayout)findViewById(R.id.loadingPanelS);
            notify = (TextView)findViewById(R.id.note1S);

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ShopBrandLayout.this, LinearLayout.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            load.setVisibility(View.VISIBLE);
            //Toast.makeText(ShopBrandLayout.this,maxid+" shop",Toast.LENGTH_SHORT).show();
            SearchShops(citty,cid);

        }
        if(n==1){
            maxidBrands="0";    vb.clear();
            recyclerView=(RecyclerView)findViewById(R.id.category_recyclerViewB);
            recyclerView.setHasFixedSize(true);
            retailersToCustomerList=new ArrayList<>();
            adapter=new RetailersToCustomerAdapter(ShopBrandLayout.this, retailersToCustomerList);

            load = (RelativeLayout)findViewById(R.id.loadingPanelB);
            notify = (TextView)findViewById(R.id.note1B);

            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(ShopBrandLayout.this, LinearLayout.VERTICAL,false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            load.setVisibility(View.VISIBLE);
            //Toast.makeText(ShopBrandLayout.this,maxidBrands+" brands",Toast.LENGTH_SHORT).show();
            SearchBrands(citty,cid);

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
                                retailersToCustomerList.add(null);
                                adapter.notifyItemInserted(retailersToCustomerList.size() - 1);
                                loadin = true;
                            }
                            //load.setVisibility(View.VISIBLE);
                            //Toast.makeText(ShopBrandLayout.this,maxidBrands+" brands",Toast.LENGTH_SHORT).show();
                            SearchBrands1(citty,cid);
                        }
                    }
                }

            });
        }
    }

    private void SearchShops1(final String citty,final String cid) {
        if(recyclerView.getAdapter().getItemCount()==0){
            load.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getRetailerForCustomerSorted.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;

                        if(checkForNext==true){
                            retailersToCustomerList.remove(retailersToCustomerList.size() - 1);
                        }

                        if (result.isEmpty()) {
                            final Toast toast = Toast.makeText(ShopBrandLayout.this, "No more Stores", Toast.LENGTH_SHORT);
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
                                        //Toast.makeText(ShopBrandLayout.this,sellid+" "+name,Toast.LENGTH_SHORT).show();
                                        vs.add(sellid);

                                        int c =Collections.frequency(vs,sellid);
                                        if(c==1){
                                            try{
                                                RetailersToCustomerAdapter sale=new RetailersToCustomerAdapter();
                                                RetailersToCustomer a=new RetailersToCustomer(name,bitmap,Integer.parseInt(sellid),contact,str3[7],str3[9],str3[8],parsedDistance);
                                                retailersToCustomerList.add(a);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        //Toast.makeText(ShopBrandLayout.this,name +" layout",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ShopBrandLayout.this,"Cant connect to server! Try again",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",citty);
                params.put("state",state);
                params.put("mid",maxid);
                params.put("catid",cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SearchShops(final String citty,final String cid) {
        if(recyclerView.getAdapter().getItemCount()==0){
            load.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getRetailerForCustomerSorted.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;

                        if (result.isEmpty()) {
                            if(recyclerView.getAdapter().getItemCount()==0){
                                load.setVisibility(View.INVISIBLE);
                                notify.setVisibility(View.VISIBLE);
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
                                        //Toast.makeText(ShopBrandLayout.this,sellid+" "+name,Toast.LENGTH_SHORT).show();
                                        vs.add(sellid);

                                        int c =Collections.frequency(vs,sellid);
                                        if(c==1){
                                            try{
                                                RetailersToCustomerAdapter sale=new RetailersToCustomerAdapter();
                                                RetailersToCustomer a=new RetailersToCustomer(name,bitmap,Integer.parseInt(sellid),contact,str3[7],str3[9],str3[8],parsedDistance);
                                                retailersToCustomerList.add(a);
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                        //Toast.makeText(ShopBrandLayout.this,name +" layout",Toast.LENGTH_SHORT).show();
                                        maxid = sellid;
                                    }
                                }else{
                                    Toast.makeText(ShopBrandLayout.this,"No more stores!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ShopBrandLayout.this,"Cant connect to server! Try again",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",citty);
                params.put("state",state);
                params.put("mid",maxid);
                params.put("catid",cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SearchBrands(final String citty,final String cid) {
        final ProgressDialog loading;

        if(recyclerView.getAdapter().getItemCount()==0){
            load.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getBrandsForCustomerSorted.php",
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

                                        vb.add(sellid);

                                        int c =Collections.frequency(vb,sellid);
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
                                        maxidBrands = sellid;
                                    }
                                }else{
                                    Toast.makeText(ShopBrandLayout.this,"No more Brands!",Toast.LENGTH_SHORT).show();
                                }
                            }catch(IllegalStateException e){
                                //requestQueue.stop();
                                Log.e("HERE",e.toString());
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShopBrandLayout.this,"Cant connect to server! Try again",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",citty);
                params.put("state",state);
                params.put("mid",maxidBrands);
                params.put("catid",cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SearchBrands1(final String citty,final String cid) {
        final ProgressDialog loading;

        if(recyclerView.getAdapter().getItemCount()==0){
            load.setVisibility(View.VISIBLE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getBrandsForCustomerSorted.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;

                        if(checkForNext==true){
                            retailersToCustomerList.remove(retailersToCustomerList.size() - 1);
                        }

                        if (result.isEmpty()) {
                            final Toast toast = Toast.makeText(ShopBrandLayout.this, "No more Brands.", Toast.LENGTH_SHORT);
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

                                        vb.add(sellid);

                                        int c =Collections.frequency(vb,sellid);
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
                                        maxidBrands = sellid;
                                    }
                                }else{
                                    Toast.makeText(ShopBrandLayout.this,"No more Brands!",Toast.LENGTH_SHORT).show();
                                }
                            }catch(IllegalStateException e){
                                //requestQueue.stop();
                                Log.e("HERE",e.toString());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        loadin = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShopBrandLayout.this,"Cant connect to server! Try again",Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",citty);
                params.put("state",state);
                params.put("mid",maxidBrands);
                params.put("catid",cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
