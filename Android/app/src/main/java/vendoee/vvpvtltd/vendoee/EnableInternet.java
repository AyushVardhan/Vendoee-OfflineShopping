package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by shagilsid on 01-02-2017.
 */

public class EnableInternet extends Activity {
    Button settings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.enable_internet);

        settings=(Button)findViewById(R.id.button5);

        Toast.makeText(this,"Change your network settings manually",Toast.LENGTH_SHORT).show();
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isNetworkAvailable()){
                    Intent in = new Intent(EnableInternet.this,SplashScreen.class);
                    startActivity(in);
                }else{
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume");
        if(isNetworkAvailable()){
            Intent in = new Intent(EnableInternet.this,SplashScreen.class);
            startActivity(in);
        }else{
            Toast.makeText(EnableInternet.this,"Enable internet connection!",Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
