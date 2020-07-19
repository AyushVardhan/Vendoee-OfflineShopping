package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by shagilsid on 23-11-2016.
 */

public class SaleItemsRetailer {
    private String itemName;
    private double itemSalePrice;
    private double itemOriginalPrice;

    private String saleStartDate;
    private String saleEndDate;
    private String itemDesc;
    private Bitmap itemThumbnail;
    private int offerId;
    private int selleerId;
    private String itemCategory;
    private String itemSubCategory;
    private String shopName;
    private String telephone;
    private String lat;
    private String lng;
    private int cash;
    private String verify;
    private String vieww;
    private String distance;
    private String like; private String textcash;

    public SaleItemsRetailer(){}

    public SaleItemsRetailer(String itemName, double itemSalePrice, double itemOriginalPrice, String saleStartDate, String saleEndDate,
                             String itemDesc, Bitmap itemThumbnail, int offerId, int selleerId, String itemCategory, String itemSubCategory,
                             String shopName, String telephone, String lat, String lng, int cash, String vc, String dist, String like, String text) {
        this.itemName = itemName;
        this.itemSalePrice = itemSalePrice;
        this.itemOriginalPrice = itemOriginalPrice;

        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.itemDesc = itemDesc;
        this.itemThumbnail = itemThumbnail;
        this.offerId = offerId;
        this.selleerId = selleerId;
        this.itemCategory = itemCategory;
        this.itemSubCategory = itemSubCategory;
        this.shopName=shopName;
        this.telephone=telephone;
        this.lat=lat;
        this.lng=lng;
        this.cash=cash;
        this.vieww = vc;
        this.distance = dist;
        this.like = like;   this.textcash = text;
    }
                        //1             //2                 //3/                        4                       5                   6               7                   8               9               10              11                          12                  13               14         15          16          17
    public SaleItemsRetailer(String itemName, double itemSalePrice, double itemOriginalPrice, String saleStartDate, String saleEndDate, String itemDesc, Bitmap itemThumbnail, int offerId, int selleerId, String itemCategory, String itemSubCategory, String shopName, String telephone, String lat, String lng, int cash, String verify, String vc, String dist, String like, String text) {
        this.itemName = itemName;
        this.itemSalePrice = itemSalePrice;
        this.itemOriginalPrice = itemOriginalPrice;
        this.saleStartDate = saleStartDate;
        this.saleEndDate = saleEndDate;
        this.itemDesc = itemDesc;
        this.itemThumbnail = itemThumbnail;
        this.offerId = offerId;
        this.selleerId = selleerId;
        this.itemCategory = itemCategory;
        this.itemSubCategory = itemSubCategory;
        this.shopName = shopName;
        this.telephone = telephone;
        this.lat = lat;
        this.lng = lng;
        this.cash = cash;
        this.verify = verify;
        this.vieww = vc;
        this.distance = dist;
        this.like = like; this.textcash = text;
    }

    public String getVerify() {
        return verify;
    }

    public int getCash() {
        return cash;
    }

    public String getTextcash() { return  textcash;}

    public String getView() {
        return vieww;
    }

    public String getDistance(){
        return distance;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }

    public void setView(String vie) {
        this.vieww = vie;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getShopName() {
        return shopName;
    }

    public String getLike(){ return like; }

    public int getOfferId() {
        return offerId;
    }



    public int getSelleerId() {
        return selleerId;
    }



    public String getItemCategory() {
        return itemCategory;
    }



    public String getItemSubCategory() {
        return itemSubCategory;
    }


    public Bitmap getItemThumbnail() {
        return itemThumbnail;
    }



    public String getItemName() {
        return itemName;
    }



    public double getItemSalePrice() {
        return itemSalePrice;
    }



    public double getItemOriginalPrice() {
        return itemOriginalPrice;
    }

    public String getSaleStartDate() {
        return saleStartDate;
    }

    public String getSaleEndDate() {
        return saleEndDate;
    }

    public String getItemDesc() {
        return itemDesc;
    }

}
