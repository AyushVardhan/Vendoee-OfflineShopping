package vendoee.vvpvtltd.vendoee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Ayush Vardhan on 5/2/2017.
 */

public class autostart extends BroadcastReceiver
{
    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context,service.class);
        context.startService(intent);
        Log.i("Autostart", "started");
    }
}
