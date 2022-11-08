package com.scorpion.healingland.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HealingLandDB.db";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // User table creation
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Fields.UserData.TABLE_NAME + "(" +
            Fields.UserData._ID + " INTEGER," +
            Fields.UserData.COLUMN_1 + "TEXT," +
            Fields.UserData.COLUMN_2 + "TEXT," +
            Fields.UserData.COLUMN_3 + "TEXT PRIMARY KEY," +
            Fields.UserData.COLUMN_4 + "INTEGER," +
            Fields.UserData.COLUMN_5 + "TEXT)";

    // Drop user table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Fields.UserData.TABLE_NAME;

    public long RegisterUser (String firstname, String lastname, String email, String imageUrl, String phone, String password){
        // Get the database instance in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fields.UserData.COLUMN_1, firstname);
        contentValues.put(Fields.UserData.COLUMN_2, lastname);
        contentValues.put(Fields.UserData.COLUMN_3, email);
        contentValues.put(Fields.UserData.COLUMN_4, phone);
        contentValues.put(Fields.UserData.COLUMN_5, password);

        // insert the new row and returning
        long newRow = db.insert(Fields.UserData.TABLE_NAME, null, contentValues);

        return newRow;
    }

    public boolean userLogin (String email, String password) {
        // Get the database instance in read mode
        SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
//                BaseColumns._ID,
                Fields.UserData.COLUMN_3,
                Fields.UserData.COLUMN_5
        };

        // Filter results where email and password
        String selection = Fields.UserData.COLUMN_3 + " = ? AND " + Fields.UserData.COLUMN_5 + " = ?";
        String[] selectionArgs = { email, password };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                Fields.UserData.COLUMN_3 + " ASC";

        Cursor cursor = db.query(
                Fields.UserData.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        String ValidUser = null;
        while(cursor.moveToNext()) {
            ValidUser = cursor.getString(cursor.getColumnIndexOrThrow(Fields.UserData.COLUMN_3));
        }
        cursor.close();

        // assertion
        assert ValidUser != null;

       // return a boolean
        if(ValidUser.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}