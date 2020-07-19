package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ListView;
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

public class CustomerRequestedProduct extends AppCompatActivity {

    ArrayList<OfferRequestCustModel> dataModels;
    ListView listView;  String phonenum;
    private static OfferRequestCustAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_requested_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Requested Offers");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
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

        listView=(ListView)findViewById(R.id.list);
        dataModels= new ArrayList<>();

        SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        String mobile = sharedpreferences2.getString("number_C", "");

        phonenum = mobile.substring(3,mobile.length());
        getRequests(phonenum);
    }

    private void getRequests(final String phonenum) {

        final AlertDialog loading;

        loading = new SpotsDialog(CustomerRequestedProduct.this, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getProductRequests.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(ProductsRequestRetailer.this,response,Toast.LENGTH_LONG).show();
                        StringTokenizer str = new StringTokenizer(response,";");
                        if(str.countTokens()>0){
                            while(str.hasMoreElements()){
                                String str1 = str.nextToken();
                                StringTokenizer str2 = new StringTokenizer(str1,"||");
                                String data[] = new String[str2.countTokens()]; int i =0;
                                while(str2.hasMoreElements()){
                                    data[i] = str2.nextToken();
                                    i++;
                                }

                                dataModels.add(new OfferRequestCustModel(data[0],data[1],data[2],data[3]));
                            }

                            adapter= new OfferRequestCustAdapter(dataModels,CustomerRequestedProduct.this);
                            listView.setAdapter(adapter);

                        }else{
                            Toast.makeText(CustomerRequestedProduct.this,"No Product Request!",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(CustomerRequestedProduct.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}