package com.example.mju_sns.util.config.database;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    public FeedReaderContract(){};

    public static final String DATABASE_NAME = "mju_sns.db";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " ( " +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_TITLE + " text , " +
                    FeedEntry.COLUMN_CONTENT + " text " + " ) ";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "BOARD";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
    }
}
