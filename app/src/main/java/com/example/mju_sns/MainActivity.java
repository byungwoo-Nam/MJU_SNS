package com.example.mju_sns;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mju_sns.util.config.app.CodeConfig;
import com.example.mju_sns.util.gps.CurrentLocation;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LocationManager locationManager;
    private TabPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        CurrentLocation currentLocation = new CurrentLocation(locationManager, this);
        currentLocation.setCurrentLocation();

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        for(String s : CodeConfig.TAB_NAME){
            tabLayout.addTab(tabLayout.newTab().setText(s));
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Initializing ViewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        // Creating TabPagerAdapter adapter
        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //((BaseFragment)pagerAdapter.getItem(tab.getPosition())).onChange();
                ((BaseFragment)pagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem())).onChange();
                //pagerAdapter.getItem(tab.getPosition()).onResume();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    public void onTabChange(int index){
        tabLayout.getTabAt(index).select();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            //하드웨어 뒤로가기 버튼에 따른 이벤트 설정
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this)
                        .setTitle("MJU_SNS 종료")
                        .setMessage("앱을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 프로세스 종료.
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.photo:
                Intent intent = new Intent(this, SignUpPhotoActivity.class);
                startActivity(intent);
                //Toast.makeText(this, "photo",Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}