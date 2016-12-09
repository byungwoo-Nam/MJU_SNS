package com.example.mju_sns.util.config.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mju_sns.R;
import com.example.mju_sns.util.dto.Writings;

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
                    v = inflater.inflate(R.layout.writings_list_item, parent, false);
                    break;
                default:
                    break;
            }
        }

        switch (classType){
            case "writings":
                writingsList(v, pos);
                break;
            default:
                break;
        }
        return v;
    }

    public void writingsList(View v, int pos){
        ImageView profile=(ImageView)v.findViewById(R.id.profile_image);
        TextView name=(TextView)v.findViewById(R.id.name);
        TextView chat=(TextView)v.findViewById(R.id.chat);

//        profile.setImageResource(((Writings)getItem(pos)).get);

        name.setText(((Writings)getItem(pos)).getTitle());
        chat.setText(((Writings)getItem(pos)).getContents());
        System.out.println("-------------------------------------------");
        System.out.println(((Writings)getItem(pos)).getTitle());
    }
}