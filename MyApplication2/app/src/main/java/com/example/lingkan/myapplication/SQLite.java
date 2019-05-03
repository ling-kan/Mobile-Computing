package com.example.lingkan.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ling on 03/12/2017.
 */

public class SQLite extends SQLiteOpenHelper {

    private static final String TAG = "SQLiteHelp";
    //Table name
    private static final String TableName = "TableHistory";
    //Column Names for TableHistory
    private static final String col2 = "Location";

    public SQLite(Context context) { super(context, TableName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Creating the table
      String createTable  = "CREATE TABLE " + TableName +
              " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
              col2 + " TEXT)";
        database.execSQL(createTable);
    }

    @Override
    //Drop and reset database if needed
    public void onUpgrade(SQLiteDatabase database, int before, int after) {
        //Drop if an older table exist therefore changing with the new one
        database.execSQL("DROP TABLE IF EXISTS " + TableName);
        //Recreate table again
        onCreate(database);
    }
    //Insert Record
    public boolean insertRecord(String info) throws SQLiteException {
        //create and open database which is used for reading and writing
        SQLiteDatabase database = this.getWritableDatabase();
        //Content Values insert in record
        ContentValues content = new ContentValues();
        content.put(col2, info);
        //Inserting the row
        long result = database.insert(TableName, null, content);
        //Close database
     return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + TableName, null);
        return data;
    }
}
