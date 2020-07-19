package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

public class nearby extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker gps;
    Dialog settingdialognearby;
    MaterialSpinner spinner;
    TextView txt, shop;
    EditText radmap;
    Button b1;
    HashMap<String, String> shopDetail;
    String radius,cid;
    String Categories[] = {"All Categories","Electronics","Appliances","Men","Women","Kids & Baby","Home & Furniture","Books & more","Grocery","Gift Hampers","Food & Restaurants","Other","Handlooms"};
    View mapView; Button b;
    private ArrayList<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);

        gps = new GPSTracker(nearby.this);
        shopDetail = new HashMap<String, String>();
        b1 = (Button)findViewById(R.id.setRangeButton);
        spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setItems(Categories);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rad = radmap.getText().toString();
                if(rad.isEmpty()){
                    Toast.makeText(nearby.this,"Enter Radius correctly!",Toast.LENGTH_LONG).show();
                }else {
                    radius = radmap.getText().toString();

                    String cid = "0"; String cat = spinner.getText().toString();
                    if(cat.contains("All")){
                        cid = "0";
                    }else if(cat.contains("Electronics")){
                        cid = "1";
                    }else if(cat.contains("Appliances")){
                        cid = "2";
                    }else if(cat.contains("Men")){
                        cid = "3";
                    }else if(cat.contains("Women")){
                        cid = "4";
                    }else if(cat.contains("Kids & Baby")){
                        cid = "5";
                    }else if(cat.contains("Home & Furniture")){
                        cid = "6";
                    }else if(cat.contains("Books & more")){
                        cid = "7";
                    }else if(cat.contains("Grocery")){
                        cid = "8";
                    }else if(cat.contains("Gift Hampers")){
                        cid = "9";
                    }else if(cat.contains("Tools & Hardware")){
                        cid = "10";
                    }else if(cat.contains("Electrical & Lighting")){
                        cid = "11";
                    }else if(cat.contains("Food & Restaurants")){
                        cid = "12";
                    }else if(cat.contains("Other")){
                        cid = "13";
                    }else if(cat.contains("Handlooms")){
                        cid = "14";
                    }

                    mMap.clear();
                    double lat = gps.getLatitude(); double lng = gps.getLongitude();
                    if(lat == 0.0 || lng == 0.0){
                        Toast.makeText(nearby.this,"Location can't be fetched. Enable GPS",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }else{
                        fetchShops(cid,radius,String.valueOf(lat),String.valueOf(lng));
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                    }
                }
            }
        });
        //radius = getIntent().getStringExtra("Radius");
        //cid = getIntent().getStringExtra("categoryID");

        radmap =(EditText)findViewById(R.id.radmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Toast.makeText(nearby.this,"Here",Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        // and next place it, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                locationButton.getLayoutParams();
        // position on right bottom
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 0, 30, 30);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {
                View v = getLayoutInflater().inflate(R.layout.windowlayout, null);
                // Getting the position from the marker
                // Setting the latitude
                //tvLat.setText("Latitude:" + latLng.latitude);
                // Setting the longitude
                //tvLng.setText("Longitude:"+ latLng.longitude);
                //LatLng latLng = arg0.getPosition();
                /*
                Button b = (Button)v.findViewById(R.id.takeme);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(nearby.this,"Yeas",Toast.LENGTH_SHORT).show();
                    }
                });
                */

                String str = shopDetail.get(arg0.getSnippet());
                StringTokenizer str1 = new StringTokenizer(str,";");
                String data[] = new String[str1.countTokens()]; int i = 0;
                while(str1.hasMoreElements()){
                    data[i] = str1.nextToken();
                    i++;
                }

                TextView name = (TextView) v.findViewById(R.id.shopname);
                TextView contact = (TextView) v.findViewById(R.id.shopContact);
                TextView category = (TextView) v.findViewById(R.id.shopCategory);
                name.setText(data[0]);  contact.setText(data[1]);  category.setText(data[2]);
                return v;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Toast.makeText(nearby.this, marker.getSnippet(),Toast.LENGTH_SHORT).show();
                Intent in = new Intent(nearby.this,Offers_MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("sellerid", marker.getSnippet());
                in.putExtras(bundle);
                startActivity(in);
            }
        });
        /*
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                settingdialognearby = new Dialog(nearby.this);
                settingdialognearby.setContentView(R.layout.dialog_showretail);
                settingdialognearby.setTitle("Settings Menu");
                settingdialognearby.show();
                txt = (TextView) settingdialognearby.findViewById(R.id.radius);
                shop = (TextView) settingdialognearby.findViewById(R.id.txt1);
                b = (Button)settingdialognearby.findViewById(R.id.getCity);
                String str = marker.getSnippet();
                if(str.contains("0")){
                    txt.setText("No details found!");
                }else{
                    int n = str.indexOf("[");
                    txt.setText(shopDetail.get(marker.getSnippet().substring(0,n-1)));
                }

                shop.setText(marker.getTitle());

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        }); */



        LatLng sydney = new LatLng(gps.getLatitude(), gps.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));



        //Toast.makeText(nearby.this,radius+" "+gps.getLatitude()+" "+gps.getLongitude(),Toast.LENGTH_LONG).show();
        //fetchShops(cid,radius,String.valueOf(gps.getLatitude()),String.valueOf(gps.getLongitude()));
    }

    private void fetchShops(final String cid, final String rad, final String lat, final String longitude) {
        //Toast.makeText(nearby.this,radius+" "+lat+" "+longitude,Toast.LENGTH_LONG).show();
        final AlertDialog loading;
        loading = new SpotsDialog(nearby.this, R.style.Custom);
        loading.setMessage("locating shops near you...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://www.vendoee.com/android-scripts/getRetailerRadius.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(nearby.this,response,Toast.LENGTH_LONG).show();
                        if(response.isEmpty()){
                            Toast.makeText(nearby.this,"No shops in this radius.",Toast.LENGTH_LONG).show();
                        }else{
                            StringTokenizer str1 = new StringTokenizer(response,"~");
                            while(str1.hasMoreElements()){
                                String tmp = str1.nextToken();

                                String status = tmp.substring(tmp.lastIndexOf('|') + 1);

                                StringTokenizer str2 = new StringTokenizer(tmp,"|");
                                String[] str = new String[str2.countTokens()];
                                int i =0;
                                while(str2.hasMoreElements()){
                                    str[i] = str2.nextToken();
                                    i++;
                                }

                                LatLng sydney = new LatLng(Double.parseDouble(str[1]), Double.parseDouble(str[2]));

                                if(status.equals("live")){
                                    //Toast.makeText(nearby.this,str[3],Toast.LENGTH_LONG).show();
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(str[3]).snippet(str[0]).icon(BitmapDescriptorFactory.fromResource(R.mipmap.vendoricon2)));
                                    markers.add(marker);
                                }else if(status.equals("end")){
                                    Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title(str[3]).snippet(str[0]).icon(BitmapDescriptorFactory.fromResource(R.mipmap.vendoricon1)));
                                    markers.add(marker);
                                }

                                int n = str[5].length();
                                shopDetail.put(str[0],str[3]+";"+str[4]+";"+str[5].replace(';',',').substring(0,n-1));
                            }
                        }

                        Circle circle = mMap.addCircle(new CircleOptions()
                                .center(new LatLng(Double.parseDouble(lat), Double.parseDouble(longitude)))
                                .radius(Integer.parseInt(rad)*1000)
                                .fillColor(Color.argb(100, 244,143,177))
                                .strokeColor(Color.GRAY)
                                .strokeWidth(5));

                        if(markers.isEmpty()){}else {
                            //Toast.makeText(nearby.this,"loaded",Toast.LENGTH_LONG).show();
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (Marker marker : markers) {
                                builder.include(marker.getPosition());
                            }
                            LatLngBounds bounds = builder.build();
                            int padding = 0; // offset from edges of the map in pixels
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.animateCamera(cu);
                        }

                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(nearby.this,"Cant connect to server! Try again.",Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("rad",rad);
                params.put("cid",cid);
                params.put("latit",lat);
                params.put("longit",longitude);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Toast.makeText(nearby.this,"here",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(nearby.this,CustomerSales.class));
    }
}
