package vendoee.vvpvtltd.vendoee;

/**
 * Created by Ayush Vardhan on 3/27/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ViewHolder> {
    private Context mContext;
    private List<String> mDataSet;


    String [] name;
    String [] daam;
    String [] ID;
    Bitmap[] Img;
    String pname123;

    public ColorsAdapter(Context context, Bitmap[] img, String[] pname, String[] price, String[] id, String pname1){
        mContext = context;
        Img = img;
        name = pname;
        daam = price;
        ID = id;
        pname123 = pname1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView pname;
        public TextView prie;
        public TextView id;
        public ImageView imgV,rss;

        public ViewHolder(View v){
            super(v);
            // Get the widget reference from the custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            pname = (TextView) v.findViewById(R.id.product);
            prie = (TextView) v.findViewById(R.id.price);
            id = (TextView) v.findViewById(R.id.id);
            imgV = (ImageView) v.findViewById(R.id.image);
            rss = (ImageView)v.findViewById(R.id.rupee);
        }
    }

    @Override
    public ColorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.cardview_offer_similarproducts,parent,false);
        ViewHolder vh = new ViewHolder(v);

        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        // Get the current color from the data set
        String Product = name[position];
        String Price = daam[position];
        String Id = ID[position];
        holder.imgV.setImageBitmap(Img[position]);
        if(Product.length()>=15){
            Product = Product.substring(0,13);
            Product = Product+"..";
        }

        if(Product.equals("View More")){
            holder.rss.setVisibility(View.INVISIBLE);
            holder.prie.setVisibility(View.INVISIBLE);
        }

        holder.pname.setText(Product);
        holder.id.setText(Id);
        // Set the TextView text color same as CardView background color
        holder.prie.setText(Price);

        // Set a click listener for CardView
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new Intent
                final String str = ID[position];
                if(str.equals("0")){
                    //Toast.makeText(mContext,"Load new "+ID[position-1] + "  "+ pname123+";",Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(mContext,ViewMore.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("prodname", pname123);
                    bundle.putString("oid", ID[position-1]);
                    in.putExtras(bundle);
                    mContext.startActivity(in);
                }else{

                    SharedPreferences sharedpreferences21 = mContext.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                    String mobile = sharedpreferences21.getString("number_C", "");

                    Intent in = new Intent(mContext,SearchResults2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SaleName", str);
                    bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                    in.putExtras(bundle);
                    mContext.startActivity(in);
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        // Count the items
        return name.length;
    }
}