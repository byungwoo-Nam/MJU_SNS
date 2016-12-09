package com.example.mju_sns;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.mju_sns.util.config.app.ListViewAdapter;
import com.example.mju_sns.util.config.database.CursorHelper;
import com.example.mju_sns.util.config.database.FeedReaderContract;
import com.example.mju_sns.util.config.database.FeedReaderDbHelper;
import com.example.mju_sns.util.dto.Writings;

import java.util.ArrayList;
import java.util.List;


public class TabFragment2 extends BaseFragment implements AbsListView.OnScrollListener {
    private List<Writings> list = new ArrayList();
    private ListView lv;
    private ListViewAdapter listViewAdatper;

    private boolean isLoading = false;
    private int pageNum = 1;    // 현재 페이지 번호
    private boolean mLockListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_2, container, false);
        lv = (ListView)view.findViewById(R.id.lv);
        listViewAdatper = new ListViewAdapter(list, getActivity(), "writings");
        lv.setAdapter(listViewAdatper);
        lv.setOnScrollListener(this);

        return view;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
//        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
//        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
//        int count = totalItemCount - visibleItemCount;
//
//        if(firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false){
//            getListData();
//        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onChange() {
        listRefresh();
    }

    public void listRefresh(){
        System.out.println(list.size());
        list.clear();
        System.out.println(list.size());

        List<Writings> dataList = getListData();

        System.out.println("!!!!!!!!!!!!!!!!!!!"+dataList.size());

        // 아이템 추가
        for(int i=0; i<dataList.size(); i++) {
            System.out.println(dataList.get(i).getSeq());
            System.out.println(dataList.get(i).getTitle());
            list.add(dataList.get(i));
        }

        listViewAdatper.notifyDataSetChanged();

        mLockListView = false;
    }

    public List<Writings> getListData(){
        mLockListView = true;

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
                FeedReaderContract.FeedEntry.COLUMN_DATE
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,	// 테이블 이름
                projection,				    // 컬럼
                null,				        // WHERE 절의 컬럼
                null,			            // WHERE 절의 컬럼 값
                null,					    // row를 그룹화하지 않도록
                null,					    // 그룹화한 row 를 필터 하지 않도록
                "seq desc"		            // ORDER BY 절
        );

        CursorHelper cursorHelper = new CursorHelper(cursor, Writings.class);
        return (List<Writings>)cursorHelper.getList();


//        URLConnector urlConnector = new URLConnector();
//        JSONObject param = new JSONObject();
//        Gson gson = new Gson();
//        SharedPreferences prefs = getActivity().getSharedPreferences("mju_sns", MODE_PRIVATE);
//        String token = prefs.getString("token", "");
//        try {
//            param.put("mode", "getWritingsList");
//            param.put("token", token);
//            param.put("pageNum", pageNum);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        String result = urlConnector.starter(param, true);
//        gson.toJson(result);
//
//        return gson.fromJson(result, new TypeToken<List<Writings>>(){}.getType());
    }
}



