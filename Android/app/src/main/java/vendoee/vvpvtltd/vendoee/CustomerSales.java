package vendoee.vvpvtltd.vendoee;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
import com.facebook.accountkit.AccountKit;
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class CustomerSales extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {

    GPSTracker gps;     String Category[];
    private CursorAdapter myAdapter; AutoCompleteTextView prod;
    private GoogleApiClient mGoogleApiClient;
    MaterialSpinner spinnerSub;
    Spinner spinner1,spinner;
    private static final String TAG=CustomerSales.class.getSimpleName();
    EditText txt; Dialog settingdialog,settingdialognearby,askproduct;
    LinearLayout filter,city,nearby;

    private String[] strArrData = {"No Suggestions"};
    String city1,phonenum,state1;
    String cities[];
    TabLayout tabLayout ; LinearLayout store,brand,recomend;
    ViewPager viewPager ;  ;
    RadioGroup rg; TextView showcity,phone_no;

    double latitude, longitude;
    String parsedDistance;
    int distance;
    private Context mcontext;
    private Activity mActivity;
    SearchView sv;Toolbar toolbar;
    Button b,broadcast,askone;int pgno;
    public FragmentAdapterClass fragmentAdapter ;
    static int tokenvalue = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2_layout);

        rg = (RadioGroup) findViewById(R.id.radioSex);
        filter = (LinearLayout)findViewById(R.id.filter);
        city = (LinearLayout)findViewById(R.id.city);
        nearby = (LinearLayout)findViewById(R.id.nearby);
        store = (LinearLayout)findViewById(R.id.store);
        brand = (LinearLayout)findViewById(R.id.brands);
        recomend = (LinearLayout)findViewById(R.id.recommendation);
        final String[] from = new String[] {"id"};
        final int[] to = new int[] {android.R.id.text1};
        gps = new GPSTracker(CustomerSales.this);
        showcity = (TextView)findViewById(R.id.showcity);

        mcontext=getApplicationContext();
        mActivity=CustomerSales.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myDB = new DatabaseHelper(this);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = (ViewPager) findViewById(R.id.pager1);


        setSupportActionBar(toolbar);

        new AsyncCaller().execute();

        settingdialog = new Dialog(CustomerSales.this);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.dialog_city);
        spinner = (Spinner) settingdialog.findViewById(R.id.spinnerState);
        spinner1 = (Spinner) settingdialog.findViewById(R.id.spinner);
        spinner1.setPrompt("State");
        spinner.setPrompt("City");

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner1.getSelectedItem().toString().equals("State")){
                    Toast.makeText(CustomerSales.this, "Select a state!", Toast.LENGTH_SHORT).show();
                }else{
                    getcities(spinner1.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        txt = (EditText) settingdialog.findViewById(R.id.password);
        b = (Button)settingdialog.findViewById(R.id.getCity);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( spinner.getSelectedItem().toString().isEmpty()){
                    Toast.makeText(CustomerSales.this, "Please choose a city!", Toast.LENGTH_SHORT).show();
                }else{
                    settingdialog.dismiss();
                    city1 = spinner.getSelectedItem().toString();

                    SharedPreferences.Editor editor1 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
                    editor1.putString("Ccity", city1);
                    editor1.commit();

                    SharedPreferences.Editor editor3 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
                    editor3.putString("state", spinner1.getSelectedItem().toString());
                    editor3.commit();

                    SharedPreferences.Editor editor2 = getSharedPreferences("OTPV", MODE_PRIVATE).edit();
                    editor2.putString("otpv", "done");
                    editor2.commit();

                    myDB.dropTable();
                    myDB.createTable();
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        SharedPreferences sharedpreferences1 = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state1 = sharedpreferences1.getString("state", "");

        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        String citty = sharedpreferences.getString("Ccity", "");
        showcity.setText(citty);

        myAdapter = new SimpleCursorAdapter(CustomerSales.this, R.layout.hint_row, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        sv = (SearchView)findViewById(R.id.search);
        sv.setIconifiedByDefault(false);
        sv.setSuggestionsAdapter(myAdapter);
        sv.clearFocus();

        EditText searchEditText = (EditText) sv.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(Color.parseColor("#959595"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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

        Target viewTarget = new ViewTarget(UIUtilsC.getNavButtonView(toolbar));
        new ShowcaseView.Builder(CustomerSales.this)
                .withMaterialShowcase()
                .setTarget(viewTarget)
                .setStyle(R.style.CustomShowcaseTheme2)
                .setContentTitle("Hi! Click here")
                .setContentText("Find your deals, Vouchers and many more!")
                .singleShot(4125)
                .build()
                .setOnShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        if(tokenvalue==0){
                            presentShowcaseSequence();
                            tokenvalue++;
                        }
                    }

                    @Override
                    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewShow(ShowcaseView showcaseView) {

                    }

                    @Override
                    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

                    }
                });

        SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        String mobile = sharedpreferences21.getString("number_C", "");
        //Toast.makeText(CustomerSales.this,mobile.substring(3,mobile.length()),Toast.LENGTH_SHORT).show();
        //phone_no.setText(mobile.substring(3,mobile.length()));

        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CustomerSales.this,"Load Stores",Toast.LENGTH_SHORT).show();
                Intent in = new Intent(CustomerSales.this,ShopBrandLayout.class);
                in.putExtra("cat","Shop");
                startActivity(in);
            }
        });

        brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CustomerSales.this,ShopBrandLayout.class);
                in.putExtra("cat","Brand");
                startActivity(in);
            }
        });

        recomend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences2.getString("number_C", "");

                phonenum = mobile.substring(3,mobile.length());
                checkCustomer(phonenum);
            }
        });




        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getstate();
            }
        });

        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkGPS()){
                    Intent in = new Intent(CustomerSales.this, vendoee.vvpvtltd.vendoee.nearby.class);
                    startActivity(in);
                }else{
                    startActivity(new Intent(CustomerSales.this,gpsnotfound1.class));
                }

            }
        });

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String name = null;
                if(query.contains("Search for:")){
                    name = query.substring(12,query.length());
                    SearchResult(name);
                }else{
                    SearchResult(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Toast.makeText(MainActivity.this,"OnQueryTextExchange:   "+newText,Toast.LENGTH_SHORT).show();
                //Log.d("CHECK THIS SEARCHED FOR: ",newText);
                getSuggestion1(newText);
                int c = 0;

                // Filter data
                final MatrixCursor mc = new MatrixCursor(new String[]{ BaseColumns._ID, "id" });
                for (int i=0; i<strArrData.length; i++) {
                    if (strArrData[i].toLowerCase().contains(newText.toLowerCase()))
                    {
                        mc.addRow(new Object[] {i, strArrData[i]});
                        c=1;
                    }else{
                        c++ ;
                    }
                }
                if(c>0){
                    mc.addRow(new Object[] {0, "Search for: " + newText.toString()});
                }
                myAdapter.changeCursor(mc);
                return false;
            }

        });

        sv.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                CursorAdapter ca = sv.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                sv.setQuery(cursor.getString(cursor.getColumnIndex("id")),false);
                String str = cursor.getString(cursor.getColumnIndex("id"));

                String name = null;
                if(str.contains("Search for:")){
                    name = str.substring(12,str.length());
                    SearchResult(name);
                }else{
                    SearchResult(str);
                }
                //Toast.makeText(CustomerSales.this,name,Toast.LENGTH_SHORT).show();


                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter ca = sv.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                sv.setQuery(cursor.getString(cursor.getColumnIndex("id")),false);
                String str = cursor.getString(cursor.getColumnIndex("id"));

                String name = null;
                if(str.contains("Search for:")){
                    name = str.substring(12,str.length());
                    SearchResult(name);
                }else{
                    SearchResult(str);
                }

                return false;
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final String grpname[] = {"Price(low to high)","Discount(max to min)","Live offers","Recent offers","Old offers"};

                AlertDialog.Builder alt_bld = new AlertDialog.Builder(CustomerSales.this);
                //alt_bld.setIcon(R.drawable.icon);
                alt_bld.setTitle("Select a filter");
                alt_bld.setSingleChoiceItems(grpname, -1, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        int n = viewPager.getCurrentItem();

                        if(grpname[item].contains("low to high")){

                            if(n==0){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView0=(RecyclerView)findViewById(R.id.recycler_view0);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView0.setLayoutManager(layoutManager);
                                recyclerView0.setItemAnimator(new DefaultItemAnimator());
                                recyclerView0.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Electronics");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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


                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();

                            }

                            if(n==1){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view1);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Appliances");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==2){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view2);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Men");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==3){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view3);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Women");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==4){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view4);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Kids & Baby");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==5){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view5);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Home & Furniture");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==6){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view6b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Books & more");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();

                            }

                            if(n==7){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view7b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Grocery");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==8){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view8);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Gift Hampers");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==9){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view9);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Tools & Hardware");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==10){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view10);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Electrical & Lightining");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==11){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view11);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Food & Restaurants");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==12){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view13);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsPrice("Handlooms");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==13){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view12);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){
                                        res = myDB.getSortedItemDetailsPrice("Other");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }

                        }else if(grpname[item].contains("Discount")){

                            if(n==0){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView0=(RecyclerView)findViewById(R.id.recycler_view0);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView0.setLayoutManager(layoutManager);
                                recyclerView0.setItemAnimator(new DefaultItemAnimator());
                                recyclerView0.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Electronics");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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


                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();

                            }

                            if(n==1){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view1);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Appliances");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==2){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view2);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Men");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==3){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view3);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Women");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==4){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view4);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Kids & Baby");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==5){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view5);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Home & Furniture");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==6){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view6b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Books & more");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();

                            }

                            if(n==7){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view7b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Grocery");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==8){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view8);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Gift Hampers");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==9){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view9);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Tools & Hardware");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==10){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view10);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Electrical & Lightining");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==11){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view11);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Food & Restaurants");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==12){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view13);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemDetailsDiscount("Handlooms");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==13){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view12);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){
                                        res = myDB.getSortedItemDetailsDiscount("Other");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }

                        }
                        else if(grpname[item].contains("Recent offers")){

                            if(n==0){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView0=(RecyclerView)findViewById(R.id.recycler_view0);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView0.setLayoutManager(layoutManager);
                                recyclerView0.setItemAnimator(new DefaultItemAnimator());
                                recyclerView0.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Electronics");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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


                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();

                            }

                            if(n==1){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view1);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Appliances");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==2){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view2);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Men");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==3){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view3);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Women");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==4){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view4);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Kids & Baby");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==5){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view5);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Home & Furniture");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==6){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view6b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Books & more");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();

                            }

                            if(n==7){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view7b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Grocery");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==8){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view8);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Gift Hampers");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==9){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view9);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Tools & Hardware");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==10){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view10);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Electrical & Lightining");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==11){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view11);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Food & Restaurants");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==12){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view13);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemNewestFirst("Handlooms");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==13){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view12);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){
                                        res = myDB.getSortedItemNewestFirst("Other");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }

                        }else if(grpname[item].contains("Old offers")){

                            if(n==0){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView0=(RecyclerView)findViewById(R.id.recycler_view0);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView0.setLayoutManager(layoutManager);
                                recyclerView0.setItemAnimator(new DefaultItemAnimator());
                                recyclerView0.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Electronics");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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


                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();

                            }

                            if(n==1){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view1);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Appliances");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==2){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view2);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Men");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==3){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view3);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Women");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==4){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view4);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Kids & Baby");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==5){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view5);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Home & Furniture");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });

                                        }

                                    }
                                }).start();
                            }

                            if(n==6){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view6b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Books & more");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();

                            }

                            if(n==7){

                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view7b);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Grocery");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();

                            }

                            if(n==8){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view8);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Gift Hampers");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==9){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view9);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Tools & Hardware");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==10){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view10);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Electrical & Lightining");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==11){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view11);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Food & Restaurants");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==12){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view13);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                new Thread(new Runnable() {
                                    public void run(){

                                        res = myDB.getSortedItemOldestFirst("Handlooms");

                                        while (res.moveToNext()){

                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                    }
                                }).start();
                            }

                            if(n==13){
                                dialog.dismiss();
                                Toast.makeText(CustomerSales.this, "Please wait..",Toast.LENGTH_SHORT).show();

                                RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view12);
                                myDB = new DatabaseHelper(CustomerSales.this);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
                                recyclerView1.setLayoutManager(layoutManager);
                                recyclerView1.setItemAnimator(new DefaultItemAnimator());
                                recyclerView1.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                new Thread(new Runnable() {
                                    public void run(){
                                        res = myDB.getSortedItemOldestFirst("Other");

                                        while (res.moveToNext()){
                                            if(res.isLast()){
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(CustomerSales.this, "Items refreshed..",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    adapter.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }).start();
                            }

                        }else if(grpname[item].contains("Live offers")){

                            liveSale(n);
                        }

                        dialog.dismiss();// dismiss the alertbox after chose option

                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.show();

            }
        });

        String num = getIntent().getStringExtra("pgno");
        if(num!=null){
            pgno = Integer.parseInt(num);
            if(pgno>0){
                viewPager.setCurrentItem(pgno);
            }
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sv.clearFocus();
        sv.setFocusable(false);
    }
    private static final String SHOWCASE_ID = "sequence xa";
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);


        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(recomend)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#e51249d9"))
                        .setContentText("Request product to a retailer from here..")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(nearby)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#e51249d9"))
                        .setContentText("Know sales and offer near you..")
                        .withCircleShape()
                        .build()
        );


        sequence.start();

    }

    public void liveSale(int n){
        if(n==0){

            RecyclerView recyclerView0=(RecyclerView)findViewById(R.id.recycler_view0);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView0.setLayoutManager(layoutManager);
            recyclerView0.setItemAnimator(new DefaultItemAnimator());
            recyclerView0.setAdapter(adapter);

            res = myDB.getSortedItemLiveSale("Electronics");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);


                                        final Cursor res = myDB.getItemDetails("Electronics");
                                        while (res.moveToNext()){

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }

                                        }
                                adapter.notifyDataSetChanged();
                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();

                                        final Cursor res = myDB.getItemDetails("Electronics");
                                        while (res.moveToNext()){

                                            byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                            String cash=res.getString(12);

                                            final double latitude = gps.getLatitude();
                                            final double longitude = gps.getLongitude();
                                            //String parsedDistance;

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

                                            SaleItemsAdapter sale=new SaleItemsAdapter();
                                            if(cash.equals("1"))
                                            {
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(1);
                                            }else if (cash.equals("0")){
                                                SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setCheckFlag(0);
                                            }
                                        }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }

            new Thread(new Runnable() {
                public void run(){
                    while (res.moveToNext()){

                        byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        String cash=res.getString(12);

                        final double latitude = gps.getLatitude();
                        final double longitude = gps.getLongitude();
                        //String parsedDistance;

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

                        SaleItemsAdapter sale=new SaleItemsAdapter();
                        if(cash.equals("1"))
                        {
                            SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                            saleItemsList.add(a);
                            sale.setCheckFlag(1);
                        }else if (cash.equals("0")){
                            SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                            saleItemsList.add(a);
                            sale.setCheckFlag(0);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            }).start();

        }
        if(n==1){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view1);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Appliances");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Appliances");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Appliances");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            }
            }).start();
        }

        if(n==2){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view2);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Men");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Men");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Men");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==3){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view3);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //SaleItemsAdapter sale=new SaleItemsAdapter();

            res = myDB.getSortedItemLiveSale("Women");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Women");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Women");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
            }).start();

        }

        if(n==4){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view4);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Kids & Baby");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Kids & Baby");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Kids & Baby");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==5){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view5);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Home & Furniture");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Home & Furniture");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Home & Furniture");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==6){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view6b);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Books & more");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Books & more");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Books & more");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==7){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view7b);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Grocery");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Grocery");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Grocery");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();

        }

        if(n==8){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view8);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Gift Hampers");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Gift Hampers");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Gift Hampers");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==9){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view9);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Tools & Hardware");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Tools & Hardware");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Tools & Hardware");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==10){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view10);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Electrical & Lightining");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Electrical & Lightining");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Electrical & Lightining");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==11){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view11);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Food & Restaurants");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Food & Restaurants");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Food & Restaurants");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==12){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view13);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            res = myDB.getSortedItemLiveSale("Handlooms");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Handlooms");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Handlooms");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }

        if(n==13){

            RecyclerView recyclerView1=(RecyclerView)findViewById(R.id.recycler_view12);
            myDB = new DatabaseHelper(CustomerSales.this);
            saleItemsList=new ArrayList<>();
            adapter=new SaleItemsAdapter(CustomerSales.this, saleItemsList);
            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerSales.this, LinearLayout.VERTICAL,false);
            recyclerView1.setLayoutManager(layoutManager);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            res = myDB.getSortedItemLiveSale("Other");
            if(res.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerSales.this);
                builder1.setMessage("No live Sale in this Category. Request some offers?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences2.getString("number_C", "");
                                dialog.cancel();
                                phonenum = mobile.substring(3,mobile.length());
                                checkCustomer(phonenum);

                                final Cursor res = myDB.getItemDetails("Other");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();

                            }
                        });
                builder1.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                final Cursor res = myDB.getItemDetails("Other");
                                while (res.moveToNext()){

                                    byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    String cash=res.getString(12);

                                    final double latitude = gps.getLatitude();
                                    final double longitude = gps.getLongitude();
                                    //String parsedDistance;

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

                                    SaleItemsAdapter sale=new SaleItemsAdapter();
                                    if(cash.equals("1"))
                                    {
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(1);
                                    }else if (cash.equals("0")){
                                        SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setCheckFlag(0);
                                    }

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
            new Thread(new Runnable() {
                public void run(){
            while (res.moveToNext()){

                byte [] encodeByte= Base64.decode(res.getString(11), Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                String cash=res.getString(12);

                final double latitude = gps.getLatitude();
                final double longitude = gps.getLongitude();
                //String parsedDistance;

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

                SaleItemsAdapter sale=new SaleItemsAdapter();
                if(cash.equals("1"))
                {
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cashless");
                    saleItemsList.add(a);
                    sale.setCheckFlag(1);
                }else if (cash.equals("0")){
                    SaleItems a=new SaleItems(res.getString(2),res.getDouble(6),res.getDouble(5),res.getString(7),res.getString(8),res.getString(10),bitmap,res.getInt(0),res.getInt(1),res.getString(3),res.getString(4),res.getString(9),res.getString(14),res.getString(15),res.getString(16),getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),res.getString(17),parsedDistance,res.getString(18),"Cash");
                    saleItemsList.add(a);
                    sale.setCheckFlag(0);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }).start();
        }
    }

    private void SearchResult(final String str) {
        //Toast.makeText(CustomerSales.this,"Searching: "+str,Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/CountSearchItem.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            if(response.contains("ID")){
                                StringTokenizer str = new StringTokenizer(response," ");
                                String s[] = new String[str.countTokens()]; int i = 0;
                                while(str.hasMoreElements()){
                                    s[i++] = str.nextToken();
                                }

                                SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                String mobile = sharedpreferences21.getString("number_C", "");

                                Intent in = new Intent(CustomerSales.this,SearchResults2.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("SaleName", s[1]);
                                bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                                in.putExtras(bundle);
                                startActivity(in);

                            }else if(response.contains("many")){

                                Intent in = new Intent(CustomerSales.this,CategorySearch.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("SaleName", str);
                                in.putExtras(bundle);
                                startActivity(in);
                            }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CustomerSales.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("search",str);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void checkCustomer(final String phonenum) {
        final AlertDialog loading;

        loading = new SpotsDialog(CustomerSales.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/checkCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.equals("1")){
                            loading.dismiss();

                            startActivity(new Intent(CustomerSales.this,RequestProduct.class));
                            /*
                            */
                        }else{
                            if(response.equals("Profile Incomplete!")){
                                Toast.makeText(CustomerSales.this,"Please complete your profile!",Toast.LENGTH_LONG).show();
                                Intent in = new Intent(CustomerSales.this,CustomerProfile.class);
                                in.putExtra("EditPro","vendoee");
                                startActivity(in);
                            }else{
                                //Toast.makeText(CustomerSales.this,response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(CustomerSales.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",phonenum);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private RecyclerView recyclerView;
    private SaleItemsAdapter adapter;
    private List<SaleItems> saleItemsList;
    DatabaseHelper myDB;
    Cursor res;

    private boolean doubleBackToExitPressedOnce;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
            String citty = sharedpreferences.getString("Ccity", "");

            if(citty.isEmpty()){
                Intent intent = new Intent(CustomerSales.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    finishAffinity();
                }
            }
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(CustomerSales.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }*/

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_sales, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id==R.id.signout) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    Intent intent = new Intent(CustomerSales.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.activity_title_about_us) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            Intent in = new Intent(CustomerSales.this,About.class);
            startActivity(in);

        } else if (id == R.id.nav_privacy_policy) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            Intent in = new Intent(CustomerSales.this,Contact.class);
            startActivity(in);

        } else if (id == R.id.nav_electronics) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_appliances) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_men) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_women) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_kids) {
            viewPager.setCurrentItem(4);
        } else if (id == R.id.nav_homef) {
            viewPager.setCurrentItem(5);
        }else if (id == R.id.nav_books) {
            viewPager.setCurrentItem(6);
        }else if (id == R.id.nav_grocery) {
            viewPager.setCurrentItem(7);
        }else if (id == R.id.nav_automobile) {
            viewPager.setCurrentItem(8);
        }else if (id == R.id.nav_foods) {
            viewPager.setCurrentItem(9);
        }else if (id == R.id.nav_others) {
            viewPager.setCurrentItem(11);
        }else if (id == R.id.nav_handloom) {
            viewPager.setCurrentItem(10);
        }else if (id == R.id.Profile) {
            Intent in = new Intent(CustomerSales.this,CustomerProfile.class);
            startActivity(in);
        }else if (id == R.id.home) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
        }else if (id == R.id.recoverAccount) {
            startActivity(new Intent(CustomerSales.this,RecoverCustomer.class));
        }else if (id == R.id.requestp) {
            startActivity(new Intent(CustomerSales.this,CustomerRequestedProduct.class));
        }else if (id == R.id.deals) {
            startActivity(new Intent(CustomerSales.this,CustomerDeals.class));
        }else if (id == R.id.voucher) {
            startActivity(new Intent(CustomerSales.this,voucher.class));
        }else if (id == R.id.shareApp) {

            Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.logo);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/LatestShare.jpg";
            OutputStream out = null;
            File file=new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path=file.getPath();
            Uri bmpUri = Uri.parse("file://"+path);
            Intent shareIntent = new Intent();
            shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            String sAux = "\nLet me recommend you this application\n\n ";
            sAux = sAux + "Vendoee - Dekho Online, Kharido Offline\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=vendoee.vvpvtltd.vendoee \n\n";
            sAux = sAux + "Or visit: http://www.vendoee.com/ \n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, sAux);
            shareIntent.setType("image/jpg");
            startActivity(Intent.createChooser(shareIntent,"Share with"));

        }else if(id == R.id.logoutc){

            SharedPreferences.Editor editor1 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
            editor1.putString("Ccity", "");
            editor1.commit();

            SharedPreferences.Editor editor2 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
            editor2.putString("state", "");
            editor2.commit();


            SharedPreferences.Editor editor3 = getSharedPreferences("MOBNO", MODE_PRIVATE).edit();
            editor3.putString("number_C", "");
            editor3.commit();

            stopService(new Intent(CustomerSales.this, TotalSaleServiceNotify.class));

            AccountKit.logOut();

            Intent in = new Intent(CustomerSales.this, MainActivity.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getSuggestion1(final String search){
        Log.d("FOR TESTING:" ,"search order: "+search);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getSuggestion.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("FOR TESTING:" ,"Result: "+response);

                        if(response.equals("no rows")){

                        }else {
                            StringTokenizer str = new StringTokenizer(response,";");
                            ArrayList<String> dataList = new ArrayList<String>();
                            while(str.hasMoreTokens()){
                                String str1 = str.nextToken();
                                dataList.add(str1);
                            }
                            strArrData = dataList.toArray(new String[dataList.size()]);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {@Override
        protected Map<String,String> getParams(){
            Map<String,String> params = new HashMap<String, String>();
            params.put("city",showcity.getText().toString());
            params.put("state",state1);
            params.put("search",search);
            return params;
        }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(CustomerSales.this);
        requestQueue.add(stringRequest);
    }

    LocationManager locationManager;

    private boolean checkGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    // Every time when you press search button on keypad an Activity is recreated which in turn calls this function
    @Override
    protected void onNewIntent(Intent intent) {


    }
    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {


        Log.d(TAG,"onConnectionFailed: "+connectionResult);

    }

    String state[];

    private void getstate() {
        loading = new SpotsDialog(CustomerSales.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getStateHome.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        StringTokenizer str = new StringTokenizer(response,",");
                        state = new String[str.countTokens()];
                        int i=0;

                        if(str.countTokens()==0){
                            //Toast.makeText(MainActivity.this,"No sale in any City!",Toast.LENGTH_SHORT).show();
                        }else{
                            while(str.hasMoreTokens()){
                                String str1 = str.nextToken();
                                state[i] = str1;
                                i++;
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CustomerSales.this,
                                    android.R.layout.simple_spinner_item, state);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner1.setAdapter(dataAdapter);

                        }
                        if(state.length != 0){

                            settingdialog.show();
                        }else{
                            Toast.makeText(CustomerSales.this,"No sale in any City!",Toast.LENGTH_SHORT).show();

                            Intent in = new Intent(CustomerSales.this, MainActivity.class);
                            startActivity(in);

                            SharedPreferences.Editor editor2 = getSharedPreferences("OTPV", MODE_PRIVATE).edit();
                            editor2.putString("otpv", "done");
                            editor2.commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(CustomerSales.this,"Can't load States! Please try again.",Toast.LENGTH_LONG).show();
                    }
                }){
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    AlertDialog loading;
    public void getcities(final String s){

        loading = new SpotsDialog(CustomerSales.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getCities1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        StringTokenizer str = new StringTokenizer(response,",");
                        cities = new String[str.countTokens()];
                        int i=0;

                        if(str.countTokens()==0){
                            //Toast.makeText(MainActivity.this,"No sale in any City!",Toast.LENGTH_SHORT).show();
                        }else{

                            while(str.hasMoreTokens()){
                                String str1 = str.nextToken();
                                cities[i] = str1;
                                i++;
                            }
                        }
                        if(cities.length != 0){

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CustomerSales.this,
                                    android.R.layout.simple_spinner_item, cities);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);

                        }
                        else{
                            Toast.makeText(CustomerSales.this,"No sale in any City!",Toast.LENGTH_SHORT).show();

                            Intent in = new Intent(CustomerSales.this, MainActivity.class);
                            startActivity(in);

                            SharedPreferences.Editor editor2 = getSharedPreferences("OTPV", MODE_PRIVATE).edit();
                            editor2.putString("otpv", "done");
                            editor2.commit();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(CustomerSales.this,"Can't load cities! Please try again.",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("state",s);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sv.clearFocus();
        sv.setFocusable(false);
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        AlertDialog pdLoading =  new SpotsDialog(CustomerSales.this, R.style.Custom);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            tabLayout.addTab(tabLayout.newTab().setText("Electronics"));
            tabLayout.addTab(tabLayout.newTab().setText("Appliances"));
            tabLayout.addTab(tabLayout.newTab().setText("Men"));
            tabLayout.addTab(tabLayout.newTab().setText("Women"));
            tabLayout.addTab(tabLayout.newTab().setText("Kids & Baby"));
            tabLayout.addTab(tabLayout.newTab().setText("Home & Furniture"));
            tabLayout.addTab(tabLayout.newTab().setText("Books & more"));
            tabLayout.addTab(tabLayout.newTab().setText("Grocery"));
            tabLayout.addTab(tabLayout.newTab().setText("gift Hampers"));
            tabLayout.addTab(tabLayout.newTab().setText("Food & Restaurants"));
            tabLayout.addTab(tabLayout.newTab().setText("Handlooms"));
            tabLayout.addTab(tabLayout.newTab().setText("Other"));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            fragmentAdapter = new FragmentAdapterClass(getSupportFragmentManager(), tabLayout.getTabCount());

            viewPager.setAdapter(fragmentAdapter);
            viewPager.setOffscreenPageLimit(14);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            pdLoading.dismiss();

        }

    }

}

