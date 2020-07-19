package vendoee.vvpvtltd.vendoee;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by shagilsid on 29-01-2017.
 */

public class Vendoee extends Application {
    private Tracker mTracker;
    private static GoogleAnalytics sAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        sAnalytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {

            mTracker = sAnalytics.newTracker(R.xml.analytics_tracker);
        }
        return mTracker;
    }
}