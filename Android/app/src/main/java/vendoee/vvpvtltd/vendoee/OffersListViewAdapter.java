package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shagilsid on 27-01-2017.
 */

public class OffersListViewAdapter extends BaseAdapter {
    String [] result;
    String [] salePrice;
    String [] discount;

    Context context;
    Bitmap [] imageId;
    int[] pid;
    private static LayoutInflater inflater=null;
    public OffersListViewAdapter(Context mainActivity, String[] prgmNameList, Bitmap[] prgmImages, int[] pid,String[] saleprice, String[] discount) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        this.pid=pid;
        this.salePrice=saleprice;
        this.discount=discount;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv,salePrice,discount;
        ImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.offers_listview, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.salePrice=(TextView)rowView.findViewById(R.id.salePrice);
        holder.discount=(TextView)rowView.findViewById(R.id.saleDiscount);
        holder.tv.setText(result[position]);
        holder.salePrice.setText("₹ "+salePrice[position]);
        holder.discount.setText(discount[position]+"% off");
        holder.img.setImageBitmap(imageId[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                SharedPreferences sharedpreferences21 = context.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                String mobile = sharedpreferences21.getString("number_C", "");

                final String str = String.valueOf(pid[position]);
                Intent in = new Intent(context,SearchResults2.class);
                Bundle bundle = new Bundle();
                bundle.putString("SaleName", str);
                bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                in.putExtras(bundle);
                ((Activity) context).startActivity(in);
            }
        });
        return rowView;
    }
}
