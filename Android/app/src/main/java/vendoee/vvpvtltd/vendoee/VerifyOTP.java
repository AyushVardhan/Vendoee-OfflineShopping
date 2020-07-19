package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class VerifyOTP extends AppCompatActivity {

    SmsVerifyCatcher smsVerifyCatcher;
    EditText otp;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        otp = (EditText)findViewById(R.id.ETOtp);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                 code = message.substring(0,6);
                otp.setText(code);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void verifyOTP(View view) {

        final EditText OTP = (EditText) findViewById(R.id.ETOtp);
        final String sessionId = getIntent().getStringExtra("sessionId");
        final String phone = getIntent().getStringExtra("phone");
        final AlertDialog loading;
        loading = new SpotsDialog(VerifyOTP.this, R.style.Custom);
        loading.setMessage("Loading...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/ps/verifyOTP.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Toast.makeText(VerifyOTP.this, response, Toast.LENGTH_LONG).show();
                        try{
                            JSONObject JSONResponse = new JSONObject(response);
                            if(JSONResponse.getString("Status").equals("Success")){

                                Toast.makeText(getBaseContext(), "Phone No Verified Succesfully", Toast.LENGTH_LONG).show();
                                Intent in = new Intent(VerifyOTP.this,ChooseCity.class);
                                in.putExtra("phone",phone);
                                startActivity(in);
                            }else{
                                Toast.makeText(getBaseContext(), "Failed to verify OTP try again", Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            //Toast.makeText(getBaseContext(), "Exception: "+e, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(VerifyOTP.this,"Error connecting to Server: "+error, Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("OPT",OTP.getText().toString());
                params.put("sessionId",sessionId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
