package vendoee.vvpvtltd.vendoee;

import android.*;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;
import dmax.dialog.SpotsDialog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class RetailerSignIn extends AppCompatActivity {

    TextView email; EditText pass; Button button3;
    ImageView viewp; static int tmp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        button3 = (Button)findViewById(R.id.button3);
        pass = (EditText)findViewById(R.id.password);
        viewp = (ImageView)findViewById(R.id.viewp);
        email = (TextView)findViewById(R.id.email);
        email.setFocusable(true);
        email.requestFocus();

        presentShowcaseSequence();
    }

    LocationManager locationManager;
    static int gps = 0;
    public static int APP_REQUEST_CODE = 99;
    DatabaseHelper myDB;

    public void goToRegister(View view) {
        GPSTracker gpss = new GPSTracker(RetailerSignIn.this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        double z = 0.0;
        double latitude = gpss.getLatitude();
        double longitude = gpss.getLongitude();
        int retval1 = Double.compare(latitude, z);
        int retval2 = Double.compare(longitude, z);
        if(retval1 == 0 || retval2 == 0){

            Toast.makeText(RetailerSignIn.this,"Location can't be fetched! Please try again.",Toast.LENGTH_SHORT).show();

        }else {
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
                    getAccount();
                }
            }
            // Surface the result to your user in an appropriate way.


            // desired intent
        }
    }

    private void getAccount(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                String phoneNumberString = phoneNumber.toString();
                String mobile = phoneNumberString.substring(3,phoneNumberString.length());
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent intent=new Intent(RetailerSignIn.this,RetailerSignup.class );
                    intent.putExtra("Mobile",mobile);
                    startActivity(intent);
                } else {
                    Intent in = new Intent(RetailerSignIn.this,gpsnotfound.class);
                    startActivity(in);
                }
            }

            @Override
            public void onError(final AccountKitError error) {
                Log.e("AccountKit",error.toString());
                // Handle Error
                /*
                Toast.makeText(
                        getBaseContext(), error.toString(),
                        Toast.LENGTH_LONG)
                        .show();
                */
            }
        });
    }
    private static final String SHOWCASE_ID = "sequence example1";
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);


        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(button3)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#e5f44336"))
                        .setContentText("Register your shop first..")
                        .withRectangleShape()
                        .build()
        );

        sequence.start();

    }

    public void loginRetailer(View view) {
        EditText email = (EditText)findViewById(R.id.email);
        EditText pass = (EditText)findViewById(R.id.password);

        int chk = 0;

        String regex = "[0-9]+";
        if(email.getText().toString().matches(regex)){
            if(email.getText().length()!=10){
                Toast.makeText(RetailerSignIn.this,"Enter Mobile No. correct length!",Toast.LENGTH_SHORT).show();
                chk = 1;
            }
        }else{
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email.getText().toString());
            if(m.matches()){

            }else{
                Toast.makeText(RetailerSignIn.this,"Enter Email correctly!",Toast.LENGTH_SHORT).show();
                chk = 1;
            }
        }

        if(email.getText().toString().isEmpty()){
            chk = 1;
            email.setError("Mandatory field!");
        }

        if(pass.getText().toString().isEmpty()){
            chk = 1;
            pass.setError("Mandatory field!");
        }

        if(chk==0){
            getSID(email.getText().toString(),pass.getText().toString());
        }
    }

    private void getSID(final String email, final String pass) {
        final AlertDialog loading;
        loading = new SpotsDialog(RetailerSignIn.this, R.style.CustomR);
        loading.setMessage("Loading...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getIDretLogin.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "0";
                        if (response.matches(str)) {
                            Toast.makeText(RetailerSignIn.this, "No Results! Please register.", Toast.LENGTH_SHORT).show();
                        } else {

                            SharedPreferences.Editor editor = getSharedPreferences("SID", MODE_PRIVATE).edit();
                            editor.putString("sid", response);
                            editor.commit();

                            SharedPreferences.Editor editor1 = getSharedPreferences("LOG", MODE_PRIVATE).edit();
                            editor1.putString("out", "LoggedIn");
                            editor1.commit();

                            checkCatSet(response);
                        }

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(RetailerSignIn.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email",email);
                params.put("pass", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(RetailerSignIn.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }


    private void checkCatSet(final String result1) {

        final AlertDialog loading;
        loading = new SpotsDialog(RetailerSignIn.this, R.style.CustomR);
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

                            myDB = new DatabaseHelper(RetailerSignIn.this);
                            myDB.dropTable2();
                            myDB.createTable2();

                            stopService(new Intent(RetailerSignIn.this, TotalSaleServiceNotify.class));
                            startService(new Intent(RetailerSignIn.this, TotalRetailerNotify.class));

                            SharedPreferences.Editor editor = getSharedPreferences("SIDSERVICE", MODE_PRIVATE).edit();
                            editor.putString("sidService", result1);
                            editor.commit();

                            Intent in = new Intent(RetailerSignIn.this,RetailHome.class);
                            startActivity(in);

                        } else {

                            Intent in = new Intent(RetailerSignIn.this,showCat.class);
                            startActivity(in);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(RetailerSignIn.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    public void ForGotPass(View view) {
        String url =  "http://www.vendoee.com/seller/password/reset";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        BackPressed();
        return true;
    }

    private void BackPressed() {
        Intent intent = new Intent(RetailerSignIn.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
