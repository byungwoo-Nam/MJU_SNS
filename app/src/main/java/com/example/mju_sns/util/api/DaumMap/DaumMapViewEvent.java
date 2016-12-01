package com.example.mju_sns.util.api.DaumMap;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class DaumMapViewEvent implements MapView.MapViewEventListener {

    DaumMap daumMap;

    public DaumMapViewEvent(DaumMap daumMap){
        this.daumMap = daumMap;
        this.daumMap.getMapView().setMapViewEventListener(this);
        //onMapViewInitialized(this.daumMap.getMapView());
    }

    public void onMapViewInitialized(MapView mapView) {
        //Log.i(LOG_TAG, "MapView had loaded. Now, MapView APIs could be called safely");
        //mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        // 중심점 변경 + 줌 레벨 변경
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(this.daumMap.default_latitude, this.daumMap.default_longitude), this.daumMap.default_zoom, true);
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
