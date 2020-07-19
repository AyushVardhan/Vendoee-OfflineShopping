package vendoee.vvpvtltd.vendoee;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferRequestCustModel {


    String price,date,time;
    String PDesc;
    String PName;

    public OfferRequestCustModel(String name, String desc, String price, String date ) {
        this.PDesc=desc;
        this.PName=name; this.price = price;
         this.date = date;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
