package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

import static vendoee.vvpvtltd.vendoee.MainActivity.APP_REQUEST_CODE;

public class CustomerProfile extends AppCompatActivity {

    TextInputLayout password;
    TextView pass,recover,points,cont,updateContact;
    String phone;
    EditText name,address,email,passC,number;
    GPSTracker gps; Bundle b; String mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_HOME_AS_UP);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 5;
        */

        points = (TextView)findViewById(R.id.pointC);
        name = (EditText)findViewById(R.id.cusName);
        address = (EditText)findViewById(R.id.cusAddress);
        email = (EditText)findViewById(R.id.cusEmail);
        password = (TextInputLayout) findViewById(R.id.cusPassword);
        passC = (EditText)findViewById(R.id.passC);
        recover = (TextView)findViewById(R.id.recover);
        //pass = (TextView)findViewById(R.id.pass1);
        cont = (TextView)findViewById(R.id.contactNo);
        updateContact = (TextView)findViewById(R.id.updateMobile);

        gps = new GPSTracker(CustomerProfile.this);

        b = getIntent().getExtras();
        if(b!=null){
            mark = b.getString("EditPro");
        }else{
            mark = "update";
        }

        updateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent intent = new Intent(CustomerProfile.this, AccountKitActivity.class);
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
        });

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerProfile.this,RecoverCustomer.class));
            }
        });

        SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        String mobile = sharedpreferences21.getString("number_C", "");
        cont.setText(mobile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        phone = mobile.substring(3,mobile.length());

        //Toast.makeText(CustomerProfile.this,"Phone: "+phone,Toast.LENGTH_SHORT).show();

        getCustomerDets(phone);

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
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    //toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    getAccount();
                }
            }


            // desired intent
        }
    }
    String phoneNumberString;
    private void getAccount(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                phoneNumberString = phoneNumber.toString();

                SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences21.getString("number_C", "");
                String newc = phoneNumberString;

                updateCont(newc.substring(3,newc.length()),mobile.substring(3,mobile.length()));
                // Surface the result to your user in an appropriate way.
                /*
                Toast.makeText(
                        getBaseContext(),
                        phoneNumberString+"Ayush",
                        Toast.LENGTH_LONG)
                        .show();
                */
                //SharedPreferences.Editor editor1 = getSharedPreferences("MOBNO", MODE_PRIVATE).edit();
                //editor1.putString("number_C", phone);
                //editor1.commit();
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

    private void updateCont(final String newcontact, final String oldcontact) {

        //Toast.makeText(CustomerProfile.this,newcontact+" "+oldcontact,Toast.LENGTH_SHORT).show();
        final AlertDialog loading;

        loading = new SpotsDialog(CustomerProfile.this, R.style.Custom);
        loading.setMessage("Updating contact...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/CustomerUpdateContact.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.equals("1")){
                            SharedPreferences.Editor editor1 = getSharedPreferences("MOBNO", MODE_PRIVATE).edit();
                            editor1.putString("number_C", phoneNumberString);
                            editor1.commit();

                            Toast.makeText(CustomerProfile.this,"Contact Updated!",Toast.LENGTH_SHORT).show();
                            finish();
                            //getCustomerDets(newcontact);
                        }else{
                            //Toast.makeText(CustomerProfile.this,response,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CustomerProfile.this,error.toString(),Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oldcontact",oldcontact);
                params.put("newcontact",newcontact);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getAddress1(final String lat, final String lng) {
        final AlertDialog loading;

        loading = new SpotsDialog(CustomerProfile.this, R.style.Custom);
        loading.setMessage("Getting your address...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/GetAddr.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
//                        Toast.makeText(RetailerSignup.this,response,Toast.LENGTH_SHORT).show();

                        StringTokenizer str = new StringTokenizer(response,"||");
                        String str1[] = new String[str.countTokens()];
                        int i =0;
                        while(str.hasMoreTokens()){
                            str1[i] = str.nextToken();
                            i++;
                        }
                        //                      Toast.makeText(RetailerSignup.this,"Length: " +str1.length,Toast.LENGTH_SHORT).show();

                        if(str1.length>1){
                            address.setText(str1[0]);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //finish();
                        //Toast.makeText(CustomerProfile.this,"Please check internet connection!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("lat", lat);
                params.put("lng", lng);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getCustomerDets(final String ph) {
        final AlertDialog loading;

        loading = new SpotsDialog(CustomerProfile.this, R.style.Custom);
        loading.setMessage("Checking profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getCustomerDets.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        StringTokenizer str = new StringTokenizer(response,";");
                        String data[] = new String[str.countTokens()];int i = 0;
                        while(str.hasMoreElements()){
                            data[i] = str.nextToken();
                            i++;
                        }

                        String Name = data[0]; String Address = data[1]; String Email = data[2]; String Password = data[3]; String poin = data[4];
                        if(!Name.equals("vendoee")){
                            name.setText(Name);
                        }else{
                            name.setText("");
                        }

                        if(!Address.equals("vendoee")){
                            address.setText(Address);
                        }else{
                            String lat = String.valueOf(gps.getLatitude());
                            String lng = String.valueOf(gps.getLongitude());
                            getAddress1(lat,lng);
                        }

                        if(!Email.equals("vendoee")){
                            email.setText(Email);
                        }else{
                            email.setText("");
                        }

                        if(Password.equals("vendoee")){
                            password.setVisibility(View.VISIBLE);
                        }

                        if(Email.equals("vendoee")&&Password.equals("vendoee")){
                            Toast.makeText(CustomerProfile.this,"Complete profile to avail all feature!",Toast.LENGTH_SHORT).show();
                        }

                        points.setText(poin);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CustomerProfile.this,"Cant connect to server! Try Again",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",ph);
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

    public void UpdateCustomerProfile(View view) {
        if(password.isShown()){
            if(name.getText().toString().isEmpty()||address.getText().toString().isEmpty()||email.getText().toString().isEmpty()||passC.getText().toString().isEmpty()){
                Toast.makeText(CustomerProfile.this,"All fields are required!",Toast.LENGTH_SHORT).show();
            }else{
                final AlertDialog loading;

                loading = new SpotsDialog(CustomerProfile.this, R.style.Custom);
                loading.setMessage("Updating profile...");
                loading.setCanceledOnTouchOutside(false);
                loading.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/updateCustomer.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                if(response.equals("1")){

                                    password.setVisibility(View.INVISIBLE);
                                    //pass.setVisibility(View.INVISIBLE);

                                    Toast.makeText(CustomerProfile.this,"Profile Updated!",Toast.LENGTH_SHORT).show();
                                    if(mark.equals("vendoee")){
                                        startActivity(new Intent(CustomerProfile.this,RequestProduct.class));
                                    }else{
                                        finish();
                                    }
                                }else{
                                    //Toast.makeText(CustomerProfile.this,response,Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CustomerProfile.this,"Cant connect to server! Try Again",Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("name",name.getText().toString());
                        params.put("address",address.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("password",passC.getText().toString());
                        params.put("contact",phone);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }else{
            if(name.getText().toString().isEmpty()||address.getText().toString().isEmpty()||email.getText().toString().isEmpty()){
                Toast.makeText(CustomerProfile.this,"All fields are required!",Toast.LENGTH_SHORT).show();
            }else{
                final AlertDialog loading;

                loading = new SpotsDialog(CustomerProfile.this, R.style.Custom);
                loading.setMessage("Updating profile...");
                loading.setCanceledOnTouchOutside(false);
                loading.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/updateCustomer.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                loading.dismiss();
                                if(response.equals("1")){
                                    Toast.makeText(CustomerProfile.this,"Profile Updated!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    //Toast.makeText(CustomerProfile.this,response,Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(CustomerProfile.this,"Cant connect to server! Try Again",Toast.LENGTH_SHORT).show();
                                loading.dismiss();
                            }
                        }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("name",name.getText().toString());
                        params.put("address",address.getText().toString());
                        params.put("email",email.getText().toString());
                        params.put("contact",phone);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
    }

    public void passwordforgot(View view) {
        String url =  "http://www.vendoee.com/password/reset";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
