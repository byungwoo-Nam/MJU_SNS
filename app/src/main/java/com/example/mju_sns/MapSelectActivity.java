package com.example.mju_sns;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.daum.mf.map.api.MapView;

import java.util.HashMap;

public class MapSelectActivity extends Activity {

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapselect);

        mapView = MainActivity.mapView;

    }
}