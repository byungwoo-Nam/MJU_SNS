package com.example.mju_sns;

import android.content.SharedPreferences;
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

import com.example.mju_sns.util.config.app.URLConnector;
import com.example.mju_sns.util.dto.Writings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by kook on 2016-11-15.
 */

public class TabFragment4 extends BaseFragment {
    private View view;
    private Switch sButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 객체 설정 & 리스너 부착
        view = inflater.inflate(R.layout.tab_fragment_4, container, false);

        //Get widgets reference from XML layout
        final TextView tView = (TextView) view.findViewById(R.id.tv);

        sButton = (Switch) view.findViewById(R.id.switch_btn);

        //Set a CheckedChange Listener for Switch Button
        sButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on){
                if(on)
                {
                    //Do something when Switch button is on/checked
                    tView.setText("푸쉬 알림 ON");
                    Log.e("Push on", "dsakfhsdlakfjlkasdf");
                    changePushStatus();
                }
                else
                {
                    //Do something when Switch is off/unchecked
                    tView.setText("푸쉬 알림 OFF");
                    changePushStatus();
                }
            }
        });

        return view;
    }

    @Override
    public void onChange() {
        selectRefresh();
    }

    public void selectRefresh(){
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        SharedPreferences prefs = getActivity().getSharedPreferences("mju_sns", MODE_PRIVATE);
        String id = prefs.getString("id", "");
        try {
            param.put("mode", "getPushOption");
            param.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = urlConnector.starter(param, true, false);
        boolean buttonOnOff = Integer.parseInt(result.trim()) == 1 ? true : false;
        sButton.setChecked(buttonOnOff);
    }

    public void changePushStatus(){
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        try {
            SharedPreferences prefs = getActivity().getSharedPreferences("mju_sns", MODE_PRIVATE);
            String id = prefs.getString("id", "");
            param.put("mode", "pushOptionChange");
            param.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        urlConnector.starter(param, true, false);
    }

}
