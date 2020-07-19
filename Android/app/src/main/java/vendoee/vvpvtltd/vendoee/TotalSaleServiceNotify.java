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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by Ayush Vardhan on 4/30/2017.
 */

public class TotalSaleServiceNotify extends Service {

    public Context context = this;
    String phonenum;

    public Handler handler = null;
    public static Runnable runnable = null;

    public Handler handler1 = null;
    public static Runnable runnable1 = null;

    public Handler handler3 = null;
    public static Runnable runnable3 = null;

    public Handler handler5 = null;
    public static Runnable runnable5 = null;

    private static final String TAG = "MyService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Toast.makeText(this, "Customer - My Service Created", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid)
    {
        //Toast.makeText(this, "Customer - My Service Started", Toast.LENGTH_LONG).show();
        try{
            SharedPreferences sharedpreferences21 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
            String mobile = sharedpreferences21.getString("number_C", "");
            phonenum = mobile.substring(3,mobile.length());

            handler5 = new Handler();
            runnable5 = new Runnable() {
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/productRequestAcceptedNotify.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(context,"Customer - handler5 "+ response, Toast.LENGTH_LONG).show();

                                    if(response.isEmpty()){
                                        //Toast.makeText(context,"Customer - handler5 "+ "Empty", Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedpreferences5 = context.getSharedPreferences("ProdAccIdsNotified", Context.MODE_PRIVATE);
                                        String ids = sharedpreferences5.getString("PAIDS", "");

                                        if(ids.isEmpty()){

                                            Intent resultIntent ;

                                            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                            String mobile = sharedpreferences2.getString("number_C", "");

                                            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                            String citty = sharedpreferences.getString("Ccity", "");

                                            if(!citty.isEmpty() && !mobile.isEmpty()){
                                                resultIntent = new Intent(context, CustomerRequestedProduct.class);
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
                                                    .setContentTitle("Requested product is accepted!")//add a 24x24 image to the drawable folder
                                                    .setSound(alarmSound)
                                                    .setContentText("Check requested offer!");

                                            builder.setSmallIcon(getNotificationIcon(builder));
                                            final int not_nu=generateRandom();
                                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(not_nu, builder.build());

                                            SharedPreferences.Editor editor1 = getSharedPreferences("ProdAccIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("PAIDS", response);
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

                                                    SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                                    String mobile = sharedpreferences2.getString("number_C", "");

                                                    SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                                    String citty = sharedpreferences.getString("Ccity", "");

                                                    if(!citty.isEmpty() && !mobile.isEmpty()){
                                                        resultIntent = new Intent(context, CustomerRequestedProduct.class);
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
                                                            .setContentTitle("Requested product is accepted!")//add a 24x24 image to the drawable folder
                                                            .setSound(alarmSound)
                                                            .setContentText("Check requested offer!");

                                                    builder.setSmallIcon(getNotificationIcon(builder));
                                                    final int not_nu=generateRandom();
                                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(not_nu, builder.build());
                                                    tmp = tmp+","+val;
                                                }
                                            }

                                            SharedPreferences.Editor editor1 = getSharedPreferences("ProdAccIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("PAIDS", tmp);
                                            editor1.commit();

                                            v.clear();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(context,"Customer - "+ error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("phone",phonenum);
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TotalSaleServiceNotify.this);
                    requestQueue.add(stringRequest);

                    handler5.postDelayed(runnable5, 600000);
                }
            };

            handler5.postDelayed(runnable5, 15000);

            //****************************************************************************************************************************

            handler3 = new Handler();
            runnable3 = new Runnable() {
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getSimilarProductNotify.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(context,"CustomerSim - handler3 "+ response, Toast.LENGTH_LONG).show();

                                    if(response.isEmpty()){
                                        //Toast.makeText(context,"CustomerSim - handler3 "+ "Empty", Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedpreferences5 = context.getSharedPreferences("SimIdsNotified", Context.MODE_PRIVATE);
                                        String ids = sharedpreferences5.getString("SIDS", "");

                                        if(ids.isEmpty()){

                                            Intent resultIntent ;

                                            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                            String mobile = sharedpreferences2.getString("number_C", "");

                                            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                            String citty = sharedpreferences.getString("Ccity", "");

                                            if(!citty.isEmpty() && !mobile.isEmpty()){
                                                resultIntent = new Intent(context, CustomerSales.class);
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
                                                    .setContentTitle("Hurry! Click to display")//add a 24x24 image to the drawable folder
                                                    .setSound(alarmSound)
                                                    .setContentText("You have sale from your favorite category.");

                                            builder.setSmallIcon(getNotificationIcon(builder));
                                            final int not_nu=generateRandom();
                                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(not_nu, builder.build());

                                            SharedPreferences.Editor editor1 = getSharedPreferences("SimIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("SIDS", response);
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

                                                    SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                                    String mobile = sharedpreferences2.getString("number_C", "");

                                                    SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                                    String citty = sharedpreferences.getString("Ccity", "");

                                                    if(!citty.isEmpty() && !mobile.isEmpty()){
                                                        resultIntent = new Intent(context, CustomerSales.class);
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
                                                            .setContentTitle("Hurry! Click to display")//add a 24x24 image to the drawable folder
                                                            .setSound(alarmSound)
                                                            .setContentText("You have sale from your favorite category.");

                                                    builder.setSmallIcon(getNotificationIcon(builder));
                                                    final int not_nu=generateRandom();
                                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(not_nu, builder.build());
                                                    tmp = tmp+","+val;
                                                }
                                            }

                                            SharedPreferences.Editor editor1 = getSharedPreferences("SimIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("SIDS", tmp);
                                            editor1.commit();

                                            v.clear();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(context, "CustomerSim - "+error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("phone",phonenum);
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TotalSaleServiceNotify.this);
                    requestQueue.add(stringRequest);

                    handler3.postDelayed(runnable3, 7200000);
                }
            };

            handler3.postDelayed(runnable3, 15000);

            //****************************************************************************************************************************

            handler1 = new Handler();
            runnable1 = new Runnable() {
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/customerDealAcceptedNotify.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    //Toast.makeText(context,"Customer - handler1 "+ response, Toast.LENGTH_LONG).show();

                                    if(response.isEmpty()){
                                        //Toast.makeText(context,"Customer - handler1 "+ "Empty", Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedpreferences5 = context.getSharedPreferences("IdsNotified", Context.MODE_PRIVATE);
                                        String ids = sharedpreferences5.getString("IDS", "");

                                        if(ids.isEmpty()){

                                            Intent resultIntent ;

                                            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                            String mobile = sharedpreferences2.getString("number_C", "");

                                            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                            String citty = sharedpreferences.getString("Ccity", "");

                                            if(!citty.isEmpty() && !mobile.isEmpty()){
                                                resultIntent = new Intent(context, CustomerProfile.class);
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
                                                    .setContentTitle("Points Awarded!")//add a 24x24 image to the drawable folder
                                                    .setSound(alarmSound)
                                                    .setContentText("Now enjoy our vouchers with your points at every deal!");

                                            builder.setSmallIcon(getNotificationIcon(builder));
                                            final int not_nu=generateRandom();
                                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(not_nu, builder.build());

                                            SharedPreferences.Editor editor1 = getSharedPreferences("IdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("IDS", response);
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

                                                    SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                                    String mobile = sharedpreferences2.getString("number_C", "");

                                                    SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                                    String citty = sharedpreferences.getString("Ccity", "");

                                                    if(!citty.isEmpty() && !mobile.isEmpty()){
                                                        resultIntent = new Intent(context, CustomerProfile.class);
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
                                                            .setContentTitle("Points Awarded!")//add a 24x24 image to the drawable folder
                                                            .setSound(alarmSound)
                                                            .setContentText("Now enjoy our vouchers with your points at every deal!");

                                                    builder.setSmallIcon(getNotificationIcon(builder));
                                                    final int not_nu=generateRandom();
                                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(not_nu, builder.build());
                                                    tmp = tmp+","+val;
                                                }
                                            }

                                            SharedPreferences.Editor editor1 = getSharedPreferences("IdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("IDS", tmp);
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
                                    //Toast.makeText(context, "Customer - "+error.toString(), Toast.LENGTH_LONG).show();
                                    //Toast.makeText(context,"GC RUN", Toast.LENGTH_LONG).show();
                                    System.gc();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams(){
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("phone",phonenum);
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TotalSaleServiceNotify.this);
                    requestQueue.add(stringRequest);
                    handler1.postDelayed(runnable1, 300000);
                }
            };

            handler1.postDelayed(runnable1, 15000);

            //****************************************************************************************************************************


            handler = new Handler();
            runnable = new Runnable() {
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/countTodaysOffer.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    if(response.isEmpty()){
                                        //Toast.makeText(context,"Customer - handler1 "+ "Empty", Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedpreferences5 = context.getSharedPreferences("FlashIdsNotified", Context.MODE_PRIVATE);
                                        String ids = sharedpreferences5.getString("FIDS", "");

                                        if(ids.isEmpty()){

                                            Intent resultIntent ;
                                            Bitmap remote_picture = null;
                                            SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                            String mobile = sharedpreferences2.getString("number_C", "");

                                            SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                            String citty = sharedpreferences.getString("Ccity", "");

                                            if(!citty.isEmpty() && !mobile.isEmpty()){
                                                resultIntent = new Intent(context, CustomerSales.class);
                                            }else{
                                                resultIntent = new Intent(context,MainActivity.class);
                                            }

                                            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

                                            remote_picture = BitmapFactory.decodeResource(context.getResources(),
                                                    R.drawable.notify);

                                            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                                            notiStyle.bigPicture(remote_picture);
                                            notiStyle.setSummaryText("New sale launched today by retailers in your city. Enjoy shopping!");

                                            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            //builder.setSound(alarmSound);

                                            NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                    .setContentIntent(pi)
                                                    .setAutoCancel(true)
                                                    .setContentTitle("Daily Deals")//add a 24x24 image to the drawable folder
                                                    .setSound(alarmSound)
                                                    .setContentText("Hurry! " + "New sale waiting for you at your local store!")
                                                    .setStyle(notiStyle);

                                            builder.setSmallIcon(getNotificationIcon(builder));
                                            final int not_nu=generateRandom();
                                            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            nm.notify(not_nu, builder.build());

                                            SharedPreferences.Editor editor1 = getSharedPreferences("FlashIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("FIDS", response);
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
                                                    Bitmap remote_picture = null;
                                                    SharedPreferences sharedpreferences2 = getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                                                    String mobile = sharedpreferences2.getString("number_C", "");

                                                    SharedPreferences sharedpreferences = getSharedPreferences("CUSTOMER_CITY", Context.MODE_PRIVATE);
                                                    String citty = sharedpreferences.getString("Ccity", "");

                                                    if(!citty.isEmpty() && !mobile.isEmpty()){
                                                        resultIntent = new Intent(context, CustomerSales.class);
                                                    }else{
                                                        resultIntent = new Intent(context,MainActivity.class);
                                                    }

                                                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                    PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

                                                    remote_picture = BitmapFactory.decodeResource(context.getResources(),
                                                            R.drawable.notify);

                                                    NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                                                    notiStyle.bigPicture(remote_picture);
                                                    notiStyle.setSummaryText("New sale launched today by retailers in your city. Enjoy shopping!");

                                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                    //builder.setSound(alarmSound);

                                                    NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                                                            .setContentIntent(pi)
                                                            .setAutoCancel(true)
                                                            .setContentTitle("Daily Deals")//add a 24x24 image to the drawable folder
                                                            .setSound(alarmSound)
                                                            .setContentText("Hurry! " + "New sale waiting for you at your local store!")
                                                            .setStyle(notiStyle);

                                                    builder.setSmallIcon(getNotificationIcon(builder));
                                                    final int not_nu=generateRandom();
                                                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                    nm.notify(not_nu, builder.build());
                                                    tmp = tmp+","+val;

                                                }
                                            }

                                            SharedPreferences.Editor editor1 = getSharedPreferences("FlashIdsNotified", MODE_PRIVATE).edit();
                                            editor1.putString("FIDS", tmp);
                                            editor1.commit();

                                            v.clear();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //Toast.makeText(context, "Customer - "+error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }){

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(TotalSaleServiceNotify.this);
                    requestQueue.add(stringRequest);

                    handler.postDelayed(runnable, 10800000);
                }
            };

            handler.postDelayed(runnable, 15000);

            Log.d(TAG, "onStart");
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
        //Toast.makeText(this, "Customer - MyService Stopped", Toast.LENGTH_LONG).show();
        handler.removeCallbacks(runnable);
        handler1.removeCallbacks(runnable1);
        handler3.removeCallbacks(runnable3);
        handler5.removeCallbacks(runnable5);
    }
}
