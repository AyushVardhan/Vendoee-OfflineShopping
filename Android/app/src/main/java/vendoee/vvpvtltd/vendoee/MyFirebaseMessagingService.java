package vendoee.vvpvtltd.vendoee;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by vishal on 26/11/16.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Intent resultIntent =  new Intent(context,MainActivity.class);

        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, resultIntent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //builder.setSound(alarmSound);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setContentTitle("Vendoee")//add a 24x24 image to the drawable folder
                .setSound(alarmSound)
                .setContentText(remoteMessage.getNotification().getBody());

        builder.setSmallIcon(getNotificationIcon(builder));
        final int not_nu=generateRandom();
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(not_nu, builder.build());
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
}
