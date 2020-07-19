package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class updateRetailerProfile extends AppCompatActivity {

    EditText oname, bname, email, open, close, pass, desc, city, state, country, pin, baddr, nameu, contact;
    Bitmap photo;String imgString = null;
    private static final int CAMERA_REQUEST = 1888;
    String myJSON;private ImageView call;
    Bundle bundle; int PLACE_PICKER_REQUEST = 2;
    String sid;
    private ImageView imageView;
    private Uri mTempImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE = 16;
    String cashnocash,deliver,brand;
    int selectedId,deliverID,brandID;

    double latitude = 0.0;
    double longitude = 0.0;

    private RadioGroup radioGroup,radioGroup1;
    private RadioButton radioButton,radioButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_retailer_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_HOME_AS_UP);


        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 5;


        oname = (EditText) findViewById(R.id.oname);
        bname = (EditText) findViewById(R.id.bname);
        email = (EditText) findViewById(R.id.email);
        open = (EditText) findViewById(R.id.open);
        close = (EditText) findViewById(R.id.close);
        desc = (EditText) findViewById(R.id.desc);

        city = (EditText)findViewById(R.id.city) ;
        baddr = (EditText)findViewById(R.id.baddr);
        state = (EditText)findViewById(R.id.state);
        country = (EditText)findViewById(R.id.country);
        pin = (EditText)findViewById(R.id.pin);

        radioGroup = (RadioGroup)findViewById(R.id.radioCash);
        radioGroup1= (RadioGroup)findViewById(R.id.radiodelivery);

        call = (ImageView)this.findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"8126136019"));
                startActivity(callIntent);
            }
        });

        baddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(updateRetailerProfile.this, "Please wait map is loading..",Toast.LENGTH_SHORT).show();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(updateRetailerProfile.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(updateRetailerProfile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        open.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(updateRetailerProfile.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        close.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(updateRetailerProfile.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                //Toast.makeText(updateRetailerProfile.this,"N",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(getPackageManager()) != null){
                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex){
                                        // nothing
                                    }
                                    if (photoFile != null){
                                        try {
                                            mTempImageUri = FileProvider.getUriForFile(updateRetailerProfile.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }
                            } else{
                                //Toast.makeText(updateRetailerProfile.this,"<N",Toast.LENGTH_SHORT).show();
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
                        } else if (items[item].equals("Choose from Library")) {

                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

                        } else if (items[item].equals("Cancel")) {

                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        bundle = getIntent().getExtras();

        if(bundle == null){
            SharedPreferences sharedpreferences1 = getSharedPreferences("SID", Context.MODE_PRIVATE);
            sid = sharedpreferences1.getString("sid", "");
            getInfo(sid);
        }else{
            sid = bundle.getString("ID");
            //Toast.makeText(RetailerSignup.this,sid, Toast.LENGTH_SHORT).show();
            getInfo(sid);

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                mTempImageUri = data.getData();
                photo = BitmapFactory.decodeStream(inputStream);
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo = getResizedBitmap(photo,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString = Base64.encodeToString(arr, Base64.DEFAULT);

                imageView.setImageBitmap(photo);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo = getResizedBitmap(photo,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString = Base64.encodeToString(arr, Base64.DEFAULT);
                imageView.setImageBitmap(photo);
                if(imageView.getDrawable()==null){
                    Toast.makeText(updateRetailerProfile.this,"Please try again",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Geocoder mGeocoder = new Geocoder(updateRetailerProfile.this, Locale.getDefault());
                Place place = PlacePicker.getPlace(data, this);

                baddr.setText(place.getAddress());

                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                    if (addresses != null) {
                        country.setText(addresses.get(0).getCountryName());
                        pin.setText(addresses.get(0).getPostalCode());
                        city.setText(addresses.get(0).getLocality());
                        state.setText(addresses.get(0).getAdminArea());
                        //Toast.makeText(RetailerSignup.this,"City: "+ addresses.get(0).getLocality(),Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException ignored) {
                    //Toast.makeText(RetailerSignup.this,"City: NULL",Toast.LENGTH_SHORT).show();                    //do something
                }
            }
        }

    }


    private void getInfo(final String sid) {

        loading = new SpotsDialog(updateRetailerProfile.this, R.style.CustomR);
        loading.setMessage("Loading Profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getRetailerData_beta.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        myJSON=response;
                        showList(myJSON);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(updateRetailerProfile.this,"Cant connect to Server! Try again",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    JSONArray peoples = null;

    private void showList(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            peoples = jsonObj.getJSONArray("result");

            for(int i=0;i<peoples.length();i++){

                JSONObject c = peoples.getJSONObject(i);

                String name = c.getString("name");
                String bsname = c.getString("bname");
                String eail = c.getString("email");
                String description = c.getString("description");
                String image = c.getString("image");
                String sopen = c.getString("sopen");
                String sclose = c.getString("sclose");
                String cash = c.getString("cashless");
                String deliver = c.getString("delivery");
                String cityD = c.getString("city");
                String stateD = c.getString("state");
                String countryD = c.getString("country");
                String pinD = c.getString("pin");
                String baddrD = c.getString("baddr");
                String brand = c.getString("brand");

                imgString = image;
                byte [] encodeByte= Base64.decode(image,Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                imageView.setImageBitmap(bitmap); email.setText(eail); open.setText(sopen); close.setText(sclose);
                oname.setText(name); bname.setText(bsname); desc.setText(description); city.setText(cityD); state.setText(stateD);
                country.setText(countryD); pin.setText(pinD); baddr.setText(baddrD);

                if(cash.equals("1")){
                    radioGroup.check(R.id.radioMale);
                }else{
                    radioGroup.check(R.id.radioFemale);
                }

                if(deliver.equals("1")){
                    radioGroup1.check(R.id.radioyes);
                }else{
                    radioGroup1.check(R.id.radiono);
                }

                //.makeText(updateRetailerProfile.this,sopen+","+sclose,Toast.LENGTH_SHORT).show();
                //Toast.makeText(RetailerSignup.this,image,Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void updateIt(View view) {
        String name = oname.getText().toString();
        String bsname = bname.getText().toString();
        String eail = email.getText().toString();
        String description = desc.getText().toString();
        String image = imgString;
        String sopen = open.getText().toString();
        String sclose = close.getText().toString();
        int mailchk = 0; int timechk = 0;

        if(sopen.equals(sclose)){
            Toast.makeText(updateRetailerProfile.this,"Please check shop's open/close timings.",Toast.LENGTH_SHORT).show();
            timechk = 1;
        }

        if(!email.getText().toString().isEmpty()){
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email.getText().toString());
            if(!m.matches()){
                Toast.makeText(updateRetailerProfile.this,"Enter Email correctly!",Toast.LENGTH_SHORT).show();
                mailchk = 1;
            }
        }

        selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        cashnocash = (String) radioButton.getText();

        deliverID = radioGroup1.getCheckedRadioButtonId();
        radioButton1 = (RadioButton) findViewById(deliverID);
        deliver = (String) radioButton1.getText();


        int stat = -1;
        if(cashnocash.contains("Yes")){
            stat = 1;
        }else if(cashnocash.contains("No")){
            stat = 0;
        }

        int flagD = -1;
        if(deliver.contains("Yes")){
            flagD = 1;
        }else if(deliver.contains("No")){
            flagD = 0;
        }

        if(mailchk == 0 && timechk == 0){
            if(name.matches(".*\\d.*")){
                Toast.makeText(updateRetailerProfile.this,"Enter owner's name correctly!",Toast.LENGTH_SHORT).show();
            }else{
                if(latitude == 0.0 && longitude == 0.0){
                    insertToDatabase(name, bsname, eail, description, image, sopen, sclose, stat, flagD);
                }else{
                    insertToDatabasewithAddress(name, bsname, eail, description, image, sopen, sclose, stat, flagD);
                }
            }
        }
    }

    AlertDialog loading;

    private void insertToDatabase(final String name, final String busname, final String mail, final String descr, final String img, final String open, final String close, final int stat, final int flagD) {

        loading = new SpotsDialog(updateRetailerProfile.this, R.style.CustomR);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/dba_update_beta.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        String str = "1";
                        if(response.equals(str)){
                            Toast.makeText(updateRetailerProfile.this,"Profile Updated !",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            loading.dismiss();
                            if(response.contains("username")){
                                Toast.makeText(updateRetailerProfile.this,"Username already exist!",Toast.LENGTH_LONG).show();
                            }
                            if(response.contains("phone_no")){
                                Toast.makeText(updateRetailerProfile.this,"Contact no already exist!",Toast.LENGTH_LONG).show();
                            }
                            if(response.contains("email")){
                                Toast.makeText(updateRetailerProfile.this,"Email already exist!",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(updateRetailerProfile.this,response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(updateRetailerProfile.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                params.put("name", name);
                params.put("bname", busname);
                params.put("email", mail);
                params.put("desc", descr);
                params.put("sopen", open);
                params.put("sclose", close);
                params.put("image", img);
                params.put("cash", String.valueOf(stat));
                params.put("deliver", String.valueOf(flagD));
                params.put("pincode", pin.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void insertToDatabasewithAddress(final String name, final String busname, final String mail, final String descr, final String img, final String open, final String close, final int stat, final int flagD) {

        loading = new SpotsDialog(updateRetailerProfile.this, R.style.CustomR);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/dba_update_beta_addr.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        String str = "1";
                        if(response.equals(str)){
                            Toast.makeText(updateRetailerProfile.this,"Profile Updated !",Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            loading.dismiss();
                            if(response.contains("username")){
                                Toast.makeText(updateRetailerProfile.this,"Username already exist!",Toast.LENGTH_LONG).show();
                            }
                            if(response.contains("phone_no")){
                                Toast.makeText(updateRetailerProfile.this,"Contact no already exist!",Toast.LENGTH_LONG).show();
                            }
                            if(response.contains("email")){
                                Toast.makeText(updateRetailerProfile.this,"Email already exist!",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(updateRetailerProfile.this,response,Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(updateRetailerProfile.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",sid);
                params.put("name", name);
                params.put("bname", busname);
                params.put("email", mail);
                params.put("desc", descr);
                params.put("sopen", open);
                params.put("sclose", close);
                params.put("image", img);
                params.put("cash", String.valueOf(stat));
                params.put("deliver", String.valueOf(flagD));
                params.put("pincode", pin.getText().toString());
                params.put("city", city.getText().toString());
                params.put("state", state.getText().toString());
                params.put("country", country.getText().toString());
                params.put("address", baddr.getText().toString());
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}