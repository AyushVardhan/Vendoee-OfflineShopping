/*
 * Copyright 2015 Udacity, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class Offers_MainActivity extends AppCompatActivity {
/*
    private List<OffersList> offersLists;
 //   private RecyclerView mRecyclerView;
 //   private RecyclerView.LayoutManager mLayoutManager;
  //  private RecyclerView.Adapter mAdapter;

    private OffersAdapter offersAdapter;
    DatabaseHelper myDB;
    Cursor res;
    private TextView retailMobile,retailEmail,shopTitle,retailAddress, categoryTextView;
    private ImageView shopImage;


    //Bitmap pimage[]; int pid[]; String pname[]; String salePrice[]; String discount[];

    //OffersList offersList1;
    int size; Button sendR;
    //Bitmap simage;
    String bname,mobile,email, address,retailLat,retailLng;
    //ListView offersListView;
    Context context;
    String category;
*/CollapsingToolbarLayout collapsingToolbarLayout;
    public static final String JSON_URL = "http://www.vendoee.com/android-scripts/offerRetailVerified.php";
    public static final String JSON_URL1 = "http://www.vendoee.com/android-scripts/getRetailer.php";
    String sid,contact,lat,lng,name; ImageView propic; TextView state,city;
    TabLayout tabLayout ; Toolbar toolbar;
    ViewPager viewPager ;   public FragmentAdapterCustomerEndShopProfile fragmentAdapter ;
    FloatingActionButton call,prodReq,waay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offers_activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout1);
        viewPager = (ViewPager) findViewById(R.id.pager1);
        propic = (ImageView)findViewById(R.id.shopImage);
        call = (FloatingActionButton)findViewById(R.id.callRet);
        prodReq = (FloatingActionButton)findViewById(R.id.prodReq);
        waay = (FloatingActionButton)findViewById(R.id.direc);
        state = (TextView)findViewById(R.id.state);
        city = (TextView)findViewById(R.id.city);

        Bundle bundle = getIntent().getExtras();

        sid = bundle.getString("sellerid");

        SharedPreferences.Editor editor1 = getSharedPreferences("Customer_Shop_Profile", MODE_PRIVATE).edit();
        editor1.putString("ShopProfile", sid);
        editor1.commit();


        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Offers"));
        //tabLayout.addTab(tabLayout.newTab().setText("Product"));

        fragmentAdapter = new FragmentAdapterCustomerEndShopProfile(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(fragmentAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.getAdapter().notifyDataSetChanged();

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

        waay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng));
                startActivity(intent);
            }
        });

        prodReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences2.getString("number_C", "");

                String phonenum = mobile.substring(3,mobile.length());
                checkCustomer(phonenum);

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+contact));
                //Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"8958372912"));
                startActivity(callIntent);
            }
        });
        //tabLayout = (TabLayout)findViewById(R.id.infoprofile);
        //viewPager = (ViewPager) findViewById(R.id.pager1);


        //context=this;
        //Toast.makeText(Offers_MainActivity.this,"Seller1: "+sid,Toast.LENGTH_SHORT).show();
        //retailMobile=(TextView)findViewById(R.id.retailContactNumber);
        //retailEmail=(TextView)findViewById(R.id.retailEmail);
        //shopImage=(ImageView)findViewById(R.id.shopImage);
        //shopTitle=(TextView)findViewById(R.id.retailer_name);
        //retailAddress=(TextView)findViewById(R.id.retailAddress);
        //sendR = (Button)findViewById(R.id.sendReq);
        //mRecyclerView=(RecyclerView)findViewById(R.id.offersRecyclerview);
        //offersListView=(ListView)findViewById(R.id.offerslistview);
        //categoryTextView=(TextView)findViewById(R.id.retailCategory);
        /*
        sendR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Offers_MainActivity.this,RequestProduct.class);
                in.putExtra("sid",sid);
                startActivity(in);
            }
        });
        */
  //      mLayoutManager=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
//        mRecyclerView.setLayoutManager(mLayoutManager);

        //offersLists=new ArrayList<>();
  //      mAdapter=new OffersAdapter(context,offersLists);
   //     mRecyclerView.setAdapter(mAdapter);

        getSellerDetail(sid);
    }


    private static final String SHOWCASE_ID = "selerParticularPRodReq";
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);

        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(prodReq)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#e51249d9"))
                        .setContentText("Request Offer to this particular retailer..")
                        .withCircleShape()
                        .build()
        );

        sequence.start();

    }

    private void getSellerDetail(final String sid) {
        final AlertDialog loading;
        loading = new SpotsDialog(Offers_MainActivity.this, R.style.Custom);
        loading.setMessage("Loading profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        StringTokenizer st = new StringTokenizer(response,";");
                        String[] str = new String[st.countTokens()];
                        int i =0;

                        while (st.hasMoreTokens()) {
                            str[i++]=st.nextToken();
                        }

                        byte [] encodeByte= Base64.decode(str[0],Base64.DEFAULT);
                        Bitmap simage= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        propic.setImageBitmap(simage);

                        state.setText(str[15]); city.setText(str[14]); name = str[1];
                        collapsingToolbarLayout.setTitle(name);
                        contact = str[4]; lat = str[6]; lng = str[7];
                        presentShowcaseSequence();

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Offers_MainActivity.this,"Can't connect to server",Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        finish();
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

    private void checkCustomer(final String phonenum) {
        final AlertDialog loading;
        loading = new SpotsDialog(Offers_MainActivity.this, R.style.Custom);
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

                            Intent in = new Intent(Offers_MainActivity.this,RequestProduct.class);
                            in.putExtra("sid",sid);
                            startActivity(in);

                        }else{
                            if(response.equals("Profile Incomplete!")){
                                Toast.makeText(Offers_MainActivity.this,"Please complete your profile!",Toast.LENGTH_LONG).show();
                                Intent in = new Intent(Offers_MainActivity.this,CustomerProfile.class);
                                in.putExtra("EditPro","vendoee");
                                startActivity(in);
                            }else{
                                //Toast.makeText(Offers_MainActivity.this,response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(Offers_MainActivity.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    /*
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


                                //OffersList offersList = new OffersList(pname[j],pimage[j],pid[j]);
                                //offersLists.add(offersList);


                                j++;
                            }

                        offersListView.setAdapter(new OffersListViewAdapter(Offers_MainActivity.this, pname,pimage,pid,salePrice,discount));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Offers_MainActivity.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    */

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
