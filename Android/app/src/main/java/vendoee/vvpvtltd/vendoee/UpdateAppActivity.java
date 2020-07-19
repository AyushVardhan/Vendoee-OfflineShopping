package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class UpdateAppActivity extends AppCompatActivity {

    Bundle b; Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_update_app);

        b1 = (Button)findViewById(R.id.button5);

        b = getIntent().getExtras();
        if(b!=null){
            b1.setVisibility(View.INVISIBLE);
        }

    }

    public void toMain(View view) {

        SharedPreferences sharedpreferences122 = getSharedPreferences("updateCh", Context.MODE_PRIVATE);
        String ids = sharedpreferences122.getString("update", "");

        if(ids.equals("100")){
            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
            String mobile = sharedpreferences2.getString("number_C", "");

            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
            String citty = sharedpreferences.getString("Ccity", "");

            SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
            String sid = sharedpreferences1.getString("sid", "");

            if(!citty.isEmpty() && !mobile.isEmpty()){

                DatabaseHelper myDB = new DatabaseHelper(UpdateAppActivity.this);
                myDB.dropTable();
                myDB.createTable();
                Intent in = new Intent(UpdateAppActivity.this, CustomerSales.class);
                startActivity(in);

            }else if(!sid.isEmpty()){
                if(Integer.parseInt(sid)>0){
                    checkCatSet(sid);
                }
            }else{
                startActivity(new Intent(UpdateAppActivity.this,MainActivity.class));
            }
        }else{
            startActivity(new Intent(UpdateAppActivity.this,Welcome.class));
        }
    }

    public void openPlay(View view) {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void checkCatSet(final String result1) {

        final AlertDialog loading;
        loading = new SpotsDialog(UpdateAppActivity.this, R.style.Custom);
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

                            Intent in = new Intent(UpdateAppActivity.this,RetailHome.class);
                            startActivity(in);

                        } else {

                            Intent in = new Intent(UpdateAppActivity.this,showCat.class);
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
