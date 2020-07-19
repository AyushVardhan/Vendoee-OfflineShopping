package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
import com.facebook.accountkit.PhoneNumber;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class ChooseCity1 extends AppCompatActivity {

    String city,cities[],state[];
    MaterialSpinner spinner,spinner1;
    DatabaseHelper myDB; LinearLayout fetch;
    String phone; String phoneNumberString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.activity_choose_city1);

        fetch = (LinearLayout)findViewById(R.id.reloads);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner1 = (MaterialSpinner)findViewById(R.id.spinnerState);
        phone = getIntent().getStringExtra("phone");
        //Toast.makeText(ChooseCity.this,phone,Toast.LENGTH_SHORT).show();
        getAccount();

        spinner.setText("City");
        spinner1.setText("State");

        spinner1.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(spinner1.getText().toString().equals("State")){
                    Toast.makeText(ChooseCity1.this, "Select a state!", Toast.LENGTH_SHORT).show();
                }else{
                    getcities(spinner1.getText().toString());
                }
            }
        });
    }

    /**
     * Gets current account from Facebook Account Kit which include user's phone number.
     */
    private void getAccount(){
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                // Get Account Kit ID
                String accountKitId = account.getId();

                // Get phone number
                PhoneNumber phoneNumber = account.getPhoneNumber();
                phoneNumberString = phoneNumber.toString();

                getstate(phoneNumberString.substring(3,phoneNumberString.length()));
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

    private void getstate(final String substring) {
        loading = new SpotsDialog(ChooseCity1.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getStates.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        StringTokenizer str = new StringTokenizer(response,",");
                        state = new String[str.countTokens()];
                        int i=0;

                        SharedPreferences.Editor editor1 = getSharedPreferences("MOBNO", MODE_PRIVATE).edit();
                        editor1.putString("number_C", phoneNumberString);
                        editor1.commit();

                        if(str.countTokens()==0){
                            //Toast.makeText(MainActivity.this,"No sale in any City!",Toast.LENGTH_SHORT).show();
                        }else{

                            while(str.hasMoreTokens()){
                                String str1 = str.nextToken();
                                state[i] = str1;
                                i++;
                            }
                        }
                        if(state.length != 0){
                            spinner1.setItems(state);
                            getcities(spinner1.getText().toString());
                        }
                        else{
                            Toast.makeText(ChooseCity1.this,"No sale in any City!",Toast.LENGTH_SHORT).show();

                            Intent in = new Intent(ChooseCity1.this, MainActivity.class);
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
                        Toast.makeText(ChooseCity1.this,"Can't load States! Please try again.",Toast.LENGTH_LONG).show();
                        fetch.setVisibility(View.VISIBLE);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phoneNo",substring);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    AlertDialog loading;
    public void getcities(final String s){

        loading = new SpotsDialog(ChooseCity1.this, R.style.Custom);
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
                            spinner.setItems(cities);
                        }
                        else{
                            Toast.makeText(ChooseCity1.this,"No sale in any City!",Toast.LENGTH_SHORT).show();

                            Intent in = new Intent(ChooseCity1.this, MainActivity.class);
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
                        Toast.makeText(ChooseCity1.this,"Can't load cities! Please try again.",Toast.LENGTH_LONG).show();
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

    private void loadOffers(final String city) {

        final AlertDialog loading;

        loading = new SpotsDialog(ChooseCity1.this, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/offerSales2EP.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "0";
                        String result = response;
                        //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        if(result.isEmpty()){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ChooseCity1.this);
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
                                Toast.makeText(ChooseCity1.this, "No Results!", Toast.LENGTH_SHORT).show();
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

                                    if(isInserted){
                                        //Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                                        SharedPreferences.Editor editor1 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
                                        editor1.putString("Ccity", city);
                                        editor1.commit();

                                        SharedPreferences.Editor editor3 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
                                        editor3.putString("state", spinner1.getText().toString());
                                        editor3.commit();

                                        SharedPreferences.Editor editor2 = getSharedPreferences("OTPV", MODE_PRIVATE).edit();
                                        editor2.putString("otpv", "done");
                                        editor2.commit();

                                    }else{
                                        //Toast.makeText(ChooseCity.this,"Error connecting to Server", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(ChooseCity.this,"Error connecting to Server", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                if(isInserted){
                                    startService(new Intent(ChooseCity1.this, TotalSaleServiceNotify.class));
                                    Intent in = new Intent(ChooseCity1.this,CustomerSales.class);
                                    startActivity(in);
                                }
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(ChooseCity.this,error.toString()+"Loadoffer Failed",Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ChooseCity1.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    boolean isInserted;
    private void sendPhone(final String phone) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/otpv.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(ChooseCity.this,response+": ChooseCity Sucess",Toast.LENGTH_SHORT).show();
                        if(response.equals("1")){
                            Intent in = new Intent(ChooseCity1.this,CustomerSales.class);
                            startActivity(in);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(ChooseCity1.this,"Error connecting to Server", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("ph",phone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void toCustomer(View view) {

        if(spinner.getText().toString().equals("City") || spinner1.getText().toString().equals("State")){
            Toast.makeText(this, "Please choose a city!", Toast.LENGTH_SHORT).show();
        }else{
            city = spinner.getText().toString();
            myDB = new DatabaseHelper(this);
            myDB.dropTable();
            myDB.createTable();

            SharedPreferences.Editor editor1 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
            editor1.putString("Ccity", city);
            editor1.commit();

            SharedPreferences.Editor editor3 = getSharedPreferences("CUSTOMER_CITY", MODE_PRIVATE).edit();
            editor3.putString("state", spinner1.getText().toString());
            editor3.commit();

            SharedPreferences.Editor editor2 = getSharedPreferences("OTPV", MODE_PRIVATE).edit();
            editor2.putString("otpv", "done");
            editor2.commit();

            stopService(new Intent(ChooseCity1.this, TotalRetailerNotify.class));
            startService(new Intent(ChooseCity1.this, TotalSaleServiceNotify.class));
            Intent in = new Intent(ChooseCity1.this,CustomerSales.class);
            startActivity(in);
            //Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
            //loadOffers(city);
        }
    }

    public void reloadState(View view) {
        getstate(phoneNumberString.substring(3,phoneNumberString.length()));
    }
}
