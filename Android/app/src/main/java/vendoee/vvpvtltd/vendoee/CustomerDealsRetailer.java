package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class CustomerDealsRetailer extends AppCompatActivity {

    ArrayList<OfferRetailerCustomerDealModel> dataModels;
    ListView listView;
    private static OfferRetailerCustomerDealAdapter adapter;
    TextView notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_deals);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Deals");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_HOME_AS_UP);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 5;

        notify = (TextView)findViewById(R.id.notify);
        listView=(ListView)findViewById(R.id.list);
        dataModels= new ArrayList<>();

        SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
        String sid = sharedpreferences1.getString("sid", "");
        getRequests(sid);

    }

    private void getRequests(final String sid) {
        final AlertDialog loading;

        loading = new SpotsDialog(CustomerDealsRetailer.this, R.style.CustomR);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getCustomerDealRequests.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        notify.setVisibility(View.INVISIBLE);
                        StringTokenizer str = new StringTokenizer(response,";");
                        if(str.countTokens()>0){
                            while(str.hasMoreElements()){
                                String str1 = str.nextToken();
                                StringTokenizer str2 = new StringTokenizer(str1,"||");
                                String data[] = new String[str2.countTokens()];int i =0;
                                while(str2.hasMoreElements()){
                                    data[i] = str2.nextToken();
                                    i++;
                                }

                                byte [] encodeByte= Base64.decode(data[2],Base64.DEFAULT);
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                dataModels.add(new OfferRetailerCustomerDealModel(data[0],data[1],bitmap,data[3],data[4],data[5]));
                            }

                            adapter= new OfferRetailerCustomerDealAdapter(dataModels,CustomerDealsRetailer.this);
                            listView.setAdapter(adapter);
                        }else{
                            notify.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(CustomerDealsRetailer.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid",sid);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
