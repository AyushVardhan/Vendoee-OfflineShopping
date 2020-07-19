package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class RequestProduct extends AppCompatActivity {

    MaterialSpinner spinner, spinnerSub;
    private int year, month, day;
    Button askone;
    String Category[];
    EditText prod, desc, price, sdate;
    String des;
    private Calendar calendar; Bundle b;
    GPSTracker gps;

    Geocoder geocoder;
    String bestProvider;
    List<Address> user = null;
    double lat; Toolbar toolbar;
    double lng;String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_offer);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            findViewById(android.R.id.content).setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        gps = new GPSTracker(RequestProduct.this);

        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinnerSub = (MaterialSpinner) findViewById(R.id.spinnerSub);
        askone = (Button) findViewById(R.id.oneShop);

        prod = (EditText) findViewById(R.id.prodName);
        desc = (EditText) findViewById(R.id.prodDesc);
        price = (EditText) findViewById(R.id.prodPrice);
        sdate = (EditText) findViewById(R.id.datea);
        //time = (EditText) findViewById(R.id.timee);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        b = getIntent().getExtras();
        if(b!=null){
            sid = b.getString("sid");
            askone.setText("Send");
            getCategorieswithID(sid);
        }else{
            getCategories();
        }

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                String value = spinner.getText().toString();
                getSubCategories(value);
            }
        });

        askone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(askone.getText().toString().equals("Next")){
                    if (prod.getText().toString().isEmpty() || price.getText().toString().isEmpty() || sdate.getText().toString().isEmpty()) {
                        Toast.makeText(RequestProduct.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (desc.getText().toString().isEmpty()) {
                            des = "NA";
                        }else{
                            des = desc.getText().toString();
                        }
                        String subcat = spinnerSub.getText().toString();
                        if(subcat.equals("No SubCategory")){
                            subcat = "0";
                        }else{
                            subcat = spinnerSub.getText().toString();;
                        }

                        Intent in = new Intent(RequestProduct.this,BroadCastActivity.class);
                        in.putExtra("subcat",subcat);
                        in.putExtra("category",spinner.getText().toString());
                        in.putExtra("pname",prod.getText().toString());
                        in.putExtra("price",price.getText().toString());
                        in.putExtra("time","00:00");
                        in.putExtra("date",sdate.getText().toString());
                        in.putExtra("desc",des);
                        startActivity(in);

                    }
                }else if(askone.getText().toString().equals("Send")){
                    if (prod.getText().toString().isEmpty() || price.getText().toString().isEmpty() || sdate.getText().toString().isEmpty()) {
                        Toast.makeText(RequestProduct.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (desc.getText().toString().isEmpty()) {
                            des = "NA";
                        }else{
                            des = desc.getText().toString();
                        }
                        /*
                        String subcat = spinnerSub.getText().toString();
                        if(subcat.equals("No SubCategory")){
                            subcat = "0";
                        }else{
                            subcat = spinnerSub.getText().toString();;
                        }

                        Intent in = new Intent(RequestProduct.this,BroadCastActivity.class);
                        in.putExtra("subcat",subcat);
                        in.putExtra("category",spinner.getText().toString());
                        in.putExtra("pname",prod.getText().toString());
                        in.putExtra("price",price.getText().toString());
                        in.putExtra("time","00:00");
                        in.putExtra("date",sdate.getText().toString());
                        in.putExtra("desc",des);
                        startActivity(in); */

                        Date date1 = null;
                        try {
                            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(sdate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String dateString2 = new SimpleDateFormat("yyyy-MM-dd").format(date1);

                        SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                        String mobile = sharedpreferences2.getString("number_C", "");
                        String contact = mobile.substring(3,mobile.length());
                        sendToDB(prod.getText().toString(),price.getText().toString(),dateString2,des,contact,sid);
                    }
                }

            }
        });
    }

    private void sendToDB(final String pname,final String price, final String date, final String des, final String contact, final String idcatT) {
        final AlertDialog loading;
        loading = new SpotsDialog(RequestProduct.this, R.style.Custom);
        loading.setMessage("Sending request...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/broadCast.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(BroadCastActivity.this,response,Toast.LENGTH_SHORT).show();
                        if(response.equals("1")){
                            Toast.makeText(RequestProduct.this,"Request sent!",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(RequestProduct.this,"Request not sent! Try again.",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toast.makeText(RequestProduct.this,"Cant connect to server. Try again!",Toast.LENGTH_SHORT).show();
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

    String[] SPINNERLIST ;
    private void addCat() {
        spinner.setItems(SPINNERLIST);
        getSubCategories(SPINNERLIST[0]);
    }

    String myJSON;
    JSONArray peoples = null;

    private void getCategorieswithID(final String sid) {
        loading = new SpotsDialog(RequestProduct.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getCategory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON = response;
                        loading.dismiss();
                        showList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(RequestProduct.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    AlertDialog loading;

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");

            SPINNERLIST = new String[peoples.length()];

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString("id");
                String category = c.getString("category");

                SPINNERLIST[i] = category;
                //Toast.makeText(AnnounceOffer.this, id+" "+category,Toast.LENGTH_SHORT).show();
            }

            loading.dismiss();
            addCat();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private void showDate(int year, int month, int day) {
        sdate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };


    public void openTIME(View view) {


    }

    private void getCategories() {
        final AlertDialog loading;
        loading = new SpotsDialog(RequestProduct.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getAllCategory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        StringTokenizer str = new StringTokenizer(response.substring(1,response.length()),",");
                        Category = new String[str.countTokens()]; int i=0;
                        while(str.hasMoreElements()){
                            Category[i]=str.nextToken();
                            i++;
                        }

                        spinner.setItems(Category);
                        getSubCategories(Category[0]);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        finish();
                        //Toast.makeText(RequestProduct.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void getSubCategories(final String value){
        final AlertDialog loading1;
        loading1 = new SpotsDialog(RequestProduct.this, R.style.Custom);
        loading1.setMessage("Loading Subcategories.");
        loading1.setCanceledOnTouchOutside(false);
        loading1.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getAllSubCategoryDec.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading1.dismiss();
                        //Toast.makeText(RequestProduct.this,response,Toast.LENGTH_LONG).show();
                        StringTokenizer str = new StringTokenizer(response,",");
                        if(str.countTokens()>0){
                            String data[] = new String[str.countTokens()]; int i = 0;
                            while(str.hasMoreElements()){
                                data[i] = str.nextToken();
                                i++;
                            }

                            spinnerSub.setItems(data);
                        }else{
                            spinnerSub.setItems("No SubCategory");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading1.dismiss();
                        //Toast.makeText(RequestProduct.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("category",value);
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
