package com.example.mju_sns.util.config.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;

import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Users;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;

public class AppBoot {

    private Activity activity;
    private SQLiteDatabase mDB;
    private DeviceUuidFactory duf;
    private URLConnector urlConnector;
    private UUID uuid;
    private boolean isMember;

    public boolean isMember() {
        return isMember;
    }

    public AppBoot(Activity activity){
        this.activity = activity;
    }

    public void init(){
        createDB();
        getUUID();
        //getGcm 들어가야 함
        this.isMember = checkMember();
    }
    public void createDB(){
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this.activity);
        mDB = mDbHelper.getWritableDatabase();
        mDbHelper.onCreate(mDB);
    }
    public void getUUID(){
        this.duf = new DeviceUuidFactory(this.activity);
        this.uuid = duf.getDeviceUuid();
    }
    public boolean checkMember(){
        SharedPreferences prefs = this.activity.getSharedPreferences("mju_sns", MODE_PRIVATE);
        String text = prefs.getString("isMember", "");
        return text.equals("true");

//        this.urlConnector = new URLConnector();
//        JSONObject param = new JSONObject();
//        try {
//            param.put("mode", "memberCheck");
//            param.put("id", uuid);
//        }catch (JSONException e){
//            e.printStackTrace();
//        }
//        String result = this.urlConnector.getData(param);
//
//        if(result!=""){
//            Gson gson = new Gson();
//            gson.fromJson(result, Users.class);
//            return true;
//        }else{
//            return false;
//        }
    }
}
