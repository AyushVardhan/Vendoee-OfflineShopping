package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;


public class updateSale extends AppCompatActivity {

    String oid,img,sid,start,end,oriprice,saleprice,description,subcat;
    EditText sdate, edate, price, oprice, desc;
    EditText pname;HashMap categories = new HashMap(); HashMap categories1 = new HashMap();
    Bundle bundle;
    Bitmap photo;String imgString = null;

    TextInputLayout pnameTIL, sdateTIL, edateTIL, priceTIL, opriceTIL, descTIL;
    TextView categoryAndSubcategoryTV, uploadImagesTV, editOfferTV, FacingProblemTV;
    Button langBtn, butt;

    private ImageView imageView;
    String today;
    private Calendar calendar;
    Date daay;DatabaseHelper myDB;
    private int year, month, day;
    static int count = 1;
    String imgString1 = null;
    String imgString2 = null;
    Bitmap photo1,photo2;

    ImageView imageview1,imageview2,imageview3;
    MaterialSpinner materialDesignSpinner,materialDesignSpinner1;
    String[] SPINNERLIST1;
    String myJSON,myJSON1;
    String[] SPINNERLIST ;
    JSONArray peoples = null;
    AlertDialog loading3;
    private Uri mTempImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE1 = 2;
    static final int REQUEST_IMAGE_CAPTURE2 = 9;

    static final int PICK_IMAGE = 16;
    static final int PICK_IMAGE1 = 17;
    static final int PICK_IMAGE2 = 18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sale);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Offer");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
//            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_HOME| ActionBar.DISPLAY_SHOW_CUSTOM);


        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 5;

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        today = day+"-"+(month+1)+"-"+year;
        try {
            daay = df.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bundle = getIntent().getExtras();
        SharedPreferences sharedpreferences = getSharedPreferences("SID", Context.MODE_PRIVATE);
        sid = sharedpreferences.getString("sid", "");
        getCategories(sid);


        if(bundle == null){
            //Toast.makeText(RetailerSignup.this, "Null Bundle", Toast.LENGTH_SHORT).show();
        }else{
            oid = bundle.getString("ID");
            //img = bundle.getString("image");
            //imgString = img;
            getInfo(oid);
        }

        materialDesignSpinner1 = (MaterialSpinner )findViewById(R.id.android_material_design_spinner1);
        materialDesignSpinner = (MaterialSpinner)findViewById(R.id.android_material_design_spinner);

        materialDesignSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                String value = (String) categories.get(item);
                getSubCategories(value);
            }
        });

        imageview1 = (ImageView)findViewById(R.id.imgv1);
        imageview1.setImageResource(R.drawable.image);
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),R.drawable.image);
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        imgString1=Base64.encodeToString(b, Base64.DEFAULT);

        imageview2 = (ImageView)findViewById(R.id.imgv2);
        imageview2.setImageResource(R.drawable.image);
        imgString2 = imgString1;

        getImagesOID(oid);

        FacingProblemTV = (TextView)findViewById(R.id.facing_problem);
        sdate = (EditText)findViewById(R.id.uname);
        edate = (EditText)findViewById(R.id.contact);
        price = (EditText)findViewById(R.id.bname);
        oprice = (EditText)findViewById(R.id.email);
        desc = (EditText)findViewById(R.id.desc);
        pname = (EditText)findViewById(R.id.pname);
        sdateTIL = (TextInputLayout)findViewById(R.id.unameTIL);
        edateTIL = (TextInputLayout)findViewById(R.id.contactTIL);
        priceTIL = (TextInputLayout)findViewById(R.id.bnameTIL);
        opriceTIL = (TextInputLayout)findViewById(R.id.emailTIL);
        descTIL = (TextInputLayout)findViewById(R.id.descTIL);
        pnameTIL = (TextInputLayout)findViewById(R.id.pnameTIL);

        editOfferTV = (TextView) findViewById(R.id.edit_offerTV);
        categoryAndSubcategoryTV = (TextView) findViewById(R.id.textView90);
        uploadImagesTV = (TextView) findViewById(R.id.txt);

        butt = (Button) findViewById(R.id.button);

        langBtn = (Button) findViewById(R.id.langBtn);
        imageView = (ImageView) this.findViewById(R.id.imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(updateSale.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                //Toast.makeText(updateSale.this,"N",Toast.LENGTH_SHORT).show();
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
                                            mTempImageUri = FileProvider.getUriForFile(updateSale.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }
                            } else{
                                //Toast.makeText(updateSale.this,"<N",Toast.LENGTH_SHORT).show();
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

    public  void language(View view){

        if(count%2==0){
            pnameTIL.setHint("Offer Name");
            priceTIL.setHint("Price(MRP)");
            opriceTIL.setHint("Offer Price");
            sdateTIL.setHint("Start Date");
            edateTIL.setHint("End Date");
            descTIL.setHint("Description(Color, Size, Type, etc)");
            FacingProblemTV.setText("Facing Problem? Call us");

            categoryAndSubcategoryTV.setText("Category and Subcategories:");
            uploadImagesTV.setText("(click below to add offer images)");
            editOfferTV.setText("Edit Offer");

            langBtn.setText("हिंदी");
            butt.setText("Update");
        }else{
            pnameTIL.setHint("प्रस्ताव(ऑफ़र) का नाम");
            priceTIL.setHint("मूल्य(एम.आर.पी/MRP)");
            opriceTIL.setHint("ऑफर किया गया मूल्य");
            sdateTIL.setHint("आरंभ करने की तिथि");
            edateTIL.setHint("अंतिम तिथि");
            descTIL.setHint("विवरण(रंग, आकार, प्रकार आदि)");

            FacingProblemTV.setText("कोई परेशानी? अभी कॉल करें");
            categoryAndSubcategoryTV.setText("श्रेणी तथा उप्रेणी:");
            uploadImagesTV.setText("(तस्वीर डालना के लिए नीचे क्लिक करें)");
            editOfferTV.setText("प्रस्ताव का परिवर्तन");

            langBtn.setText("English");
            butt.setText("अद्यतन");
        }
        count++;
    }

    private void getImagesOID(final String oid) {


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
                        //Toast.makeText(updateSale.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void img1(View view) {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(updateSale.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                        Toast.makeText(updateSale.this,"N",Toast.LENGTH_SHORT).show();
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
                                    mTempImageUri = FileProvider.getUriForFile(updateSale.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE1);
                            }
                        }
                    } else{
                        Toast.makeText(updateSale.this,"<N",Toast.LENGTH_SHORT).show();
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
                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE1);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    public void img2(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(updateSale.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

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
                                    mTempImageUri = FileProvider.getUriForFile(updateSale.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE2);
                            }
                        }
                    } else{
                        Toast.makeText(updateSale.this,"<N",Toast.LENGTH_SHORT).show();
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
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE2);
                            }
                        }
                    }
                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE2);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
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

        if (requestCode == PICK_IMAGE1 && resultCode == RESULT_OK){

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                mTempImageUri = data.getData();
                photo = BitmapFactory.decodeStream(inputStream);
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo = getResizedBitmap(photo,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString1 = Base64.encodeToString(arr, Base64.DEFAULT);

                imageview1.setImageBitmap(photo);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }

        if (requestCode == PICK_IMAGE2 && resultCode == RESULT_OK){

            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                mTempImageUri = data.getData();
                photo = BitmapFactory.decodeStream(inputStream);
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo = getResizedBitmap(photo,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString2 = Base64.encodeToString(arr, Base64.DEFAULT);

                imageview2.setImageBitmap(photo);
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
                    Toast.makeText(updateSale.this,"Please try again",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE1 && resultCode == RESULT_OK){
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo = getResizedBitmap(photo,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString1 = Base64.encodeToString(arr, Base64.DEFAULT);
                imageview1.setImageBitmap(photo);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE2 && resultCode == RESULT_OK){
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mTempImageUri);
                photo = getResizedBitmap(photo,330,380);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] arr = baos.toByteArray();
                imgString2 = Base64.encodeToString(arr, Base64.DEFAULT);
                imageview2.setImageBitmap(photo);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }
    }
    /*public class GetImage extends AsyncTask<Bitmap,Void,Void>{
        public ProgressDialog proDialog=null;

        @Override
        protected void onPreExecute() {
            proDialog=ProgressDialog.show(updateSale.this,"","Loading..",true);
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmaps[0].compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            imgString=Base64.encodeToString(b, Base64.DEFAULT);
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(proDialog.isShowing())
                proDialog.dismiss();
        }
    }*/

    public void getCategories(final String sid){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getCategory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON = response;
                        showList1();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        finish();
                        //Toast.makeText(updateSale.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    private void showList1() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray("result");

            SPINNERLIST = new String[peoples.length()];

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString("id");
                String category = c.getString("category");

                SPINNERLIST[i] = category;
                //Toast.makeText(AnnounceOffer.this, id+" "+category,Toast.LENGTH_SHORT).show();

                categories.put(category,id);

            }

            materialDesignSpinner.setItems(SPINNERLIST);
            ;
            String value = (String) categories.get(materialDesignSpinner.getText().toString());
            //Toast.makeText(AnnounceOffer.this,value,Toast.LENGTH_SHORT).show();
            //getSubCategories(value);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void convertImageToString(Bitmap bitmap) {
        Bitmap bit = bitmap;
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        imgString=Base64.encodeToString(b, Base64.DEFAULT);

    }

    AlertDialog loading;

    private void getInfo(final String sid) {

        loading = new SpotsDialog(updateSale.this, R.style.CustomR);
        loading.setMessage("Loading Details...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/editSales.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();


                        StringTokenizer str = new StringTokenizer(response,",");
                        String vals[] = new String[str.countTokens()];

                        int i=0;
                        while(str.hasMoreTokens()){
                            vals[i++] = str.nextToken();
                        }

                        materialDesignSpinner.setText(vals[1]);
                        String value = (String) categories.get(materialDesignSpinner.getText().toString());
                        getSubCategories(value);

                        String strt =  vals[6]; String ed = vals[7]; imgString = vals[11];
                        subcat = vals[2];
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
                        String ds1 = null,ds2 = null;
                        try {
                            ds1 = sdf2.format(sdf1.parse(strt));
                            ds2 = sdf2.format(sdf1.parse(ed));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        pname.setText(vals[0]);desc.setText(vals[3]);price.setText(vals[4]);oprice.setText(vals[5]);

                        sdate.setText(ds1);
                        edate.setText(today);

                        byte [] encodeByte=Base64.decode(imgString,Base64.DEFAULT);
                        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        imageView.setImageBitmap(bitmap);

                        start = vals[6]; end = vals[7]; oriprice = vals[4]; saleprice = vals[5]; description = vals[3];

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(updateSale.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    private void showList2() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON1);
            peoples = jsonObj.getJSONArray("result");

            if(peoples.length()>0)
            {
                SPINNERLIST1 = new String[peoples.length()];

                for(int i=0;i<peoples.length();i++){
                    JSONObject c = peoples.getJSONObject(i);
                    String id = c.getString("id");
                    String category = c.getString("category");

                    SPINNERLIST1[i] = category;
                    //Toast.makeText(AnnounceOffer.this, id+" "+category,Toast.LENGTH_SHORT).show();

                    categories1.put(category,id);

                }

                materialDesignSpinner1.setItems(SPINNERLIST1);
            }else{
                materialDesignSpinner1.setItems("Others");
            }
            materialDesignSpinner1.setText(subcat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getSubCategories(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getSubCategoryDec.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON1=response;
                        showList2();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(updateSale.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }
    */

    @SuppressWarnings("deprecation")
    public void setDate1(View view) {
        showDialog(990);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if (id == 990) {
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2 + 1, arg3);
                }
            };


    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate1(arg1, arg2 + 1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        sdate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    private void showDate1(int year, int month, int day) {
        edate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    public void done(View view) {
        if (imgString != null) {
            if (!pname.getText().toString().isEmpty() && !price.getText().toString().isEmpty() && !oprice.getText().toString().isEmpty() && !sdate.getText().toString().isEmpty() && !edate.getText().toString().isEmpty() && !desc.getText().toString().isEmpty()) {

                Double MRP = Double.parseDouble(price.getText().toString());
                Double SaleP = Double.parseDouble(oprice.getText().toString());

                if (SaleP < MRP) {

                    Date d1 = null,d2 = null;
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

                    try {
                        d1 = formatter.parse(sdate.getText().toString());
                        d2 = formatter.parse(edate.getText().toString());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(d2.compareTo(daay)>=0)
                    {
                        if(d2.after(d1)){
                            //Toast.makeText(AnnounceOffer.this, "Go for it!" , Toast.LENGTH_SHORT).show();
                            String SDA = null,EDA = null;
                            try {
                                Date st = new SimpleDateFormat("dd-MM-yyyy").parse(sdate.getText().toString());
                                Date et = new SimpleDateFormat("dd-MM-yyyy").parse(edate.getText().toString());

                                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");

                                SDA = formatter1.format(st);
                                EDA = formatter1.format(et);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            updatesale(SDA,EDA);
                        }else if(d2.equals(d1)) {
                            Toast.makeText(updateSale.this, "Start date/End date cannot be same!" , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(updateSale.this, "Enter dates correctly!" , Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(updateSale.this,"Enter end of sale date correctly!" ,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(updateSale.this, "Sale cost should be less than MRP!", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(updateSale.this, "All fields are mandatory!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(updateSale.this, "Image is required!", Toast.LENGTH_LONG).show();
        }
    }

    public void updatesale(final String SDA, final String EDA){

        final String price1 = price.getText().toString();
        final String oprice1 = oprice.getText().toString();
        final String desc1 = desc.getText().toString();
        final AlertDialog loading3;
        loading3 = new SpotsDialog(updateSale.this, R.style.CustomR);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/updateSale.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "1";
                        loading3.dismiss();
                        if (response.matches(str)) {

                            otherImage();

                        } else {
                            //Toast.makeText(updateSale.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        //Toast.makeText(updateSale.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid", oid);
                params.put("desc", desc1);
                params.put("price", price1);
                params.put("oprice", oprice1);
                params.put("SDate", SDA);
                params.put("EDate", EDA);
                params.put("img", imgString);
                params.put("pname", pname.getText().toString());
                params.put("maincat", materialDesignSpinner.getText().toString());
                params.put("subcat", materialDesignSpinner1.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void otherImage() {
        final AlertDialog loading3;
        loading3 = new SpotsDialog(updateSale.this, R.style.CustomR);
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

                            Toast.makeText(updateSale.this, "Sale Updated!" , Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(updateSale.this,RetailHome.class);
                            startActivity(in);;

                        } else {
                            //Toast.makeText(updateSale.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        //Toast.makeText(updateSale.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        count = 1;
        return true;
    }

    public void callvendoee(View view) {
        Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"8126136019"));
        startActivity(callIntent);
    }
}
