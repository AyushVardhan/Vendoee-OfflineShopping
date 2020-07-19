package vendoee.vvpvtltd.vendoee;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import dmax.dialog.SpotsDialog;


public class ShowCategory extends AppCompatActivity {

    String sid;
    String JSON_URL1 = "http://www.vendoee.com/android-scripts/getCategoryret.php";
    String JSON_URL2 = "http://www.vendoee.com/android-scripts/setCategory2.php";
    String JSON_URL3 = "http://www.vendoee.com/android-scripts/DeletePrevCatRet.php";

    CheckBox electronics,appliances,men,women,kids,home,book,grocery,automobile,food,other, handlooms;
    Button refreshcat; ImageView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("My Categories");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        electronics = (CheckBox)findViewById(R.id.electronics) ;    appliances = (CheckBox)findViewById(R.id.appliances) ;
        men = (CheckBox)findViewById(R.id.men); women = (CheckBox)findViewById(R.id.women); kids = (CheckBox)findViewById(R.id.kids);
        home = (CheckBox)findViewById(R.id.home); book = (CheckBox)findViewById(R.id.books); grocery = (CheckBox)findViewById(R.id.grocery);
        automobile = (CheckBox)findViewById(R.id.automobile); handlooms = (CheckBox)findViewById(R.id.hand);
        food = (CheckBox)findViewById(R.id.food); other = (CheckBox)findViewById(R.id.other);
        refreshcat = (Button)findViewById(R.id.refreshcat); call = (ImageView)findViewById(R.id.call);

        SharedPreferences sharedpreferences = getSharedPreferences("SID", Context.MODE_PRIVATE);
        sid = sharedpreferences.getString("sid", "");

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"8126136019"));
                startActivity(callIntent);
            }
        });

        refreshcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategory(sid);;
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                getCategory(sid);
            }
        }, 1000);


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

    private void getCategory(final String sid) {

        final AlertDialog loading;
        loading = new SpotsDialog(ShowCategory.this, R.style.CustomR);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ShowCategory.this,response,Toast.LENGTH_SHORT).show();
                        StringTokenizer st = new StringTokenizer(response,",");
                        if(st.countTokens()>0){
                            while(st.hasMoreTokens()){
                                String str = st.nextToken();
                                if(str.contains(electronics.getText())){
                                    electronics.setChecked(true);
                                }else if(str.contains(appliances.getText()))
                                {
                                    appliances.setChecked(true);
                                }else if(str.contains(men.getText()))
                                {
                                    men.setChecked(true);
                                }else if(str.contains(women.getText()))
                                {
                                    women.setChecked(true);
                                }else if(str.contains(kids.getText()))
                                {
                                    kids.setChecked(true);
                                }else if(str.contains(home.getText()))
                                {
                                    home.setChecked(true);
                                }else if(str.contains(book.getText()))
                                {
                                    book.setChecked(true);
                                }else if(str.contains(grocery.getText()))
                                {
                                    grocery.setChecked(true);
                                }else if(str.contains(automobile.getText()))
                                {
                                    automobile.setChecked(true);
                                }else if(str.contains(food.getText()))
                                {
                                    food.setChecked(true);
                                }else if(str.contains(other.getText()))
                                {
                                    other.setChecked(true);
                                }else if(str.contains(handlooms.getText()))
                                {
                                    handlooms.setChecked(true);
                                }
                                loading.dismiss();
                            }
                        }else {
                            loading.dismiss();
                            Toast.makeText(ShowCategory.this,"No Categories found !",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowCategory.this,"Cant connect to Server! Try again",Toast.LENGTH_LONG).show();
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
    AlertDialog loadingz; static int k = 0;
    public void update(View view) {

        Vector v = new Vector(13, 2);

        if(electronics.isChecked()){
            v.add("Electronics"+","+"1");
        }
        if(appliances.isChecked()){
            v.add("Appliances"+","+"2");
        }
        if(men.isChecked()){
            v.add("Men"+","+"3");
        }
        if(women.isChecked()){
            v.add("Women"+","+"4");
        }
        if(kids.isChecked()){
            v.add("Kids & Baby"+","+"5");
        }
        if(home.isChecked()){
            v.add("Home & Furniture"+","+"6");
        }
        if(book.isChecked()){
            v.add("Books & more"+","+"7");
        }
        if(grocery.isChecked()){
            v.add("Grocery"+","+"8");
        }
        if(automobile.isChecked()){
            v.add("gift Hampers"+","+"9");
        }
        if(food.isChecked()){
            v.add("Food & Restaurants"+","+"12");
        }
        if(other.isChecked()){
            v.add("Other"+","+"13");
        }
        if(handlooms.isChecked()){
            v.add("Handlooms"+","+"14");
        }

        Enumeration vEnum1 = v.elements();
        String[] contains = new String[2] ;

        if(v.size()>0){
            loadingz = new SpotsDialog(ShowCategory.this, R.style.CustomR);
            loadingz.setMessage("Please wait...");
            loadingz.setCanceledOnTouchOutside(false);
            loadingz.show();
            int j=0;
                k = 0;
            deleteCats();

            while(vEnum1.hasMoreElements())
            {
                String str = vEnum1.nextElement().toString();
                int i =0; final String cname, cid;

                StringTokenizer st = new StringTokenizer(str,",");
                while (st.hasMoreTokens()) {
                    contains[i++] = st.nextToken();
                }

                cname = contains[0]; cid = contains[1];
                //Toast.makeText(ShowCategory.this,contains[0]+" "+contains[1],Toast.LENGTH_SHORT).show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        insertCategory(sid,cname,cid);
                    }
                }, 1000);



                j++;
            }

            loadingz.dismiss();
            if(j==k)
            {
                Toast.makeText(ShowCategory.this,"Categories Updated!",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(ShowCategory.this,"Coonection issue! Please refresh and try again. ",Toast.LENGTH_SHORT).show();
                //Toast.makeText(ShowCategory.this,k + " "+ j,Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(ShowCategory.this, "Please Select a Category!",Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCats() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //.makeText(ShowCategory.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingz.dismiss();
                        //Toast.makeText(ShowCategory.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    private void insertCategory(final String sid, final String cname, final String cid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ShowCategory.this,response,Toast.LENGTH_LONG).show();
                        if(response.equals("1")){
                            k++;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loadingz.dismiss();
                        //Text(ShowCategory.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid", sid);
                params.put("cname", cname);
                params.put("cid", cid);
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

