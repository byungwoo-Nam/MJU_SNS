package com.example.mju_sns.util.config.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.example.mju_sns.util.config.database.FeedReaderDbHelper;

import static android.content.Context.MODE_PRIVATE;

public class AppBoot {

    private Activity activity;
    private SQLiteDatabase mDB;
    private DeviceUuidFactory duf;

    private String uuid;
    private String fcmToken;
    private boolean isMember;

    public AppBoot(Activity activity){
        this.activity = activity;
    }

    public void init(){
        createDB();
        getUUID();
        this.isMember = checkMember();
    }

    public void createDB(){
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this.activity);
        mDB = mDbHelper.getReadableDatabase();
    }
    public void getUUID(){
        this.duf = new DeviceUuidFactory(this.activity);
        this.uuid = duf.getDeviceUuid().toString();
    }
    public boolean checkMember(){
        SharedPreferences prefs = this.activity.getSharedPreferences("mju_sns", MODE_PRIVATE);
        String text = prefs.getString("isMember", "");
        return text.equals("true");
    }

    public String getUuid() {
        return this.uuid;
    }
    public String getFcmToken() {
        return this.fcmToken;
    }
    public boolean getIsMember() {
        return isMember;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
