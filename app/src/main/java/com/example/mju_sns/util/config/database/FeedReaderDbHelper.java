package com.example.mju_sns.util.config.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;

    public FeedReaderDbHelper(Context context)
    {
        super(context, FeedReaderContract.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(FeedReaderContract.SQL_CREATE_SEND_TABLE);
        db.execSQL(FeedReaderContract.SQL_CREATE_RECIEVE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL(FeedReaderContract.SQL_DELETE_SEND_TABLE);
        //db.execSQL(FeedReaderContract.SQL_DELETE_RECIEVE_TABLE);
        //onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL(FeedReaderContract.SQL_SEND_DELETE_TABLE);
    }
}