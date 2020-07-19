package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
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

public class SearchResults2 extends AppCompatActivity {
    Bundle bundle;
    public static final String JSON_URL = "http://www.vendoee.com/android-scripts/getOffImage.php";
    public static final String JSON_URL4 = "http://www.vendoee.com/android-scripts/getSimilarProduct.php";
    String oid,cphone; String imga,state,pname;
    TextView notfound;
    DatabaseHelper myDB; Bitmap img_intent;
    //ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    static final int NUM_ITEMS = 3; static int count = 0;
    //ViewPager viewPager; C
    //Button takeDeal,like;
    CoordinatorLayout rootlayout;
    public static String[] tmp;
    public static String[] images;

    int like2 = R.drawable.heart_1;
    int like3 = R.drawable.heart_white;

    TextView discount,view1,like1,descr,price,oprice,startd,endd,shopp;
    LinearLayout call,direction,shopprofile,graboffer;
    ImageView imgP, img1, img2, img3, exp ;
    /*
        TextView category,subcategory,description,price,oprice,start,end,shop;
        ImageView img;
      */
    String oprce,callnow;

    FloatingActionButton heart;
    //private Context mContext;
    //private RecyclerView mRecyclerView;
    /*
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Activity mActivity;
*/
    CardView cv2,cv3;
    String pName,sid,enddate; String pnme,prce;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private static final String SHOWCASE_ID = "sequence examl";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private Activity mActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedpreferences1 = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state = sharedpreferences1.getString("state", "");

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        myDB = new DatabaseHelper(SearchResults2.this);
//        mContext = SearchResults2.this;

//        mActivity = SearchResults2.this;
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_viewSim);
/*
    viewPager = (ViewPager) findViewById(R.id.pager);
    TabLayout tabLayout=(TabLayout)findViewById(R.id.tabDots);
    tabLayout.setupWithViewPager(viewPager,true);
*/
        //img = (ImageView)findViewById(R.id.imgOffr);
        notfound = (TextView)findViewById(R.id.notfound);
        rootlayout = (CoordinatorLayout)findViewById(R.id.rootlayout);
        heart = (FloatingActionButton)findViewById(R.id.heart);
        imgP = (ImageView)findViewById(R.id.imagePrimary);
        discount = (TextView)findViewById(R.id.discount);
        descr = (TextView)findViewById(R.id.descr);
        view1 = (TextView) findViewById(R.id.views);
        like1 = (TextView)findViewById(R.id.love);
        price = (TextView)findViewById(R.id.oprice);
        shopp = (TextView)findViewById(R.id.shop);
        startd = (TextView)findViewById(R.id.startd);
        endd = (TextView)findViewById(R.id.endd);
        oprice = (TextView)findViewById(R.id.price);
        call = (LinearLayout)findViewById(R.id.call_now);
        direction= (LinearLayout)findViewById(R.id.locate);
        shopprofile = (LinearLayout)findViewById(R.id.shop_profile);
        graboffer = (LinearLayout)findViewById(R.id.grab_offer);
        exp = (ImageView)findViewById(R.id.expiry);

        mContext = SearchResults2.this;
        mActivity = SearchResults2.this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_viewSim);

        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);

        cv2 = (CardView)findViewById(R.id.imgCard2);
        cv3 = (CardView)findViewById(R.id.imgCard3);

        bundle = getIntent().getExtras();
        oid = bundle.getString("SaleName");
        cphone = bundle.getString("CMobile");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+callnow));
                startActivity(callIntent);
            }
        });


        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLatLong(oid);
            }
        });

        shopprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String sid = myDB.getRetailerID(oid);
                Intent in = new Intent(SearchResults2.this,Offers_MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sellerid", sid);
                in.putExtras(bundle);
                startActivity(in);
            }
        });

        graboffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date d = new Date();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                String tday = formatter.format(d);

                StringTokenizer str1 = new StringTokenizer(enddate,"-");
                String endDate[] = new String[str1.countTokens()]; int i = 0;
                while(str1.hasMoreElements()){
                    endDate[i] = str1.nextToken();
                    i++;
                }

                StringTokenizer str2 = new StringTokenizer(tday,"-");
                String tdaya[] = new String[str2.countTokens()]; int j = 0;
                while(str2.hasMoreElements()){
                    tdaya[j] = str2.nextToken();
                    j++;
                }

                //Toast.makeText(SearchResults2.this,endDate[2] + " " + endDate[1]+ " "+ endDate[0],Toast.LENGTH_SHORT).show();
                //Toast.makeText(SearchResults2.this,tdaya[0] + " " + tdaya[1]+ " "+ tdaya[2],Toast.LENGTH_SHORT).show();

                if(Integer.parseInt(endDate[0])<Integer.parseInt(tdaya[2])){
                    Toast.makeText(SearchResults2.this,"Sorry...Sale Expired!",Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(endDate[1])<Integer.parseInt(tdaya[1]) && Integer.parseInt(endDate[0])==Integer.parseInt(tdaya[2]))){
                    Toast.makeText(SearchResults2.this,"Sorry...Sale Expired!",Toast.LENGTH_SHORT).show();
                }else if((Integer.parseInt(endDate[2])<Integer.parseInt(tdaya[0]) && Integer.parseInt(endDate[1])==Integer.parseInt(tdaya[1]) && Integer.parseInt(endDate[0])==Integer.parseInt(tdaya[2]))){
                    Toast.makeText(SearchResults2.this,"Sorry...Sale Expired!",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                    final String phonenum = sharedpreferences2.getString("number_C", "");

                    new AlertDialog.Builder(SearchResults2.this)
                            .setTitle("Grab this deal!")
                            .setMessage("Grab this offer to get points.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    checkCustomer(phonenum.substring(3,phonenum.length()));
                                }
                            })
                            .setIcon(R.drawable.logo3)
                            .show();
                }

            }
        });

        imgP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bitmap image=((BitmapDrawable)imgP.getDrawable()).getBitmap();
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String str=Base64.encodeToString(b, Base64.DEFAULT);

                SharedPreferences.Editor editor = getSharedPreferences("IMAGEE", MODE_PRIVATE).edit();
                editor.putString("emlargeImage", str);
                editor.commit();
                //Toast.makeText(SearchResults2.this,pnme,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(SearchResults2.this,EnlargeImage.class);
                intent.putExtra("pname",pnme);
                startActivity(intent);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image=((BitmapDrawable)img1.getDrawable()).getBitmap();
                imgP.setImageBitmap(image);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image=((BitmapDrawable)img2.getDrawable()).getBitmap();
                imgP.setImageBitmap(image);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap image=((BitmapDrawable)img3.getDrawable()).getBitmap();
                imgP.setImageBitmap(image);
            }
        });
        /*
        takeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(like.getText().toString().equals("Liked")){
                    Toast.makeText(SearchResults2.this,"Decrement Function",Toast.LENGTH_SHORT).show();
                    like.setText("Like");
                    decrement(oid);
                }else if(like.getText().toString().equals("Like")){
                    Toast.makeText(SearchResults2.this,"Increment Function",Toast.LENGTH_SHORT).show();
                    like.setText("Liked");
                    increment(oid);
                }
            }
        });
           */

        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((int)heart.getTag()==like2){
                    Toast.makeText(SearchResults2.this,"Unliked!",Toast.LENGTH_SHORT).show();
                    heart.setTag(like3);
                    heart.setImageResource(like3);
                    decrement(oid);
                }else if((int)heart.getTag()==like3){
                    Toast.makeText(SearchResults2.this,"Liked!",Toast.LENGTH_SHORT).show();
                    heart.setTag(like2);
                    heart.setImageResource(like2);
                    increment(oid);
                }
            }
        });

        FloatingActionButton shr = (FloatingActionButton) findViewById(R.id.share);
        shr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pathofBmp = MediaStore.Images.Media.insertImage(getContentResolver(), img_intent,"title", null);
                Uri bmpUri = Uri.parse(pathofBmp);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, pnme + " (Rs. " + oprce + ")"+"\n"+"http://www.vendoee.com/product/"+oid+"\n");
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));

            }
        });

        loadingoffer();

    }

    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);


        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(heart)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#D942A5F5"))
                        .setContentText("Like this product to get updates..")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(shopprofile)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#D942A5F5"))
                        .setContentText("Check shop profile from here..")
                        .withCircleShape()
                        .build()
        );

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(graboffer)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#D942A5F5"))
                        .setContentText("Grab this offer from here..")
                        .withCircleShape()
                        .build()
        );

        sequence.start();

    }

    private void decrement(final String oid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/LoveInc.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SearchResults2.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SearchResults2.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",oid);
                params.put("phone",cphone);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void increment(final String oid) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/LoveInc.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(SearchResults2.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SearchResults2.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",oid);
                params.put("phone",cphone);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void checkCustomer(final String phonenum) {
        final AlertDialog loading;

        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Validating Customer...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/checkCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.equals("1")){
                            getPoints(phonenum,oid);
                        }else{
                            Toast.makeText(SearchResults2.this,"Sorry! your profile is incomplete.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(SearchResults2.this,"Can't connect to server! Try again",Toast.LENGTH_LONG).show();
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

    private void getPoints(final String phonenum, final String oid) {

        final AlertDialog loading;

        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/requestPointCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(SearchResults2.this,response,Toast.LENGTH_LONG).show();
                        if(response.equals("sucess")){
                            loading.dismiss();

                            Snackbar snackbar = Snackbar
                                    .make(rootlayout, "Request Sent! Please visit this shop, purchase and complete this deal to earn points.", 8000);
                            snackbar.show();

                        }else{
                            loading.dismiss();
                            Snackbar snackbar = Snackbar
                                    .make(rootlayout, "Your point limit for this shop, has been exhausted. Please wait "+ response+ " days. Happy shopping!", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(SearchResults2.this,"Can't connect to server! Try again",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",phonenum);
                params.put("oid",oid);
                params.put("price",oprce);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void ViewMore(View view) {


    }

    private class ImagePagerAdapter extends PagerAdapter {
        //


        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Context context = SearchResults2.this;
            final ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            final String str = mImages[position];
            //strp = str;
            byte [] encodeByte= Base64.decode(str,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            imageView.setImageBitmap(bitmap);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200,230);
            imageView.setLayoutParams(layoutParams);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor editor = getSharedPreferences("IMAGEE", MODE_PRIVATE).edit();
                    editor.putString("emlargeImage", str);
                    editor.commit();
                    //Toast.makeText(SearchResults2.this,pnme,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SearchResults2.this,EnlargeImage.class);
                    intent.putExtra("pname",pnme);
                    startActivity(intent);

                }
            });

            //imageView.setImageResource(imageArrayIcon.getResourceId(mImages[position], -1));
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

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
    private String parseDate(String date){
        //Toast.makeText(SearchResults2.this,date,Toast.LENGTH_SHORT).show();

        Date initDate = null;
        try {
            initDate = new SimpleDateFormat("yyyy-mm-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        String parsedDate = formatter.format(initDate);
        return parsedDate;
    }
    private void loadingoffer() {
        final AlertDialog  loading;

        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Loading details...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(SearchResults2.this,response,Toast.LENGTH_SHORT).show();

                        StringTokenizer str = new StringTokenizer(response,"||");
                        String vals[] = new String[str.countTokens()];
                        int i=0;
                        while(str.hasMoreTokens()){
                            vals[i++] = str.nextToken();
                        }

                        imga = vals[0]; String bname = vals[1];  pnme = vals[2]; String maincat = vals[3];
                        String subcat = vals[4]; String desc = vals[5]; prce = vals[6]; oprce = vals[7];
                        String strt = vals[8]; String ed = vals[9]; String love = vals[10]; String disc = vals[11];
                        String love1 = vals[12]; String vc = vals[13]; String call = vals[14]; sid = vals[15];
                        //Toast.makeText(SearchResults2.this,discount+" "+love1+" "+vc+" "+call,Toast.LENGTH_SHORT).show();
                        mImages = new String[1];
                        mImages[0] = imga;
                        callnow = call;
                        //ImagePagerAdapter adapter = new ImagePagerAdapter();
                        //viewPager.setAdapter(adapter);

                        if(love.equals("YES")){
                            heart.setImageResource(like2);
                            heart.setTag(like2);
                        }else if(love.equals("NO")){
                            heart.setImageResource(like3);
                            heart.setTag(like3);
                        }

                        byte [] encodeByte=Base64.decode(imga,Base64.DEFAULT);
                        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        img_intent = bitmap;
                        discount.setText(disc+"% Off"); view1.setText(vc); like1.setText(love1);
                        //descr.setText(desc);
                        imgP.setImageBitmap(bitmap);
                        img1.setImageBitmap(bitmap);
//                        img.setImageBitmap(bitmap);
                        //pname.setText(pnme);
                        enddate = ed;
                        startd.setText(strt); endd.setText(ed);
                        pName = pnme; oprice.setText(oprce);
                        price.setPaintFlags(price.getPaintFlags()  | Paint.STRIKE_THRU_TEXT_FLAG);
                        price.setText(prce);
                        collapsingToolbarLayout.setTitle(pName);
                        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
                        descr.setText("\u2022  "+desc.replace("~","\n\u2022 "));
                        shopp.setText(bname);

                        Date d = new Date();
                        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                        String tday = formatter.format(d);

                        StringTokenizer str1 = new StringTokenizer(enddate,"-");
                        String endDate[] = new String[str1.countTokens()]; int i1 = 0;
                        while(str1.hasMoreElements()){
                            endDate[i1] = str1.nextToken();
                            i1++;
                        }

                        StringTokenizer str2 = new StringTokenizer(tday,"-");
                        String tdaya[] = new String[str2.countTokens()]; int j = 0;
                        while(str2.hasMoreElements()){
                            tdaya[j] = str2.nextToken();
                            j++;
                        }

                        //Toast.makeText(SearchResults2.this,endDate[2] + " " + endDate[1]+ " "+ endDate[0],Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SearchResults2.this,tdaya[0] + " " + tdaya[1]+ " "+ tdaya[2],Toast.LENGTH_SHORT).show();

                        if(Integer.parseInt(endDate[0])<Integer.parseInt(tdaya[2])){
                            exp.setVisibility(View.VISIBLE);
                        }else if((Integer.parseInt(endDate[1])<Integer.parseInt(tdaya[1]) && Integer.parseInt(endDate[0])==Integer.parseInt(tdaya[2]))){
                            exp.setVisibility(View.VISIBLE);
                        }else if((Integer.parseInt(endDate[2])<Integer.parseInt(tdaya[0]) && Integer.parseInt(endDate[1])==Integer.parseInt(tdaya[1]) && Integer.parseInt(endDate[0])==Integer.parseInt(tdaya[2]))){
                            exp.setVisibility(View.VISIBLE);
                        }

                        // price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        //price.setText("₹ "+prce); oprice.setText("₹ "+oprce); start.setText(parseDate(strt)+" to"); end.setText(parseDate(ed));
                        //shop.setText(bname);
                        loading.dismiss();
                        loadimage();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchResults2.this,error.toString(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",oid);
                params.put("phone",cphone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    String myJSON;
    String[] SPINNERLIST ;
    JSONArray peoples = null;
    public static final String JSON_URL2 = "http://www.vendoee.com/android-scripts/getAdditionalOfferImage.php";

    private void loadimage() {
        /*
        final ProgressDialog loading;

        loading = new ProgressDialog(SearchResults2.this);
        loading.setMessage("Loading offer images...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();
        */
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON = response;
                        //                loading.dismiss();
                        showList();
                        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                        String citty = sharedpreferences.getString("Ccity", "");
                        similarProducts(citty,pName);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SearchResults2.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                        //               loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid",oid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    private String[] mImages;
    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");

            mImages = new String[peoples.length()+1];
            if(peoples.length()!=0){
                SPINNERLIST = new String[peoples.length()];
                tmp = new String[peoples.length()];
                images = new String[peoples.length()+1];
                for(int i=0;i<peoples.length();i++){
                    JSONObject c = peoples.getJSONObject(i);
                    String id = c.getString("id");
                    String category = c.getString("img");

                    //SPINNERLIST[i] = category;
                    tmp[i]=category;
                    //Toast.makeText(SearchResults.this,category,Toast.LENGTH_SHORT).show();
                }

                images[0] = imga;
                //images[1] = tmp[0];
                //images[2] = tmp[1];

                int t = tmp.length;

                for(int j=0; j<t; j++){
                    images[j+1] = tmp[j];
                }

                mImages[0] = images[0];
                int i=1;
                while (i<mImages.length){
                    mImages[i]=tmp[i-1];
                    i++;
                }
                //mImages[1] = images[1];
                //mImages[2] = images[2];
                //Toast.makeText(SearchResults2.this,mImages.length+" ",Toast.LENGTH_SHORT).show();

                if(mImages.length==2){
                    byte [] encodeByte=Base64.decode(mImages[1],Base64.DEFAULT);
                    Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    img2.setImageBitmap(bitmap);
                    cv2.setVisibility(View.VISIBLE);
                }

                if(mImages.length==3){
                    byte [] encodeByte1=Base64.decode(mImages[1],Base64.DEFAULT);
                    Bitmap bitmap1=BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                    img2.setImageBitmap(bitmap1);
                    cv2.setVisibility(View.VISIBLE);

                    byte [] encodeByte=Base64.decode(mImages[2],Base64.DEFAULT);
                    Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    img3.setImageBitmap(bitmap);
                    cv3.setVisibility(View.VISIBLE);
                }

                ImagePagerAdapter adapter = new ImagePagerAdapter();

                //viewPager.setAdapter(adapter);

            }else{
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.productadd);
                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp=Base64.encodeToString(b, Base64.DEFAULT);
                mImages[0] = imga;
                //mImages[1] = temp;
                //mImages[2] = temp;
                ImagePagerAdapter adapter = new ImagePagerAdapter();
                //viewPager.setAdapter(adapter);
                //Toast.makeText(SearchResults.this,"Fuck You",Toast.LENGTH_SHORT).show();
            }
            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
            String citty = sharedpreferences.getString("Ccity", "");

            if(pName.indexOf(' ')>0){
                pName = pName.substring(0,pName.indexOf(' '));
            }
            //Toast.makeText(SearchResults2.this,pName,Toast.LENGTH_SHORT).show();
            //similarProducts(citty,pName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void similarProducts(final String city,final String pName) {
        final ProgressDialog loading;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.isEmpty()){
                            notfound.setVisibility(View.VISIBLE);
                        }else{
                            notfound.setVisibility(View.INVISIBLE);
                            StringTokenizer str = new StringTokenizer(response,";");
                            String[] str1 = new String[str.countTokens()]; int i = 0; int n = str.countTokens();
                            //Toast.makeText(SearchResults2.this,n+" ",Toast.LENGTH_SHORT).show();
                            while(str.hasMoreTokens()){
                                str1[i] = str.nextToken();
                                i++;
                            }

                            Bitmap img[] = new Bitmap[n];
                            String pname[] = new String[n];
                            String price[] = new String[n];
                            String id[] = new String[n];

                            for(int j = 0; j < n; j++){

                                String tmp = str1[j];
                                StringTokenizer tmp1 = new StringTokenizer(tmp,"||");
                                String cool[] = new String[tmp1.countTokens()]; int k = 0;
                                while(tmp1.hasMoreTokens()){
                                    cool[k]=tmp1.nextToken();
                                    k++;
                                }

                                if(cool[1].equals("View More")){
                                    Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.viewmore);
                                    img[j] = icon; pname[j] = cool[1]; price[j] = cool[2] ; id[j] = cool[0];
                                }else{
                                    byte [] encodeByte= Base64.decode(cool[3], Base64.DEFAULT);
                                    Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                    img[j] = bitmap; pname[j] = cool[1]; price[j] = cool[2] ; id[j] = cool[0];
                                }
                            }

                            mLayoutManager = new LinearLayoutManager(
                                    mContext,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                            );
                            mRecyclerView.setLayoutManager(mLayoutManager);

                            mAdapter = new ColorsAdapter(mContext,img,pname,price,id,pName);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        presentShowcaseSequence();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SearchResults2.this,error.toString(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city);
                params.put("state",state);
                params.put("id",oid);
                params.put("name",pName);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void go(View view) {
        getLatLong(oid);
    }

    public static final String JSON_URL1 = "http://www.vendoee.com/android-scripts/getOffLoc.php";

    private void getLatLong(final String id) {

        final AlertDialog loading;

        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Fetching location...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        StringTokenizer str = new StringTokenizer(response,",");
                        int i=0;
                        String str1[] = new String[str.countTokens()];
                        while(str.hasMoreTokens())
                        {
                            str1[i++]=str.nextToken();
                        }
                        double lat = Double.parseDouble(str1[0]);
                        double lng = Double.parseDouble(str1[1]);


                        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?"+ "&daddr=" + lat + "," +lng));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(SearchResults2.this,"Error connecting to Server",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
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

}
