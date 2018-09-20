package com.mobitechs.vbags.sqlite_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VBags";


    public static final String Table_ProductDetails = "ProductDetails";
    public static final String ProductDetails_ID = "Id";
    public static final String Product_Id = "ProductId";
    public static final String Product_Name = "ProductName";
    public static final String Product_Category = "Category";
    public static final String Product_Price = "Price";
    public static final String Product_Color = "Color";
    public static final String Product_Size = "Size";
    public static final String Product_Details = "Details";
    public static final String Product_Description = "Description";
    public static final String Product_Images = "Images";
    public static final String Product_Ratings = "Ratings";

    // user details
    public static final String Table_UserDetails = "UserDetails";
    public static final String UserDetails_Id = "Id";
    public static final String UserDetails_UserId = "UserId";
    public static final String UserDetails_Name = "Name";
    public static final String UserDetails_MobileNo = "MobileNo";
    public static final String UserDetails_EmailId = "EmailId";
    public static final String UserDetails_Password = "Password";
    public static final String UserDetails_Address = "Address";
    public static final String UserDetails_City = "City";
    public static final String UserDetails_Area = "Area";
    public static final String UserDetails_PinCode = "PinCode";

    // order details
    public static final String Table_OrderDetails = "OrderDetails";
    public static final String OrderDetails_Id = "Id";
    public static final String UserId = "userId";

    // wishList Details details
    public static final String Table_WishListDetails = "WishListDetails";
    public static final String WishListDetails_Id = "Id";

    // cart list details
    public static final String Table_CartListDetails = "CartListDetails";
    public static final String CartListDetails_Id = "Id";

    public static final String Table_CategoryDetails = "CategoryDetails";
    public static final String CategoryDetailsId = "Id";
    public static final String CategoryId = "CategoryId";
    public static final String CategoryName = "CategoryName";


    String Create_Table_User_Details = "CREATE TABLE " + Table_UserDetails + "(" + UserDetails_Id + " INTEGER PRIMARY KEY," + UserDetails_UserId + " TEXT," + UserDetails_Name + " TEXT," + UserDetails_MobileNo + " TEXT," + UserDetails_EmailId + " TEXT," + UserDetails_Password + " TEXT," + UserDetails_Address + " TEXT," + UserDetails_City + " TEXT," + UserDetails_Area + " TEXT," + UserDetails_PinCode + " TEXT" + ")";

    String Create_Table_Product_Details = "CREATE TABLE " + Table_ProductDetails + "(" + ProductDetails_ID + " INTEGER PRIMARY KEY," + Product_Id + " TEXT," + Product_Name + " TEXT UNIQUE," + Product_Category + " TEXT," + Product_Price + " TEXT," + Product_Color + " TEXT," + Product_Size + " TEXT," + Product_Details + " TEXT," + Product_Description + " TEXT," + Product_Images + " TEXT," + Product_Ratings + " TEXT" + ")";
    String Create_Table_Category_Details = "CREATE TABLE " + Table_CategoryDetails + "(" + CategoryDetailsId + " INTEGER PRIMARY KEY," + CategoryId + " TEXT UNIQUE," + CategoryName + " TEXT," + Product_Category + " TEXT"+")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        super(context, Environment.getExternalStorageDirectory()+"/StudentCares/Database".toString()+DATABASE_NAME, null, DATABASE_VERSION);
//        SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/StudentCares/Database".toString()+DATABASE_NAME,null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create_Table_User_Details);
        db.execSQL(Create_Table_Product_Details);
        db.execSQL(Create_Table_Category_Details);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_UserDetails);
        db.execSQL("DROP TABLE IF EXISTS " + Table_ProductDetails);

        onCreate(db);
    }

    public boolean insertCategory (String cId,String cName) {

        String selectQuery = "SELECT * FROM "+ Table_CategoryDetails +" WHERE "+ CategoryId +" = "+ cId +"";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count =  cursor.getCount();
        cursor.close();

        if(count == 0){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CategoryId, cId);
            contentValues.put(CategoryName, cName);
            db.insert(Table_CategoryDetails, null, contentValues);
            db.close();
            return true;
        }
        else{
            return false;
        }
    }

    //---------------------------- All Standard details-------------------------------//
    public boolean InsertProduct(String product_Id, String productName, String category, String price, String color, String size, String details,
                                 String description, String images, String ratings) {

        String selectQuery = "SELECT * FROM " + Table_ProductDetails + " WHERE " + Product_Id + " = " + product_Id + "";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Product_Id, product_Id);
            contentValues.put(Product_Name, productName);
            contentValues.put(Product_Category, category);
            contentValues.put(Product_Price, price);
            contentValues.put(Product_Color, color);
            contentValues.put(Product_Size, size);
            contentValues.put(Product_Details, details);
            contentValues.put(Product_Description, description);
            contentValues.put(Product_Images, images);
            contentValues.put(Product_Ratings, ratings);
            db.insert(Table_ProductDetails, null, contentValues);
            db.close();
            return true;
        } else {
            return false;
        }
    }

//---------------------------- Product Details -------------------------------//

    public JSONArray Get_All_Product() throws JSONException {

        String selectQuery = "SELECT  * FROM " + Table_ProductDetails;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(Product_Id, res.getString(res.getColumnIndex(Product_Id)));
            details.put(Product_Name, res.getString(res.getColumnIndex(Product_Name)));
            details.put(Product_Category, res.getString(res.getColumnIndex(Product_Category)));
            details.put(Product_Price, res.getString(res.getColumnIndex(Product_Price)));
            details.put(Product_Color, res.getString(res.getColumnIndex(Product_Color)));
            details.put(Product_Size, res.getString(res.getColumnIndex(Product_Size)));
            details.put(Product_Details, res.getString(res.getColumnIndex(Product_Details)));
            details.put(Product_Description, res.getString(res.getColumnIndex(Product_Description)));
            details.put(Product_Images, res.getString(res.getColumnIndex(Product_Images)));
            details.put(Product_Ratings, res.getString(res.getColumnIndex(Product_Ratings)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    //---------------------------- Product Details Category wise -------------------------------//
    public JSONArray Get_All_Product_CategoryWise(String category) throws JSONException {

        String selectQuery = "SELECT * FROM " + Table_ProductDetails + " WHERE " + Product_Category + " = " + category + "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery(selectQuery, null);
        res.moveToFirst();
        JSONObject Root = new JSONObject();
        JSONArray detailsArray = new JSONArray();
        int i = 0;
        while (res.isAfterLast() == false) {
            JSONObject details = new JSONObject();
            details.put(Product_Id, res.getString(res.getColumnIndex(Product_Id)));
            details.put(Product_Name, res.getString(res.getColumnIndex(Product_Name)));
            details.put(Product_Category, res.getString(res.getColumnIndex(Product_Category)));
            details.put(Product_Price, res.getString(res.getColumnIndex(Product_Price)));
            details.put(Product_Color, res.getString(res.getColumnIndex(Product_Color)));
            details.put(Product_Size, res.getString(res.getColumnIndex(Product_Size)));
            details.put(Product_Details, res.getString(res.getColumnIndex(Product_Details)));
            details.put(Product_Description, res.getString(res.getColumnIndex(Product_Description)));
            details.put(Product_Images, res.getString(res.getColumnIndex(Product_Images)));
            details.put(Product_Ratings, res.getString(res.getColumnIndex(Product_Ratings)));

            res.moveToNext();
            detailsArray.put(i, details);
            i++;
        }
        res.close();
        return detailsArray;
    }

    public boolean Update_Product_Details(String product_Id, String productName, String category, String price, String color, String size, String details,
                                          String description, String images, String ratings) {

        String selectQuery = "SELECT * FROM " + Table_ProductDetails + " WHERE " + Product_Id + " = " + product_Id + "";
        SQLiteDatabase db2 = this.getReadableDatabase();
        Cursor cursor = db2.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        if (count == 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Product_Name, productName);
            contentValues.put(Product_Category, category);
            contentValues.put(Product_Price, price);
            contentValues.put(Product_Color, color);
            contentValues.put(Product_Size, size);
            contentValues.put(Product_Details, details);
            contentValues.put(Product_Description, description);
            contentValues.put(Product_Images, images);

            //  for multiple where clause
            String[] whereClauseArgument = new String[1];
            whereClauseArgument[0] = productName;
            whereClauseArgument[1] = category;
            whereClauseArgument[2] = price;
            whereClauseArgument[3] = color;
            whereClauseArgument[4] = size;
            whereClauseArgument[5] = details;
            whereClauseArgument[6] = description;
            whereClauseArgument[7] = images;
            db.update(Table_ProductDetails, contentValues, Product_Name + " =? AND " + Product_Category + " = ?" + Product_Price + " = ?" + Product_Color + " = ?" + Product_Size + " = ?" + Product_Details + " = ?" + Product_Description + " = ?" + Product_Images + " = ?", whereClauseArgument);

//            db.update(TABLE_ATTENDANCE, contentValues, ATTENDANCE_DATE + " = ?",new String[]{dateForOutTime});
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public boolean DeleteProduct(String product_Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_ProductDetails, Product_Id + "=" + product_Id, null) > 0;
    }

    //join query
//    private final String MY_QUERY = "SELECT * FROM table_a a INNER JOIN table_b b ON a.id=b.other_id WHERE b.property_id=?";
//    db.rawQuery(MY_QUERY, new String[]{String.valueOf(propertyId)});

}
