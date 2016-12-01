package com.example.mju_sns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.mju_sns.util.api.daumMap.DaumMap;
import com.example.mju_sns.util.config.app.CodeConfig;

import static android.view.KeyEvent.ACTION_UP;

public class TabFragment1 extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CodeConfig codeConfig = new CodeConfig();

        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);

        DaumMap daumMap = new DaumMap(getActivity(), codeConfig.mju_latitude, codeConfig.mju_longitude, 3);

        daumMap.getMapView().setOnTouchListener(new View.OnTouchListener()
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

        mapViewContainer.addView(daumMap.getMapView());

        return view;
    }

    public void startMapSelect(View v){
        Intent intent = new Intent(v.getContext(), MapSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        v.getContext().startActivity(intent);
    }
}
