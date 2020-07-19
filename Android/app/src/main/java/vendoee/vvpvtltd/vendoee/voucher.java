package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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

public class voucher extends AppCompatActivity {

    ArrayList<voucherModel> dataModels;
    ListView listView;   TextView notice,notice1;
    private static voucherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);

        notice = (TextView)findViewById(R.id.notice);
        notice1 = (TextView)findViewById(R.id.notice1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Product & vouchers");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        listView=(ListView)findViewById(R.id.list);
        dataModels= new ArrayList<>();

        getRequests();
    }

    private void getRequests() {

        final AlertDialog loading;

        loading = new SpotsDialog(voucher.this, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getVouchers.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        notice.setVisibility(View.INVISIBLE);
                        notice1.setVisibility(View.INVISIBLE);

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

                                byte [] encodeByte= Base64.decode(data[1],Base64.DEFAULT);
                                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                dataModels.add(new voucherModel(data[0],data[2],bitmap,data[3]));
                            }

                            adapter= new voucherAdapter(dataModels,voucher.this);
                            listView.setAdapter(adapter);

                        }else{
                            notice1.setVisibility(View.VISIBLE);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(voucher.this,"Cant connect to server! Try Again",Toast.LENGTH_SHORT).show();
                    }
                }){
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
