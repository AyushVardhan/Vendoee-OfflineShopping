package vendoee.vvpvtltd.vendoee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ayush Vardhan on 11/23/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "vendoee.db";
    public static final String TABLE_NAME1 = "offers";
    public static final String TABLE_NAME2 = "retailOffers";
    public static final String TABLE_NAME3 = "distanceTable";

    public static final String oid = "id";
    public static final String seller_id = "seller_id";
    public static final String product_name = "product_name";
    public static final String seller_category = "seller_category";
    public static final String product_category = "product_category";
    public static final String price = "price";
    public static final String offer_price = "offer_price";
    public static final String start_date = "start_date";
    public static final String end_date = "end_date";
    public static final String shopname = "shopname";
    public static final String description = "description";
    public static final String image = "image";
    public static final String discount = "discount";
    public static final String latit = "latitude";
    public static final String lngit = "longitude";
    public static final String cont = "contact";
    public static final String cashno = "cashless";
    public static final String viewc = "viewc";
    public static final String like = "like";

    public static final String oid_2 = "id";
    public static final String product_name_2 = "product_name";
    public static final String seller_category_2 = "seller_category";
    public static final String product_category_2 = "product_category";
    public static final String price_2 = "price";
    public static final String offer_price_2 = "offer_price";
    public static final String start_date_2 = "start_date";
    public static final String end_date_2 = "end_date";
    public static final String shopname_2 = "shopname";
    public static final String image_2 = "image";
    public static final String discount_2 = "discount";
    public static final String latit_2 = "latitude";
    public static final String lngit_2 = "longitude";
    public static final String cont_2 = "contact";
    public static final String cashno_2 = "cashless";
    public static final String verified = "verified";
    public static final String viewc_2 = "viewc";
    public static final String seller_id_2 = "seller_id";
    public static final String like_2 = "like";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table IF NOT EXISTS "+ TABLE_NAME1 + " (id INTEGER PRIMARY KEY, seller_id INTEGER, product_name TEXT, seller_category TEXT, product_category TEXT, price DECIMAL(8,2), offer_price DECIMAL(8,2), start_date DATE, end_date DATE, shopname TEXT, description TEXT, image BLOB, cashless TEXT, discount DECIMAL(8,2), latitude TEXT, longitude TEXT, contact TEXT, viewc TEXT, like TEXT)");
        db.execSQL("create table IF NOT EXISTS "+ TABLE_NAME2 + " (id INTEGER PRIMARY KEY, product_name TEXT, seller_category TEXT, product_category TEXT, price DECIMAL(8,2), offer_price DECIMAL(8,2), start_date DATE, end_date DATE, shopname TEXT, image BLOB, cashless TEXT, discount DECIMAL(8,2), latitude TEXT, longitude TEXT, contact TEXT, verified TEXT, viewc TEXT, seller_id INTEGER, like TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public boolean insertOffer(String id, String sid, String pname, String seller_cat, String prodCat, double prce, double oprice, String sdate, String edate, String sname, String desc, String img, double dis, String lat, String lng, String contact, String cash, String vc, String lik)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(oid,id);
        contentValues.put(seller_id,sid);
        contentValues.put(product_name,pname);
        contentValues.put(seller_category,seller_cat);
        contentValues.put(product_category,prodCat);
        contentValues.put(price,prce);
        contentValues.put(offer_price,oprice);
        contentValues.put(start_date,sdate);
        contentValues.put(end_date,edate);
        contentValues.put(shopname,sname);
        contentValues.put(description,desc);
        contentValues.put(image,img);
        contentValues.put(discount,dis);
        contentValues.put(latit,lat);
        contentValues.put(lngit,lng);
        contentValues.put(cont,contact);
        contentValues.put(cashno,cash);
        contentValues.put(viewc,vc);
        contentValues.put(like,lik);
        long res = db.insert(TABLE_NAME1,null,contentValues);
        if(res==-1)
        {
            return false;
        }else{
            return true;
        }
    }

    public boolean insertRetailOffer(String id, String pname, String seller_cat, String prodCat, double prce, double oprice, String sdate, String edate, String sname, String img, double dis, String lat, String lng, String contact, String cash, String verify, String vc, String sid, String lik)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(oid_2,id);
        contentValues.put(product_name_2,pname);
        contentValues.put(seller_category_2,seller_cat);
        contentValues.put(product_category_2,prodCat);
        contentValues.put(price_2,prce);
        contentValues.put(offer_price_2,oprice);
        contentValues.put(start_date_2,sdate);
        contentValues.put(end_date_2,edate);
        contentValues.put(shopname_2,sname);
        contentValues.put(image_2,img);
        contentValues.put(discount_2,dis);
        contentValues.put(latit_2,lat);
        contentValues.put(lngit_2,lng);
        contentValues.put(cont_2,contact);
        contentValues.put(cashno_2,cash);
        contentValues.put(verified,verify);
        contentValues.put(viewc_2,vc);
        contentValues.put(seller_id_2,sid);
        contentValues.put(like_2,lik);

        long res = db.insert(TABLE_NAME2,null,contentValues);
        if(res==-1)
        {
            return false;
        }else{
            return true;
        }
    }

    @Override
    public synchronized void close() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    public void deleteData(String cat){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME1+ " where seller_category = '"+ cat+"'");
    }

    public void deleteOffer(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME2+ " where id = '"+ id+"'");
    }

    public void deleteDat(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME2);
        db.execSQL("VACUUM");
    }

    public Cursor getCategorySearch(String cat){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res;
         res=db.rawQuery("select * from "+TABLE_NAME1+" where product_name like '%"+cat+"%'",null);
        return res;
    }


    public long countRow(){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement s = db.compileStatement( "select count(*) from "+ TABLE_NAME1);
        long count = s.simpleQueryForLong();
        return count;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select image from offers where id=47",null);
        return res;
    }

    public Cursor getAllDatatest1(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select cashless from "+TABLE_NAME1 + " where id = '"+ id +"'",null);
        return res;
    }

    public Cursor getItemDetails(String cat){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"' ORDER BY id DESC LIMIT 3",null);
        return res;
    }


    public void dropTable()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
    }

    public void dropTable2()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
    }

    public void createTable() {
        SQLiteDatabase db = this.getWritableDatabase();
                                                                    //1                     2                   3               4                       5                       6                       7                       8                   9           10              11                  12          13              14                  15              16              17
        db.execSQL("create table IF NOT EXISTS "+ TABLE_NAME1 + " (id INTEGER PRIMARY KEY, seller_id INTEGER, product_name TEXT, seller_category TEXT, product_category TEXT, price DECIMAL(8,2), offer_price DECIMAL(8,2), start_date DATE, end_date DATE, shopname TEXT, description TEXT, image BLOB, cashless TEXT, discount DECIMAL(8,2), latitude TEXT, longitude TEXT, contact TEXT, viewc TEXT, like TEXT)");
    }


    public void createTable2() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table IF NOT EXISTS "+ TABLE_NAME2 + " (id INTEGER PRIMARY KEY, product_name TEXT, seller_category TEXT, product_category TEXT, price DECIMAL(8,2), offer_price DECIMAL(8,2), start_date DATE, end_date DATE, shopname TEXT, image BLOB, cashless TEXT, discount DECIMAL(8,2), latitude TEXT, longitude TEXT, contact TEXT, verified TEXT, viewc TEXT, seller_id INTEGER, like TEXT)");
    }

    public Cursor getSortedItemDetailsPrice(String cat) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"'" + " ORDER BY offer_price ASC",null);
        return res;
    }

    public Cursor getSortedItemDetailsPriceReverse(String cat) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"'" + " ORDER BY offer_price DESC",null);
        return res;
    }

    public Cursor getSortedItemNewestFirst(String cat) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"'" + " ORDER BY id DESC",null);
        return res;
    }

    public Cursor getSortedItemOldestFirst(String cat) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"'" + " ORDER BY id ASC",null);
        return res;
    }

    public Cursor getSortedItemLiveSale(String cat) {
        SQLiteDatabase db=this.getReadableDatabase();

        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);

        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where end_date > '"+ modifiedDate + "' and seller_category = '"+ cat +"'",null);
        return res;
    }

    public Cursor getSortedItemDetailsPriceRetail() {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME2 + " ORDER BY offer_price ASC",null);
        return res;
    }

    public Cursor getSortedItemDetailsDiscountRetail() {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME2 + " ORDER BY discount DESC",null);
        return res;
    }

    public Cursor getSortedItemDetailsDiscount(String cat) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"'" + " ORDER BY discount DESC",null);
        return res;
    }

    public String getRetailerID(String oid){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement s = db.compileStatement( "select seller_id from "+ TABLE_NAME1 + " where id = "+oid);
        String count = s.simpleQueryForString();
        return count;
    }

    public Cursor getSortedItemDetailsDistance(String cat, double latitude, double longitude) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select id,latitude,longitude from "+TABLE_NAME1 + " where seller_category = '"+ cat +"'",null);
        return res;
    }

    public Cursor getSortedItemDetailsDistanceID() {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select id from "+TABLE_NAME3 + " ORDER BY distance",null);
        return res;
    }

    public void createTable3() {
        SQLiteDatabase db = this.getWritableDatabase();
        //1                     2                   3               4                       5                       6                       7                       8                   9           10              11                  12          13              14                  15              16              17
        db.execSQL("create table IF NOT EXISTS "+ TABLE_NAME3 + " (id TEXT PRIMARY KEY, distance INTEGER)");
    }

    public Boolean insertToTable3(String string, int distance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",string);
        contentValues.put("distance",distance);

        long res = db.insert(TABLE_NAME3,null,contentValues);
        if(res==-1)
        {
            return false;
        }else{
            return true;
        }
    }

    public void dropTable3()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
    }

    public Cursor getSortedItemDetailsDistanceFinal(String id) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where id = '"+ id +"'",null);
        return res;
    }

    public Cursor getItemDetailsfIX(String cat, String id , int i) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME1 + " where seller_category = '"+ cat +"' AND id<'"+ id +"' ORDER BY id DESC LIMIT "+i,null);
        return res;
    }

    public Cursor getItemDetailsfIXR( String id , int i) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME2 + " where "+" id<'"+ id +"' ORDER BY id DESC LIMIT "+i,null);
        return res;
    }

    public long getMaxID(String cat){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement s = db.compileStatement( "select MAX(ID) from "+ TABLE_NAME1 + " where seller_category = '"+ cat +"'");
        long count = s.simpleQueryForLong();
        return count;
    }

    public Cursor getItemDetailsRetail(String sid) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("Select * from "+TABLE_NAME2 + " where seller_id = '"+ sid +"' ORDER BY id DESC LIMIT 3",null);
        return res;
    }
}