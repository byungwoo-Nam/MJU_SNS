package com.example.mju_sns.util.config.database;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    public FeedReaderContract(){};

    public static final String DATABASE_NAME = "mju_sns.db";
    public static final String SQL_CREATE_SEND_TABLE =
            "CREATE TABLE " + FeedEntry.SEND_TABLE_NAME + " ( " +
                    FeedEntry.COLUMN_SEQ + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_TITLE + " VARCHAR , " +
                    FeedEntry.COLUMN_CONTENTS + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_LATITUDE + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_LONGITUDE + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_RANGE + " INTEGER, " +
                    FeedEntry.COLUMN_DATE + " TIMESTAMP " +
                    " ) ";
    public static final String SQL_CREATE_RECIEVE_TABLE =
            "CREATE TABLE " + FeedEntry.RECIEVE_TABLE_NAME + " ( " +
                    FeedEntry.COLUMN_SEQ + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_TITLE + " VARCHAR , " +
                    FeedEntry.COLUMN_CONTENTS + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_LATITUDE + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_LONGITUDE + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_RANGE + " INTEGER, " +
                    FeedEntry.COLUMN_DATE + " TIMESTAMP, " +
                    FeedEntry.COLUMN_ISAPPLY + " BOOLEAN DEFAULT FALSE " +
                    " ) ";

    public static final String SQL_DELETE_SEND_TABLE =
            "DROP TABLE IF EXISTS " + FeedEntry.SEND_TABLE_NAME;

    public static final String SQL_DELETE_RECIEVE_TABLE =
            "DROP TABLE IF EXISTS " + FeedEntry.RECIEVE_TABLE_NAME;

    public static abstract class FeedEntry implements BaseColumns {
        public static final String SEND_TABLE_NAME = "SEND_BOARD";
        public static final String RECIEVE_TABLE_NAME = "RECIEVE_BOARD";
        public static final String COLUMN_SEQ = "seq";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENTS = "contents";
        public static final String COLUMN_LOCATION_LATITUDE = "location_latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = "location_longitude";
        public static final String COLUMN_LOCATION_RANGE = "location_range";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_ISAPPLY = "isapply";
    }
}
