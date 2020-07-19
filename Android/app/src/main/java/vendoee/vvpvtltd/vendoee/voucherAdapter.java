package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ayush Vardhan on 5/1/2017.
 */

public class voucherAdapter extends ArrayAdapter<voucherModel> {
    private ArrayList<voucherModel> dataSet;
    Context mContext;

    private int lastPosition = -1;

    private static class ViewHolder {
        TextView pname,points;
        ImageView img;
        CardView cv;
    }

    public voucherAdapter(ArrayList<voucherModel> data, Context context) {
        super(context, R.layout.cardview_voucher_product, data);
        this.dataSet = data;
        this.mContext=context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final voucherModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        voucherAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new voucherAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cardview_voucher_product, parent, false);
            viewHolder.pname = (TextView) convertView.findViewById(R.id.item_name);
            viewHolder.points = (TextView) convertView.findViewById(R.id.points);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.cv = (CardView)convertView.findViewById(R.id.cardview_offer);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (voucherAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.pname.setText(dataModel.getName());
        viewHolder.points.setText(dataModel.getPoints());
        viewHolder.img.setImageBitmap(dataModel.getImage());

        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedpreferences21 = mContext.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences21.getString("number_C", "");
                String phone = mobile.substring(3,mobile.length());

                getCustomerDets(phone,dataModel.getId(),dataModel.getPoints());

                //Toast.makeText(mContext,"You don't have enough points to claim his product",Toast.LENGTH_SHORT).show();
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void getCustomerDets(final String phone, final String id, final String points) {
        final AlertDialog loading;

        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Checking profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getCustomerPoints.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        
                        int point;
                        
                        if(response.matches("[0-9]+")){
                            point = Integer.parseInt(response);   
                            if(point >= Integer.parseInt(points)){
                                sendtoVendoee(phone,id);
                            }else{
                                Toast.makeText(mContext,"You don't have enough points to claim his product",Toast.LENGTH_SHORT).show();                                
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,"Cant connect to server! Try Again",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",phone);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }

    private void sendtoVendoee(final String phone, final String id) {

        final AlertDialog loading;

        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Checking profile...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getVoucherCustomerRequest.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.equals("1")){
                            Toast.makeText(mContext,"Request received! We will contact you soon.",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(mContext,"Cant connect to server! Try Again",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",phone);
                params.put("vid",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

    }
}
