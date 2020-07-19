package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by shagilsid on 23-11-2016.
 */

public class RetailersToCustomer {
    private String ShopName;
    private Bitmap itemThumbnail;
    private int selleerId;
    private String telephone;
    private String distance;
    private String category,address,owner;

    public RetailersToCustomer(){}

    public RetailersToCustomer(String ShopName, Bitmap itemThumbnail, int selleerId, String telephone, String category, String address, String ownerName , String dist) {
        this.itemThumbnail = itemThumbnail;
        this.selleerId = selleerId;
        this.telephone=telephone;
        this.address = address;
        this.category = category;
        this.ShopName = ShopName;
        this.distance = dist;
        this.owner = ownerName;
    }

    public String getShopName(){return this.ShopName;}
    public String getTelephone(){return this.telephone;}
    public String getCategory(){return this.category;}
    public String getAddress(){return this.address;}
    public String getOwner(){return this.owner;}
    public Bitmap getImage(){return this.itemThumbnail;}
    public int getSelleerId(){return this.selleerId;}

    public String getDistance(){return this.distance;};

    public void setShopName(String shopN){
        this.ShopName = shopN;
    }

    public void setDistance(String dist){
        this.distance = dist;
    }

    public void setItemThumbnail(Bitmap img){
        this.itemThumbnail = img;
    }

    public void setSelleerId(int id){
        this.selleerId = id;
    }

    public void setTelephone(String con){
        this.telephone = con;
    }

}
