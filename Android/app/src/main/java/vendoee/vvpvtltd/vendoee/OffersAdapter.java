package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by shagilsid on 25-01-2017.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {
    private Context mContext;
    private List<OffersList> offersLists;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView itemTitle,retailMobile,retailEmail,shopTitle;
        private ImageView itemThumbnail,shopImage;



        public MyViewHolder(View itemView) {
            super(itemView);
            itemTitle = (TextView)itemView.findViewById(R.id.offer_item_name);
            itemThumbnail =(ImageView)itemView.findViewById(R.id.item_image);



        }


    }

    public OffersAdapter(Context mContext, List<OffersList> offersLists) {
        this.mContext = mContext;
        this.offersLists = offersLists;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_view_offers,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
       final OffersList offersList=offersLists.get(position);
        holder.itemTitle.setText(offersList.getOfferstitle()); //for horizontal recyclerView
        holder.itemThumbnail.setImageBitmap(offersList.getOffersThumbnail());
        //for horizontal recyclerView
/*        holder.retailMobile.setText(offersList.getRetailMob());
        holder.retailEmail.setText(offersList.getRetailEmail());
        holder.shopImage.setImageBitmap(offersList.getShopImage());
        holder.shopTitle.setText(offersList.getShopTitle());*/


    }

    @Override
    public int getItemCount() {
        return offersLists.size();
    }
}
