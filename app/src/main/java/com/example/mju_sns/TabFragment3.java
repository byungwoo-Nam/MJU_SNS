package com.example.mju_sns;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mju_sns.util.config.app.ListViewAdapter;
import com.example.mju_sns.util.config.database.CursorHelper;
import com.example.mju_sns.util.config.database.FeedReaderContract;
import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Writings;
import com.example.mju_sns.util.gps.CurrentLocation;

import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.List;


public class TabFragment3 extends BaseFragment{
    private List<Writings> list = new ArrayList();
    private ListView lv;
    private ListViewAdapter listViewAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);
        lv = (ListView)view.findViewById(R.id.lv);
        listViewAdatper = new ListViewAdapter(list, getActivity(), "writings");
        lv.setAdapter(listViewAdatper);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id){
                Intent intent = new Intent(getContext(), DetailViewActivity.class);
                intent.putExtra("type", "RECIEVE");
                intent.putExtra("data", list.get(position));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onChange() {
        listRefresh();
    }

    public void listRefresh(){
        list.clear();
        List<Writings> dataList = getListData();

        // 아이템 추가
        for(int i=0; i<dataList.size(); i++) {
            list.add(dataList.get(i));
        }

        listViewAdatper.notifyDataSetChanged();
    }

    public List<Writings> getListData(){
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 데이터를 가져올 컬럼 정의
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_SEQ,
                FeedReaderContract.FeedEntry.COLUMN_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_CONTENTS,
                FeedReaderContract.FeedEntry.COLUMN_LOCATION_LATITUDE,
                FeedReaderContract.FeedEntry.COLUMN_LOCATION_LONGITUDE,
                FeedReaderContract.FeedEntry.COLUMN_LOCATION_RANGE,
                FeedReaderContract.FeedEntry.COLUMN_DATE,
                FeedReaderContract.FeedEntry.COLUMN_ISAPPLY
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.RECIEVE_TABLE_NAME,	// 테이블 이름
                projection,				    // 컬럼
                null,				        // WHERE 절의 컬럼
                null,			            // WHERE 절의 컬럼 값
                null,					    // row를 그룹화하지 않도록
                null,					    // 그룹화한 row 를 필터 하지 않도록
                "seq desc"		            // ORDER BY 절
        );

        CursorHelper cursorHelper = new CursorHelper(cursor, Writings.class);
        return (List<Writings>)cursorHelper.getList();
    }
}
