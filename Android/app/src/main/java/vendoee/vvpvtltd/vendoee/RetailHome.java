package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static java.security.AccessController.getContext;

public class RetailHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView recyclerView;
    ImageView propic;   TextView shopname,shopaddr,points;
    String sid; TextView notify,notice; FloatingActionButton fab;
    private SaleItemsAdapterRetailer adapter;
    private List<SaleItemsRetailer> saleItemsList;
    public static final String JSON_URL = "http://www.vendoee.com/android-scripts/offerRetail_beta.php";
    public static final String JSON_URL1 = "http://www.vendoee.com/android-scripts/getRetailer_beta.php";
    public static final String JSON_URL2 = "http://www.vendoee.com/android-scripts/offerRetailID_beta.php";
    SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseHelper myDB;    RelativeLayout load;
    private Tracker mTracker;
    private Context mcontext; private boolean loadin;
    private Activity mActivity; String parsedDistance;
    Cursor res;String last_id="0"; GPSTracker gps;
    private String mActivityName = "Retail Home";
    SaleItemsAdapterRetailer sale=new SaleItemsAdapterRetailer();
    Toolbar toolbar; private boolean checkForNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retail_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        load = (RelativeLayout) findViewById(R.id.loadingPanel);
        notice = (TextView)findViewById(R.id.noteify);
        gps = new GPSTracker(RetailHome.this);
        mcontext=getApplicationContext();
        mActivity=RetailHome.this;

        myDB = new DatabaseHelper(this);
       // filter = (RelativeLayout)findViewById(R.id.filter);
        saleItemsList=new ArrayList<>();
        adapter=new SaleItemsAdapterRetailer(this, saleItemsList);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent in = new Intent(RetailHome.this,AnnounceOffer.class);
                startActivity(in);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Target viewTarget = new ViewTarget(UIUtilsC.getNavButtonView(toolbar));
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(viewTarget)
                .setStyle(R.style.CustomShowcaseTheme1)
                .setContentTitle("Hi! Click here")
                .setContentText("Find Customer requested products and Deals..")
                .singleShot(4115)
                .build()
                .setOnShowcaseEventListener(new OnShowcaseEventListener() {
                    @Override
                    public void onShowcaseViewHide(ShowcaseView showcaseView) {
                        presentShowcaseSequence();
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

        SharedPreferences sharedpreferences = getSharedPreferences("SID", Context.MODE_PRIVATE);
        sid = sharedpreferences.getString("sid", "");

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.activity_main_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         myDB.deleteDat(sid);
                                                         saleItemsList=new ArrayList<>();
                                                         adapter=new SaleItemsAdapterRetailer(RetailHome.this, saleItemsList);
                                                         RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(RetailHome.this, LinearLayout.VERTICAL,false);
                                                         recyclerView.setLayoutManager(layoutManager);
                                                         recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                         recyclerView.setAdapter(adapter);
                                                         adapter.notifyDataSetChanged();
                                                         checkForNext = true;
                                                         mSwipeRefreshLayout.setRefreshing(false);

                                                         saleItemsList=new ArrayList<>();
                                                         Toast.makeText(RetailHome.this,"Reloading..",Toast.LENGTH_SHORT).show();
                                                         load.setVisibility(View.VISIBLE);
                                                         sendRequest(sid);
                                                     }
                                                 }
        );
        notify = (TextView)findViewById(R.id.note);

        if(last_id.equals("0")){
            loadingprofile(sid);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                myDB.deleteDat(sid);
                res = myDB.getItemDetailsRetail(sid);

                if(res.getCount()>0){
                    RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                    //Toast.makeText(getContext(),"Electronis-GotData",Toast.LENGTH_SHORT).show();
                    saleItemsList=new ArrayList<>();
                    adapter=new SaleItemsAdapterRetailer(RetailHome.this, saleItemsList);

                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(RetailHome.this, LinearLayout.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                    if(!res.moveToFirst()){
                    }
                    res.moveToPrevious();

                    while (res.moveToNext()){

                        byte [] encodeByte= Base64.decode(res.getString(9), Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                        String sdis = res.getString(11);  String verify = res.getString(15);
                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        Location locationA = new Location("point A");
                        locationA.setLatitude(latitude);
                        locationA.setLongitude(longitude);

                        Location locationB = new Location("point B");
                        locationB.setLatitude(Double.parseDouble(res.getString(14)));
                        locationB.setLongitude(Double.parseDouble(res.getString(15)));

                        float distance = locationA.distanceTo(locationB);
                        distance = distance/1000;
                        distance = (float) Math.round(distance * 100) / 100;

                        String cash=res.getString(10);
                        if(cash.equals("1")) {
                            sale.setCheckFlag(1);
                            if (verify.equals("1")) {
                                SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()), "Yes",res.getString(16),"",res.getString(18),"Cashless");
                                saleItemsList.add(a);
                                sale.setVerifyFlag(1);
                            }else {
                                SaleItemsRetailer a=new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),"No",res.getString(16),"",res.getString(18),"Cash");
                                saleItemsList.add(a);
                                sale.setVerifyFlag(0);
                            }
                        }else if (cash.equals("0")){
                            sale.setCheckFlag(0);
                            if (verify.equals("1")) {
                                SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()), "Yes",res.getString(16),"",res.getString(18),"Cashless");
                                saleItemsList.add(a);
                                sale.setVerifyFlag(1);
                            }
                            else{
                                SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()), "No",res.getString(16),"",res.getString(18),"Cash");
                                saleItemsList.add(a);
                                sale.setVerifyFlag(0);
                            }
                        }

                        last_id = res.getString(0);
                        //Toast.makeText(RetailHome.this,"localDB "+last_id,Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();

                }else{
                    //Toast.makeText(RetailHome.this,"NoData",Toast.LENGTH_SHORT).show();
                    load.setVisibility(View.VISIBLE);
                    sendRequest(sid);
                }


            }
        }, 1000);

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
                        };

                        res = myDB.getItemDetailsfIXR(last_id,2);
                        if(res.getCount()>0){
                            if(checkForNext==true){
                                saleItemsList.remove(saleItemsList.size() - 1);
                                adapter.notifyItemRemoved(saleItemsList.size());
                            }

                            while(res.moveToNext()){
                                byte [] encodeByte= Base64.decode(res.getString(9), Base64.DEFAULT);
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                String sdis = res.getString(11);  String verify = res.getString(15);

                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();

                                Location locationA = new Location("point A");
                                locationA.setLatitude(latitude);
                                locationA.setLongitude(longitude);

                                Location locationB = new Location("point B");
                                locationB.setLatitude(Double.parseDouble(res.getString(14)));
                                locationB.setLongitude(Double.parseDouble(res.getString(15)));

                                float distance = locationA.distanceTo(locationB);
                                distance = distance/1000;
                                distance = (float) Math.round(distance * 100) / 100;

                                String cash=res.getString(10);
                                if(cash.equals("1")) {
                                    sale.setCheckFlag(1);
                                    if (verify.equals("1")) {
                                        SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()), "yes",res.getString(16),"",res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setVerifyFlag(1);
                                    }else {
                                        SaleItemsRetailer a=new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),"No",res.getString(16),"",res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setVerifyFlag(0);
                                    }
                                }else if (cash.equals("0")){
                                    sale.setCheckFlag(0);
                                    if (verify.equals("1")) {
                                        SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()), "Yes",res.getString(16),"",res.getString(18),"Cashless");
                                        saleItemsList.add(a);
                                        sale.setVerifyFlag(1);
                                    }
                                    else{
                                        SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()), "No",res.getString(16),"",res.getString(18),"Cash");
                                        saleItemsList.add(a);
                                        sale.setVerifyFlag(0);
                                    }
                                }

                                last_id = res.getString(0);
                                //Toast.makeText(RetailHome.this,"scrolledListener "+last_id,Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();
                            if(checkForNext==true){
                                loadin = false;
                            }
                        }else{
                            //Toast.makeText(RetailHome.this,"scrolledListener + funccall "+last_id,Toast.LENGTH_SHORT).show();
                            loadOffersID(sid);
                        }
                    }
                }
            }

        });

    }
    private static final String SHOWCASE_ID = "sequenc3";
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);


        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(fab)
                        .setDismissText("GOT IT")
                        .setMaskColour(Color.parseColor("#e5f44336"))
                        .setContentText("Launch sale from here..")
                        .withCircleShape()
                        .build()
        );

        sequence.start();

    }

    private void loadOffersID(final String sid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        load.setVisibility(View.INVISIBLE);

                        if(checkForNext==true){
                            saleItemsList.remove(saleItemsList.size() - 1);
                            adapter.notifyItemRemoved(saleItemsList.size());
                        }

                        if(response.isEmpty()){
                            final Toast toast = Toast.makeText(RetailHome.this,"No more offers in your basket!", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 400);
                            //notify.setVisibility(View.VISIBLE);
                        }else{
                            notify.setVisibility(View.INVISIBLE);
                            StringTokenizer st=new StringTokenizer(response,";");
                            String str[];

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
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                String sdis = str[10];  String verify = str[15];

                                String nextCheck = str[19];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }

                                //Toast.makeText(RetailHome.this,str[0]+" "+str[16],Toast.LENGTH_SHORT).show();
                                myDB.insertRetailOffer(str[0],str[1],str[2],str[3],Double.parseDouble(str[4]),Double.parseDouble(str[5]),str[6],str[7],str[8],str[9],Double.parseDouble(str[10]),str[11],str[12],str[13],str[14],verify,str[16],str[17],str[18]);
                            }
                            //adapter.notifyDataSetChanged();
                            if(st.countTokens()>0){
                                res = myDB.getItemDetailsfIXR(last_id,2);
                                if(res.getCount()>0){
                                    while(res.moveToNext()){

                                        byte [] encodeByte= Base64.decode(res.getString(9), Base64.DEFAULT);
                                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                        String sdis = res.getString(11);  String verify = res.getString(15);
                                        double latitude = gps.getLatitude();
                                        double longitude = gps.getLongitude();

                                        Location locationA = new Location("point A");
                                        locationA.setLatitude(latitude);
                                        locationA.setLongitude(longitude);

                                        Location locationB = new Location("point B");
                                        locationB.setLatitude(Double.parseDouble(res.getString(14)));
                                        locationB.setLongitude(Double.parseDouble(res.getString(15)));

                                        float distance = locationA.distanceTo(locationB);
                                        distance = distance/1000;
                                        distance = (float) Math.round(distance * 100) / 100;

                                        String cash=res.getString(10);
                                        if(cash.equals("1")) {
                                            sale.setCheckFlag(1);
                                            if (verify.equals("1")) {
                                                SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()), "Yes",res.getString(16),"",res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setVerifyFlag(1);
                                            }else {
                                                SaleItemsRetailer a=new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),"No",res.getString(16),"",res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setVerifyFlag(0);
                                            }
                                        }else if (cash.equals("0")){
                                            sale.setCheckFlag(0);
                                            if (verify.equals("1")) {
                                                SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()), "Yes",res.getString(16),"",res.getString(18),"Cashless");
                                                saleItemsList.add(a);
                                                sale.setVerifyFlag(1);
                                            }
                                            else{
                                                SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),"No",res.getString(16),"",res.getString(18),"Cash");
                                                saleItemsList.add(a);
                                                sale.setVerifyFlag(0);
                                            }
                                        }

                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        loadin = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RetailHome.this,"Cant connect to server! Try again",Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                params.put("lastid",last_id);
                params.put("itemCount","3");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*mTracker.setScreenName(mActivityName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());*/
    }


    private void loadingprofile(final String sid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(RetailHome.this,response,Toast.LENGTH_LONG).show();
                        propic = (ImageView)findViewById(R.id.propic);
                        points = (TextView)findViewById(R.id.points);
                        shopname = (TextView)findViewById(R.id.textView);
                        shopaddr = (TextView)findViewById(R.id.textView3);

                        StringTokenizer st = new StringTokenizer(response,";");
                        String[] str = new String[st.countTokens()];
                        int i =0;

                        while (st.hasMoreTokens()) {
                            str[i++]=st.nextToken();
                        }

                        byte [] encodeByte=Base64.decode(str[0],Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        propic.setImageBitmap(bitmap);

                        //String tmp = str[1];

                        shopname.setText(str[1]);
                        shopaddr.setText(str[2]);
                        points.setText("Points: "+str[9]);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        SharedPreferences preferences=getSharedPreferences("LOG",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences preferences1=getSharedPreferences("SID",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1=preferences1.edit();
                        editor1.clear();
                        editor1.commit();

                        Intent in = new Intent(RetailHome.this,RetailerSignIn.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        Toast.makeText(RetailHome.this,"Can't load Profile! Try to login Again!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.retail_home, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
        } else if (id == R.id.add) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
            Toast.makeText(RetailHome.this,"Please Wait...",Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent in = new Intent(RetailHome.this,AnnounceOffer.class);
                    startActivity(in);
                }
            }, 1000);
        } else if (id == R.id.division) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
            Toast.makeText(RetailHome.this,"Please Wait...",Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent in = new Intent(RetailHome.this,ShowCategory.class);
                    startActivity(in);
                }
            }, 1000);
        } else if (id == R.id.request) {
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();
            Toast.makeText(RetailHome.this,"Please Wait...",Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent in = new Intent(RetailHome.this,ProductsRequestRetailer.class);
                    startActivity(in);
                }
            }, 1000);
        } else if (id == R.id.logout) {

            SharedPreferences preferences0=getSharedPreferences("SIDSERVICE",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor0=preferences0.edit();
            editor0.clear();
            editor0.commit();

            SharedPreferences preferences=getSharedPreferences("LOG",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.clear();
            editor.commit();

            SharedPreferences preferences1=getSharedPreferences("SID",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1=preferences1.edit();
            editor1.clear();
            editor1.commit();

            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            stopService(new Intent(RetailHome.this, TotalRetailerNotify.class));

            Intent in = new Intent(RetailHome.this,RetailerSignIn.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);

        } else if (id == R.id.contact) {
            //Toast.makeText(RetailHome.this,"Contact Us",Toast.LENGTH_SHORT).show();

            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            Intent in = new Intent(RetailHome.this,Contact.class);
            startActivity(in);

        } else if (id == R.id.about) {
            //Toast.makeText(RetailHome.this,"About Us",Toast.LENGTH_SHORT).show();
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            Intent in = new Intent(RetailHome.this,About.class);
            startActivity(in);

        } else if (id == R.id.customerDeals) {
            //Toast.makeText(RetailHome.this,"About Us",Toast.LENGTH_SHORT).show();
            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            Intent in = new Intent(RetailHome.this,CustomerDealsRetailer.class);
            startActivity(in);

        } else if(id == R.id.profile){

            DrawerLayout mDrawerLayout;
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerLayout.closeDrawers();

            Toast.makeText(RetailHome.this,"Please Wait",Toast.LENGTH_SHORT).show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {

                    Intent in = new Intent(RetailHome.this,updateRetailerProfile.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("ID", sid);
                    in.putExtras(bundle);
                    startActivity(in);

                }
            }, 1000);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean doubleBackToExitPressedOnce;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            if(sid.isEmpty()){
                Intent intent = new Intent(RetailHome.this, MainActivity.class);
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

    private void sendRequest(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(RetailHome.this,"Offers Send:",Toast.LENGTH_LONG).show();

                        if(response.isEmpty()){
                            Toast.makeText(RetailHome.this,"Hurry up! Launch some sale ",Toast.LENGTH_LONG).show();
                            notify.setVisibility(View.VISIBLE);
                            load.setVisibility(View.INVISIBLE);
                        }else{

                            notify.setVisibility(View.INVISIBLE);
                            StringTokenizer st=new StringTokenizer(response,";");
                            int cnt = st.countTokens();
                            String str[];

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
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                String nextCheck = str[19];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }
                                String sdis = str[10];  String verify = str[15];

                                //Toast.makeText(RetailHome.this,str[0]+" "+str[16],Toast.LENGTH_SHORT).show();
                                myDB.insertRetailOffer(str[0],str[1],str[2],str[3],Double.parseDouble(str[4]),Double.parseDouble(str[5]),str[6],str[7],str[8],str[9],Double.parseDouble(str[10]),str[11],str[12],str[13],str[14],verify,str[16],str[17],str[18]);
                            }
                            //adapter.notifyDataSetChanged();
                            if(cnt>0){

                                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                                saleItemsList=new ArrayList<>();
                                adapter=new SaleItemsAdapterRetailer(RetailHome.this, saleItemsList);

                                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(RetailHome.this, LinearLayout.VERTICAL,false);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                                load.setVisibility(View.INVISIBLE);
                                res = myDB.getItemDetailsRetail(sid);
                                while(res.moveToNext()){
                                    byte [] encodeByte= Base64.decode(res.getString(9), Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                    String sdis = res.getString(11);  String verify = res.getString(15);

                                    double latitude = gps.getLatitude();
                                    double longitude = gps.getLongitude();

                                    Location locationA = new Location("point A");
                                    locationA.setLatitude(latitude);
                                    locationA.setLongitude(longitude);

                                    Location locationB = new Location("point B");
                                    locationB.setLatitude(Double.parseDouble(res.getString(14)));
                                    locationB.setLongitude(Double.parseDouble(res.getString(15)));

                                    float distance = locationA.distanceTo(locationB);
                                    distance = distance/1000;
                                    distance = (float) Math.round(distance * 100) / 100;

                                    String cash=res.getString(10);
                                    if(cash.equals("1")) {
                                        sale.setCheckFlag(1);
                                        if (verify.equals("1")) {
                                            SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),"Yes",res.getString(16),"",res.getString(18),"Cashless");
                                            saleItemsList.add(a);
                                            sale.setVerifyFlag(1);
                                        }else {
                                            SaleItemsRetailer a=new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_credit_card_black_24dp",  "drawable", getPackageName()),"No",res.getString(16),"",res.getString(18),"Cash");
                                            saleItemsList.add(a);
                                            sale.setVerifyFlag(0);
                                        }
                                    }else if (cash.equals("0")){
                                        sale.setCheckFlag(0);
                                        if (verify.equals("1")) {
                                            SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()),"Yes",res.getString(16),"",res.getString(18),"Cashless");
                                            saleItemsList.add(a);
                                            sale.setVerifyFlag(1);
                                        }
                                        else{
                                            SaleItemsRetailer a = new SaleItemsRetailer(res.getString(1), Double.parseDouble(res.getString(5)), Double.parseDouble(res.getString(4)), res.getString(6), res.getString(7), res.getString(2), bitmap, Integer.parseInt(res.getString(0)), Integer.parseInt(res.getString(0)), res.getString(2), res.getString(3), res.getString(8), res.getString(12), res.getString(13), res.getString(14), getResources().getIdentifier("ic_monetization_on_black_24dp",  "drawable", getPackageName()), "No",res.getString(16),"",res.getString(18),"Cash");
                                            saleItemsList.add(a);
                                            sale.setVerifyFlag(0);
                                        }
                                    }

                                    last_id = res.getString(0);
                                    //Toast.makeText(RetailHome.this,"scrolledListener "+last_id,Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }
                        //loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        notice.setVisibility(View.VISIBLE);
                        Toast.makeText(RetailHome.this,"Can't load offers! Try to refresh.",Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                params.put("itemCount","3");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
