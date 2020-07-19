package vendoee.vvpvtltd.vendoee;

import android.graphics.Bitmap;

/**
 * Created by Ayush Vardhan on 5/1/2017.
 */

public class voucherModel {

    String name,points,id;
    Bitmap image;

    public voucherModel(String nam, String point, Bitmap img, String id ) {
        this.name = nam;
        this.points = point;
        this.image = img;
        this.id = id;
    }

    public String getName(){return this.name;}

    public String getPoints(){return this.points;}

    public Bitmap getImage() {return this.image;}

    public String getId() {return this.id;}
}
