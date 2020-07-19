package vendoee.vvpvtltd.vendoee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferRequestCustAdapter extends ArrayAdapter<OfferRequestCustModel> {
    private ArrayList<OfferRequestCustModel> dataSet;
    Context mContext;

    ArrayList<OfferSellerCustModel> dataModels1;
    private static OfferSellerCustAdapter adapter1;

    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        TextView price;
        TextView pname,date;
        TextView pdesc,time;
        Button launch;
    }

    public OfferRequestCustAdapter(ArrayList<OfferRequestCustModel> data, Context context) {
        super(context, R.layout.offers_request_customer, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final OfferRequestCustModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cardview_myrequestoffers, parent, false);
            viewHolder.pname = (TextView) convertView.findViewById(R.id.name);
            viewHolder.pdesc = (TextView) convertView.findViewById(R.id.pdesc);
            viewHolder.price = (TextView) convertView.findViewById(R.id.pprice);
            viewHolder.date = (TextView) convertView.findViewById(R.id.pdate);
            //viewHolder.time = (TextView) convertView.findViewById(R.id.ptime);
            viewHolder.launch = (Button) convertView.findViewById(R.id.launchOff);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.pname.setText(dataModel.getPName());
        viewHolder.pdesc.setText(dataModel.getPDesc());
        viewHolder.price.setText(dataModel.getPrice());
        viewHolder.date.setText(dataModel.getDate());
        //viewHolder.time.setText(dataModel.getTime());

        viewHolder.launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences2 = mContext.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences2.getString("number_C", "");

                String phonenum = mobile.substring(3,mobile.length());
                getSellers(dataModel.getPName(),phonenum);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

    private void getSellers(final String pName, final String phone) {
        final AlertDialog loading;
        loading = new SpotsDialog(mContext, R.style.Custom);
        loading.setMessage("Please Wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://vendoee.com/android-scripts/getProductRequestsSeller.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.equals("0")){
                            Toast.makeText(mContext,"No product demanded",Toast.LENGTH_LONG).show();
                        }else{
                            //Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                            dataModels1= new ArrayList<>();
                            StringTokenizer str = new StringTokenizer(response,";");

                            String reqId[] = new String[str.countTokens()]; String selID[] = new String[str.countTokens()];
                            String ShopName[] = new String[str.countTokens()]; String Oid[] = new String[str.countTokens()];
                            int j = 0;

                            while(str.hasMoreElements()){
                                String str1 = str.nextToken();
                                StringTokenizer str2 = new StringTokenizer(str1,",");
                                String data[] = new String[str2.countTokens()]; int  i =0;
                                while(str2.hasMoreElements()){
                                    data[i] = str2.nextToken();
                                    i++;
                                }
                                dataModels1.add(new OfferSellerCustModel(data[2],data[3]));
                            }
                            adapter1= new OfferSellerCustAdapter(dataModels1,mContext);

                            Dialog settingdialog = new Dialog(mContext);
                            settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            settingdialog.setContentView(R.layout.dialog_prod_req);
                            settingdialog.show();
                            ListView lv = (ListView) settingdialog.findViewById(R.id.listView);
                            lv.setAdapter(adapter1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        //Toast.makeText(mContext,"Error connecting to Server",Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("phone",phone);
                params.put("pname",pName);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}
