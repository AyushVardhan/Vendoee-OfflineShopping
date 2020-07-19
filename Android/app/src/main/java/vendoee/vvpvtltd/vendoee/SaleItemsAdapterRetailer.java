package vendoee.vvpvtltd.vendoee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

/**
 * Created by shagilsid on 23-11-2016.
 */

public class SaleItemsAdapterRetailer extends RecyclerView.Adapter {
    private Context context;
    private List<SaleItemsRetailer> saleItemsList;
    public int cashFlag; DatabaseHelper myDb;
    public static int verifyFlag;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View v) {
            super(v);

        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView itemName,salePrice, originalPrice ,verified, startDate, like ,endDate,vieww, descrption,itemCategory, itemSubCategory,discountPrice;
        public ImageView itemThumbnail,verifid; //public ImageView cash,verified;
        public CardView cv; public Button delete,editoffer;

        public MyViewHolder(View view){
            super(view);
            itemName=(TextView)view.findViewById(R.id.ItemName);
            vieww=(TextView)view.findViewById(R.id.CountView);
            salePrice=(TextView)view.findViewById(R.id.itemPrice);
            originalPrice=(TextView)view.findViewById(R.id.original_price);
            verified = (TextView)view.findViewById(R.id.veriefiedText);
            verifid = (ImageView)view.findViewById(R.id.veriefied);
            like = (TextView)view.findViewById(R.id.likeP);
            //offereeName=(TextView)view.findViewById(R.id.offereeTextView);
            startDate=(TextView)view.findViewById(R.id.start_date);
            endDate=(TextView)view.findViewById(R.id.end_date);
            itemThumbnail=(ImageView)view.findViewById(R.id.item_image);
            //endMarker = (ImageView)view.findViewById(R.id.endMarker);
            cv = (CardView)view.findViewById(R.id.cardView3);
            delete = (Button)view.findViewById(R.id.delete);
            editoffer = (Button)view.findViewById(R.id.edit);
            //itemCategory=(TextView) view.findViewById(R.id.item_category);
            //itemSubCategory=(TextView)view.findViewById(R.id.item_subcategory);
            //descrption=(TextView)view.findViewById(R.id.description);
            discountPrice=(TextView)view.findViewById(R.id.discount_off);
            //verified=(ImageView)view.findViewById(R.id.veriefied);
            //cash.getI
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
    public SaleItemsAdapterRetailer(Context mContext, List<SaleItemsRetailer> saleItemsList){
        this.context=mContext;
        this.saleItemsList=saleItemsList;
    }
    public SaleItemsAdapterRetailer(){}

    @Override
    public int getItemViewType(int position) {
        return saleItemsList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_retailer_offer,parent,false);
            return new MyViewHolder(itemView);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item_home, parent, false);

            return new ProgressViewHolder(v);
        }
    }
    private int calculateDiscount(double saleprice, double originalPrice ){
        double discount=originalPrice-saleprice;

        int discountPercent= (int) ((discount/originalPrice)*100);
        return discountPercent;
    }

    private String parseDate(String date){
        Date initDate = null;
        try {
            initDate = new SimpleDateFormat("yyyy-mm-dd").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
        String parsedDate = formatter.format(initDate);
        return parsedDate;

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            final SaleItemsRetailer saleItems=saleItemsList.get(position);
            ((MyViewHolder) holder).itemName.setText(saleItems.getItemName());
            ((MyViewHolder) holder).salePrice.setText( String.valueOf(saleItems.getItemSalePrice()));
            ((MyViewHolder) holder).originalPrice.setText( String.valueOf(saleItems.getItemOriginalPrice()));


            int n = calculateDiscount(saleItems.getItemSalePrice(),saleItems.getItemOriginalPrice());
            if(n>0){
                ((MyViewHolder) holder).discountPrice.setText(String.valueOf(n)+"%\nOFF");
            }

            ((MyViewHolder) holder).originalPrice.setPaintFlags(((MyViewHolder) holder).originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            ((MyViewHolder) holder).startDate.setText(parseDate(saleItems.getSaleStartDate()));
            ((MyViewHolder) holder).endDate.setText(parseDate(saleItems.getSaleEndDate()));
            ((MyViewHolder) holder).like.setText(saleItems.getLike());
            ((MyViewHolder) holder).verified.setText(saleItems.getVerify());

            if(saleItems.getVerify().equals("Yes")){
                ((MyViewHolder) holder).verifid.setImageDrawable(context.getResources().getDrawable(R.drawable.green_tick));
            }else if(saleItems.getVerify().equals("No")){
                ((MyViewHolder) holder).verifid.setImageDrawable(context.getResources().getDrawable(R.drawable.red_cross));
            }

            int t = checkFade(saleItems.getSaleEndDate());

            Date d = new Date();
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String tday = formatter.format(d);

            StringTokenizer str1 = new StringTokenizer(saleItems.getSaleEndDate(),"-");
            String endDate[] = new String[str1.countTokens()]; int i = 0;
            while(str1.hasMoreElements()){
                endDate[i] = str1.nextToken();
                i++;
            }

            StringTokenizer str2 = new StringTokenizer(tday,"-");
            String tdaya[] = new String[str2.countTokens()]; int j = 0;
            while(str2.hasMoreElements()){
                tdaya[j] = str2.nextToken();
                j++;
            }

            //Toast.makeText(context,endDate[2] + " " + endDate[1]+ " "+ endDate[0],Toast.LENGTH_SHORT).show();
            //Toast.makeText(context,tdaya[0] + " " + tdaya[1]+ " "+ tdaya[2],Toast.LENGTH_SHORT).show();
        /*
        holder.endMarker.setVisibility(View.VISIBLE);
        double r1 = 0.4;
        float f1 = (float)r1;
        holder.cv.setAlpha(f1);

        if(Integer.parseInt(endDate[2])<Integer.parseInt(tdaya[0])){
            holder.endMarker.setVisibility(View.VISIBLE);
            double r = 0.4;
            float f = (float)r;
            holder.cv.setAlpha(f);
        }else if(Integer.parseInt(endDate[1])<Integer.parseInt(tdaya[1]) && Integer.parseInt(endDate[2])==Integer.parseInt(tdaya[0])){
            holder.endMarker.setVisibility(View.VISIBLE);
            double r = 0.4;
            float f = (float)r;
            holder.cv.setAlpha(f);
        }else if(Integer.parseInt(endDate[0])<Integer.parseInt(tdaya[2]) && Integer.parseInt(endDate[1])==Integer.parseInt(tdaya[1]) && Integer.parseInt(endDate[2])==Integer.parseInt(tdaya[0])){
            holder.endMarker.setVisibility(View.VISIBLE);
            double r = 0.4;
            float f = (float)r;
            holder.cv.setAlpha(f);
        }
        */
        /*
        if(t==1){
            holder.endMarker.setVisibility(View.VISIBLE);
            double d = 0.4;
            float f = (float)d;
            holder.cv.setAlpha(f);
        }else{
            Toast.makeText(context,t+" ",Toast.LENGTH_SHORT).show();
        }*/

            //holder.descrption.setText(saleItems.getItemDesc());
            // holder.itemCategory.setText(saleItems.getItemCategory());
            //holder.itemSubCategory.setText(" -> "+saleItems.getItemSubCategory());
            myDb = new DatabaseHelper(context);
            ((MyViewHolder) holder).itemThumbnail.setImageBitmap(saleItems.getItemThumbnail());

            ((MyViewHolder) holder).vieww.setText(saleItems.getView());


              /*
        holder.cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Cashless Transactions",Toast.LENGTH_SHORT).show();
            }
        });
*/
        /*
        holder.verified.setImageResource(saleItems.getVerify());
        holder.verified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getVerifyFlag()==1){
                    Toast.makeText(context,"Verified",Toast.LENGTH_SHORT).show();
                }if (getVerifyFlag()==0){
                    Toast.makeText(context,"Not Verified",Toast.LENGTH_SHORT).show();
                }
            }
        });
            */

            ((MyViewHolder) holder).delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setMessage("Are you sure to delete this offer :")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String str = String.valueOf(saleItems.getOfferId());
                                    myDb.deleteOffer(str);
                                    deleteSale(str);
                                }
                            })
                            .setNegativeButton("no", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            ((MyViewHolder) holder).editoffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String str = String.valueOf(saleItems.getOfferId());
                    Intent in = new Intent(context,updateSale.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("ID", str);

                    in.putExtras(bundle);

                    context.startActivity(in);
                }
            });

        /*
        //Glide.with(context).load(saleItems.getItemThumbnail()).into(holder.itemThumbnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = String.valueOf(saleItems.getOfferId());
                //Toast.makeText(context,"ID is"+str, Toast.LENGTH_SHORT).show();
                if(context.toString().contains("CustomerSales")||context.toString().contains("CategorySearch")||context.toString().contains("ViewMore")){

                    SharedPreferences sharedpreferences21 = context.getSharedPreferences("MOBNO", Context.MODE_PRIVATE);
                    String mobile = sharedpreferences21.getString("number_C", "");

                    Intent in = new Intent(context,SearchResults2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("SaleName", str);
                    bundle.putString("CMobile", mobile.substring(3,mobile.length()));
                    in.putExtras(bundle);
                    ((Activity) context).startActivity(in);
                }

                if(context.toString().contains("RetailHome")){
                    new AlertDialog.Builder(context)
                            .setMessage("Choose an option :")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent in = new Intent(context,updateSale.class);
                                    Bundle bundle = new Bundle();

                                    bundle.putString("ID", str);

                                    in.putExtras(bundle);

                                    context.startActivity(in);
                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    myDb.deleteOffer(str);
                                    deleteSale(str);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        */
        }else{

        }



    }

    private int checkFade(String saleEndDate)  {
        int n = -1;
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        String dte = dateFormat.format(date);

        try{
            Date dt = dateFormat.parse(dte);
            if (new SimpleDateFormat("MM/yyyy").parse(saleEndDate).before(dt)) {
                n = 1;
                //Toast.makeText(context,"Yes",Toast.LENGTH_SHORT).show();
            }else{
                //Toast.makeText(context,"No "+ new SimpleDateFormat("MM/yyyy").parse(saleEndDate)+" "+ dateFormat.parse(dte),Toast.LENGTH_SHORT).show();
                n = 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return n;
    }

    public static final String JSON_URL5 = "http://www.vendoee.com/android-scripts/HitCount.php";

    private void Counter(final String str) {
        final AlertDialog loading;
        loading = new SpotsDialog(context, R.style.Custom);
        loading.setMessage("Please wait...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context,"Error connecting to Server",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",str);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static final String JSON_URL1 = "http://www.vendoee.com/android-scripts/deleteSales.php";

    private void deleteSale(final String id) {
        final AlertDialog loading;
        loading = new SpotsDialog(context, R.style.Custom);
        loading.setMessage("Deleting sale...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        String str = "1";
                        if(response.equals(str)){
                            Toast.makeText(context,"Sale Deleted!",Toast.LENGTH_SHORT).show();
                            Intent intent = ((Activity)context).getIntent() ;
                            ((Activity)context).finish();
                            ((Activity)context).startActivity(intent);
                        }else{
                            Toast.makeText(context,"Sale Not deleted. Try again!",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context,"Error connecting to Server",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public static final String JSON_URL = "http://www.vendoee.com/android-scripts/getOffLoc.php";

    private void getLatLong(final String id) {

        final AlertDialog loading;
        loading = new SpotsDialog(context, R.style.Custom);
        loading.setMessage("Loading offers...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        StringTokenizer str = new StringTokenizer(response,",");
                        int i=0;
                        String str1[] = new String[str.countTokens()];
                        while(str.hasMoreTokens())
                        {
                                    str1[i++]=str.nextToken();
                        }
                        double lat = Double.parseDouble(str1[0]);
                        double lng = Double.parseDouble(str1[1]);


                        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?"+ "&daddr=" + lat + "," +lng));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        ((Activity) context).startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context,"Error connecting to Server",Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }

    @Override
    public int getItemCount() {
        return saleItemsList.size();
    }
}