package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class CustomerDealAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<CustomerDealModel> RetailersToCustomerList;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    public static class MyViewHolder extends RecyclerView.ViewHolder{

         TextView oname,disc;  CardView cv;
         TextView oprice,sname;
         TextView start,end;
         ImageView img;

        public MyViewHolder(View view){
            super(view);
            oname = (TextView) view.findViewById(R.id.pname);
            oprice = (TextView) view.findViewById(R.id.price);
            sname = (TextView) view.findViewById(R.id.sname);
            start = (TextView) view.findViewById(R.id.start);
            end = (TextView) view.findViewById(R.id.end);
            disc = (TextView) view.findViewById(R.id.discount);
            img = (ImageView) view.findViewById(R.id.oimage);
            cv = (CardView)view.findViewById(R.id.store);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    public CustomerDealAdapter(Context mContext, List<CustomerDealModel> RetailersToCustomerList){
        this.context=mContext;
        this.RetailersToCustomerList=RetailersToCustomerList;
    }
    public CustomerDealAdapter(){}

    @Override
    public int getItemViewType(int position) {
        return RetailersToCustomerList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_deal,parent,false);
            return new CustomerDealAdapter.MyViewHolder(itemView);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            return new ProgressViewHolder(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final CustomerDealModel dataModel=RetailersToCustomerList.get(position);

            if(dataModel.getPNAME().length()>14){
                String cat = dataModel.getPNAME().substring(0,11);
                cat = cat +"...";
                ((MyViewHolder) holder).oname.setText(cat);
            }else{
                ((MyViewHolder) holder).oname.setText(dataModel.getPNAME());
            }

            ((MyViewHolder) holder).oprice.setText(dataModel.getPRICE());
            ((MyViewHolder) holder).sname.setText(dataModel.getSNAME());
            ((MyViewHolder) holder).start.setText(dataModel.getSTART());
            ((MyViewHolder) holder).end.setText(dataModel.getEND());
            ((MyViewHolder) holder).disc.setText("("+dataModel.getDISCOUNT()+"% Off)");
            ((MyViewHolder) holder).img.setImageBitmap(dataModel.getOimg());
            ((MyViewHolder) holder).cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context,"here",Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedpreferences21 = context.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                    String mobile = sharedpreferences21.getString("number_C", "");
                    String str = dataModel.getOID();
                    Intent in = new Intent(context,SearchResults2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SaleName", str);
                    bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                    in.putExtras(bundle);
                    ((Activity) context).startActivity(in);
                }
            });
        }else{

        }
    }

    @Override
    public int getItemCount() {
        return RetailersToCustomerList.size();
    }
}
