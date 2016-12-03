package com.example.mju_sns.util.api.daumMap;

import android.app.Activity;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.io.Serializable;

public class DaumMap implements MapView.MapViewEventListener {
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

    public void setEvent(MapView.MapViewEventListener m){
        // 이벤트 등록
        System.out.println("이벤트등록!!!!!!!!!!");
        this.mapView.setMapViewEventListener(m);
    }


    public void onMapViewInitialized(MapView mapView) {
        //Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        //mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        // 중심점 변경 + 줌 레벨 변경
        System.out.println("3333dfsdfsdfDFSDSFDSF");
        //mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(this.daumMap.default_latitude, this.daumMap.default_longitude), this.daumMap.default_zoom, true);
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapCenterPoint) {
        MapPoint.GeoCoordinate mapPointGeo = mapCenterPoint.getMapPointGeoCoord();
        //System.out.println(mapPointGeo.latitude + " + " + mapPointGeo.longitude);
        //Log.i(LOG_TAG, String.format("MapView onMapViewCenterPointMoved (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
        System.out.println("onMapViewSingleTapped");
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onMapViewSingleTapped (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
        System.out.println("onMapViewDragStarted");
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onMapViewDragStarted (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        System.out.println("onMapViewDragEnded");
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onMapViewDragEnded (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        System.out.println("onMapViewMoveFinished");
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onMapViewMoveFinished (%f,%f)", mapPointGeo.latitude, mapPointGeo.longitude));
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int zoomLevel) {
        //Log.i(LOG_TAG, String.format("MapView onMapViewZoomLevelChanged (%d)", zoomLevel));
    }
}
