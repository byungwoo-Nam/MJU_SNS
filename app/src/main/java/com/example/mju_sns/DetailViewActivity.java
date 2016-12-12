package com.example.mju_sns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mju_sns.util.config.app.CodeConfig;
import com.example.mju_sns.util.config.app.ListViewAdapter;
import com.example.mju_sns.util.config.app.URLConnector;
import com.example.mju_sns.util.config.database.CursorHelper;
import com.example.mju_sns.util.config.database.FeedReaderContract;
import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Reply;
import com.example.mju_sns.util.dto.Writings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.mju_sns.R.id.lv;


public class DetailViewActivity extends Activity implements MapView.MapViewEventListener{

    CodeConfig codeConfig = new CodeConfig();

    MapView mMapView;
    RelativeLayout mapArea;

    EditText edit_reply;

    Intent intent;

    Writings writings;

    private List<Reply> list = new ArrayList();
    private ListView lv;
    private ListViewAdapter listViewAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        intent = getIntent();
        writings = (Writings) intent.getSerializableExtra("data");
        String type = intent.getStringExtra("type");

        mapArea = (RelativeLayout)findViewById(R.id.detail_area);
        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(this.codeConfig.DAUM_MAP_API_KEY);
        mMapView.setMapViewEventListener(this);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("선택 장소");
        marker.setTag(0);
        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(writings.getLocation_latitude()), Double.parseDouble(writings.getLocation_longitude())));
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(null); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setShowCalloutBalloonOnTouch(false);

        mMapView.addPOIItem(marker);

        TextView title_textview = (TextView) findViewById(R.id.title_textview);
        TextView content_textview = (TextView) findViewById(R.id.title_textview);
        TextView location_textview = (TextView) findViewById(R.id.location_textview);

        lv = (ListView)findViewById(R.id.lv);
        listViewAdatper = new ListViewAdapter(list, this, "reply");
        lv.setAdapter(listViewAdatper);

        if(type.equals("SEND") || writings.getIsapply()){
            setDefaultLayout(false);
            listRefresh();
        }else{
            setDefaultLayout(true);
        }

        title_textview.setText(writings.getTitle());

        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();

        try {
            param.put("mode", "daumAPI");
            param.put("location_latitude", writings.getLocation_latitude());
            param.put("location_longitude", writings.getLocation_longitude());
            String result = urlConnector.starter(param, true, false);

            JSONObject fullAddress = new JSONObject(result);
            JSONObject newAddress = (JSONObject)fullAddress.get("new");
            JSONObject oldAddress = (JSONObject)fullAddress.get("old");

            location_textview.setText(!newAddress.get("name").toString().equals("") ? newAddress.get("name").toString() : oldAddress.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void listRefresh(){
        list.clear();
        List<Reply> dataList = getListData();

        // 아이템 추가
        for(int i=0; i<dataList.size(); i++) {
            list.add(dataList.get(i));
        }

        listViewAdatper.notifyDataSetChanged();
    }

    public List<Reply> getListData(){
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        Gson gson = new Gson();
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("mju_sns", MODE_PRIVATE);
        String id = prefs.getString("id", "");
        try {
            param.put("mode", "getReplyList");
            param.put("id", id);
            param.put("writings_seq", writings.getSeq());
            //param.put("pageNum", pageNum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = urlConnector.starter(param, true, false);

        return gson.fromJson(result, new TypeToken<List<Reply>>(){}.getType());
    }

    public void requestApply(View v){
        new AlertDialog.Builder(this)
                .setTitle("요청 수락")
                .setMessage("요청을 수락 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        URLConnector urlConnector = new URLConnector();
                        JSONObject param = new JSONObject();
                        Gson gson = new Gson();
                        SharedPreferences prefs = getApplicationContext().getSharedPreferences("mju_sns", MODE_PRIVATE);
                        String id = prefs.getString("id", "");
                        try {
                            param.put("mode", "requestApply");
                            param.put("id", id);
                            param.put("seq", writings.getSeq());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String result = urlConnector.starter(param, true, false);

                        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());

                        // 데이터베이스를 쓰기 모드로 가져온다.
                        SQLiteDatabase db = mDbHelper.getReadableDatabase();

                        // 데이터 준비
                        ContentValues values = new ContentValues();
                        values.put( FeedReaderContract.FeedEntry.COLUMN_ISAPPLY, true);

                        // 데이터 추가
                        db.update(FeedReaderContract.FeedEntry.RECIEVE_TABLE_NAME, values, "seq=?", new String[]{writings.getSeq()+""});

                        setDefaultLayout(false);
                        listRefresh();
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
    }

    public void setDefaultLayout(boolean b){
        // default(true) : 버튼o, 댓글x -> 수락하기 전
        if(b){
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.content_area);
            rl.setVisibility(View.VISIBLE);
            LinearLayout ll = (LinearLayout)findViewById(R.id.reply_area);
            ll.setVisibility(View.GONE);
        }else{
            RelativeLayout rl = (RelativeLayout)findViewById(R.id.content_area);
            rl.setVisibility(View.GONE);
            LinearLayout ll = (LinearLayout)findViewById(R.id.reply_area);
            ll.setVisibility(View.VISIBLE);
        }
    }

    public void sendReply(View v){
        edit_reply = (EditText)findViewById(R.id.reply_content);

        final String content = edit_reply.getText().toString().trim();
        if (content.getBytes().length > 0) {
            URLConnector urlConnector = new URLConnector();
            JSONObject param = new JSONObject();
            Gson gson = new Gson();
            SharedPreferences prefs = getSharedPreferences("mju_sns", MODE_PRIVATE);
            String id = prefs.getString("id", "");
            try {
                param.put("mode", "sendReply");
                param.put("id", id);
                param.put("writings_seq", writings.getSeq());
                param.put("content", content);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String result = urlConnector.starter(param, true, false);
            listRefresh();
        }
    }

    public void locationCancel(){
        mapArea.removeView(mMapView);
        onBackPressed();
    }

    public void backBtn(View v){
        locationCancel();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                locationCancel();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    // MapView.MapViewEventListener
    @Override
    public void onMapViewInitialized(MapView mapView) {
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(Double.parseDouble(writings.getLocation_latitude()), Double.parseDouble(writings.getLocation_longitude())), 3, true);
    }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {}
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {}
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {}
}