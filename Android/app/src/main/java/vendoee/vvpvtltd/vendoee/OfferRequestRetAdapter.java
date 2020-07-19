package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferRequestRetAdapter extends ArrayAdapter<OfferRequestRetailerModel> {
    private ArrayList<OfferRequestRetailerModel> dataSet;
    Context mContext;

    private int lastPosition = -1;

    // View lookup cache
    private static class ViewHolder {
        TextView cname,price;
        TextView pname,date;
        TextView pdesc,time;
        Button call,launch;
    }

    public OfferRequestRetAdapter(ArrayList<OfferRequestRetailerModel> data, Context context) {
        super(context, R.layout.cardview_retailer_offer_request, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final OfferRequestRetailerModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.cardview_retailer_offer_request, parent, false);
            viewHolder.cname = (TextView) convertView.findViewById(R.id.customName);
            viewHolder.pname = (TextView) convertView.findViewById(R.id.name);
            viewHolder.pdesc = (TextView) convertView.findViewById(R.id.pdesc);
            viewHolder.price = (TextView) convertView.findViewById(R.id.pprice);
            viewHolder.date = (TextView) convertView.findViewById(R.id.pdate);
            //viewHolder.time = (TextView) convertView.findViewById(R.id.ptime);
            viewHolder.call = (Button) convertView.findViewById(R.id.callCustomer);
            viewHolder.launch = (Button) convertView.findViewById(R.id.launchOff);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.cname.setText(dataModel.getCName());
        viewHolder.pname.setText(dataModel.getPName());
        viewHolder.pdesc.setText(dataModel.getPDesc());
        viewHolder.price.setText(dataModel.getPrice());
        viewHolder.date.setText(dataModel.getDate());
        //viewHolder.time.setText(dataModel.getTime());
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+dataModel.getCContact()));
                mContext.startActivity(callIntent);
            }
        });

        viewHolder.launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext,dataModel.getCid(),Toast.LENGTH_SHORT).show();
                Intent in = new Intent(mContext,AnnounceOffer.class);
                in.putExtra("CustID",dataModel.getCid()+","+dataModel.getRID());
                mContext.startActivity(in);
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}
