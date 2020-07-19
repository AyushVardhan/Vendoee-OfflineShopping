package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferRetailerCustomerDealModel {

    Bitmap OImage;
    String OName,OPrice;
    String CName;
    String CPhone;
    String DId;
    String Cid,Sid,Oid;

    public OfferRetailerCustomerDealModel(String did, String oname, Bitmap oimage, String cname,String cphone,String oprice) {
        this.CName=cname;
        this.OImage=oimage;
        this.OName=oname;
        this.CPhone=cphone;
        this.DId=did;
        this.OPrice = oprice;
    }

    public String getOName() {
        return OName;
    }

    public String getCName() {
        return CName;
    }

    public String getCPhone() {
        return CPhone;
    }

    public String getDId() {
        return DId;
    }

    public String getCid(){ return Cid;}

    public String getSid() { return  Sid;}

    public String getOid() { return Oid;}

    public String getOPrice() { return OPrice;}

    public Bitmap getOImage(){return OImage;};
}
