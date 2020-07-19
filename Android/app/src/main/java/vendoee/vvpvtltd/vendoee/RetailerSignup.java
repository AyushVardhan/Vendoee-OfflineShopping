package vendoee.vvpvtltd.vendoee;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class RetailerSignup extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView,viewp,viewp1;
    double lat, lng; private ImageView call;
    String imgString = null;
    EditText oname, bname, email, open, close, pass,pass1, desc, city, state, country, pin, baddr, contact, dealsb;
    int selectedId,deliverID,brandID;
    private RadioGroup radioGroup,radioGroup1,radioGroup2;
    private RadioButton radioButton,radioButton1,radioButton2;
    Bitmap photo;     static final int PICK_IMAGE = 16;
    int PLACE_PICKER_REQUEST = 1;
    LocationManager locationManager; static int count_hit = 1;
    Location startPoint, endPoint;
    double latitude, longitude, distance;
    String cashnocash,deliver,brand;                private static int count = 0;
    Location lastlocation;
    GPSTracker gps; static int tmp = 0; static int tmp1 = 0;

    TextView language, cashlessTV, deliveryTV, brandTV, policyTV, registrationTV, uploadShopImageTV, FacingProblemTV;
    Button langBtn, b;
    TextInputLayout onameTIL, bnameTIL, emailTIL, openTIL, closeTIL, passTIL,pass1TIL, dealsbTIL, descTIL, cityTIL, stateTIL, countryTIL, pinTIL, baddrTIL, contactTIL;
    RadioButton cashlessYes, cashlessNo, deliveryYes, deliverNo, brandYes, brandNo;

    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "";

    private Uri mTempImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 15;
    Bundle bReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_signup1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Registration");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        radioGroup = (RadioGroup) findViewById(R.id.radioCash);
        radioGroup1 = (RadioGroup) findViewById(R.id.radiodelivery);
        radioGroup2 = (RadioGroup) findViewById(R.id.radiobrand);
        contact = (EditText) findViewById(R.id.contact);
        oname = (EditText) findViewById(R.id.oname);
        bname = (EditText) findViewById(R.id.bname);
        email = (EditText) findViewById(R.id.email);
        open = (EditText) findViewById(R.id.open);
        close = (EditText) findViewById(R.id.close);
        pass = (EditText) findViewById(R.id.pass);
        pass1 = (EditText) findViewById(R.id.confirmp);
        desc = (EditText) findViewById(R.id.desc);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        country = (EditText) findViewById(R.id.country);
        pin = (EditText) findViewById(R.id.pin);
        baddr = (EditText) findViewById(R.id.baddr);
        viewp = (ImageView)findViewById(R.id.viewp);
        dealsb = (EditText)findViewById(R.id.dealsb);
        //viewp1 = (ImageView)findViewById(R.id.viewp1);

        contactTIL = (TextInputLayout) findViewById(R.id.contactTIL);
        onameTIL = (TextInputLayout) findViewById(R.id.onameTIL);
        bnameTIL = (TextInputLayout) findViewById(R.id.bnameTIL);
        emailTIL = (TextInputLayout) findViewById(R.id.emailTIL);
        openTIL = (TextInputLayout) findViewById(R.id.openTIL);
        closeTIL = (TextInputLayout) findViewById(R.id.closeTIL);
        passTIL = (TextInputLayout) findViewById(R.id.passTIL);
        pass1TIL = (TextInputLayout) findViewById(R.id.confirmpTIL);
        descTIL = (TextInputLayout) findViewById(R.id.descTIL);
        cityTIL = (TextInputLayout) findViewById(R.id.cityTIL);
        stateTIL = (TextInputLayout) findViewById(R.id.stateTIL);
        countryTIL = (TextInputLayout) findViewById(R.id.countryTIL);
        pinTIL = (TextInputLayout) findViewById(R.id.pinTIL);
        baddrTIL = (TextInputLayout) findViewById(R.id.baddrTIL);
        dealsbTIL = (TextInputLayout)findViewById(R.id.dealsbTIL);

        cashlessTV = (TextView) findViewById(R.id.cashlessTV);
        deliveryTV= (TextView) findViewById(R.id.deliveryTV);
        brandTV = (TextView) findViewById(R.id.brandTV);
        policyTV = (TextView) findViewById(R.id.policyTV);
        registrationTV = (TextView) findViewById(R.id.registrationTV);
        uploadShopImageTV = (TextView) findViewById(R.id.upload_shop_imageTV);
        FacingProblemTV = (TextView)findViewById(R.id.facing_problem);

        b = (Button) findViewById(R.id.button);
        langBtn = (Button) findViewById(R.id.langBtn);

        cashlessYes = (RadioButton) findViewById(R.id.radioMale);
        cashlessNo = (RadioButton) findViewById(R.id.radioFemale);
        deliveryYes = (RadioButton) findViewById(R.id.radioyes);
        deliverNo = (RadioButton) findViewById(R.id.radiono);
        brandYes = (RadioButton) findViewById(R.id.radiobyes);
        brandNo = (RadioButton) findViewById(R.id.radiobno);

        bReq = getIntent().getExtras();
        if(bReq!=null){
            String mobile = bReq.getString("Mobile");
            contact.setText(mobile);
        }

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radiobyes:
                        dealsbTIL.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radiobno:
                        dealsbTIL.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });

        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RetailerSignup.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                //Toast.makeText(RetailerSignup.this,"N",Toast.LENGTH_SHORT).show();
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
                                            mTempImageUri = FileProvider.getUriForFile(RetailerSignup.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }
                            } else{
                                //Toast.makeText(RetailerSignup.this,"<N",Toast.LENGTH_SHORT).show();
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

        call = (ImageView)this.findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"8126136019"));
                startActivity(callIntent);
            }
        });

        try{
            gps = new GPSTracker(RetailerSignup.this);


            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                //latitude = 0.0; longitude = 0.0;

                double z = 0.0;

                int retval1 = Double.compare(latitude, z);
                int retval2 = Double.compare(longitude, z);

                if(retval1 == 0 || retval2 == 0){

                    LocationManager locationManager = (LocationManager) RetailerSignup.this
                            .getSystemService(Context.LOCATION_SERVICE);
                    String locationProvider = LocationManager.NETWORK_PROVIDER;
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location lastlocation = locationManager.getLastKnownLocation(locationProvider);

                    latitude = lastlocation.getLatitude();
                    longitude =lastlocation.getLongitude();

                    int retval111 = Double.compare(latitude, z);
                    int retval211 = Double.compare(longitude, z);

                    if(retval111 == 0 || retval211 == 0){
                        finish();
                        Toast.makeText(RetailerSignup.this,"Location can't be fetched! Please try again.",Toast.LENGTH_SHORT).show();
                    }else{
                        getAddress1(String.valueOf(latitude),String.valueOf(longitude));
                    }
                }else{
                    getAddress1(String.valueOf(latitude),String.valueOf(longitude));
                }
            }else{
                finish();
                Toast.makeText(RetailerSignup.this,"Location can't be fetched! Please try again.",Toast.LENGTH_SHORT).show();
            }

            //Toast.makeText(RetailerSignup.this,latitude + " "+longitude,Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            finish();
            Toast.makeText(RetailerSignup.this,"Location can't be fetched! Please try again.",Toast.LENGTH_SHORT).show();
        }

        endPoint=new Location("locationA");
        endPoint.setLatitude(latitude);
        endPoint.setLongitude(longitude);

        baddr.setText("");
        baddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RetailerSignup.this, "Please wait map is loading..",Toast.LENGTH_SHORT).show();
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(RetailerSignup.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        b = (Button) findViewById(R.id.button);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(RetailerSignup.this, "OnClick",Toast.LENGTH_SHORT).show();
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(RetailerSignup.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String min="",hour="";
                        if(String.valueOf(selectedMinute).length()==1){
                            min = "0"+selectedMinute;
                        }else {
                            min = String.valueOf(selectedMinute);
                        }

                        if(String.valueOf(selectedHour).length()==1){
                            //Toast.makeText(RetailerSignup.this,"HERE",Toast.LENGTH_SHORT).show();
                            hour = "0"+selectedHour;
                        }else {
                            hour = String.valueOf(selectedHour);
                        }

                        //Toast.makeText(RetailerSignup.this,selectedHour+"",Toast.LENGTH_SHORT).show();

                        open.setText( hour + ":" + min);
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
                mTimePicker = new TimePickerDialog(RetailerSignup.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String min="",hour="";
                        if(String.valueOf(selectedMinute).length()==1){
                            min = "0"+selectedMinute;
                        }else{
                            min = String.valueOf(selectedMinute);
                        }

                        if(String.valueOf(selectedHour).length()==1){
                            //Toast.makeText(RetailerSignup.this,"HERE",Toast.LENGTH_SHORT).show();
                            hour = "0"+selectedHour;
                        }else{
                            hour = String.valueOf(selectedHour);
                        }

                        //Toast.makeText(RetailerSignup.this,selectedHour+"",Toast.LENGTH_SHORT).show();

                        close.setText( hour + ":" + min);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        b.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int mailchk = 0; int conchk = 1; int nullval = 0;
                        if (imgString != null) {

                            if(contact.getText().toString().isEmpty() ||  bname.getText().toString().isEmpty()
                                    || oname.getText().toString().isEmpty() || email.getText().toString().isEmpty() || open.getText().toString().isEmpty() ||
                                    close.getText().toString().isEmpty() || pass.getText().toString().isEmpty() || desc.getText().toString().isEmpty() || city.getText().toString().isEmpty()
                                    ||state.getText().toString().isEmpty() || country.getText().toString().isEmpty() || pin.getText().toString().isEmpty() || baddr.getText().toString().isEmpty())
                            {
                                Toast.makeText(RetailerSignup.this, "All fields are mandatory!",Toast.LENGTH_SHORT).show();
                                nullval = 1;
                                if(oname.getText().toString().isEmpty()){
                                    oname.setError("Mandatory field!");
                                    oname.requestFocus();
                                }else if(bname.getText().toString().isEmpty()){
                                    bname.setError("Mandatory field!");
                                    bname.requestFocus();
                                }else if(email.getText().toString().isEmpty()){
                                    email.setError("Mandatory field!");
                                    email.requestFocus();
                                }else if(contact.getText().toString().isEmpty()){
                                    contact.setError("Mandatory field!");
                                    contact.requestFocus();
                                }else if(open.getText().toString().isEmpty()){
                                    open.setError("Mandatory field!");
                                    open.requestFocus();
                                }else if(close.getText().toString().isEmpty()){
                                    close.setError("Mandatory field!");
                                    close.requestFocus();
                                }else if(pass.getText().toString().isEmpty()){
                                    pass.setError("Mandatory field!");
                                    pass.requestFocus();
                                }else if(desc.getText().toString().isEmpty()){
                                    desc.setError("Mandatory field!");
                                    desc.requestFocus();
                                }else if(city.getText().toString().isEmpty()){
                                    city.setError("Mandatory field!");
                                    city.requestFocus();
                                }else if(state.getText().toString().isEmpty()){
                                    state.setError("Mandatory field!");
                                    state.requestFocus();
                                }else if(country.getText().toString().isEmpty()){
                                    country.setError("Mandatory field!");
                                    country.requestFocus();
                                }else if(pin.getText().toString().isEmpty()){
                                    pin.setError("Mandatory field!");
                                    pin.requestFocus();
                                }else if(baddr.getText().toString().isEmpty()){
                                    baddr.setError("Mandatory field!");
                                    baddr.requestFocus();
                                }

                            }

                            if(!email.getText().toString().isEmpty()){

                                String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
                                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                                java.util.regex.Matcher m = p.matcher(email.getText().toString());
                                if(!m.matches()){
                                    Toast.makeText(RetailerSignup.this,"Enter Email correctly!",Toast.LENGTH_SHORT).show();
                                    mailchk = 1;
                                }
                            }

                            if(contact.getText().toString().length()==10){
                                conchk = 0;
                            }

                            if(conchk == 1){
                                Toast.makeText(RetailerSignup.this,"Enter contact correctly!",Toast.LENGTH_SHORT).show();
                            }

                            if(nullval==0 && mailchk == 0 && conchk == 0 ){
                                if(distance<1000){
                                    if(open.getText().toString().equals(close.getText().toString())){
                                        Toast.makeText(RetailerSignup.this,"Please check shop's open/close timings.",Toast.LENGTH_SHORT).show();
                                    }else{
                                        String name = oname.getText().toString();
                                        String busname = bname.getText().toString();
                                        String mail = email.getText().toString();
                                        String passw = pass.getText().toString();
                                        String descr = desc.getText().toString();
                                        String sopen = open.getText().toString();
                                        String sclose = close.getText().toString();

                                        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

                                        String uname = sdf.format(timestamp);

                                        selectedId = radioGroup.getCheckedRadioButtonId();
                                        radioButton = (RadioButton) findViewById(selectedId);
                                        cashnocash = (String) radioButton.getText();

                                        deliverID = radioGroup1.getCheckedRadioButtonId();
                                        radioButton1 = (RadioButton) findViewById(deliverID);
                                        deliver = (String) radioButton1.getText();

                                        brandID = radioGroup2.getCheckedRadioButtonId();
                                        radioButton2 = (RadioButton)findViewById(brandID);
                                        brand = (String)radioButton2.getText();

                                        int brandF = -1;
                                        if(brand.equals("Brand") || brand.equals("ब्रांड")){
                                            brandF = 1;
                                        }else if(brand.equals("Shop") || brand.contains("सामान्य")){
                                            brandF = 0;
                                        }

                                        int stat = -1;
                                        if(cashnocash.contains("Yes") || cashnocash.equals("हाँ")){
                                            stat = 1;
                                        }else if(cashnocash.contains("No") || cashnocash.equals("नहीं")){
                                            stat = 0;
                                        }

                                        int flagD = -1;
                                        if(deliver.contains("Yes") || deliver.equals("हाँ")){
                                            flagD = 1;
                                        }else if(deliver.contains("No") || deliver.equals("नहीं")){
                                            flagD = 0;
                                        }

                                        if(name.matches(".*\\d.*")){
                                            Toast.makeText(RetailerSignup.this,"Enter owner's name correctly!",Toast.LENGTH_SHORT).show();
                                        } else{
                                            if(pass.getText().toString().equals(pass1.getText().toString())){
                                                /*
                                                Toast.makeText(RetailerSignup.this,"Normal :" + imgString.length()+"",Toast.LENGTH_SHORT).show();
                                                byte[] input = new byte[0];
                                                try {
                                                    input = imgString.getBytes("UTF-8");
                                                    Toast.makeText(RetailerSignup.this,"Input :" + input.length+"",Toast.LENGTH_SHORT).show();
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }

                                                // Compress the bytes
                                                byte[] output = new byte[input.length];
                                                Deflater compresser = new Deflater();
                                                compresser.setInput(input);
                                                compresser.finish();
                                                int compressedDataLength = compresser.deflate(output);
                                                Toast.makeText(RetailerSignup.this,"Compressed :" + compressedDataLength+"",Toast.LENGTH_SHORT).show();

                                                // Decompress the bytes
                                                Inflater decompresser = new Inflater();
                                                decompresser.setInput(output, 0, compressedDataLength);
                                                byte[] result = new byte[1000000];
                                                try {
                                                    int resultLength = decompresser.inflate(result);
                                                    Toast.makeText(RetailerSignup.this,"De-Compressed :" +resultLength+"",Toast.LENGTH_SHORT).show();
                                                } catch (DataFormatException e) {
                                                    e.printStackTrace();
                                                }
                                                decompresser.end();
                                                */

                                                if(dealsbTIL.getVisibility() == View.VISIBLE){
                                                    if(dealsb.getText().toString().isEmpty()){
                                                        Toast.makeText(RetailerSignup.this,"Brand name is required!",Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        busname = busname + "(" + dealsb.getText().toString() +")";
                                                        //Toast.makeText(RetailerSignup.this, "All Cool "+ busname, Toast.LENGTH_SHORT).show();
                                                        insertToDatabase(name, busname, mail, uname, passw, descr, imgString, sopen, sclose,stat, baddr.getText().toString(), Double.toString(latitude), Double.toString(longitude), city.getText().toString(), state.getText().toString(), country.getText().toString(), pin.getText().toString(), contact.getText().toString(),flagD,brandF);
                                                    }
                                                }else{
                                                    //Toast.makeText(RetailerSignup.this, "All Cool "+ busname, Toast.LENGTH_SHORT).show();
                                                    insertToDatabase(name, busname, mail, uname, passw, descr, imgString, sopen, sclose,stat, baddr.getText().toString(), Double.toString(latitude), Double.toString(longitude), city.getText().toString(), state.getText().toString(), country.getText().toString(), pin.getText().toString(), contact.getText().toString(),flagD,brandF);
                                                }

                                            }else{
                                                Toast.makeText(RetailerSignup.this,"Passwords doesn't match!",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }else {
                                    Toast.makeText(RetailerSignup.this,"Enter Business Address correctly!",Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            Toast.makeText(RetailerSignup.this, "Select an Image!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
        presentShowcaseSequence();
    }

    private static final String SHOWCASE_ID = "sequence example10";
    private void presentShowcaseSequence() {

        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, SHOWCASE_ID);


        sequence.setConfig(config);

        sequence.addSequenceItem(
                new MaterialShowcaseView.Builder(this)
                        .setTarget(langBtn)
                        .setDismissText("GOT IT")
                        .setDismissOnTouch(true)
                        .setMaskColour(Color.parseColor("#e5f44336"))
                        .setContentText("Change your language from here..")
                        .withCircleShape()
                        .build()
        );

        sequence.start();

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

    private void getAddress1(final String lat, final String lng) {
        loading = new SpotsDialog(RetailerSignup.this, R.style.CustomR);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/GetAddr.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//                        Toast.makeText(RetailerSignup.this,response,Toast.LENGTH_SHORT).show();

                        StringTokenizer str = new StringTokenizer(response,"||");
                        String str1[] = new String[str.countTokens()];
                        int i =0;
                        while(str.hasMoreTokens()){
                            str1[i] = str.nextToken();
                            i++;
                        }
                        //                      Toast.makeText(RetailerSignup.this,"Length: " +str1.length,Toast.LENGTH_SHORT).show();

                        if(str1.length>1){
                            baddr.setText(str1[0]);     city.setText(str1[1]);
                            state.setText(str1[2]);     pin.setText(str1[3]);
                            country.setText(str1[4]);
                            loading.dismiss();
                        }

                        if(baddr.getText().length()>0){
                            baddr.setFocusable(false);
                        }

                        if(city.getText().length()>0){
                            city.setFocusable(false);
                        }

                        if(state.getText().length()>0){
                            state.setFocusable(false);
                        }

                        if(pin.getText().length()>0){

                        }

                        if(country.getText().length()>0){
                            country.setFocusable(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //finish();
                        //Toast.makeText(RetailerSignup.this,"Please check internet connection!",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("lat", lat);
                params.put("lng", lng);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
                    Toast.makeText(RetailerSignup.this,"Please try again",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Geocoder mGeocoder = new Geocoder(RetailerSignup.this, Locale.getDefault());
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

    private void findDistance() {
        distance=startPoint.distanceTo(endPoint);
        if(distance>1000){
            Toast.makeText(RetailerSignup.this,"Your device is located far away from business address! Please add correct location..",Toast.LENGTH_SHORT).show();
        }
    }


    AlertDialog loading;

    private void insertToDatabase(final String name, final String busname, final String mail, final String uname, final String passw, final String descr, final String img, final String open, final String close, final int stat, final String addr, final String lat, final String lng, final String city, final String state, final String country, final String pin, final String ph, final int flagD, final int brand ) {

        loading = new SpotsDialog(RetailerSignup.this, R.style.CustomR);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/dba_beta.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Toast.makeText(RetailerSignup.this,response,Toast.LENGTH_SHORT).show();

                        if (!response.contains("Error")) {
                            loading.dismiss();
                            SharedPreferences.Editor editor = getSharedPreferences("SID", MODE_PRIVATE).edit();
                            editor.putString("sid", response);
                            editor.commit();
                            loading.dismiss();
                            Toast.makeText(RetailerSignup.this, "Login to continue.." , Toast.LENGTH_SHORT).show();

                            Intent in = new Intent(RetailerSignup.this,RetailerSignIn.class);
                            startActivity(in);

                        } else {
                            loading.dismiss();
                            if(response.contains("username")){
                                Toast.makeText(RetailerSignup.this,"Username already exist!",Toast.LENGTH_LONG).show();
                            }
                            if(response.contains("phone_no")){
                                Toast.makeText(RetailerSignup.this,"Contact no already exist!",Toast.LENGTH_LONG).show();
                            }
                            if(response.contains("email")){
                                Toast.makeText(RetailerSignup.this,"Email already exist!",Toast.LENGTH_LONG).show();
                            }else{
                                //Toast.makeText(RetailerSignup.this,response,Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();

                        Intent in = new Intent(RetailerSignup.this,RetailerSignIn.class);
                        startActivity(in);
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("name", busname);
                params.put("bname", name);
                params.put("email", mail);
                params.put("uname", uname);
                params.put("pass", passw);
                params.put("desc", descr);
                params.put("sopen", open);
                params.put("sclose", close);
                params.put("image", img);
                params.put("cash", String.valueOf(stat));
                params.put("addr", addr);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("city", city);
                params.put("state", state);
                params.put("country", country);
                params.put("pin", pin);
                params.put("ph", ph);
                params.put("deliver", String.valueOf(flagD));
                params.put("brand", String.valueOf(brand));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void language(View view){
        if(count_hit%2==0){
            contactTIL.setHint("Phone Number");
            onameTIL.setHint("Owner's Name");
            bnameTIL.setHint("Shop Name");
            emailTIL.setHint("Email ID");
            openTIL.setHint("Shop Opening Time");
            closeTIL.setHint("Shop Closing Time");
            passTIL.setHint("Password");
            pass1TIL.setHint("Confirm Password");
            descTIL.setHint("Description");
            cityTIL.setHint("City");
            stateTIL.setHint("State");
            countryTIL.setHint("Country");
            pinTIL.setHint("PIN Code");
            baddrTIL.setHint("Shop Address");

            cashlessTV.setText("Cashless Transaction?");
            deliveryTV.setText("Do you provide home delivery?");
            brandTV.setText("Are you a brand shop?");
            policyTV.setText("By registering, you agree to Vendoee's Team of use and privacy policies.");
            registrationTV.setText("Registration");
            uploadShopImageTV.setText("(Upload Shop Image)");
            b.setText("Register");
            dealsbTIL.setHint("Brand Name(Levis,VeroModa,Apple,etc)");
            langBtn.setText("हिंदी");

            FacingProblemTV.setText("Facing Problem? Call us");
            cashlessYes.setText("Yes");
            cashlessNo.setText("No");
            deliveryYes.setText("Yes");
            deliverNo.setText("No");
            brandYes.setText("Brand");
            brandNo.setText("Shop");
        }else {
            contactTIL.setHint("फ़ोन नंबर");
            onameTIL.setHint("मालिक का नाम");
            bnameTIL.setHint("दुकान का नाम");
            emailTIL.setHint("ईमेल आईडी");
            openTIL.setHint("दुकान खोलने का समय");
            closeTIL.setHint("दुकान बंद करने का समय");
            passTIL.setHint("पासवर्ड");
            FacingProblemTV.setText("कोई परेशानी? अभी कॉल करें");
            pass1TIL.setHint("पासवर्ड फिर से लिखें");
            descTIL.setHint("विवरण");
            cityTIL.setHint("शहर");
            stateTIL.setHint("राज्य");
            countryTIL.setHint("देश");
            pinTIL.setHint("पिन कोड");
            baddrTIL.setHint("पता");
            dealsbTIL.setHint("ब्रांड का नाम(Levis,VeroModa,Apple,etc.)");

            cashlessTV.setText("कैशलेस लेनदेन?");
            deliveryTV.setText("क्या आप होम डिलीवरी प्रदान करते हैं?");
            brandTV.setText("क्या आप एक ब्रांड की दुकान हैं?");
            policyTV.setText("पंजीकरण करके, आप वेंडोइ के उपयोग की टीम और गोपनीयता नीतियों से सहमत हैं।");
            registrationTV.setText("पंजीकरण");
            uploadShopImageTV.setText("(दुकान की तस्वीर अपलोड करें)");
            b.setText("रजिस्टर");
            langBtn.setText("English");

            cashlessYes.setText("हाँ");
            cashlessNo.setText("नहीं");
            deliveryYes.setText("हाँ");
            deliverNo.setText("नहीं");
            brandYes.setText("ब्रांड");
            brandNo.setText("सामान्य विक्रेता");
        }
        count_hit++;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        count_hit= 1;
        return true;
    }

}

