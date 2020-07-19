package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import dmax.dialog.SpotsDialog;

public class BroadCastActivity extends ListActivity {

    String Category[],category,subcategory,shopid[],pname,price,time,date,desc,lat,lng;
    Bundle bundle; GPSTracker gps;
    CheckBox checkBox;  View v1;    Button send;
    LinkedHashMap<String, String> shopLists = new LinkedHashMap<String, String>();
    Vector v = new Vector(13, 2);
    String id_cat ="";Date today; String city,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast);


        gps = new GPSTracker(BroadCastActivity.this);

        send = (Button) findViewById(R.id.sendall);

        SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        city = sharedpreferences.getString("Ccity", "");

        SharedPreferences sharedpreferences1 = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
        state = sharedpreferences1.getString("state", "");

        bundle = getIntent().getExtras();
        category = bundle.getString("category");
        subcategory = bundle.getString("subcat");
        pname = bundle.getString("pname");
        price = bundle.getString("price");
        time = bundle.getString("time");
        date = bundle.getString("date");
        desc = bundle.getString("desc");
        //Toast.makeText(BroadCastActivity.this,category+" "+subcategory,Toast.LENGTH_SHORT).show();
        time = time +":00";
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getListView().getCount()==0){
                    Toast.makeText(BroadCastActivity.this,"No Shops matching your category!",Toast.LENGTH_SHORT).show();
                }else{
                    id_cat = "";
                    int itemCount = getListView().getCount();
                    for(int i=0 ; i < itemCount ; i++){
                        //getListView().setItemChecked(i, chk.isChecked());
                        if(getListView().isItemChecked(i)){

                            id_cat = id_cat+","+shopid[i];
                        }
                    }

                    if(id_cat.isEmpty()){
                        Toast.makeText(BroadCastActivity.this,"Choose some stores!",Toast.LENGTH_SHORT).show();
                    }else{
                        //Toast.makeText(BroadCastActivity.this,"Hey",Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                        String mobile = sharedpreferences2.getString("number_C", "");

                        String contact = mobile.substring(3,mobile.length());

                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date1);
                        //Toast.makeText(BroadCastActivity.this,pname + " " + price+" "+dateString2+" "+desc+" "+contact+" "+id_cat.substring(1,id_cat.length()),Toast.LENGTH_SHORT).show();
                        sendToDB(pname,price,dateString2,desc,contact,id_cat.substring(1,id_cat.length()));

                    }
                }
            }
        });

        //Toast.makeText(BroadCastActivity.this,category + " " + subcategory+" "+pname+" "+price,Toast.LENGTH_SHORT).show();
        //Toast.makeText(BroadCastActivity.this,time + " " + date+" "+desc+" ",Toast.LENGTH_SHORT).show();
        //Toast.makeText(BroadCastActivity.this,lat + " " + lng,Toast.LENGTH_SHORT).show();
        getShopList();
    }

    private void sendToDB(final String pname,final String price, final String date, final String des, final String contact, final String idcatT) {
        final AlertDialog loading;

        loading = new SpotsDialog(BroadCastActivity.this, R.style.Custom);
        loading.setMessage("Broadcasting request...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/broadCast.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(BroadCastActivity.this,response,Toast.LENGTH_SHORT).show();
                        if(response.equals("1")){
                            Toast.makeText(BroadCastActivity.this,"Request sent!",Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(BroadCastActivity.this,CustomerSales.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(in);
                        }else{
                            //Toast.makeText(BroadCastActivity.this,response,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        //Toast.makeText(BroadCastActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("prodname",pname);
                params.put("contact",contact);
                params.put("desc",des);
                params.put("sids",idcatT);
                params.put("price",price);
                params.put("date",date);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getShopList() {
        final AlertDialog loading;

        loading = new SpotsDialog(BroadCastActivity.this, R.style.Custom);
        loading.setMessage("Loading offers...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getShopList.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(BroadCastActivity.this,response,Toast.LENGTH_SHORT).show();

                        if(response.length()<=1){
                            Toast.makeText(BroadCastActivity.this,"No shops found!",Toast.LENGTH_SHORT).show();
                        }else{
                            StringTokenizer str = new StringTokenizer(response.substring(1,response.length()),",");
                            int j = 0; String shop[] = new String[str.countTokens()];
                            shopid = new String[str.countTokens()];
                            while(str.hasMoreElements()){
                                String name = str.nextToken();
                                String id = name.substring(name.lastIndexOf('(')+1,name.lastIndexOf(')'));
                                String shopN = name.substring(0,name.lastIndexOf('('));
                                shop[j] = shopN;
                                shopid[j]=id;
                                j++;
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(BroadCastActivity.this, android.R.layout.simple_list_item_multiple_choice, shop);
                            getListView().setAdapter(adapter);

                            View.OnClickListener clickListener = new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    v1 = v;
                                    CheckBox chk = (CheckBox) v;
                                    int itemCount = getListView().getCount();
                                    for(int i=0 ; i < itemCount ; i++){
                                        getListView().setItemChecked(i, chk.isChecked());
                                    }
                                }
                            };

                            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                    CheckBox chk = (CheckBox) findViewById(R.id.chkAll);
                                    int checkedItemCount = getCheckedItemCount();

                                    if(getListView().getCount()==checkedItemCount)
                                        chk.setChecked(true);
                                    else
                                        chk.setChecked(false);
                                }
                            };


                            CheckBox chkAll =  ( CheckBox ) findViewById(R.id.chkAll);

                            chkAll.setOnClickListener(clickListener);

                            getListView().setOnItemClickListener(itemClickListener);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("city",city);
                params.put("state",state);
                params.put("cat",category);
                params.put("subcat",subcategory);
                params.put("latitude",String.valueOf(gps.getLatitude()));
                params.put("longitude", String.valueOf(gps.getLongitude()));
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

    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = getListView().getCheckedItemPositions();
        int itemCount = getListView().getCount();

        for(int i=0;i<itemCount;i++){
            if(positions.get(i))
                cnt++;
        }
        return cnt;
    }
}
