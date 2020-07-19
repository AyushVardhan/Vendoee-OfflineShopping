package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferRetailerCustomerDealAdapter extends ArrayAdapter<OfferRetailerCustomerDealModel> {
    private ArrayList<OfferRetailerCustomerDealModel> dataSet;
    Context mContext; Button b;
    Dialog confirm; TextView txt;

    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        TextView cname;
        TextView pname;
        TextView pcost;
        TextView cphon;
        ImageView pimg;
        Button accept;
    }

    public OfferRetailerCustomerDealAdapter(ArrayList<OfferRetailerCustomerDealModel> data, Context context) {
        super(context, R.layout.cardview_retailer_deal, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final OfferRetailerCustomerDealModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cardview_retailer_deal, parent, false);
            viewHolder.pimg = (ImageView) convertView.findViewById(R.id.item_image);
            viewHolder.cname = (TextView) convertView.findViewById(R.id.customName);
            viewHolder.pname = (TextView) convertView.findViewById(R.id.name);
            viewHolder.cphon = (TextView) convertView.findViewById(R.id.customPhone);
            viewHolder.pcost = (TextView) convertView.findViewById(R.id.pdesc);
            viewHolder.accept = (Button)convertView.findViewById(R.id.callCustomer);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.cname.setText(dataModel.getCName());
        viewHolder.pname.setText(dataModel.getOName());
        viewHolder.pimg.setImageBitmap(dataModel.getOImage());
        viewHolder.cphon.setText(dataModel.getCPhone());
        viewHolder.pcost.setText(dataModel.getOPrice());

        viewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                confirm = new Dialog(mContext);
                confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
                confirm.setContentView(R.layout.dialog_deals);
                confirm.show();
                txt = (EditText) confirm.findViewById(R.id.radius);
                txt.setText(dataModel.getOPrice());
                b = (Button)confirm.findViewById(R.id.getCity);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(txt.getText().toString().isEmpty()){
                            Toast.makeText(mContext,"Enter the total shopping amount.",Toast.LENGTH_SHORT).show();
                        }else{
                            confirm.dismiss();
                            sendSubmit(dataModel.getDId(),txt.getText().toString());
                        }
                    }
                });
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    private void sendSubmit(final String dId, final String price) {
        final AlertDialog loading;
        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/RetailerFinalDeal.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        if(response.equals("1")){
                            Toast.makeText(mContext,"Points Awarded!",Toast.LENGTH_LONG).show();
                        }else if(response.equals("2")){
                            Toast.makeText(mContext,"Points Awarded!",Toast.LENGTH_LONG).show();
                        }

                        Intent in = new Intent(mContext,RetailHome.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(in);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(mContext,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("dealid",dId);
                params.put("price",price);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}
