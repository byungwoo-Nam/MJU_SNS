package com.example.mju_sns;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kook on 2016-11-15.
 */

public class TabFragment4 extends BaseFragment {
    private Switch swc; // 스위치 객체
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 객체 설정 & 리스너 부착
        view = inflater.inflate(R.layout.tab_fragment_4, container, false);


        //Get widgets reference from XML layout
        final TextView tView = (TextView) view.findViewById(R.id.tv);
        Switch sButton = (Switch) view.findViewById(R.id.switch_btn);

        //Set a CheckedChange Listener for Switch Button
        sButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    //Do something when Switch button is on/checked
                    tView.setText("푸쉬 알림 ON");
                    Log.e("Push on", "dsakfhsdlakfjlkasdf");
                    //db로 on 전송
                }
                else
                {
                    //Do something when Switch is off/unchecked
                    tView.setText("푸쉬 알림 OFF");
                    Log.e("Push off", "adfdggfsakfhsdlakfjlkasdf");
                    //db로 off 전송
                }
            }
        });

        return view;
    }

}
