package com.example.mju_sns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mju_sns.util.config.app.CodeConfig;
import com.example.mju_sns.util.config.app.URLConnector;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.view.KeyEvent.ACTION_UP;

public class TabFragment1 extends Fragment implements MapView.MapViewEventListener{

    CodeConfig codeConfig = new CodeConfig();
    TextView location_textview;
    MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);

        location_textview = (TextView) view.findViewById(R.id.location);

//        mapView = new MapView(getActivity());
        mapView = MainActivity.mapView;
        mapView.setDaumMapApiKey(this.codeConfig.DAUM_MAP_API_KEY);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);

        mapView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
            // 지도 터치 이벤트 제거 & 상세 지도 터치만 예외
            if(event.getAction() == ACTION_UP){
                //상세지도 페이지 open
                startMapSelect(v);
            }
            return true;
        };
    });
        return view;
    }

    public void startMapSelect(View v){
        Intent intent = new Intent(v.getContext(), MapSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
    }

    // MapView.MapViewEventListener
    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(this.codeConfig.MJU_LATITUDE, this.codeConfig.MJU_LONGITUDE), 3, true);
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
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        try {
            param.put("mode", "daumAPI");
            param.put("location_latitude", mapPoint.getMapPointGeoCoord().latitude);
            param.put("location_longitude", mapPoint.getMapPointGeoCoord().longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //            Gson gson = new Gson();
//            gson.fromJson(result, Users.class);

        String result = urlConnector.getData(param, true);

        try {
            JSONObject fullAddress = new JSONObject(result);
            JSONObject newAddress = (JSONObject)fullAddress.get("new");
            location_textview.setText(newAddress.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
