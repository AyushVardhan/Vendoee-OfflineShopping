package vendoee.vvpvtltd.vendoee;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by Ayush Vardhan on 5/1/2017.
 */

public class TotalRetailerNotify extends Service {

    public Context context = this;
    String sid;

    public Handler handler4 = null;
    public static Runnable runnable4 = null;

    public Handler handler2 = null;
    public static Runnable runnable2 = null;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Retailer - My Service Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
        //Toast.makeText(this, "Retailer - My Service Started", Toast.LENGTH_LONG).show();
        try{

            SharedPreferences sharedpreferences = getSharedPreferences("SID", Context.MODE_PRIVATE);
            sid = sharedpreferences.getString("sid", "");

            handler4 = new Handler();
            runnable4 = new Runnable() {
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/ProductRequestedNotify.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(context,"Retailer - handler4 "+ response, Toast.LENGTH_LONG).show();

                                    if(response.isEmpty()){
                                        //Toast.makeText(context,"Retailer - handler4 "+ "Empty", Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedpreferences5 = context.getSharedPreferences("ProdReqIdsNotified", Context.MODE_PRIVATE);
                                        String ids = sharedpreferences5.getString("PRIDS", "");

                                        if(ids.isEmpty()){

                                            Intent resultIntent ;


                                            SharedPreferences sharedpreferences1 = getSharedPreferences("SIDSERVICE", Context.MODE_PRIVATE);
                                            String sid = sharedpreferences1.getString("sidService", "");

                                            if(!sid.isEmpty()){
                                                resultIntent = new Intent(context,ProductsRequestRetailer.class);
                                            }else{
                                                resultIntent = new Intent(context,MainActivity.class);
                                            }
                                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

                                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            //builder.setSound(alarmSound);

                                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                    .setContentIntent(pi)
                                                    .setAutoCancel(true)
                                                    .setContentTitle("You have a new product request!")//add a 24x24 image to the drawable folder
                                                    .setSound(alarmSound)
                                                    .setContentText("Click here to check all your requests.");

                                            builder.setSmallIcon(getNotificationIcon(builder));
                                            final int not_nu=generateRandom();
                                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(not_nu, builder.build());

                                            SharedPreferences.Editor editor1 = getSharedPreferences("ProdReqIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("PRIDS", response);
                                            editor1.commit();
                                        }else{

                                            String tmp = ids;

                                            Vector v = new Vector(30,20);

                                            if(!ids.contains(",")){ids = ids+",";}

                                            StringTokenizer str = new StringTokenizer(ids,",");
                                            while(str.hasMoreElements()){
                                                v.add(str.nextToken());
                                            }

                                            StringTokenizer str1 = new StringTokenizer(response,",");
                                            while(str1.hasMoreElements()){
                                                String val = str1.nextToken();
                                                if(v.contains(val)){

                                                }else{
                                                    Intent resultIntent ;

                                                    SharedPreferences sharedpreferences1 = getSharedPreferences("SIDSERVICE", Context.MODE_PRIVATE);
                                                    String sid = sharedpreferences1.getString("sidService", "");

                                                    if(!sid.isEmpty()){
                                                        resultIntent = new Intent(context,ProductsRequestRetailer.class);
                                                    }else{
                                                        resultIntent = new Intent(context,MainActivity.class);
                                                    }
                                                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

                                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                    //builder.setSound(alarmSound);

                                                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                            .setContentIntent(pi)
                                                            .setAutoCancel(true)
                                                            .setContentTitle("You have a new product request!")//add a 24x24 image to the drawable folder
                                                            .setSound(alarmSound)
                                                            .setContentText("Click here to check all your requests.");

                                                    builder.setSmallIcon(getNotificationIcon(builder));
                                                    final int not_nu=generateRandom();
                                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(not_nu, builder.build());
                                                    tmp = tmp+","+val;
                                                }
                                            }

                                            SharedPreferences.Editor editor1 = getSharedPreferences("ProdReqIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("PRIDS", tmp);
                                            editor1.commit();

                                            v.clear();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(context, "Retailer - "+error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("sid",sid);
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TotalRetailerNotify.this);
                    requestQueue.add(stringRequest);

                    handler4.postDelayed(runnable4, 600000);
                }
            };

            handler4.postDelayed(runnable4, 15000);

            //****************************************************************************************************************************

            handler2 = new Handler();
            runnable2 = new Runnable() {
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/DealsRequestedNotify.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(context,"Retailer - handler2 "+ response, Toast.LENGTH_LONG).show();

                                    if(response.isEmpty()){
                                        //Toast.makeText(context,"Retailer - handler2 "+ "Empty", Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedpreferences5 = context.getSharedPreferences("ReqIdsNotified", Context.MODE_PRIVATE);
                                        String ids = sharedpreferences5.getString("RIDS", "");

                                        if(ids.isEmpty()){

                                            Intent resultIntent ;

                                            SharedPreferences sharedpreferences1 = getSharedPreferences("SIDSERVICE", Context.MODE_PRIVATE);
                                            String sid = sharedpreferences1.getString("sidService", "");

                                            if(!sid.isEmpty()){
                                                resultIntent = new Intent(context,CustomerDealsRetailer.class);
                                            }else{
                                                resultIntent = new Intent(context,MainActivity.class);
                                            }
                                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

                                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            //builder.setSound(alarmSound);

                                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                    .setContentIntent(pi)
                                                    .setAutoCancel(true)
                                                    .setContentTitle("Click to confirm the deal")//add a 24x24 image to the drawable folder
                                                    .setSound(alarmSound)
                                                    .setContentText("Accept offer and win points.");

                                            builder.setSmallIcon(getNotificationIcon(builder));
                                            final int not_nu=generateRandom();
                                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(not_nu, builder.build());

                                            SharedPreferences.Editor editor1 = getSharedPreferences("ReqIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("RIDS", response);
                                            editor1.commit();
                                        }else{

                                            String tmp = ids;

                                            Vector v = new Vector(30,20);

                                            if(!ids.contains(",")){ids = ids+",";}

                                            StringTokenizer str = new StringTokenizer(ids,",");
                                            while(str.hasMoreElements()){
                                                v.add(str.nextToken());
                                            }

                                            StringTokenizer str1 = new StringTokenizer(response,",");
                                            while(str1.hasMoreElements()){
                                                String val = str1.nextToken();
                                                if(v.contains(val)){

                                                }else{
                                                    Intent resultIntent ;

                                                    SharedPreferences sharedpreferences1 = getSharedPreferences("SIDSERVICE", Context.MODE_PRIVATE);
                                                    String sid = sharedpreferences1.getString("sidService", "");

                                                    if(!sid.isEmpty()){
                                                        resultIntent = new Intent(context,CustomerDealsRetailer.class);
                                                    }else{
                                                        resultIntent = new Intent(context,MainActivity.class);
                                                    }
                                                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

                                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                    //builder.setSound(alarmSound);

                                                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                            .setContentIntent(pi)
                                                            .setAutoCancel(true)
                                                            .setContentTitle("Click to confirm the deal")//add a 24x24 image to the drawable folder
                                                            .setSound(alarmSound)
                                                            .setContentText("Accept offer and win points.");

                                                    builder.setSmallIcon(getNotificationIcon(builder));
                                                    final int not_nu=generateRandom();
                                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(not_nu, builder.build());
                                                    tmp = tmp+","+val;
                                                }
                                            }

                                            SharedPreferences.Editor editor1 = getSharedPreferences("ReqIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("RIDS", tmp);
                                            editor1.commit();

                                            v.clear();

                                        }
                                    }
                                    //Toast.makeText(context,"GC RUN", Toast.LENGTH_LONG).show();
                                    System.gc();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(context,"GC RUN", Toast.LENGTH_LONG).show();
                                    System.gc();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("sid",sid);
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TotalRetailerNotify.this);
                    requestQueue.add(stringRequest);

                    handler2.postDelayed(runnable2, 300000);
                }
            };

            handler2.postDelayed(runnable2, 15000);
        }catch (Exception e){

        }

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(Color.parseColor("#d31e25"));
            return R.drawable.logo5;

        } else {
            return R.drawable.logo3;
        }
    }

    public int generateRandom(){
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Retailer - MyService Stopped", Toast.LENGTH_LONG).show();
        handler2.removeCallbacks(runnable2);
        handler4.removeCallbacks(runnable4);
    }
}
