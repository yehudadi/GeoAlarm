package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private final Context context;
    private SQLiteDatabase database;

    // Database Name
    private static final String DATABASENAME = "wardrobe.db";

    // Database Version
    private static final int DATABASEVERSION = 1;

    // Table Names
    private static final String TABLE_ITEM = "item";

    // ITEM table - column names
    private static final String COLUMN_ID = "item_id";
    private static final String COLUMN_NAME = "item_name";
    private static final String COLUMN_DISTANCE = "item_distance";

    private static final String COLUMN_LATITUDE = "item_latitude";

    private static final String COLUMN_LONGITUDE = "item_longitude";


    // ITEM table - all columns
    private static final String[] itemAllColumns = {COLUMN_ID, COLUMN_NAME, COLUMN_DISTANCE,COLUMN_LATITUDE,COLUMN_LONGITUDE};
    private static final String CREATE_TABLE_ITEM = "CREATE TABLE IF NOT EXISTS " +
            TABLE_ITEM + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT," +
            COLUMN_DISTANCE + " TEXT," +
            COLUMN_LATITUDE + " TEXT,"+
            COLUMN_LONGITUDE +" TEXT);";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASENAME, null, DATABASEVERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        // If multiple tables, drop each here
        onCreate(sqLiteDatabase);
    }

    public Item insertItem(Item item)
    {
        database = getWritableDatabase(); // get access to write the database
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_DISTANCE, item.getDistance());
        values.put(COLUMN_LATITUDE, item.getLatitude());
        values.put(COLUMN_LONGITUDE, item.getLongitude());

        long id = database.insert(TABLE_ITEM, null, values);
        item.setId(id);
        database.close();
        return item;
    }

    public void deleteItem(Long id)
    {
        database = getWritableDatabase();
        database.delete(TABLE_ITEM, COLUMN_ID + " = '" + id + "'", null);
        database.close();
    }

    // Update a specific item
    // Returns the number of rows affected
    public int updateItem(Item item)
    {
        database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID,item.getId());
        values.put(COLUMN_LATITUDE, item.getLatitude());
        values.put(COLUMN_LONGITUDE, item.getLongitude());
        values.put(COLUMN_NAME, item.getName());
        values.put(COLUMN_DISTANCE, item.getDistance());
        int rtn = database.update(TABLE_ITEM, values, COLUMN_ID + "=" + item.getId(), null);

        database.close();
        return rtn;
    }

    // Return all item rows in table
    public ArrayList<Item> selectAllItems()
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<Item> items = new ArrayList<>();
        Cursor cursor = database.query(TABLE_ITEM, itemAllColumns, null, null, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE));
                @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String distance = cursor.getString(cursor.getColumnIndex(COLUMN_DISTANCE));

                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                Item item= new Item(name,distance,id,latitude,longitude);
                items.add(item);
            }
        }
        database.close();
        return items;
    }

    public Item selectItemByName(String name)
    {
        String[] vals = { name };
        // if using the rawQuery
        // String query = "SELECT * FROM " + TABLE_RECORD + " WHERE " + COLUMN_NAME + " = ?";
        String column = COLUMN_NAME;
        ArrayList<Item> items = selectItem(column,vals);
        if (items.size() > 0) {
            return items.get(0);
        } else {
            return null;
        }
    }


    // INPUT: notice two options rawQuery should look like
    // rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});
    // OUTPUT: arraylist - number of elements accordingly
    public ArrayList<Item> selectItem(String column, String[] values)
    {
        database = getReadableDatabase(); // get access to read the database
        ArrayList<Item> items = new ArrayList<>();
        // Two options,
        // Since query cannot be created in compile time there is no difference
        //Cursor cursor = database.rawQuery(query, values);
        Cursor cursor= database.query(TABLE_ITEM, itemAllColumns, column +" = ? ", values, null, null, null); // cursor points at a certain row
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String latitude = cursor.getString(cursor.getColumnIndex(COLUMN_LATITUDE));
                @SuppressLint("Range") String longitude = cursor.getString(cursor.getColumnIndex(COLUMN_LONGITUDE));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String distance = cursor.getString(cursor.getColumnIndex(COLUMN_DISTANCE));
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                Item item= new Item(name,distance,id,latitude,longitude);
                items.add(item);
            }// end while
        } // end if
        database.close();
        return items;
    }

    public Context getContext() {return context;}

}
