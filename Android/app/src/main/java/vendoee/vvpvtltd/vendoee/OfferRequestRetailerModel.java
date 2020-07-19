package vendoee.vvpvtltd.vendoee;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferRequestRetailerModel {

    String cid;
    String price,date,time;
    String CName;
    String CContact;
    String PDesc;
    String PName, RID;

    public OfferRequestRetailerModel(String name, String desc, String price, String date,String time, String cid,String cname, String cnum, String rid ) {
        this.CName=cname;
        this.CContact=cnum;
        this.PDesc=desc;
        this.PName=name; this.price = price; this.RID = rid;
        this.cid = cid; this.date = date; this.time = time;
    }

    public String getCName() {
        return CName;
    }

    public String getCContact() {
        return CContact;
    }

    public String getPDesc() {
        return PDesc;
    }

    public String getPName() {
        return PName;
    }

    public String getPrice() {
        return price;
    }

    public String getCid() {
        return cid;
    }

    public String getDate() {
        return date;
    }

    public String getRID() {
        return RID;
    }

    public String getTime() {
        return time;
    }
}
