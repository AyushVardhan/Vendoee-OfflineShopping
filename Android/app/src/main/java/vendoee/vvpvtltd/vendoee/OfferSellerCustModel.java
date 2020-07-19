package vendoee.vvpvtltd.vendoee;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class OfferSellerCustModel {


    String OID;
    String PName;

    public OfferSellerCustModel(String name, String id) {
        this.PName=name; this.OID = id;
    }


    public String getPName() {
        return PName;
    }

    public String getOID() { return  OID;}
}
