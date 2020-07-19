package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by shagilsid on 25-01-2017.
 */

public class OffersList {
    private String offerstitle;
    private Bitmap offersThumbnail;
    private String retailMob;
    private String retailEmail;
    private String shopTitle;
    private Bitmap shopImage;
    private String salePrice;
    private String discount;
    private String retailLat;
    private String retailLng;
    private String retailAddress;
    private int sellerId;

    public OffersList(String offerstitle, Bitmap offersThumbnail, String salePrice, String discount,int sellerId) {
        this.offerstitle = offerstitle;
        this.offersThumbnail = offersThumbnail;
        this.salePrice = salePrice;
        this.discount = discount;

        this.sellerId = sellerId;
    }

    public OffersList(String retailMob, String retailEmail, String shopTitle, Bitmap shopImage,String retailAddress,String retailLat, String retailLng) {
        this.retailMob = retailMob;
        this.retailEmail = retailEmail;
        this.shopTitle = shopTitle;
        this.shopImage = shopImage;
        this.retailAddress=retailAddress;
        this.retailLat=retailLat;
        this.retailLng=retailLng;
    }

    public String getRetailLat() {
        return retailLat;
    }

    public void setRetailLat(String retailLat) {
        this.retailLat = retailLat;
    }

    public String getRetailLng() {
        return retailLng;
    }

    public void setRetailLng(String retailLng) {
        this.retailLng = retailLng;
    }

    public String getRetailAddress() {
        return retailAddress;
    }

    public void setRetailAddress(String retailAddress) {
        this.retailAddress = retailAddress;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }



    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public String getRetailMob() {
        return retailMob;
    }

    public void setRetailMob(String retailMob) {
        this.retailMob = retailMob;
    }

    public String getRetailEmail() {
        return retailEmail;
    }

    public void setRetailEmail(String retailEmail) {
        this.retailEmail = retailEmail;
    }

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopTitle) {
        this.shopTitle = shopTitle;
    }

    public Bitmap getShopImage() {
        return shopImage;
    }

    public void setShopImage(Bitmap shopImage) {
        this.shopImage = shopImage;
    }

    public String getOfferstitle() {
        return offerstitle;
    }

    public void setOfferstitle(String offerstitle) {
        this.offerstitle = offerstitle;
    }

    public Bitmap getOffersThumbnail() {
        return offersThumbnail;
    }

    public void setOffersThumbnail(Bitmap offersThumbnail) {
        this.offersThumbnail = offersThumbnail;
    }
}
