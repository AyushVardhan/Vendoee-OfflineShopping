package vendoee.vvpvtltd.vendoee;

/**
 * Created by Ayush Vardhan on 3/31/2017.
 */

import android.app.Application;

import com.facebook.accountkit.AccountKit;

/**
 * Created by vishal gaurav on 3/24/2017.
 */

public class AccountKitDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize Facebook Account Kit
        AccountKit.initialize(getApplicationContext());
    }
}
