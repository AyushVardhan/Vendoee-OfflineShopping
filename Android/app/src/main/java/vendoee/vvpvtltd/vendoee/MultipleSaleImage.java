package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MultipleSaleImage extends AppCompatActivity {

    String imgString1 = null;   Bundle bundle;
    String imgString2 = null;   String oid;
    Bitmap photo1,photo2;

    ImageView imageview1,imageview2,imageview3;

    private Uri mTempImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE1 = 2;

    private static final int CAMERA_REQUEST1 = 1888;
    private static final int CAMERA_REQUEST2 = 1889;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_sale_image);

        bundle = getIntent().getExtras();

        if(bundle == null){
            //Toast.makeText(RetailerSignup.this, "Null Bundle", Toast.LENGTH_SHORT).show();
        }else{
            oid = bundle.getString("oid");
        }

        imageview1 = (ImageView)findViewById(R.id.imgv1);
        imageview1.setImageResource(R.drawable.ic_add_a_photo_cam_black_48dp);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_add_a_photo_cam_black_48dp);
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        imgString1=Base64.encodeToString(b, Base64.DEFAULT);

        imageview2 = (ImageView)findViewById(R.id.imgv2);
        imageview2.setImageResource(R.drawable.ic_add_a_photo_cam_black_48dp);
        imgString2 = imgString1;

        getImagesOID(oid);
    }

    String myJSON;
    String[] SPINNERLIST ;
    JSONArray peoples = null;
    AlertDialog loading3;

    private void getImagesOID(final String oid) {

        loading3 = new SpotsDialog(MultipleSaleImage.this, R.style.Custom);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getOfferImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON = response;
                        showList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        //Toast.makeText(MultipleSaleImage.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid", oid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");

            if(peoples.length()>0){
               SPINNERLIST = new String[peoples.length()];

               for(int i=0;i<peoples.length();i++){
                   JSONObject c = peoples.getJSONObject(i);
                   String id = c.getString("id");
                   String category = c.getString("img");

                   SPINNERLIST[i] = category;
               }

                if(SPINNERLIST.length==1){
                    imgString1 = SPINNERLIST[0];
                }

                if(SPINNERLIST.length==2){
                    imgString1 = SPINNERLIST[0];
                    imgString2 = SPINNERLIST[1];
                }


               byte [] encodeByte1=Base64.decode(imgString1,Base64.DEFAULT);
               Bitmap bitmap1=BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
               imageview1.setImageBitmap(bitmap1);

               byte [] encodeByte2=Base64.decode(imgString2,Base64.DEFAULT);
               Bitmap bitmap2=BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
               imageview2.setImageBitmap(bitmap2);
           }

            loading3.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void img1(View view) {
        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, CAMERA_REQUEST1);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex){
                // nothing
            }
            if (photoFile != null){
                mTempImageUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void img2(View view) {
        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, CAMERA_REQUEST2);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex){
                // nothing
            }
            if (photoFile != null){
                mTempImageUri = Uri.fromFile(photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE1);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // mTempPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            /*
            photo1 = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo1.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arr = baos.toByteArray();
            imgString1 = Base64.encodeToString(arr, Base64.DEFAULT);
            imageview1.setImageBitmap(photo1);
            */
            try {
                photo1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo1 = getResizedBitmap(photo1,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo1.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString1 = Base64.encodeToString(arr, Base64.DEFAULT);
                imageview1.setImageBitmap(photo1);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }

        }

        if (requestCode == REQUEST_IMAGE_CAPTURE1 && resultCode == Activity.RESULT_OK) {
            /*
            photo2 = (Bitmap) data.getExtras().get("data");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo2.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arr = baos.toByteArray();
            imgString2 = Base64.encodeToString(arr, Base64.DEFAULT);
            imageview2.setImageBitmap(photo2);
            */

            try {
                photo2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo2 = getResizedBitmap(photo2,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo2.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString2 = Base64.encodeToString(arr, Base64.DEFAULT);
                imageview2.setImageBitmap(photo2);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }

        }
    }

    public void save(View view) {

        final AlertDialog loading3;
        loading3 = new SpotsDialog(MultipleSaleImage.this, R.style.Custom);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/OfferImage.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "1";
                        loading3.dismiss();
                        if (response.matches(str)) {

                            Toast.makeText(MultipleSaleImage.this, "Images Updated!" , Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(MultipleSaleImage.this,RetailHome.class);
                            startActivity(in);

                        } else {
                            //Toast.makeText(MultipleSaleImage.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        //Toast.makeText(MultipleSaleImage.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid", oid);
                params.put("img1", imgString1);
                params.put("img2", imgString2);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
