package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class gpsnotfound1 extends Activity {

    Button setting;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsnotfound1);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_gpsnotfound);
        setting = (Button)findViewById(R.id.settings);


        if(checkGPS()){
            Intent in = new Intent(gpsnotfound1.this,nearby.class);
            startActivity(in);
        }

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(gpsnotfound1.this,"Enable location services and press back button!",Toast.LENGTH_SHORT).show();

                final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                startActivity(new Intent(action));
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume");
        if(checkGPS()){
            Intent in = new Intent(gpsnotfound1.this,nearby.class);
            startActivity(in);
        }else{
            //Toast.makeText(gpsnotfound1.this,"Please enable location service !",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

}
