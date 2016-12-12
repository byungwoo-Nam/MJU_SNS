package com.example.mju_sns.util.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.mju_sns.R;
import com.example.mju_sns.util.config.database.FeedReaderContract;
import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Writings;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kook
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            JSONObject notification = new JSONObject(data.get("notification").toString());
            String title = notification.getString("title");
            String message = notification.getString("body");
            String type = data.getString("type");
            if(type.equals("newWritings")){
                insertWritings(data.getString("writings"));
            }

            Log.d(TAG, "From: " + from);
            Log.d(TAG, "Title: " + title);
            Log.d(TAG, "Message: " + message);

            // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
            sendNotification(title, message);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, com.example.mju_sns.Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void insertWritings(String writingsStr){
        Gson gson = new Gson();
        Writings writings = gson.fromJson(writingsStr, Writings.class);

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(this.getApplicationContext());

        // 데이터베이스를 쓰기 모드로 가져온다.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // 데이터 준비
        ContentValues values = new ContentValues();
        values.put( FeedReaderContract.FeedEntry.COLUMN_SEQ, writings.getSeq());
        values.put( FeedReaderContract.FeedEntry.COLUMN_TITLE, writings.getTitle());
        values.put( FeedReaderContract.FeedEntry.COLUMN_CONTENTS, writings.getContents());
        values.put( FeedReaderContract.FeedEntry.COLUMN_LOCATION_LATITUDE, writings.getLocation_latitude());
        values.put( FeedReaderContract.FeedEntry.COLUMN_LOCATION_LONGITUDE, writings.getLocation_longitude());
        values.put( FeedReaderContract.FeedEntry.COLUMN_LOCATION_RANGE, writings.getLocation_range());
        values.put( FeedReaderContract.FeedEntry.COLUMN_DATE, writings.getDate());

        // 데이터 추가
        long newRowId = db.insert(FeedReaderContract.FeedEntry.RECIEVE_TABLE_NAME, null, values);

        System.out.println("GCM!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println(newRowId);
    }
}
