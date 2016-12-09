package com.example.mju_sns.util.config.database;

import android.provider.BaseColumns;

public final class FeedReaderContract {

    public FeedReaderContract(){};

    public static final String DATABASE_NAME = "mju_sns.db";
    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " ( " +
                    FeedEntry.COLUMN_SEQ + " INTEGER PRIMARY KEY, " +
                    FeedEntry.COLUMN_TITLE + " VARCHAR , " +
                    FeedEntry.COLUMN_CONTENTS + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_LATITUDE + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_LONGITUDE + " VARCHAR, " +
                    FeedEntry.COLUMN_LOCATION_RANGE + " INTEGER, " +
                    FeedEntry.COLUMN_DATE + " TIMESTAMP " +
                    " ) ";


    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "BOARD";
        public static final String COLUMN_SEQ = "seq";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENTS = "contents";
        public static final String COLUMN_LOCATION_LATITUDE = "location_latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = "location_longitude";
        public static final String COLUMN_LOCATION_RANGE = "location_range";
        public static final String COLUMN_DATE = "date";
    }
}
