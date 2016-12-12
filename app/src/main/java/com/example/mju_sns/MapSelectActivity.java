package com.example.mju_sns;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mju_sns.util.config.app.CodeConfig;
import com.example.mju_sns.util.config.app.URLConnector;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;


public class MapSelectActivity extends Activity implements MapView.MapViewEventListener, MapView.CurrentLocationEventListener{

    CodeConfig codeConfig = new CodeConfig();

    MapView mMapView;
    RelativeLayout mapArea;

    TextView location_textview;

    double[] orig_location = {0,0};
    double[] current_location = {0,0};

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapselect);

        intent = getIntent();
        orig_location = intent.getDoubleArrayExtra("location");

        mapArea = (RelativeLayout)findViewById(R.id.map_area);
        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey(this.codeConfig.DAUM_MAP_API_KEY);
        mMapView.setMapViewEventListener(this);
        mMapView.setCurrentLocationEventListener(this);

        RelativeLayout select_area = (RelativeLayout)findViewById(R.id.select_area);

        select_area.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                // 터치 허용안함(지도 인식 막기)
                return true;
            };
        });

        location_textview = (TextView) findViewById(R.id.location_textview);
    }

    public void locationSelect(View v){
        mapArea.removeView(mMapView);
        onBackPressed();
    }

    public void locationCancel(){
        current_location = orig_location;
        mapArea.removeView(mMapView);
        onBackPressed();
    }

    public void backBtn(View v){
        locationCancel();
    }

    public void myLocation(View v){
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        intent = new Intent();
        intent.putExtra("fragmentIndex", 0);
        intent.putExtra("location", current_location);
        setResult(RESULT_OK, intent);
        super.finish();
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
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(orig_location[0], orig_location[1]), 3, true);
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
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        current_location[0] = mapPoint.getMapPointGeoCoord().latitude;
        current_location[1] = mapPoint.getMapPointGeoCoord().longitude;
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        try {
            param.put("mode", "daumAPI");
            param.put("location_latitude", current_location[0]);
            param.put("location_longitude", current_location[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = urlConnector.starter(param, true, false);

        try {
            JSONObject fullAddress = new JSONObject(result);
            JSONObject newAddress = (JSONObject)fullAddress.get("new");
            JSONObject oldAddress = (JSONObject)fullAddress.get("old");
            location_textview.setText(!newAddress.get("name").toString().equals("") ? newAddress.get("name").toString() : oldAddress.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // MapView.CurrentLocationEventListener
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters){
        mMapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(currentLocation.getMapPointGeoCoord().latitude, currentLocation.getMapPointGeoCoord().longitude), 3, true);
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float headingAngle){}
    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView){}
    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView){}
}