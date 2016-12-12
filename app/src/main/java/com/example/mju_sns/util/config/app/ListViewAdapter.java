package com.example.mju_sns.util.config.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mju_sns.R;
import com.example.mju_sns.util.dto.Reply;
import com.example.mju_sns.util.dto.Writings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List list;
    private Activity activity;
    private String classType;

    public ListViewAdapter(List list, Activity activity, String classType){
        // Adapter 생성시 list값을 넘겨 받는다.
        this.list=list;
        this.activity=activity;
        this.classType=classType;
    }

    @Override
    public int getCount() {
        // list의 사이즈 만큼 반환
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // 현재 position에 따른 list의 값을 반환 시켜준다.
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (classType){
                case "writings":
                    v = inflater.inflate(R.layout.list_item, parent, false);
                    break;
                case "reply":
                    v = inflater.inflate(R.layout.reply_list_item, parent, false);
                    break;
                default:
                    break;
            }
        }

        switch (classType){
            case "writings":
                writingsList(v, pos);
                break;
            case "reply":
                replyList(v, pos);
                break;
            default:
                break;
        }
        return v;
    }

    public void writingsList(View v, int pos){
        v.setBackgroundColor(Color.rgb(255,255,255));
        TextView title=(TextView)v.findViewById(R.id.title);
        TextView location=(TextView)v.findViewById(R.id.location);
        TextView date=(TextView)v.findViewById(R.id.date);

        if(((Writings)getItem(pos)).getIsapply()){
            v.setBackgroundColor(Color.rgb(206,251,201));
        }

        title.setText(((Writings)getItem(pos)).getTitle());

        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        try {
            param.put("mode", "daumAPI");
            param.put("location_latitude", ((Writings)getItem(pos)).getLocation_latitude());
            param.put("location_longitude", ((Writings)getItem(pos)).getLocation_longitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String result = urlConnector.starter(param, true, false);

        try {
            JSONObject fullAddress = new JSONObject(result);
            JSONObject newAddress = (JSONObject)fullAddress.get("new");
            JSONObject oldAddress = (JSONObject)fullAddress.get("old");
            location.setText(!newAddress.get("name").toString().equals("") ? newAddress.get("name").toString() : oldAddress.get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        date.setText(((Writings)getItem(pos)).getDate());
    }

    public void replyList(View v, int pos) {
        boolean message_left = ((Reply)getItem(pos)).getMine();
        TextView content = (TextView)v.findViewById(R.id.content);
        content.setText(((Reply)getItem(pos)).getContent());
        content.setBackground(ContextCompat.getDrawable(v.getContext(), (message_left ? R.drawable.bubble_b : R.drawable.bubble_a)));

        LinearLayout chatMessageContainer = (LinearLayout)v.findViewById(R.id.reply_row);
        int align;
        if(message_left) {
            align = Gravity.RIGHT;
            message_left = false;
        }else{
            align = Gravity.LEFT;
            message_left=true;
        }
        chatMessageContainer.setGravity(align);

    }
}