package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class RecoverCustomer extends AppCompatActivity {

    EditText email,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_customer2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
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

        email = (EditText)findViewById(R.id.email);
        pass = (EditText)findViewById(R.id.password);

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

    public void RestoreAcc(View view) {
        if(email.getText().toString().isEmpty()||pass.getText().toString().isEmpty()){
            Toast.makeText(RecoverCustomer.this,"All fields are mandatory!",Toast.LENGTH_SHORT).show();
        }else{
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email.getText().toString());
            if(!m.matches()){
                Toast.makeText(RecoverCustomer.this,"Enter Email correctly!",Toast.LENGTH_SHORT).show();
            }else{
                SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences21.getString("number_C", "");
                String phone = mobile.substring(3,mobile.length());
                redeemAcc(email.getText().toString(),pass.getText().toString(),phone);
                //Toast.makeText(RecoverCustomer.this,email.getText().toString()+","+pass.getText().toString()+","+phone,Toast.LENGTH_SHORT).show();
            }
        }
    }

    AlertDialog loading;
    private void redeemAcc(final String email, final String pass, final String pho) {
        loading = new SpotsDialog(RecoverCustomer.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/RecoverCustomer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        if(response.equals("Points Recovered!")){
                            Toast.makeText(RecoverCustomer.this,"Points Recovered!",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            //Toast.makeText(RecoverCustomer.this,response,Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(RecoverCustomer.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("pass", pass);
                params.put("phone", pho);
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
