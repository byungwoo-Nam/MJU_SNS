package com.example.mju_sns.util.api.daumMap;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class DaumMapViewEvent{

//    DaumMap daumMap;
//
//    public DaumMapViewEvent(DaumMap daumMap){
//        System.out.println("다음맵이벤트 생성자!!!!");
//        this.daumMap = daumMap;
//        this.daumMap.getMapView().setMapViewEventListener(this);
//        //onMapViewInitialized(this.daumMap.getMapView());
//    }

    MapView.MapViewEventListener m = new MapView.MapViewEventListener() {
        @Override
        public void onMapViewInitialized(MapView mapView) {
            System.out.println("############DSFDSFVCXVCXDR#@$$$$$$$$$$$$");
        }

        @Override
        public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewZoomLevelChanged(MapView mapView, int i) {

        }

        @Override
        public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

        }

        @Override
        public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

        }
    };

    public MapView.MapViewEventListener getTest(){
        return m;
    }
}
