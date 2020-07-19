package vendoee.vvpvtltd.vendoee;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import dmax.dialog.SpotsDialog;


public class showCat extends AppCompatActivity {

    static int elec = 1; static int app = 1; static int men = 1; static int women = 1;
    static int kid = 1; static int ghar = 1;  static int buks = 1; static int kirana = 1;static int hnd = 1;
    static int part = 1; static int har = 1; static int lit = 1; static int fud = 1; static int oth = 1;
    TextView next;
    Dialog appliancesSubcat;

    Vector v = new Vector(13, 2);
    Vector vSub = new Vector(13, 2);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cat);

        next = (TextView)findViewById(R.id.inext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                SharedPreferences prefs = getSharedPreferences("SID", Context.MODE_PRIVATE);
                String id = prefs.getString("sid","");
                //Intent in = new Intent(showCat.this,RetailerSignIn.class);
                //startActivity(in);
                String str11 = "";               String str12 = "";

                String[] contains = new String[2] ;
                Enumeration vEnum1 = v.elements();
                Enumeration vEnum2 = vSub.elements();

                while(vEnum2.hasMoreElements()){
                    str11 = vEnum2.nextElement().toString();
                    str12 = str12 +";" + str11;
                }

                int j=0;

                if(v.size()>0){

                    loading = new SpotsDialog(showCat.this, R.style.Custom);
                    loading.setMessage("Please wait...");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    while(vEnum1.hasMoreElements())
                    {
                        String str = vEnum1.nextElement().toString();
                        int i =0; String cname, cid;

                        StringTokenizer st = new StringTokenizer(str,",");
                        while (st.hasMoreTokens()) {
                            contains[i++] = st.nextToken();
                        }

                        cname = contains[0]; cid = contains[1];

                        //Toast.makeText(showCat.this, cname +" " + cid,Toast.LENGTH_SHORT).show();

                        //vEnum1.nextElement()
                        insertCategory(id,cname,cid);
                        j++;
                    }

                    loading.dismiss();
                    if(j==v.size())
                    {
                        //Toast.makeText(showCat.this,str12,Toast.LENGTH_SHORT).show();
                        updateSubcat(str12,id);
                    }
                }else{
                    Toast.makeText(showCat.this, "Please Select a Category!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateSubcat(final String subcat, final String id) {

        loading = new SpotsDialog(showCat.this, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/setSubCategory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equals("1")){
                            loading.dismiss();
                            Toast.makeText(showCat.this,"You have been successfully registered!",Toast.LENGTH_LONG).show();
                            Intent in = new Intent(showCat.this,RetailHome.class);
                            startActivity(in);
                        }else{
                            //Toast.makeText(showCat.this,response,Toast.LENGTH_LONG).show();
                        }
                        /*
                         */
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(showCat.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid", id);
                params.put("subcat", subcat.substring(1,subcat.length()));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    AlertDialog loading;

    private void insertCategory(final String id, final String cname, final String cid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/setCategory.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(showCat.this,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("sid", id);
                params.put("cname", cname);
                params.put("cid", cid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void electronics(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.elecimg);
        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_electronics);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.electronics1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.electronics2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.electronics3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.electronics4);
        final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.electronics5);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("1"+","+"1")){a1.setChecked(true);}
            if(str.equals("1"+","+"2")){a2.setChecked(true);}
            if(str.equals("1"+","+"3")){a3.setChecked(true);}
            if(str.equals("1"+","+"4")){a4.setChecked(true);}
            if(str.equals("1"+","+"5")){a5.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("1"+","+"1")){
                        vSub.add("1"+","+"1");
                    }
                }else{
                    vSub.remove("1"+","+"1");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("1"+","+"2")){
                        vSub.add("1"+","+"2");
                    }
                }else{
                    vSub.remove("1"+","+"2");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("1"+","+"3")){
                        vSub.add("1"+","+"3");
                    }
                }else{
                    vSub.remove("1"+","+"3");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("1"+","+"4")){
                        vSub.add("1"+","+"4");
                    }
                }else{
                    vSub.remove("1"+","+"4");
                }

                if(a5.isChecked()){
                    if(!vSub.contains("1"+","+"5")){
                        vSub.add("1"+","+"5");
                    }
                }else{
                    vSub.remove("1"+","+"5");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Electronics"+","+"1")){
                        v.add("Electronics"+","+"1");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Electronics"+","+"1");
                }

                appliancesSubcat.dismiss();
            }
        });
    }

    public void appliance(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.appimg);

            appliancesSubcat = new Dialog(showCat.this);
            appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
            appliancesSubcat.setContentView(R.layout.dialog_sub_appliances);
            appliancesSubcat.show();

            final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances1);
            final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances2);
            final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances3);
            final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances4);
            final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances5);
            final CheckBox a6 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances6);
            final CheckBox a7 = (CheckBox)appliancesSubcat.findViewById(R.id.appliances7);

            Enumeration vEnum2 = vSub.elements();
            while(vEnum2.hasMoreElements()){
                String str = vEnum2.nextElement().toString();
                if(str.equals("2"+","+"6")){a1.setChecked(true);}
                if(str.equals("2"+","+"7")){a2.setChecked(true);}
                if(str.equals("2"+","+"8")){a3.setChecked(true);}
                if(str.equals("2"+","+"9")){a4.setChecked(true);}
                if(str.equals("2"+","+"10")){a5.setChecked(true);}
                if(str.equals("2"+","+"11")){a6.setChecked(true);}
                if(str.equals("2"+","+"12")){a7.setChecked(true);}
            }

            Button done = (Button)appliancesSubcat.findViewById(R.id.done);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(a1.isChecked()){
                        if(!vSub.contains("2"+","+"6")){
                            vSub.add("2"+","+"6");
                        }
                    }else{
                        vSub.remove("2"+","+"6");
                    }

                    if(a2.isChecked()){
                        if(!vSub.contains("2"+","+"7")){
                            vSub.add("2"+","+"7");
                        }
                    }else{
                        vSub.remove("2"+","+"7");
                    }

                    if(a3.isChecked()){
                        if(!vSub.contains("2"+","+"8")){
                            vSub.add("2"+","+"8");
                        }
                    }else{
                        vSub.remove("2"+","+"8");
                    }

                    if(a4.isChecked()){
                        if(!vSub.contains("2"+","+"9")){
                            vSub.add("2"+","+"9");
                        }
                    }else{
                        vSub.remove("2"+","+"9");
                    }

                    if(a5.isChecked()){
                        if(!vSub.contains("2"+","+"10")){
                            vSub.add("2"+","+"10");
                        }
                    }else{
                        vSub.remove("2"+","+"10");
                    }

                    if(a6.isChecked()){
                        if(!vSub.contains("2"+","+"11")){
                            vSub.add("2"+","+"11");
                        }
                    }else{
                        vSub.remove("2"+","+"11");
                    }

                    if(a7.isChecked()){
                        if(!vSub.contains("2"+","+"12")){
                            vSub.add("2"+","+"12");
                        }
                    }else{
                        vSub.remove("2"+","+"12");
                    }

                    if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()||a6.isChecked()||a7.isChecked()){
                        img.setVisibility(View.VISIBLE);
                        if(!v.contains("Appliances"+","+"2")){
                            v.add("Appliances"+","+"2");
                        }
                    }else{
                        img.setVisibility(View.INVISIBLE);
                        v.remove("Appliances"+","+"2");
                    }

                    appliancesSubcat.dismiss();
                }
            });
    }

    public void mens(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.menimg);

        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_men);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.men1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.men2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.men3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.men4);
        final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.men5);
        final CheckBox a6 = (CheckBox)appliancesSubcat.findViewById(R.id.men6);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("3"+","+"13")){a1.setChecked(true);}
            if(str.equals("3"+","+"14")){a2.setChecked(true);}
            if(str.equals("3"+","+"15")){a3.setChecked(true);}
            if(str.equals("3"+","+"16")){a4.setChecked(true);}
            if(str.equals("3"+","+"17")){a5.setChecked(true);}
            if(str.equals("3"+","+"18")){a6.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("3"+","+"13")){
                        vSub.add("3"+","+"13");
                    }
                }else{
                    vSub.remove("3"+","+"13");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("3"+","+"14")){
                        vSub.add("3"+","+"14");
                    }
                }else{
                    vSub.remove("3"+","+"14");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("3"+","+"15")){
                        vSub.add("3"+","+"15");
                    }
                }else{
                    vSub.remove("3"+","+"15");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("3"+","+"16")){
                        vSub.add("3"+","+"16");
                    }
                }else{
                    vSub.remove("3"+","+"16");
                }

                if(a5.isChecked()){
                    if(!vSub.contains("3"+","+"17")){
                        vSub.add("3"+","+"17");
                    }
                }else{
                    vSub.remove("3"+","+"17");
                }

                if(a6.isChecked()){
                    if(!vSub.contains("3"+","+"18")){
                        vSub.add("3"+","+"18");
                    }
                }else{
                    vSub.remove("3"+","+"18");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()||a6.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Men"+","+"3")){
                        v.add("Men"+","+"3");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Men"+","+"3");
                }

                appliancesSubcat.dismiss();
            }
        });
    }

    public void women(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.womenimg);


        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_women);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.women1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.women2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.women3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.women4);
        final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.women5);
        final CheckBox a6 = (CheckBox)appliancesSubcat.findViewById(R.id.women6);
        final CheckBox a7 = (CheckBox)appliancesSubcat.findViewById(R.id.women7);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("4"+","+"19")){a1.setChecked(true);}
            if(str.equals("4"+","+"20")){a2.setChecked(true);}
            if(str.equals("4"+","+"21")){a3.setChecked(true);}
            if(str.equals("4"+","+"22")){a4.setChecked(true);}
            if(str.equals("4"+","+"23")){a5.setChecked(true);}
            if(str.equals("4"+","+"24")){a6.setChecked(true);}
            if(str.equals("4"+","+"25")){a7.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("4"+","+"19")){
                        vSub.add("4"+","+"19");
                    }
                }else{
                    vSub.remove("4"+","+"19");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("4"+","+"20")){
                        vSub.add("4"+","+"20");
                    }
                }else{
                    vSub.remove("4"+","+"20");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("4"+","+"21")){
                        vSub.add("4"+","+"21");
                    }
                }else{
                    vSub.remove("4"+","+"21");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("4"+","+"22")){
                        vSub.add("4"+","+"22");
                    }
                }else{
                    vSub.remove("4"+","+"22");
                }

                if(a5.isChecked()){
                    if(!vSub.contains("4"+","+"23")){
                        vSub.add("4"+","+"23");
                    }
                }else{
                    vSub.remove("4"+","+"23");
                }

                if(a6.isChecked()){
                    if(!vSub.contains("4"+","+"24")){
                        vSub.add("4"+","+"24");
                    }
                }else{
                    vSub.remove("4"+","+"24");
                }

                if(a7.isChecked()){
                    if(!vSub.contains("4"+","+"25")){
                        vSub.add("4"+","+"25");
                    }
                }else{
                    vSub.remove("4"+","+"25");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()||a6.isChecked()||a7.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Women"+","+"4")){
                        v.add("Women"+","+"4");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Women"+","+"4");
                }

                appliancesSubcat.dismiss();
            }
        });


    }

    public void kids(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.kidimg);

        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_kids);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.kids1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.kids2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.kids3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.kids4);
        final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.kids5);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("5"+","+"26")){a1.setChecked(true);}
            if(str.equals("5"+","+"27")){a2.setChecked(true);}
            if(str.equals("5"+","+"28")){a3.setChecked(true);}
            if(str.equals("5"+","+"29")){a4.setChecked(true);}
            if(str.equals("5"+","+"30")){a5.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("5"+","+"26")){
                        vSub.add("5"+","+"26");
                    }
                }else{
                    vSub.remove("5"+","+"26");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("5"+","+"27")){
                        vSub.add("5"+","+"27");
                    }
                }else{
                    vSub.remove("5"+","+"27");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("5"+","+"28")){
                        vSub.add("5"+","+"28");
                    }
                }else{
                    vSub.remove("5"+","+"28");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("5"+","+"29")){
                        vSub.add("5"+","+"29");
                    }
                }else{
                    vSub.remove("5"+","+"29");
                }

                if(a5.isChecked()){
                    if(!vSub.contains("5"+","+"30")){
                        vSub.add("5"+","+"30");
                    }
                }else{
                    vSub.remove("5"+","+"30");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Kids & Baby"+","+"5")){
                        v.add("Kids & Baby"+","+"5");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Kids & Baby"+","+"5");
                }

                appliancesSubcat.dismiss();
            }
        });
    }

    public void ghar(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.gharimg);

        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_home);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.home1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.home2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.home3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.home4);
        final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.home5);
        final CheckBox a6 = (CheckBox)appliancesSubcat.findViewById(R.id.home6);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("6"+","+"31")){a1.setChecked(true);}
            if(str.equals("6"+","+"32")){a2.setChecked(true);}
            if(str.equals("6"+","+"33")){a3.setChecked(true);}
            if(str.equals("6"+","+"34")){a4.setChecked(true);}
            if(str.equals("6"+","+"35")){a5.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("6"+","+"31")){
                        vSub.add("6"+","+"31");
                    }
                }else{
                    vSub.remove("6"+","+"31");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("6"+","+"32")){
                        vSub.add("6"+","+"32");
                    }
                }else{
                    vSub.remove("6"+","+"32");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("6"+","+"33")){
                        vSub.add("6"+","+"33");
                    }
                }else{
                    vSub.remove("6"+","+"33");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("6"+","+"34")){
                        vSub.add("6"+","+"34");
                    }
                }else{
                    vSub.remove("6"+","+"34");
                }

                if(a5.isChecked()){
                    if(!vSub.contains("6"+","+"35")){
                        vSub.add("6"+","+"35");
                    }
                }else{
                    vSub.remove("6"+","+"35");
                }

                if(a6.isChecked()){
                    if(!vSub.contains("6"+","+"36")){
                        vSub.add("6"+","+"36");
                    }
                }else{
                    vSub.remove("6"+","+"36");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()||a6.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Home & Furniture"+","+"6")){
                        v.add("Home & Furniture"+","+"6");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Home & Furniture"+","+"6");
                }

                appliancesSubcat.dismiss();
            }
        });


    }

    public void buk(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.bukimg);

        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_books);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.book1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.book2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.book3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.book4);
        final CheckBox a5 = (CheckBox)appliancesSubcat.findViewById(R.id.book5);
        final CheckBox a6 = (CheckBox)appliancesSubcat.findViewById(R.id.book6);
        final CheckBox a7 = (CheckBox)appliancesSubcat.findViewById(R.id.book7);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("7"+","+"37")){a1.setChecked(true);}
            if(str.equals("7"+","+"38")){a2.setChecked(true);}
            if(str.equals("7"+","+"39")){a3.setChecked(true);}
            if(str.equals("7"+","+"40")){a4.setChecked(true);}
            if(str.equals("7"+","+"41")){a5.setChecked(true);}
            if(str.equals("7"+","+"42")){a6.setChecked(true);}
            if(str.equals("7"+","+"43")){a7.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("7"+","+"37")){
                        vSub.add("7"+","+"37");
                    }
                }else{
                    vSub.remove("7"+","+"37");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("7"+","+"38")){
                        vSub.add("7"+","+"38");
                    }
                }else{
                    vSub.remove("7"+","+"38");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("7"+","+"39")){
                        vSub.add("7"+","+"39");
                    }
                }else{
                    vSub.remove("7"+","+"39");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("7"+","+"40")){
                        vSub.add("7"+","+"40");
                    }
                }else{
                    vSub.remove("7"+","+"40");
                }

                if(a5.isChecked()){
                    if(!vSub.contains("7"+","+"41")){
                        vSub.add("7"+","+"41");
                    }
                }else{
                    vSub.remove("7"+","+"41");
                }

                if(a6.isChecked()){
                    if(!vSub.contains("7"+","+"42")){
                        vSub.add("7"+","+"42");
                    }
                }else{
                    vSub.remove("7"+","+"42");
                }

                if(a7.isChecked()){
                    if(!vSub.contains("7"+","+"43")){
                        vSub.add("7"+","+"43");
                    }
                }else{
                    vSub.remove("7"+","+"43");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()||a5.isChecked()||a6.isChecked()||a7.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Books & more"+","+"7")){
                        v.add("Books & more"+","+"7");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Books & more"+","+"7");
                }

                appliancesSubcat.dismiss();
            }
        });
    }

    public void kirana(View view)
    {
        ImageView img = (ImageView)findViewById(R.id.grocer);

        kirana = kirana +1;
        if(kirana%2==0)
        {
            img.setVisibility(View.VISIBLE);
            v.add("Grocery"+","+"8");
            vSub.add("8"+","+"0");
        }else{
            img.setVisibility(View.INVISIBLE);
            v.remove("Grocery"+","+"8");
            vSub.remove("8"+","+"0");
        }
    }

    public void parts(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.partimg);

        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_parts);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.part1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.part2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.part3);
        final CheckBox a4 = (CheckBox)appliancesSubcat.findViewById(R.id.part4);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("9"+","+"44")){a1.setChecked(true);}
            if(str.equals("9"+","+"45")){a2.setChecked(true);}
            if(str.equals("9"+","+"46")){a3.setChecked(true);}
            if(str.equals("9"+","+"47")){a4.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("9"+","+"44")){
                        vSub.add("9"+","+"44");
                    }
                }else{
                    vSub.remove("9"+","+"44");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("9"+","+"45")){
                        vSub.add("9"+","+"45");
                    }
                }else{
                    vSub.remove("9"+","+"45");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("9"+","+"46")){
                        vSub.add("9"+","+"46");
                    }
                }else{
                    vSub.remove("9"+","+"46");
                }

                if(a4.isChecked()){
                    if(!vSub.contains("9"+","+"47")){
                        vSub.add("9"+","+"47");
                    }
                }else{
                    vSub.remove("9"+","+"47");
                }

                if(a1.isChecked()||a2.isChecked()||a3.isChecked()||a4.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Gift Hampers"+","+"9")){
                        v.add("Gift Hampers"+","+"9");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Gift Hampers"+","+"9");
                }

                appliancesSubcat.dismiss();
            }
        });
    }

    public void foodds(View view)
    {
        final ImageView img = (ImageView)findViewById(R.id.food);

        appliancesSubcat = new Dialog(showCat.this);
        appliancesSubcat.requestWindowFeature(Window.FEATURE_NO_TITLE);
        appliancesSubcat.setContentView(R.layout.dialog_sub_food);
        appliancesSubcat.show();

        final CheckBox a1 = (CheckBox)appliancesSubcat.findViewById(R.id.food1);
        final CheckBox a2 = (CheckBox)appliancesSubcat.findViewById(R.id.food2);
        final CheckBox a3 = (CheckBox)appliancesSubcat.findViewById(R.id.food3);

        Enumeration vEnum2 = vSub.elements();
        while(vEnum2.hasMoreElements()){
            String str = vEnum2.nextElement().toString();
            if(str.equals("12"+","+"51")){a1.setChecked(true);}
            if(str.equals("12"+","+"52")){a2.setChecked(true);}
            if(str.equals("12"+","+"53")){a3.setChecked(true);}
        }

        Button done = (Button)appliancesSubcat.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(a1.isChecked()){
                    if(!vSub.contains("12"+","+"51")){
                        vSub.add("12"+","+"51");
                    }
                }else{
                    vSub.remove("12"+","+"51");
                }

                if(a2.isChecked()){
                    if(!vSub.contains("12"+","+"52")){
                        vSub.add("12"+","+"52");
                    }
                }else{
                    vSub.remove("12"+","+"52");
                }

                if(a3.isChecked()){
                    if(!vSub.contains("12"+","+"53")){
                        vSub.add("12"+","+"53");
                    }
                }else{
                    vSub.remove("12"+","+"53");
                }


                if(a1.isChecked()||a2.isChecked()||a3.isChecked()){
                    img.setVisibility(View.VISIBLE);
                    if(!v.contains("Food & Restaurants"+","+"12")){
                        v.add("Food & Restaurants"+","+"12");
                    }
                }else{
                    img.setVisibility(View.INVISIBLE);
                    v.remove("Food & Restaurants"+","+"12");
                }

                appliancesSubcat.dismiss();
            }
        });
    }

    public void otherss(View view)
    {
        //Intent in = new Intent(showCat.this,RetailerContact.class);
        //startActivity(in);

        ImageView img = (ImageView)findViewById(R.id.othrimg);
        oth = oth +1;
        if(oth%2==0)
        {
            img.setVisibility(View.VISIBLE);
            v.add("Other"+","+"13");
            vSub.add("13"+","+"0");
        }else{
            img.setVisibility(View.INVISIBLE);
            v.remove("Other"+","+"13");
            vSub.remove("13"+","+"0");
        }

    }

    public void handloom(View view) {
        ImageView img = (ImageView)findViewById(R.id.handlum);
        hnd = hnd +1;
        if(hnd%2==0)
        {
            img.setVisibility(View.VISIBLE);
            v.add("Handlooms"+","+"14");
            vSub.add("14"+","+"0");
        }else{
            img.setVisibility(View.INVISIBLE);
            v.remove("Handlooms"+","+"14");
            vSub.remove("14"+","+"0");
        }

    }
}

