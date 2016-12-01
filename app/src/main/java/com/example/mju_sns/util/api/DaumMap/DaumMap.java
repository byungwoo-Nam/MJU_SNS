package com.example.mju_sns.util.api.DaumMap;

import android.app.Activity;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class DaumMap {
    protected Activity activity;
    private MapView mapView;
    private final String API_KEY = "74887856e0371a6f235a56dfa3bdc2bf";
    protected double default_latitude;    // 초기 위도
    protected double default_longitude;   // 초기 경도
    protected int default_zoom;           // 초기 줌레벨

    public DaumMap(Activity activity, double default_latitude, double default_longitude, int default_zoom){
        this.activity = activity;
        this.default_latitude = default_latitude;
        this.default_longitude = default_longitude;
        this.default_zoom = default_zoom;
        setValue();
        setEvent();
    }

    public void setLoacation(double latitude, double longitude){
        this.mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
    }

    public MapView getMapView(){
        return this.mapView;
    }

    public void setValue(){
        this.mapView = new MapView(this.activity);
        this.mapView.setDaumMapApiKey(this.API_KEY);
    }

    public void setEvent(){
        // 이벤트 등록
        this.mapView.setMapViewEventListener(new DaumMapViewEvent(this));
    }
}
