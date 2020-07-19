package vendoee.vvpvtltd.vendoee;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ayush Vardhan on 5/2/2017.
 */

public class service extends Service {
    public Context context = this;
    public static Runnable runnable = null;
    private static final String TAG = "MyService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        //Toast.makeText(this, "Reboot My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Reboot My Service Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(service.this, "Reboot My Service Started", Toast.LENGTH_LONG).show();

        try{
            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
            String mobile = sharedpreferences2.getString("number_C", "");

            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
            String citty = sharedpreferences.getString("Ccity", "");

            SharedPreferences sharedpreferences1 = getSharedPreferences("SIDSERVICE", Context.MODE_PRIVATE);
            String sid = sharedpreferences1.getString("sidService", "");

            if(!citty.isEmpty() && !mobile.isEmpty()){
                startService(new Intent(service.this, TotalSaleServiceNotify.class));
            }else if(!sid.isEmpty()){
                if(Integer.parseInt(sid)>0){
                    startService(new Intent(service.this, TotalRetailerNotify.class));
                }
            }

            Log.d(TAG, "onStart");
        }catch (Exception e){

        }
    }
}
