package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class SendOTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
    }

    public void requestSMSAPI(View view){
        final EditText phoneNo = (EditText) findViewById(R.id.ETPhoneNo);
        final AlertDialog loading;
        loading = new SpotsDialog(SendOTP.this, R.style.Custom);
        loading.setMessage("Loading...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/ps/sendOTP.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                        try{
                            JSONObject JSONResponse = new JSONObject(response);
                            //Toast.makeText(getBaseContext(), JSONResponse.getString("Status"), Toast.LENGTH_LONG).show();
                            if(JSONResponse.getString("Status").equals("Success")){
                                Intent i = new Intent(getBaseContext(), VerifyOTP.class);
                                i.putExtra("sessionId", JSONResponse.getString("Details"));
                                i.putExtra("phone",phoneNo.getText().toString());
                                startActivity(i);
                            }else{
                                Toast.makeText(getBaseContext(), "Error sending OTP Try again", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            //Toast.makeText(getBaseContext(), "Exception ", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(SendOTP.this,"Error connecting to Server ", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phoneNo",phoneNo.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
