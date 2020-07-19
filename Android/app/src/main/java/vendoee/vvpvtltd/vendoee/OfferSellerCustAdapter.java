package vendoee.vvpvtltd.vendoee;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferSellerCustAdapter extends ArrayAdapter<OfferSellerCustModel> {
    private ArrayList<OfferSellerCustModel> dataSet;
    Context mContext;

    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {

        TextView pname,status;
        Button launch;
    }

    public OfferSellerCustAdapter(ArrayList<OfferSellerCustModel> data, Context context) {
        super(context, R.layout.offers_request_customer, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final OfferSellerCustModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.seller_product_req, parent, false);
            viewHolder.pname = (TextView) convertView.findViewById(R.id.sname);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.launch = (Button)convertView.findViewById(R.id.launc);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.pname.setText(dataModel.getPName());

        if(dataModel.getOID().equals("-1")){
            viewHolder.launch.setVisibility(View.INVISIBLE);
            viewHolder.status.setText("Pending");
        }
        //viewHolder.time.setText(dataModel.getTime());

        viewHolder.launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedpreferences21 = mContext.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences21.getString("number_C", "");

                //Toast.makeText(mContext,dataModel.getOID(),Toast.LENGTH_SHORT).show();
                Intent in = new Intent(mContext,SearchResults2.class);
                Bundle bundle = new Bundle();
                bundle.putString("SaleName", dataModel.getOID());
                bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                in.putExtras(bundle);
                mContext.startActivity(in);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
