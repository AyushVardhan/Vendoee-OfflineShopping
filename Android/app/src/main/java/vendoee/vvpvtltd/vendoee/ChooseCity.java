package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class ChooseCity extends AppCompatActivity {

    String city,cities[];
    MaterialSpinner spinner;
    DatabaseHelper myDB;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);

        phone = getIntent().getStringExtra("phone");
        //Toast.makeText(ChooseCity.this,phone,Toast.LENGTH_SHORT).show();
        getcities();
    }

    AlertDialog loading;
    public void getcities(){
        final AlertDialog loading;

        loading = new SpotsDialog(ChooseCity.this, R.style.Custom);
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
                        if(cities.length != 0){
                            spinner = (MaterialSpinner)findViewById(R.id.spinner);
                            spinner.setItems(cities);
                        }
                        else{
                            Toast.makeText(ChooseCity.this,"No sale in any City!",Toast.LENGTH_SHORT).show();

                            Intent in = new Intent(ChooseCity.this, MainActivity.class);
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
                        Toast.makeText(ChooseCity.this,"Can't load cities! Please try again.",Toast.LENGTH_LONG).show();
                    }
                }){

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    private void loadOffers(final String city) {

        final AlertDialog loading;

        loading = new SpotsDialog(ChooseCity.this, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/offerSales1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "0";
                        String result = response;
                        //Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                        if(result.isEmpty()){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ChooseCity.this);
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
                                Toast.makeText(ChooseCity.this, "No Results!", Toast.LENGTH_SHORT).show();
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

                                        SharedPreferences.Editor editor2 = getSharedPreferences("OTPV", MODE_PRIVATE).edit();
                                        editor2.putString("otpv", "done");
                                        editor2.commit();

                                    }else{
                                        //Toast.makeText(ChooseCity.this,"Error connecting to Server", Toast.LENGTH_SHORT).show();
                                        //Toast.makeText(ChooseCity.this,"Error connecting to Server", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                if(isInserted && phone!=null){
                                    sendPhone(phone);
                                }else if(isInserted){
                                    Intent in = new Intent(ChooseCity.this,CustomerSales.class);
                                    startActivity(in);
                                }
                            }
                        }
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(ChooseCity.this,"Error connecting to Server2" + error,Toast.LENGTH_LONG).show();
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
        final EditText phoneNo = (EditText) findViewById(R.id.ETPhoneNo);
        final AlertDialog loading;

        loading = new SpotsDialog(ChooseCity.this, R.style.Custom);
        loading.setMessage("Loading...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/otpv.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.equals("1")){
                            Intent in = new Intent(ChooseCity.this,CustomerSales.class);
                            startActivity(in);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(ChooseCity.this,"Error connecting to Server", Toast.LENGTH_LONG).show();
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

        if(spinner.getText().toString().isEmpty()){
            Toast.makeText(this, "Please choose a city!", Toast.LENGTH_SHORT).show();
        }else{
            city = spinner.getText().toString();
            myDB = new DatabaseHelper(this);
            myDB.dropTable();
            myDB.createTable();
            //Toast.makeText(this, city, Toast.LENGTH_SHORT).show();
            loadOffers(city);
        }
    }

}
