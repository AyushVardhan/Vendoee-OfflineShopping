package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by shagilsid on 23-11-2016.
 */

public class RetailersToCustomerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<RetailersToCustomer> RetailersToCustomerList;
    public static int cashFlag;
    public static int verifyFlag;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView dist, shopName, owner,category,address,phone;
        public ImageView itemThumbnail; public CardView cv;

        public MyViewHolder(View view){
            super(view);
            shopName=(TextView)view.findViewById(R.id.shop_name);
            dist = (TextView)view.findViewById(R.id.distance);
            itemThumbnail=(ImageView)view.findViewById(R.id.store_image);
            owner = (TextView)view.findViewById(R.id.owner_name);
            category = (TextView)view.findViewById(R.id.category);
            address = (TextView)view.findViewById(R.id.address);
            phone = (TextView)view.findViewById(R.id.phone_no);
            cv = (CardView)view.findViewById(R.id.store);
            //cash.getI
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    public void setCheckFlag(int flag){
        this.cashFlag=flag;
    }
    public int getCheckFlag(){
        return cashFlag;
    }
    public void setVerifyFlag(int flag){
        this.verifyFlag=flag;
    }
    public int getVerifyFlag(){
        return verifyFlag;
    }
    public RetailersToCustomerAdapter(Context mContext, List<RetailersToCustomer> RetailersToCustomerList){
        this.context=mContext;
        this.RetailersToCustomerList=RetailersToCustomerList;
    }
    public RetailersToCustomerAdapter(){}

    @Override
    public int getItemViewType(int position) {
        return RetailersToCustomerList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_shops,parent,false);
            return new MyViewHolder(itemView);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyViewHolder) {
            final RetailersToCustomer retailersToCustomerList=RetailersToCustomerList.get(position);

            ((MyViewHolder) holder).itemThumbnail.setImageBitmap(retailersToCustomerList.getImage());
            ((MyViewHolder) holder).dist.setText(retailersToCustomerList.getDistance());
            ((MyViewHolder) holder).owner.setText(retailersToCustomerList.getOwner());
            ((MyViewHolder) holder).phone.setText(retailersToCustomerList.getTelephone());

            if(retailersToCustomerList.getCategory().length()>40){
                String cat = retailersToCustomerList.getCategory().substring(0,37);
                cat = cat +"...";
                ((MyViewHolder) holder).category.setText(cat);
            }else{
                String cat = retailersToCustomerList.getCategory();
                ((MyViewHolder) holder).category.setText(cat.substring(0,cat.length()-2));
            }

            if(retailersToCustomerList.getShopName().length()>21){
                String cat = retailersToCustomerList.getShopName().substring(0,18);
                cat = cat +"...";
                ((MyViewHolder) holder).shopName.setText(cat);
            }else{
                ((MyViewHolder) holder).shopName.setText(retailersToCustomerList.getShopName());
            }

            if(retailersToCustomerList.getAddress().length()>40){
                String cat = retailersToCustomerList.getAddress().substring(0,37);
                cat = cat +"...";
                ((MyViewHolder) holder).address.setText(cat);
            }else{
                ((MyViewHolder) holder).address.setText(retailersToCustomerList.getAddress());
            }



            ((MyViewHolder) holder).cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(context,Offers_MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sellerid", String.valueOf(retailersToCustomerList.getSelleerId()));
                    in.putExtras(bundle);
                    context.startActivity(in);
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
