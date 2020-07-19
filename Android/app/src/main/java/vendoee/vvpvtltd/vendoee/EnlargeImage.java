package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by shagilsid on 28-01-2017.
 */

public class EnlargeImage extends AppCompatActivity {
    ZoomableImageView  enLargeImage; String pname; LinearLayout save;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enlarge_image);

        Bundle bundle = getIntent().getExtras();
        pname = bundle.getString("pname");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(pname);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        save = (LinearLayout)findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bmp = ((BitmapDrawable)enLargeImage.getDrawable()).getBitmap();

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                File f = new File(Environment.getExternalStorageDirectory()
                        + File.separator + "V" +timeStamp+"V" +".jpg");
                try {
                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    if(f.exists()){
                        Toast.makeText(EnlargeImage.this,"File Saved!",Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Intent mediaScanIntent = new Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(f);
                            mediaScanIntent.setData(contentUri);
                            sendBroadcast(mediaScanIntent);
                        } else {
                            sendBroadcast(new Intent(
                                    Intent.ACTION_MEDIA_MOUNTED,
                                    Uri.parse("file://"
                                            + Environment.getExternalStorageDirectory())));
                        }
                    }else{
                        Toast.makeText(EnlargeImage.this,"File not Saved!",Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        enLargeImage=(ZoomableImageView )findViewById(R.id.enlageImage);

        SharedPreferences sharedpreferences1 = getSharedPreferences("IMAGEE", Context.MODE_PRIVATE);
        String img1 = sharedpreferences1.getString("emlargeImage", "");

        byte[] encodeByteq = Base64.decode(img1, Base64.DEFAULT);
        Bitmap bitmap1 = BitmapFactory.decodeByteArray(encodeByteq, 0, encodeByteq.length);
        enLargeImage.setImageBitmap(bitmap1);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
