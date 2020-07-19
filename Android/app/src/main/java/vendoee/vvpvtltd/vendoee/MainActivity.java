package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity{

    Button c,r;MaterialSpinner spinner;
    LocationManager locationManager;
    static int gps = 0;
    EditText txt; Dialog settingdialog;
    String city,cities[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }


        SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        String mobile = sharedpreferences2.getString("number_C", "");

        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        String citty = sharedpreferences.getString("Ccity", "");

        SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
        String sid = sharedpreferences1.getString("sid", "");

        if(!citty.isEmpty() && !mobile.isEmpty()){

            DatabaseHelper myDB = new DatabaseHelper(this);
            myDB.dropTable();
            myDB.createTable();

            Intent in = new Intent(MainActivity.this, CustomerSales.class);
            startActivity(in);
        }else if(!sid.isEmpty()){
            if(Integer.parseInt(sid)>0){
                checkCatSet(sid);
            }
        }

        AnimationDrawable animationDrawable =(AnimationDrawable) findViewById(R.id.activity_role_selection_layout).getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1000);

        animationDrawable.start();

        c = (Button)findViewById(R.id.customer);
        r = (Button)findViewById(R.id.retail);
        ImageView logo = (ImageView) findViewById(R.id.imageView);
        View horizontalLine = (View) findViewById(R.id.horizontalLine);
        TextView selectRole = (TextView) findViewById(R.id.select_role);
        Animation animationCustomer = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.customer); ;

        Animation animationRetailer = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.retailer); ;

        Animation animationLogo = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.logo);

        Animation animationHorizontalLine = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.horizontal_line);

        Animation animationSelectRole = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.select_role);

        c.startAnimation(animationCustomer);
        r.startAnimation(animationRetailer);
        logo.startAnimation(animationLogo);
        horizontalLine.startAnimation(animationHorizontalLine);
        selectRole.startAnimation(animationSelectRole);


        //getcities();
        /*
        SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        String mobile = sharedpreferences2.getString("number_C", "");

        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        String citty = sharedpreferences.getString("Ccity", "");
        if(!citty.isEmpty() && !mobile.isEmpty()){
            Intent in = new Intent(MainActivity.this, CustomerSales.class);
            startActivity(in);
        }

        SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
        String sid = sharedpreferences1.getString("sid", "");
        //Toast.makeText(MainActivity.this,"SID "+sid,Toast.LENGTH_SHORT).show();
        if(!sid.isEmpty()){
            if(Integer.parseInt(sid)>0){
                checkCatSet(sid);
            }
        }
        */

    }

     AlertDialog loading;
    public void getcities(){

        loading = new SpotsDialog(MainActivity.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getCities.php",
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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(MainActivity.this,"Can't load cities! Please try again.",Toast.LENGTH_LONG).show();
                    }
                }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    Toast toast;
    public static int APP_REQUEST_CODE = 99;
    public void GotoCustomer(View view) {

        SharedPreferences sharedpreferences = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        String mobile = sharedpreferences.getString("number_C", "");

        if(mobile.isEmpty()){
            final Intent intent = new Intent(this, AccountKitActivity.class);
            AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                    new AccountKitConfiguration.AccountKitConfigurationBuilder(
                            LoginType.PHONE,
                            AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
            configurationBuilder.setReadPhoneStateEnabled(true);
            configurationBuilder.setReceiveSMS(true);

            intent.putExtra(
                    AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                    configurationBuilder.build());
            startActivityForResult(intent, APP_REQUEST_CODE);

        }else{
            startActivity(new Intent(MainActivity.this,ChooseCity1.class));
        }

    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            String toastMessage= "";
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(
                        this,
                        toastMessage,
                        Toast.LENGTH_LONG)
                        .show();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";

            } else {
                if (loginResult.getAccessToken() != null) {
                    //toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    startActivity(new Intent(MainActivity.this,ChooseCity1.class));
                }
            }
            // Surface the result to your user in an appropriate way.


            // desired intent
        }
    }



    Toast toast1,toast2;

    String log;

    public void GotoRetailer(View view) {

            SharedPreferences sharedpreferences = getSharedPreferences("LOG", Context.MODE_PRIVATE);
            log = sharedpreferences.getString("out", "");
            //Toast.makeText(MainActivity.this,log,Toast.LENGTH_SHORT).show();
            if(log.equals("")){
                //Toast.makeText(MainActivity.this,"LoggedOut",Toast.LENGTH_SHORT).show();
                Intent in=new Intent(MainActivity.this, RetailerSignIn.class);
                startActivity(in);
            }else{

                SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
                String sid = sharedpreferences1.getString("sid", "");

                checkCatSet(sid);
                //Intent in=new Intent(MainActivity.this, RetailHome.class);
                //
                // startActivity(in);
            }
    }

    Toast toast3;
    DatabaseHelper myDB;

    public void GotoManufacturer(View view) {
        Toast.makeText(MainActivity.this,"Manufacturer",Toast.LENGTH_SHORT).show();
    }

    private void loadOffers(final String city) {

        final AlertDialog loading;
        loading = new SpotsDialog(MainActivity.this, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/offerSales2EP.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        String str = "0";
                        String result = response;
                        //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        if(result.isEmpty()){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("No sale have been launched yet. Please try again later!");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }else{
                            if (result.matches(str)) {
                                Toast.makeText(MainActivity.this, "No Results! Please try later.", Toast.LENGTH_SHORT).show();
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

                                    //Toast.makeText(MainActivity.this, id +" "+str3[17], Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(MainActivity.this, proCat +" "+price + " "+ oprice + " " + sdate + " ", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(MainActivity.this, edate +" "+shop + " "+ desc + " ", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(MainActivity.this, img, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(MainActivity.this, str3[16], Toast.LENGTH_SHORT).show();

                                    isInserted = myDB.insertOffer(id,sid,pname,sellCat,proCat, Double.parseDouble(price),Double.parseDouble(oprice),sdate,edate,shop,desc,img,Double.parseDouble(dis),lat,lng,contact,str3[16],str3[17],str3[18]);



                                }
                                if(isInserted){
                                    //Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor1 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
                                    editor1.putString("Ccity", city);
                                    editor1.commit();

                                    Intent in = new Intent(MainActivity.this, CustomerSales.class);
                                    startActivity(in);
                                }else{
                                    //Toast.makeText(MainActivity.this,"Error connecting to Server", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(MainActivity.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city);
                params.put("catid","1");
                params.put("itemCount","3");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    boolean isInserted;

    private boolean doubleBackToExitPressedOnce;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
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

    public void toCustomer(View view) {

        if(spinner.getText().toString().isEmpty()){
            Toast.makeText(this, "Please choose a city!", Toast.LENGTH_SHORT).show();
        }else{
            settingdialog.dismiss();
            city = spinner.getText().toString();
            SharedPreferences.Editor editor1 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
            editor1.putString("Ccity", city);
            editor1.commit();
            //Intent in = new Intent(MainActivity.this, CustomerSales.class);
            //startActivity(in);
            myDB = new DatabaseHelper(this);
            myDB.dropTable();
            myDB.createTable();
            //Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
            //loadOffers(city);

            Intent in = new Intent(MainActivity.this, CustomerSales.class);
            startActivity(in);
        }
    }

    private void checkCatSet(final String result1) {

        final AlertDialog loading;
        loading = new SpotsDialog(MainActivity.this, R.style.Custom);
        loading.setMessage("Loading...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/checkCat.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        int d = Integer.parseInt(response);

                        if (d>0) {

                            myDB = new DatabaseHelper(MainActivity.this);
                            myDB.dropTable2();
                            myDB.createTable2();

                            stopService(new Intent(MainActivity.this, TotalSaleServiceNotify.class));
                            startService(new Intent(MainActivity.this, TotalRetailerNotify.class));

                            SharedPreferences.Editor editor = getSharedPreferences("SIDSERVICE", MODE_PRIVATE).edit();
                            editor.putString("sidService", result1);
                            editor.commit();

                            Intent in = new Intent(MainActivity.this,RetailHome.class);
                            startActivity(in);

                        } else {

                            Intent in = new Intent(MainActivity.this,showCat.class);
                            startActivity(in);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(MainActivity.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid",result1);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
