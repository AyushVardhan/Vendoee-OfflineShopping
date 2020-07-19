package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class CustomerDeals extends AppCompatActivity {

    TextView notify;String maxid = "0" ;
    private RecyclerView recyclerView;private boolean loadin;
    private CustomerDealAdapter adapter; DatabaseHelper myDB; Cursor res;
    private List<CustomerDealModel> retailersToCustomerList;
    private boolean checkForNext = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_deals);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle("Deals");
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);

        retailersToCustomerList=new ArrayList<>();
        notify = (TextView)findViewById(R.id.note1);
        recyclerView=(RecyclerView)findViewById(R.id.category_recyclerView);

        adapter=new CustomerDealAdapter(CustomerDeals.this,retailersToCustomerList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CustomerDeals.this, LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
        final String mobile = sharedpreferences21.getString("number_C", "");
        getDeals(mobile.substring(3,mobile.length()));


        adapter.notifyDataSetChanged();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getAdapter().getItemCount() >= 3) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if ( loadin == false && lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                    {
                        if(checkForNext==true){
                            retailersToCustomerList.add(null);
                            adapter.notifyItemInserted(retailersToCustomerList.size() - 1);
                            loadin = true;
                        }

                        getDeals1(mobile.substring(3,mobile.length()));
                    }
                }
            }

        });
    }

    private void getDeals(final String ph) {
        final AlertDialog loading;

        loading = new SpotsDialog(CustomerDeals.this, R.style.Custom);
        loading.setMessage("Checking profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getCustomerDeals.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        StringTokenizer st=new StringTokenizer(response,";");
                        String str[];

                        if(st.countTokens()>0){
                            while(st.hasMoreTokens()){
                                int i=0;
                                String item1=st.nextToken();
                                //Log.v("Hells",item1);
                                StringTokenizer st1=new StringTokenizer(item1,"||");
                                str=new String[st1.countTokens()];
                                while (st1.hasMoreTokens()){
                                    str[i++]=st1.nextToken();
                                }

                                String oid = str[0]; String sid = str[1]; String pname = str[2]; String oprce = str[3];
                                String sname = str[4]; String start = str[6]; String end = str[7]; String disc = str[8];
                                String sc = str[9]; String tid = str[10];
                                maxid = tid;

                                String nextCheck = str[11];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }

                                byte [] encodeByte= Base64.decode(str[5], Base64.DEFAULT);
                                Bitmap img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                //Toast.makeText(CustomerDeals.this,pname + " " + maxid,Toast.LENGTH_SHORT).show();

                                CustomerDealModel a=new CustomerDealModel(oid,sid,pname,oprce,sname,img,start,end,disc,sc);
                                retailersToCustomerList.add(a);
                            }
                        }else{
                            notify.setVisibility(View.VISIBLE);
                        }

                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(CustomerDeals.this,error.toString(),Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",ph);
                params.put("mid",maxid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getDeals1(final String ph) {
        final AlertDialog loading;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getCustomerDeals.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        StringTokenizer st=new StringTokenizer(response,";");
                        String str[];

                        if(checkForNext==true){
                            retailersToCustomerList.remove(retailersToCustomerList.size() - 1);
                        }

                        if(st.countTokens()>0){
                            while(st.hasMoreTokens()){
                                int i=0;
                                String item1=st.nextToken();
                                //Log.v("Hells",item1);
                                StringTokenizer st1=new StringTokenizer(item1,"||");
                                str=new String[st1.countTokens()];
                                while (st1.hasMoreTokens()){
                                    str[i++]=st1.nextToken();
                                }

                                String oid = str[0]; String sid = str[1]; String pname = str[2]; String oprce = str[3];
                                String sname = str[4]; String start = str[6]; String end = str[7]; String disc = str[8];
                                String sc = str[9]; String tid = str[10];
                                maxid = tid;

                                String nextCheck = str[11];

                                if(nextCheck.equals("0")){
                                    checkForNext = false;
                                }else {
                                    checkForNext = true;
                                }
                                //Toast.makeText(CustomerDeals.this,pname + " " + maxid,Toast.LENGTH_SHORT).show();
                                byte [] encodeByte= Base64.decode(str[5], Base64.DEFAULT);
                                Bitmap img = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                //Toast.makeText(CustomerDeals.this,pname,Toast.LENGTH_SHORT).show();

                                CustomerDealModel a=new CustomerDealModel(oid,sid,pname,oprce,sname,img,start,end,disc,sc);
                                retailersToCustomerList.add(a);
                            }
                        }else{
                            final Toast toast = Toast.makeText(CustomerDeals.this,"No more deals!", Toast.LENGTH_SHORT);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 400);
                        }

                        adapter.notifyDataSetChanged();
                        loadin = false;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(CustomerDeals.this,"Error connecting to server",Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",ph);
                params.put("mid",maxid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
