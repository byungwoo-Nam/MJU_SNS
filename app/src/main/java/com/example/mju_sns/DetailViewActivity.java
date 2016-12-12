package com.example.mju_sns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mju_sns.util.config.app.CodeConfig;
import com.example.mju_sns.util.config.app.URLConnector;
import com.example.mju_sns.util.config.database.FeedReaderContract;
import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Writings;
import com.google.gson.Gson;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;


public class DetailViewActivity extends Activity implements MapView.MapViewEventListener{

    CodeConfig codeConfig = new CodeConfig();

    MapView mMapView;
    RelativeLayout mapArea;

    Intent intent;
    Button apply_button;

    Writings writings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        intent = getIntent();
        writings = (Writings) intent.getSerializableExtra("data");

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

        apply_button = (Button)findViewById(R.id.apply);

        if(writings.getIsapply()){
            removeButton();
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

    public void removeButton(){
        apply_button.setVisibility(View.GONE);
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

                        removeButton();
                    }
                })
                .setNegativeButton("아니오", null)
                .show();
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