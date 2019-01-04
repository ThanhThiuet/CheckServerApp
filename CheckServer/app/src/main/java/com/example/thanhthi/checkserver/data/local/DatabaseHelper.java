package com.example.thanhthi.checkserver.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ItemCheckServer.db";

    private static final String SQL_CREATE_ITEMS = "CREATE TABLE "
            + ItemContract.ItemEntry.TABLE_NAME + " ("
            + ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, "
            + ItemContract.ItemEntry.COLUMN_URL + " TEXT, "
            + ItemContract.ItemEntry.COLUMN_KEYWORD + " TEXT, "
            + ItemContract.ItemEntry.COLUMN_MESSAGE + " TEXT, "
            + ItemContract.ItemEntry.COLUMN_FREQUENCY + " REAL, "
            + ItemContract.ItemEntry.COLUMN_IS_CHECKING + " INTEGER DEFAULT 0)";  // for boolean

    private static final String SQL_DELETE_ITEMS = "DROP TABLE IF EXISTS"
            + ItemContract.ItemEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ITEMS);
        db.execSQL(SQL_CREATE_ITEMS);
    }
}
