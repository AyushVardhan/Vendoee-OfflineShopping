package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
/**
 * Created by Ayush Vardhan on 4/7/2017.
 */

public class OfferShopProfileCustoAdapter extends RecyclerView.Adapter<OfferShopProfileCustoAdapter.MyViewHolder> {
    private Context context;
    private List<OfferShopProfileCustoModel> RetailersToCustomerList;
    public static int cashFlag;
    public static int verifyFlag;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView pname, price, disc, start,end;
        public ImageView img; CardView cv;

        public MyViewHolder(View view){
            super(view);
            img = (ImageView)view.findViewById(R.id.imageView1);
            pname = (TextView)view.findViewById(R.id.textView1);
            price = (TextView)view.findViewById(R.id.salePrice);
            disc = (TextView)view.findViewById(R.id.saleDiscount);
            start = (TextView)view.findViewById(R.id.startD);
            end = (TextView)view.findViewById(R.id.endD);
            cv = (CardView)view.findViewById(R.id.cv);
            //cash.getI
        }
    }

    public OfferShopProfileCustoAdapter(Context mContext, List<OfferShopProfileCustoModel> RetailersToCustomerList){
        this.context=mContext;
        this.RetailersToCustomerList=RetailersToCustomerList;
    }
    public OfferShopProfileCustoAdapter(){}

    @Override
    public OfferShopProfileCustoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.offers_listview,parent,false);
        return new OfferShopProfileCustoAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final OfferShopProfileCustoModel retailersToCustomerList=RetailersToCustomerList.get(position);
        holder.pname.setText(retailersToCustomerList.getProdName());
        holder.price.setText(retailersToCustomerList.getPrice());
        holder.disc.setText("("+retailersToCustomerList.getDiscount()+ "% Off"+")");
        holder.img.setImageBitmap(retailersToCustomerList.getItemThumbnail());
        holder.start.setText(retailersToCustomerList.getStart());
        holder.end.setText(retailersToCustomerList.getEnd());
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedpreferences21 = context.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences21.getString("number_C", "");

                final String str = retailersToCustomerList.getOid();
                Intent in = new Intent(context,SearchResults2.class);
                Bundle bundle = new Bundle();
                bundle.putString("SaleName", str);
                bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                in.putExtras(bundle);
                ((Activity) context).startActivity(in);
            }
        });
    }


    @Override
    public int getItemCount() {
        return RetailersToCustomerList.size();
    }
}
