package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
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

import dmax.dialog.SpotsDialog;

public class AnnounceOffer extends AppCompatActivity{

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView imageView2,call;
    Bitmap photo;String imgString = null;
    String imgString1 = null;
    String imgString2 = null;
    MaterialSpinner materialDesignSpinner,materialDesignSpinner1;
    String[] SPINNERLIST ;
    String[] SPINNERLIST1;
    Date daay;

    TextView head;
    TextView categoryAndSubcategoryTV, uploadImagesTV, image1TV, image2TV, image3TV, addOfferTV, FacingProblemTV;
    private Uri mTempImageUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CAPTURE1 = 2;
    static final int REQUEST_IMAGE_CAPTURE2 = 3;

    private Calendar calendar;
    private int year, month, day;

    static int count = 1;

    TextInputLayout pnameTIL, priceTIL, opriceTIL, sdateTIL, edateTIL, descTIL;
    Button langBtn, b;
    static final int PICK_IMAGE = 16;
    static final int PICK_IMAGE1 = 17;
    static final int PICK_IMAGE2 = 18;
    HashMap categories = new HashMap();
    HashMap categories1 = new HashMap();
    Bundle bReq;
    String sid,cid;
    EditText pname,price,oprice,sdate,edate,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announce_offer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Offer");
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pname = (EditText)findViewById(R.id.oname);
        price = (EditText)findViewById(R.id.bname);
        oprice = (EditText)findViewById(R.id.email);
        sdate = (EditText)findViewById(R.id.uname);
        edate = (EditText)findViewById(R.id.contact);
        desc = (EditText)findViewById(R.id.desc);
        pnameTIL = (TextInputLayout)findViewById(R.id.onameTIL);
        priceTIL = (TextInputLayout)findViewById(R.id.bnameTIL);
        opriceTIL = (TextInputLayout)findViewById(R.id.emailTIL);
        sdateTIL = (TextInputLayout)findViewById(R.id.unameTIL);
        edateTIL = (TextInputLayout)findViewById(R.id.contactTIL);
        descTIL = (TextInputLayout)findViewById(R.id.descTIL);

        addOfferTV = (TextView) findViewById(R.id.add_offerTV);
        categoryAndSubcategoryTV = (TextView) findViewById(R.id.category_and_subcategoryTV);
        uploadImagesTV = (TextView) findViewById(R.id.txt);
        image1TV = (TextView) findViewById(R.id.image1TV);
        image2TV = (TextView) findViewById(R.id.image2TV);
        image3TV = (TextView) findViewById(R.id.image3TV);

        FacingProblemTV = (TextView)findViewById(R.id.facing_problem);

        b = (Button) findViewById(R.id.button);
        langBtn = (Button) findViewById(R.id.langBtn);

        bReq = getIntent().getExtras();
        if(bReq!=null){
            cid = bReq.getString("CustID");
            //Toast.makeText(AnnounceOffer.this,cid,Toast.LENGTH_SHORT).show();
        }

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        final Date date = new Date();



        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        String str = day+"-"+(month+1)+"-"+year;
        try {
            daay = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        materialDesignSpinner1 = (MaterialSpinner )findViewById(R.id.android_material_design_spinner1);
        materialDesignSpinner = (MaterialSpinner )findViewById(R.id.android_material_design_spinner);

        SharedPreferences sharedpreferences = getSharedPreferences("SID", Context.MODE_PRIVATE);
        sid = sharedpreferences.getString("sid", "");
        getCategories(sid);
        //Toast.makeText(AnnounceOffer.this,sid,Toast.LENGTH_SHORT).show();

        imageView = (ImageView) this.findViewById(R.id.imageView);
        imageView1 = (ImageView) this.findViewById(R.id.imageView1);
        imageView2 = (ImageView) this.findViewById(R.id.imageView2);
        call = (ImageView)this.findViewById(R.id.call);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+"8126136019"));
                startActivity(callIntent);
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AnnounceOffer.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                //Toast.makeText(AnnounceOffer.this,"N",Toast.LENGTH_SHORT).show();
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
                                            mTempImageUri = FileProvider.getUriForFile(AnnounceOffer.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                                    }
                                }
                            } else{
                                //Toast.makeText(AnnounceOffer.this,"<N",Toast.LENGTH_SHORT).show();
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

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AnnounceOffer.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                                Toast.makeText(AnnounceOffer.this,"N",Toast.LENGTH_SHORT).show();
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
                                            mTempImageUri = FileProvider.getUriForFile(AnnounceOffer.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE1);
                                    }
                                }
                            } else{
                                Toast.makeText(AnnounceOffer.this,"<N",Toast.LENGTH_SHORT).show();
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
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AnnounceOffer.this);
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
                                            mTempImageUri = FileProvider.getUriForFile(AnnounceOffer.this, getApplicationContext().getPackageName() + ".provider", createImageFile());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mTempImageUri);
                                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE2);
                                    }
                                }
                            } else{
                                Toast.makeText(AnnounceOffer.this,"<N",Toast.LENGTH_SHORT).show();
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
        });
        Camera camera= new Camera();


        materialDesignSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
                String value = (String) categories.get(item);
                getSubCategories(value);
            }
        });

    }

    private void showDate(int year, int month, int day) {
        sdate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    private void showDate1(int year, int month, int day) {
        edate.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
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

    public void done(View view) {
        String mancat = (String) categories.get(materialDesignSpinner.getText().toString());
        String subcat = (String) categories1.get(materialDesignSpinner1.getText().toString());
        //Toast.makeText(AnnounceOffer.this,mancat+","+subcat,Toast.LENGTH_SHORT).show();

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

                    if(d1.compareTo(daay)>=0 && d2.compareTo(daay)>=0)
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

                            double dis = ((MRP - SaleP)*100)/MRP ;
                            if(subcat==null){
                                subcat = String.valueOf(0);
                            }

                            if(cid==null){
                                //Toast.makeText(AnnounceOffer.this, "Without CID!" , Toast.LENGTH_SHORT).show();
                                LaunchSale(sid,pname.getText().toString(),mancat,subcat,desc.getText().toString(),price.getText().toString(),oprice.getText().toString(),dis,imgString,SDA,EDA);
                            }else{
                                //Toast.makeText(AnnounceOffer.this, "With CID!" , Toast.LENGTH_SHORT).show();
                                LaunchSaleCid(sid,pname.getText().toString(),mancat,subcat,desc.getText().toString(),price.getText().toString(),oprice.getText().toString(),dis,imgString,SDA,EDA);
                            }
                        }else if(d1.equals(d2)){
                            Toast.makeText(AnnounceOffer.this, "Start date/End date cannot be same!" , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AnnounceOffer.this, "Enter dates correctly!" , Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AnnounceOffer.this,"Enter dates correctly!",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AnnounceOffer.this, "Sale cost should be less than MRP!", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(AnnounceOffer.this, "All fields are mandatory!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(AnnounceOffer.this, "Image-1 is required!", Toast.LENGTH_LONG).show();
        }

    }

    public  void language(View view){

        if(count%2==0){
            pnameTIL.setHint("Offer Name");
            priceTIL.setHint("Price(MRP)");
            opriceTIL.setHint("Offer Price");
            sdateTIL.setHint("Start Date");
            edateTIL.setHint("End Date");
            descTIL.setHint("Description(Color, Size, Type, etc)");

            categoryAndSubcategoryTV.setText("Category and Subcategories:");
            uploadImagesTV.setText("(click below to add offer images)");
            image1TV.setText("Image - 1");
            image2TV.setText("Image - 2");
            image3TV.setText("Image - 3");
            addOfferTV.setText("Add Offer");
            FacingProblemTV.setText("Facing Problem? Call us");
            langBtn.setText("हिंदी");
            b.setText("Launch Sale");
        }else{
            pnameTIL.setHint("प्रस्ताव(ऑफ़र) का नाम");
            priceTIL.setHint("मूल्य(एम.आर.पी/MRP)");
            opriceTIL.setHint("ऑफर किया गया मूल्य");
            sdateTIL.setHint("आरंभ करने की तिथि");
            edateTIL.setHint("अंतिम तिथि");
            FacingProblemTV.setText("कोई परेशानी? अभी कॉल करें");
            descTIL.setHint("विवरण(रंग, आकार, प्रकार आदि)");

            categoryAndSubcategoryTV.setText("श्रेणी तथा उप्रेणी:");
            uploadImagesTV.setText("(तस्वीर डालना के लिए नीचे क्लिक करें)");
            image1TV.setText("तस्वीर - 1");
            image2TV.setText("तस्वीर - 2");
            image3TV.setText("तस्वीर - 3");
            addOfferTV.setText("प्रस्ताव(ऑफ़र) डाले");

            langBtn.setText("English");
            b.setText("लॉन्च ऑफ़र");
        }
        count++;
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
        /*
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");

            photo = getResizedBitmap(photo,330,380);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] arr = baos.toByteArray();
            imgString = Base64.encodeToString(arr, Base64.DEFAULT);
            imageView.setImageBitmap(photo);
        }
        */

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

                imageView1.setImageBitmap(photo);
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

                imageView2.setImageBitmap(photo);
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
                 Toast.makeText(AnnounceOffer.this,"Please try again",Toast.LENGTH_SHORT).show();
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
                imageView1.setImageBitmap(photo);
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
                imageView2.setImageBitmap(photo);
            } catch (Exception e){
                //Log.d(LOG_TAG, "imageDraw " + e.toString());
            }
        }
    }

    String myJSON,myJSON1;
    JSONArray peoples = null;

    AlertDialog loading;
    public void getCategories(final String sid){
        loading = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getCategory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON = response;
                        loading.dismiss();
                        showList();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        finish();
                        Toast.makeText(AnnounceOffer.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    private void showList() {
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

            loading.dismiss();
            addCat();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showList1() {
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
                materialDesignSpinner1.setItems("No Subcategory");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addCat() {
        materialDesignSpinner.setItems(SPINNERLIST);
        String value = (String) categories.get(materialDesignSpinner.getText().toString());
        //Toast.makeText(AnnounceOffer.this,value,Toast.LENGTH_SHORT).show();
        getSubCategories(value);
    }

    AlertDialog loading1;

    public void getSubCategories(final String id){

        loading1 = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
        loading1.setMessage("Loading Subcategories.");
        loading1.setCanceledOnTouchOutside(false);
        loading1.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getSubCategoryDec.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myJSON1=response;
                        loading1.dismiss();
                        showList1();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading1.dismiss();
                        Toast.makeText(AnnounceOffer.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
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

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

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

    public void LaunchSale(final String sid, final String name, final String mancat, final String subcat, final String desc, final String price, final String oprice, final double dis, final String imgStrin, final String start, final String end){
        final String result = null;
        final     AlertDialog loading3;
        loading3 = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/setOffer.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int n = Integer.parseInt(response);
                        loading3.dismiss();

                        if (n>0) {
                            String n2 = String.valueOf(n);
                            if(imgString1 != null || imgString2 !=null){

                                if(imgString1!=null && imgString2!=null){
                                    SetImages(n2);
                                }else if(imgString1 != null){
                                    SetImages1(n2);
                                }else if(imgString2 != null){
                                    SetImages2(n2);
                                }

                            }else{
                                Toast.makeText(AnnounceOffer.this, "Sale Launched!!", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(AnnounceOffer.this,RetailHome.class);
                                startActivity(in);
                            }

                        } else {
                            Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid", sid);
                params.put("pname", name);
                params.put("mainid", mancat);
                params.put("subid", subcat);
                params.put("desc", desc);
                params.put("price", price);
                params.put("oprice", oprice);
                params.put("dis", Double.toString(dis));
                params.put("SDate", start);
                params.put("EDate", end);
                params.put("img", imgStrin);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void LaunchSaleCid(final String sid, final String name, final String mancat, final String subcat, final String desc, final String price, final String oprice, final double dis, final String imgStrin, final String start, final String end){
        final String result = null;
        final     AlertDialog loading3;
        loading3 = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/setOfferwithCid.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int n = Integer.parseInt(response);
                        loading3.dismiss();
                        //Toast.makeText(AnnounceOffer.this, response, Toast.LENGTH_SHORT).show();

                        if (n>0) {
                            String n2 = String.valueOf(n);
                            if(imgString1 != null || imgString2 !=null){

                                if(imgString1!=null && imgString2!=null){
                                    SetImages(n2);
                                }else if(imgString1 != null){
                                    SetImages1(n2);
                                }else if(imgString2 != null){
                                    SetImages2(n2);
                                }

                            }else{
                                Toast.makeText(AnnounceOffer.this, "Sale Launched!!", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(AnnounceOffer.this,RetailHome.class);
                                startActivity(in);
                            }

                        } else {
                            Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid", sid);
                params.put("pname", name);
                params.put("mainid", mancat);
                params.put("subid", subcat);
                params.put("desc", desc);
                params.put("price", price);
                params.put("oprice", oprice);
                params.put("dis", Double.toString(dis));
                params.put("SDate", start);
                params.put("EDate", end);
                params.put("img", imgStrin);
                params.put("cid", cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SetImages(final String id) {

        final     AlertDialog loading3;
        loading3 = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
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
                            Toast.makeText(AnnounceOffer.this, "Sale Launched!!", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(AnnounceOffer.this,RetailHome.class);
                            startActivity(in);

                        } else {
                            Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        Toast.makeText(AnnounceOffer.this,"  Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid", id);
                params.put("img1", imgString1);
                params.put("img2", imgString2);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SetImages1(final String id) {

        final     AlertDialog loading3;
        loading3 = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/OfferImage1.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "1";
                        loading3.dismiss();
                        if (response.matches(str)) {
                            Toast.makeText(AnnounceOffer.this, "Sale Launched!!", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(AnnounceOffer.this,RetailHome.class);
                            startActivity(in);

                        } else {
                            Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        Toast.makeText(AnnounceOffer.this,error.toString()+"  Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid", id);
                params.put("img1", imgString1);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SetImages2(final String id) {

        final     AlertDialog loading3;
        loading3 = new SpotsDialog(AnnounceOffer.this, R.style.CustomR);
        loading3.setMessage("Please wait...");
        loading3.setCanceledOnTouchOutside(false);
        loading3.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/OfferImage2.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = "1";
                        loading3.dismiss();
                        if (response.matches(str)) {
                            Toast.makeText(AnnounceOffer.this, "Sale Launched!!", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(AnnounceOffer.this,RetailHome.class);
                            startActivity(in);

                        } else {
                            Toast.makeText(AnnounceOffer.this, "Error connecting to Server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading3.dismiss();
                        Toast.makeText(AnnounceOffer.this,error.toString()+"  Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("oid", id);
                params.put("img2", imgString2);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        count = 1;
        return true;
    }
}