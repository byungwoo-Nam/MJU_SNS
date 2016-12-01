package com.example.mju_sns;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class TabFragment2 extends Fragment {
    private ArrayList<listItem> list;
    private ListView lv;
    private MyAdapter mAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.tab_fragment_2, container, false);
        list=new ArrayList<listItem>();     // ArrayList로 생성
        lv = (ListView)v.findViewById(R.id.lv);


        mAdatper=new MyAdapter(list);
        lv.setAdapter(mAdatper);

        //아이템 추가
        list.add(new listItem(R.drawable.apple,"하","취업"));
        list.add(new listItem(R.drawable.banana,"존나","되면"));
        list.add(new listItem(R.drawable.cherry,"짱나네","접는다."));
        list.add(new listItem(R.drawable.apple,"하","취업"));
        list.add(new listItem(R.drawable.banana,"존나","되면"));
        list.add(new listItem(R.drawable.cherry,"짱나네","접는다."));
        list.add(new listItem(R.drawable.apple,"하","취업"));
        list.add(new listItem(R.drawable.banana,"존나","되면"));
        list.add(new listItem(R.drawable.cherry,"짱나네","접는다."));

        return v;
    }

    // ListView의 아이템에 들어가는 커스텀된 데이터들의 묶음
    public class listItem{
        private int profile;    // R.drawable.~ 리소스 아이디 값을 받아오는 변수
        private String name;    // String 카카오톡 대화 목록의 이름
        private String chat;    // String 마지막 대화
        // 매개변수가 있는 생성자로 받아와 값을 전달한다.
        public listItem(int profile, String name, String chat){
            this.profile=profile;
            this.name=name;
            this.chat=chat;
        }
    }

    // Adapter
    public class MyAdapter extends BaseAdapter{
        private ArrayList<listItem> list;

        public MyAdapter(ArrayList<listItem> list){
            // Adapter 생성시 list값을 넘겨 받는다.
            this.list=list;
        }

        @Override
        public int getCount() {
            // list의 사이즈 만큼 반환
            return list.size();
        }

        @Override
        public listItem getItem(int position) {
            // 현재 position에 따른 list의 값을 반환 시켜준다.
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos=position;
            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.writing_list_item, parent, false);

                ImageView profile=(ImageView)v.findViewById(R.id.profile_image);
                TextView name=(TextView)v.findViewById(R.id.name);
                TextView chat=(TextView)v.findViewById(R.id.chat);

                profile.setImageResource(getItem(pos).profile);
                name.setText(getItem(pos).name);
                chat.setText(getItem(pos).chat);
            }
            return v;
        }
    }
}



