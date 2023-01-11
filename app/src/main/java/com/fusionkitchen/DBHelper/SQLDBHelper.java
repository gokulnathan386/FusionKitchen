package com.fusionkitchen.DBHelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import com.fusionkitchen.model.cart.Cartitem;

/**
 * Created by obaro on 02/04/2015.
 */
public class SQLDBHelper extends SQLiteOpenHelper {

    ArrayList<Cartitem> storeContacts;
    ArrayList<String> item_data;

    public static final String DATABASE_NAME = "SQLiteExample.db";
    private static final int DATABASE_VERSION = 7;

    public static final String ITEM_TABLE_NAME = "item";

    public static final String ITEM_COLUMN_ID = "_id";//0
    public static final String ITEM_NAME = "itemname";//1
    public static final String ITEM_ID = "itemid";//2
    public static final String ITEM_ADDON_NAME = "itemaddonname";//3
    public static final String ITEM_ADDON_NAME_ID = "itemaddonnameid";//4
    public static final String ITEM_ADDON_EXTRA_ID = "itemaddonextraid";//5
    public static final String ITEM_AMOUNT = "itemamount";//6
    public static final String ITEM_QTY = "itemqty";//7
    public static final String ITEM_TOTAL_AMOUNT = "itemtotalamount";//8
    public static final String ITEM_Final_AMOUNT = "itemfinalamount";//9   //Amount--->
    public static final String ITEM_CATEGORY_NAME = "itemcategoryname";//10
    public static final String ITEM_SUBCATEGORY_NAME = "itemsubcategoryname";//11

    public SQLDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ITEM_TABLE_NAME +
                        "(" + ITEM_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        ITEM_NAME + " TEXT, " +
                        ITEM_ID + " TEXT, " +
                        ITEM_ADDON_NAME + " TEXT, " +
                        ITEM_ADDON_NAME_ID + " TEXT, " +
                        ITEM_ADDON_EXTRA_ID + " TEXT, " +
                        ITEM_AMOUNT + " TEXT, " +
                        ITEM_QTY + " TEXT, " +
                        ITEM_TOTAL_AMOUNT + " TEXT, " +
                        ITEM_Final_AMOUNT + " TEXT, " +
                        ITEM_CATEGORY_NAME + " TEXT, " +
                        ITEM_SUBCATEGORY_NAME + " TEXT)"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertItem(String itemname, String itemid, String itemaddonname, String itemaddonnameid, String itemaddonextraid, String itemamount, String itemqty, String itemtotalamountsub, String itemtotalamount, String itemcategoryname, String itemsubcategoryname) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ITEM_NAME, itemname);
        contentValues.put(ITEM_ID, itemid);
        contentValues.put(ITEM_ADDON_NAME, itemaddonname);
        contentValues.put(ITEM_ADDON_NAME_ID, itemaddonnameid);
        contentValues.put(ITEM_ADDON_EXTRA_ID, itemaddonextraid);
        contentValues.put(ITEM_AMOUNT, itemamount);
        contentValues.put(ITEM_QTY, itemqty);
        contentValues.put(ITEM_TOTAL_AMOUNT, itemtotalamountsub);
        contentValues.put(ITEM_Final_AMOUNT, itemtotalamount);
        contentValues.put(ITEM_CATEGORY_NAME, itemcategoryname);
        contentValues.put(ITEM_SUBCATEGORY_NAME, itemsubcategoryname);

        db.insert(ITEM_TABLE_NAME, null, contentValues);
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITEM_TABLE_NAME);
        return numRows;
    }

    public boolean updateItem(Integer id, String itemqty, String itemfinalamount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_QTY, itemqty);
        contentValues.put(ITEM_Final_AMOUNT, itemfinalamount);

        db.update(ITEM_TABLE_NAME, contentValues, ITEM_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deleteItem(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(ITEM_TABLE_NAME,
                ITEM_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }


    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + ITEM_TABLE_NAME);
        db.close();

    }


    public Cursor getItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ITEM_TABLE_NAME + " WHERE " +
                ITEM_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllItem() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + ITEM_TABLE_NAME, null);
        return res;
    }


    public ArrayList<Cartitem> listContacts() {
        String sql = "select * from " + ITEM_TABLE_NAME;



        SQLiteDatabase db = this.getReadableDatabase();
        storeContacts = new ArrayList<>();


        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String desc = cursor.getString(3);
                String qty = cursor.getString(7);
                String amount = cursor.getString(8);
                String itemamount = cursor.getString(6);
                String id = cursor.getString(0);
                String finalamt = cursor.getString(9);
                String itemid = cursor.getString(2);
                String addonnameid = cursor.getString(4);
                String addonextraid = cursor.getString(5);
                String categoryname = cursor.getString(10);
                String subcategoryname = cursor.getString(11);

                storeContacts.add(new Cartitem(name, desc, qty, amount, itemamount, id, finalamt, itemid, addonnameid, addonextraid, categoryname, subcategoryname));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeContacts;
    }

    public int GetUserByUserId(int userid) {

        String countQuery = "SELECT  * FROM " + ITEM_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ITEM_TABLE_NAME, new String[]{ITEM_ID}, ITEM_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;


    }


    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> Getqtyprice(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + ITEM_TABLE_NAME;
        Cursor cursor = db.query(ITEM_TABLE_NAME, new String[]{ITEM_QTY, ITEM_TOTAL_AMOUNT}, ITEM_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null, null);
        if (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("qty", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
            user.put("itemaddontotalamt", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
            userList.add(user);
        }
        return userList;
    }


    public boolean Updateqtyprice(int id, int itemqty, float itemfinalamount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_QTY, itemqty);
        contentValues.put(ITEM_Final_AMOUNT, itemfinalamount);
        db.update(ITEM_TABLE_NAME, contentValues, ITEM_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public boolean repeat_last_pop_up(int id, int itemqty, float itemfinalamount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_QTY, itemqty);
        contentValues.put(ITEM_Final_AMOUNT, itemfinalamount);
        db.update(ITEM_TABLE_NAME, contentValues, ITEM_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getallvalue(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + ITEM_TABLE_NAME;
        Cursor cursor = db.query(ITEM_TABLE_NAME, new String[]{
                ITEM_COLUMN_ID, ITEM_NAME, ITEM_ID, ITEM_ADDON_NAME,
                ITEM_ADDON_NAME_ID, ITEM_ADDON_EXTRA_ID,
                ITEM_AMOUNT, ITEM_QTY, ITEM_TOTAL_AMOUNT,
                ITEM_Final_AMOUNT, ITEM_CATEGORY_NAME, ITEM_SUBCATEGORY_NAME}, ITEM_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null, null);
        if (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("ITEM_COLUMN_ID", cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_ID)));
            user.put("ITEM_NAME", cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
            user.put("ITEM_ID", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
            user.put("ITEM_ADDON_NAME", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_NAME)));
            user.put("ITEM_ADDON_EXTRA_ID", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_EXTRA_ID)));
            user.put("ITEM_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_AMOUNT)));
            user.put("ITEM_QTY", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
            user.put("ITEM_TOTAL_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
            user.put("ITEM_Final_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_Final_AMOUNT)));
            user.put("ITEM_CATEGORY_NAME", cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY_NAME)));
            user.put("ITEM_SUBCATEGORY_NAME", cursor.getString(cursor.getColumnIndex(ITEM_SUBCATEGORY_NAME)));
            userList.add(user);
        }
        return userList;

    }

    @SuppressLint("Range")
    public ArrayList<String> getqtycount() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("SELECT SUM(itemqty) AS totalqty FROM item", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("totalqty")));
            res.moveToNext();
        }
        return array_list;
    }


    @SuppressLint("Range")
    public ArrayList<String> gettotalamt() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("SELECT SUM(itemfinalamount) AS totalamt FROM item", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("totalamt")));
            res.moveToNext();
        }
        return array_list;
    }


  @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> Getqtypriceaddon(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        Cursor cursor = db.query(ITEM_TABLE_NAME, new String[]{ITEM_QTY, ITEM_TOTAL_AMOUNT}, ITEM_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null, null);
        if (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("qty", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
            user.put("itemaddontotalamt", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
            userList.add(user);
        }
        return userList;
    }



  @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getlastposition(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();

      Cursor cursor =db.rawQuery("select * from item WHERE itemid = "+ userid+" " +"ORDER BY "+ITEM_COLUMN_ID+" " + "DESC LIMIT 1", null);
        if (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("qty", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
            user.put("itemaddontotalamt", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
            user.put("id", cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_ID)));
            userList.add(user);
        }

      return userList;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> Remoeveqtyprice(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + ITEM_TABLE_NAME;
        Cursor cursor = db.query(ITEM_TABLE_NAME, new String[]{ITEM_QTY, ITEM_TOTAL_AMOUNT}, ITEM_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null, null);
        if (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("qty", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
            user.put("itemaddontotalamt", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
            userList.add(user);
        }
        return userList;
    }


    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> GetUserdetails(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + ITEM_TABLE_NAME;
        Cursor cursor = db.query(ITEM_TABLE_NAME, new String[]{
                ITEM_COLUMN_ID, ITEM_NAME, ITEM_ID, ITEM_ADDON_NAME,
                ITEM_ADDON_NAME_ID, ITEM_ADDON_EXTRA_ID,
                ITEM_AMOUNT, ITEM_QTY, ITEM_TOTAL_AMOUNT,
                ITEM_Final_AMOUNT, ITEM_CATEGORY_NAME, ITEM_SUBCATEGORY_NAME}, ITEM_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null, null);
        if (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("ITEM_COLUMN_ID", cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_ID)));
            user.put("ITEM_NAME", cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
            user.put("ITEM_ID", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
            user.put("ITEM_ADDON_NAME", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_NAME)));
            user.put("ITEM_ADDON_EXTRA_ID", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_EXTRA_ID)));
            user.put("ITEM_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_AMOUNT)));
            user.put("ITEM_QTY", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
            user.put("ITEM_TOTAL_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
            user.put("ITEM_Final_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_Final_AMOUNT)));
            user.put("ITEM_CATEGORY_NAME", cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY_NAME)));
            user.put("ITEM_SUBCATEGORY_NAME", cursor.getString(cursor.getColumnIndex(ITEM_SUBCATEGORY_NAME)));
            userList.add(user);
        }
        return userList;


    }



    public ArrayList<String>  getitemlist() {

        String sql = "select * from " + ITEM_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        item_data = new ArrayList<>();


        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {

                String itemid = cursor.getString(2);

                item_data.add(itemid);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return item_data;
    }

    public void deleteItemRow(String get_ID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM item WHERE itemid='"+get_ID+"'");

    }

/*------------------------------*/

    @SuppressLint("Range")
    public ArrayList<String> getuseridcount(String itemid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("SELECT SUM(itemqty) AS totalqty FROM item WHERE "+ ITEM_ID + " = ? ",  new String[] {itemid});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex("totalqty")));
            res.moveToNext();
        }
        return array_list;
    }

 @SuppressLint("Range")
    public ArrayList<String> GetAddonid(String itemid) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("SELECT * FROM item WHERE "+ ITEM_ID + " = ? ",  new String[] {itemid});
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(ITEM_ADDON_NAME_ID)));
            res.moveToNext();
        }
        return array_list;
    }


    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getaddonvalue(String itemid) {

        ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM item WHERE "+ ITEM_ADDON_NAME_ID + " = ? ",  new String[] {itemid});
        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> map = new HashMap<String, String>();

                map.put("ITEM_COLUMN_ID", cursor.getString(cursor.getColumnIndex(ITEM_COLUMN_ID)));
                map.put("ITEM_NAME", cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
                map.put("ITEM_ID", cursor.getString(cursor.getColumnIndex(ITEM_ID)));
                map.put("ITEM_ADDON_NAME", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_NAME)));
                map.put("ITEM_ADDON_EXTRA_ID", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_EXTRA_ID)));
                map.put("ITEM_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_AMOUNT)));
                map.put("ITEM_QTY", cursor.getString(cursor.getColumnIndex(ITEM_QTY)));
                map.put("ITEM_TOTAL_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_TOTAL_AMOUNT)));
                map.put("ITEM_Final_AMOUNT", cursor.getString(cursor.getColumnIndex(ITEM_Final_AMOUNT)));
                map.put("ITEM_CATEGORY_NAME", cursor.getString(cursor.getColumnIndex(ITEM_CATEGORY_NAME)));
                map.put("ITEM_SUBCATEGORY_NAME", cursor.getString(cursor.getColumnIndex(ITEM_SUBCATEGORY_NAME)));
                map.put("ITEM_ADDON_NAME_ID", cursor.getString(cursor.getColumnIndex(ITEM_ADDON_NAME_ID)));

                productList.add(map);
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();

        return productList;
    }

    public boolean update_addon_item(String addonid, int itemqty, float itemfinalamount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_QTY, itemqty);
        contentValues.put(ITEM_Final_AMOUNT, itemfinalamount);
        db.update(ITEM_TABLE_NAME, contentValues, ITEM_ADDON_NAME_ID + " = ? ", new String[]{addonid});
        return true;
    }


    /*public boolean (int id, int itemqty, float itemfinalamount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_QTY, itemqty);
        contentValues.put(ITEM_Final_AMOUNT, itemfinalamount);
        db.update(ITEM_TABLE_NAME, contentValues, ITEM_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }*/


}

