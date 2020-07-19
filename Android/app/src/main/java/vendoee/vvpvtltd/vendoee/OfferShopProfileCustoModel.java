package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by Ayush Vardhan on 4/7/2017.
 */

public class OfferShopProfileCustoModel {
    private String prodName;
    private Bitmap itemThumbnail;
    private String oid;
    private String price;
    private String discount;
    private String start;
    private String end;

    public OfferShopProfileCustoModel(){}

    public OfferShopProfileCustoModel(String pname, Bitmap img, String id, String prce, String disc, String strt, String ed) {
        this.prodName = pname;
        this.itemThumbnail=img;
        this.oid=id;
        this.price=prce;
        this.discount=disc;
        this.start = strt;
        this.end = ed;
    }

    public String getProdName(){return prodName;}

    public String getOid(){return oid;}

    public String getPrice(){return price;}

    public String getDiscount(){return discount;}

    public String getStart(){return start;}

    public String getEnd(){return end;}

    public Bitmap getItemThumbnail(){return itemThumbnail;}
}
