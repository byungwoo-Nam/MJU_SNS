package com.example.mju_sns;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.mju_sns.util.config.app.AppBoot;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.Serializable;
import java.util.ArrayList;

public class Splash extends Activity implements Serializable {

    public static Activity activity;
    public boolean needJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        AppBoot appBoot = new AppBoot(activity);
        appBoot.init();
        this.needJoin = !appBoot.isMember();
        System.out.println("init끝!!!");

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(Splash.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent intent = null;
                        if(needJoin) {
                            intent = new Intent(Splash.this, UserJoinActivity.class);
                        }else{
                            intent = new Intent(Splash.this, MainActivity.class);
                        }
                        startActivity(intent);
                        // 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
                        //finish();
                    }
                }, 2000);

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(Splash.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한을 허용하지 않으면 서비스를 이용할 수 없습니다.\n\n[설정]메뉴에서 권한을 확인해 주세요.")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE)
                .check();

    }


}
