package com.example.mju_sns;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mju_sns.util.config.app.CodeConfig;
import com.example.mju_sns.util.config.app.URLConnector;
import com.example.mju_sns.util.config.database.FeedReaderContract;
import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Writings;
import com.google.gson.Gson;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.view.KeyEvent.ACTION_UP;

public class TabFragment1 extends BaseFragment implements MapView.MapViewEventListener{

    private static final String TAG = "frag1test";

    CodeConfig codeConfig = new CodeConfig();
    TextView location_textview;
    MapView mMapView;
    View view;
    RelativeLayout mapArea;
    Button applyBtn;

    EditText edit_title;
    EditText edit_content;
    Spinner edit_range;

    double[] location = {0,0};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        edit_title = (EditText)view.findViewById(R.id.edit_title);
        edit_content = (EditText)view.findViewById(R.id.edit_content);
        edit_range = (Spinner)view.findViewById(R.id.edit_range);

        mapArea = (RelativeLayout)view.findViewById(R.id.map_area);
        applyBtn = (Button)view.findViewById(R.id.apply);
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyAction();
            }
        });

        location_textview = (TextView) view.findViewById(R.id.location);

        return view;
    }

    public void applyAction(){
        super.downKeyboard(this, edit_title);
        super.downKeyboard(this, edit_content);
        final String title = edit_title.getText().toString().trim();
        final String contents = edit_content.getText().toString().trim();
        final int location_range = edit_range.getSelectedItemPosition()+1;
        if (title.getBytes().length <= 0) {
            Toast toast = Toast.makeText(getContext(), "올바른 제목을 입력해 주세요.", Toast.LENGTH_SHORT);
            toast.show();
        }else if (contents.getBytes().length <= 0) {
            Toast toast = Toast.makeText(getContext(), "올바른 내용을 입력해 주세요.", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            new AlertDialog.Builder(getContext())
                    .setTitle("글 등록")
                    .setMessage("새로운 글을 등록하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            URLConnector urlConnector = new URLConnector();
                            Writings writings = new Writings();
                            JSONObject param = new JSONObject();
                            Gson gson = new Gson();
                            SharedPreferences prefs = getActivity().getSharedPreferences("mju_sns", MODE_PRIVATE);
                            String id = prefs.getString("id", "");
                            try {
                                param.put("mode", "boardWrite");
                                param.put("id", id);
                                writings.setTitle(title);
                                writings.setContents(contents);
                                writings.setLocation_latitude(location[0]+"");
                                writings.setLocation_longitude(location[1]+"");
                                writings.setLocation_range(location_range);
                                param.put("data", gson.toJson(writings));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String result = urlConnector.starter(param, true, false);
                            System.out.println(result);
                            writings = gson.fromJson(result, Writings.class);


                            FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity());

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
                            long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

                            resetFragment();

                            ((MainActivity)getActivity()).onTabChange(1);
                        }
                    })
                    .setNegativeButton("아니오", null)
                    .show();
        }
    }

    public void resetFragment(){
        edit_title.setText("");
        edit_content.setText("");
        edit_range.setSelection(0);
        mapArea.removeView(mMapView);
        if(mapArea.indexOfChild(mMapView) == -1) {
            initMapView();
        }
    }

    public void startMapSelect(View v){
        Intent intent = new Intent(v.getContext(), MapSelectActivity.class);
        intent.putExtra("location", location);
        this.startActivityForResult(intent, 0);
    }

    public void initMapView(){
        mMapView = new MapView(view.getContext());
        mMapView.setDaumMapApiKey(this.codeConfig.DAUM_MAP_API_KEY);
        mMapView.setMapViewEventListener(this);
        mMapView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                // 지도 터치 이벤트 제거 & 상세 지도 터치만 예외
                if(event.getAction() == ACTION_UP){
                    //상세지도 페이지 open
                    startMapSelect(v);
                }
                return true;
            };
        });
        mapArea.addView(mMapView, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == RESULT_OK) {
            location = intent.getDoubleArrayExtra("location");
        }

    }

    @Override
    public void onResume() {
        if(mapArea.indexOfChild(mMapView) == -1) {
            initMapView();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        mapArea.removeView(mMapView);
        super.onPause();
    }

    // MapView.MapViewEventListener
    @Override
    public void onMapViewInitialized(MapView mapView) {
        location[0] = location[0] == 0 ? this.codeConfig.MJU_LATITUDE : location[0];
        location[1] = location[1] == 0 ? this.codeConfig.MJU_LONGITUDE : location[1];
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(location[0], location[1]), 3, true);
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
}
