package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by Ayush Vardhan on 3/29/2017.
 */

public class CustomerDealModel {

    private Bitmap oimg;
    private String OID,SID;
    private String PNAME,SNAME,START,END;
    private String PRICE,DISCOUNT,CONF;

    public CustomerDealModel(String oid, String sid, String pname, String price, String sname, Bitmap img, String start, String end, String discount, String conf) {
        this.OID=oid; this.oimg = img; this.END = end;
        this.PNAME=pname; this.SNAME = sname; this.PRICE = price;
         this.START = start; this.DISCOUNT =discount; this.CONF=conf;
    }


    public String getOID() {
        return OID;
    }

    public String getSID() {
        return SID;
    }

    public String getPNAME() {
        return PNAME;
    }

    public String getSNAME() {        return SNAME;    }

    public String getSTART() {
        return START;
    }

    public String getEND() {
        return END;
    }

    public String getPRICE() {
        return PRICE;
    }

    public String getDISCOUNT() {
        return DISCOUNT;
    }

    public String getCONF() {
        return CONF;
    }

    public Bitmap getOimg(){ return oimg; }
}
