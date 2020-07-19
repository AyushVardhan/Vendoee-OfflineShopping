package vendoee.vvpvtltd.vendoee;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.Manifest;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class SplashScreen extends AppCompatActivity {

    private int LOCATION_PERMISSION_CODE = 23;
    static int t=0;
    private static int SPLASH_TIME_OUT = 1000;
    private static final int REQUEST_CODE_PERMISSION = 2;
    LocationManager locationManager;
    String latestVersion = "0.0";
    String version;
    String latestVersionCopy = "0.0";
    String versionCopy;
    String[] mPermission = {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                try {
                    //Toast.makeText(SplashScreen.this,"netowork issue1 - "+ isNetworkAvailable(),Toast.LENGTH_SHORT).show();
                    if(isNetworkAvailable()){
                        //Toast.makeText(SplashScreen.this,"loadnew2",Toast.LENGTH_SHORT).show();
                        PackageManager manager = getPackageManager();
                        PackageInfo info = null;
                        try {
                            info = manager.getPackageInfo(getPackageName(), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        version = info.versionName;
                        //version = "1.0";
                        versionCopy = version;
                        VersionChecker versionChecker = new VersionChecker();
                        try {
                            latestVersion = versionChecker.execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        //latestVersion = "2.7";
                        latestVersionCopy = latestVersion;
                    }else{
                        //Toast.makeText(SplashScreen.this,"netowork issue1",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SplashScreen.this, EnableInternet.class));
                    }
                    //Toast.makeText(SplashScreen.this,"loadnew40",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SplashScreen.this,"latest: "+ latestVersion,Toast.LENGTH_SHORT).show();
                    //Toast.makeText(SplashScreen.this,"current: "+ version,Toast.LENGTH_SHORT).show();
                    if(Integer.parseInt(latestVersionCopy.substring(0,latestVersionCopy.indexOf('.'))) == Integer.parseInt(version.substring(0,version.indexOf('.')))){

                        if(Double.parseDouble(latestVersion) == Double.parseDouble(version)){
                            int result = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION);
                            int result1 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            int result2 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_SMS);
                            int result3 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.RECEIVE_SMS);
                            int result4 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                            if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED){
                                //Toast.makeText(SplashScreen.this,"issue2 - "+ isNetworkAvailable()+ " " + checkGPS(),Toast.LENGTH_SHORT).show();
                                if(checkGPS()){
                                    if(isNetworkAvailable()){

                                        SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                        String mobile = sharedpreferences2.getString("number_C", "");

                                        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                        String citty = sharedpreferences.getString("Ccity", "");

                                        SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
                                        String sid = sharedpreferences1.getString("sid", "");

                                        if(!citty.isEmpty() && !mobile.isEmpty()){

                                            DatabaseHelper myDB = new DatabaseHelper(SplashScreen.this);
                                            myDB.dropTable();
                                            myDB.createTable();

                                            Intent in = new Intent(SplashScreen.this, CustomerSales.class);
                                            startActivity(in);
                                        }else if(!sid.isEmpty()){
                                            if(Integer.parseInt(sid)>0){
                                                checkCatSet(sid);
                                            }
                                        }else{
                                            Intent in = new Intent(SplashScreen.this,Welcome.class);
                                            startActivity(in);
                                        }

                                    }else{
                                        //Toast.makeText(SplashScreen.this,"netowork issue2",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SplashScreen.this, EnableInternet.class));
                                    }
                                }else{
                                    startActivity(new Intent(SplashScreen.this, gpsnotfound.class));
                                }

                            }else{

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

                                SharedPreferences preferences2=getSharedPreferences("CUSTOMER_CITY",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2=preferences2.edit();
                                editor2.clear();
                                editor2.commit();

                                SharedPreferences preferences3=getSharedPreferences("OTPV",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3=preferences3.edit();
                                editor3.clear();
                                editor3.commit();

                                SharedPreferences preferences4=getSharedPreferences("MOBNO",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor4=preferences4.edit();
                                editor4.clear();
                                editor4.commit();

                                SharedPreferences preferences5=getSharedPreferences("updateCh",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor5=preferences5.edit();
                                editor5.clear();
                                editor5.commit();

                                ActivityCompat.requestPermissions(SplashScreen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE},LOCATION_PERMISSION_CODE);
                            }
                        }else{

                            int result = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION);
                            int result1 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            int result2 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_SMS);
                            int result3 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.RECEIVE_SMS);
                            int result4 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                            if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED){
                                //Toast.makeText(SplashScreen.this,"issue5 - "+ isNetworkAvailable()+ " " + checkGPS(),Toast.LENGTH_SHORT).show();
                                if(checkGPS()){

                                    if(isNetworkAvailable()){

                                        startActivity(new Intent(SplashScreen.this,UpdateAppActivity.class));

                                    }else{
                                        //Toast.makeText(SplashScreen.this,"netowork issue5",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SplashScreen.this, EnableInternet.class));
                                    }
                                }else{
                                    startActivity(new Intent(SplashScreen.this, gpsnotfound.class));
                                }

                            }else{

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

                                SharedPreferences preferences2=getSharedPreferences("CUSTOMER_CITY",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2=preferences2.edit();
                                editor2.clear();
                                editor2.commit();

                                SharedPreferences preferences3=getSharedPreferences("OTPV",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor3=preferences3.edit();
                                editor3.clear();
                                editor3.commit();

                                SharedPreferences preferences4=getSharedPreferences("MOBNO",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor4=preferences4.edit();
                                editor4.clear();
                                editor4.commit();

                                SharedPreferences preferences5=getSharedPreferences("updateCh",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor5=preferences5.edit();
                                editor5.clear();
                                editor5.commit();

                                ActivityCompat.requestPermissions(SplashScreen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE},LOCATION_PERMISSION_CODE);
                            }

                        }

                    }else{
                        int result = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION);
                        int result1 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        int result2 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_SMS);
                        int result3 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.RECEIVE_SMS);
                        int result4 = ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE);

                        if (result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED){
                            //Toast.makeText(SplashScreen.this,"issue5 - "+ isNetworkAvailable()+ " " + checkGPS(),Toast.LENGTH_SHORT).show();
                            if(checkGPS()){

                                if(isNetworkAvailable()){

                                    if(Integer.parseInt(latestVersionCopy.substring(0,latestVersionCopy.indexOf('.'))) > Integer.parseInt(version.substring(0,version.indexOf('.')))){
                                        Intent in = new Intent(SplashScreen.this,UpdateAppActivity.class);
                                        in.putExtra("Secret","101");
                                        startActivity(in);
                                    }else{
                                        startActivity(new Intent(SplashScreen.this,UpdateAppActivity.class));
                                    }

                                }else{
                                    //Toast.makeText(SplashScreen.this,"netowork issue5",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SplashScreen.this, EnableInternet.class));
                                }
                            }else{
                                startActivity(new Intent(SplashScreen.this, gpsnotfound.class));
                            }

                        }else{

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

                            SharedPreferences preferences2=getSharedPreferences("CUSTOMER_CITY",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2=preferences2.edit();
                            editor2.clear();
                            editor2.commit();

                            SharedPreferences preferences3=getSharedPreferences("OTPV",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor3=preferences3.edit();
                            editor3.clear();
                            editor3.commit();

                            SharedPreferences preferences4=getSharedPreferences("MOBNO",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor4=preferences4.edit();
                            editor4.clear();
                            editor4.commit();

                            SharedPreferences preferences5=getSharedPreferences("updateCh",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor5=preferences5.edit();
                            editor5.clear();
                            editor5.commit();

                            ActivityCompat.requestPermissions(SplashScreen.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE},LOCATION_PERMISSION_CODE);
                        }
                    }
                    //Toast.makeText(SplashScreen.this,"loadnewlast",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, SPLASH_TIME_OUT);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == LOCATION_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED && grantResults[3]==PackageManager.PERMISSION_GRANTED && grantResults[4]==PackageManager.PERMISSION_GRANTED){

                if(Double.parseDouble(latestVersion) == Double.parseDouble(version)){
                    //Toast.makeText(SplashScreen.this,"issue4 - "+ isNetworkAvailable()+ " " + checkGPS(),Toast.LENGTH_SHORT).show();
                    if(checkGPS()){
                        if(isNetworkAvailable()){
                            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                            String mobile = sharedpreferences2.getString("number_C", "");

                            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                            String citty = sharedpreferences.getString("Ccity", "");

                            SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
                            String sid = sharedpreferences1.getString("sid", "");

                            if(!citty.isEmpty() && !mobile.isEmpty()){

                                DatabaseHelper myDB = new DatabaseHelper(SplashScreen.this);
                                myDB.dropTable();
                                myDB.createTable();

                                Intent in = new Intent(SplashScreen.this, CustomerSales.class);
                                startActivity(in);
                            }else if(!sid.isEmpty()){
                                if(Integer.parseInt(sid)>0){
                                    checkCatSet(sid);
                                }
                            }else{
                                Intent in = new Intent(SplashScreen.this,Welcome.class);
                                startActivity(in);
                            }
                        }else{
                            //Toast.makeText(SplashScreen.this,"netowork issue4",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashScreen.this, EnableInternet.class));
                        }
                    }else{
                        startActivity(new Intent(SplashScreen.this, gpsnotfound.class));
                    }
                }else{
                    //Toast.makeText(SplashScreen.this,"issue3 - "+ isNetworkAvailable()+ " " + checkGPS(),Toast.LENGTH_SHORT).show();
                    if(checkGPS()){
                        if(isNetworkAvailable()){

                            if(Integer.parseInt(latestVersionCopy.substring(0,latestVersionCopy.indexOf('.'))) > Integer.parseInt(version.substring(0,version.indexOf('.')))){
                                Intent in = new Intent(SplashScreen.this,UpdateAppActivity.class);
                                in.putExtra("Secret","101");
                                startActivity(in);
                            }else{
                                startActivity(new Intent(SplashScreen.this,UpdateAppActivity.class));
                            }

                        }else{
                            //Toast.makeText(SplashScreen.this,"netowork issue3",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SplashScreen.this, EnableInternet.class));
                        }
                    }else{
                        startActivity(new Intent(SplashScreen.this, gpsnotfound.class));
                    }
                }
            }else{
                Toast.makeText(this,"Please allow these permissions to proceed further!", Toast.LENGTH_LONG).show();
                finishAffinity();
            }
        }
    }

    private boolean checkGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkCatSet(final String result1) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/checkCat.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        int d = Integer.parseInt(response);

                        if (d>0) {


                            Intent in = new Intent(SplashScreen.this,RetailHome.class);
                            startActivity(in);

                        } else {

                            Intent in = new Intent(SplashScreen.this,showCat.class);
                            startActivity(in);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(SplashScreen.this,error.toString(),Toast.LENGTH_LONG).show();
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
