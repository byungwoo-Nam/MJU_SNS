package com.example.mju_sns;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.mju_sns.util.config.app.AppBoot;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class AppInitActivity extends Activity{

    public static Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appinit);

        AppBoot appBoot = new AppBoot(activity);
        appBoot.init();



//        PermissionListener permissionlistener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
////                Toast.makeText(AppInitActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                //needJoin = !appBoot.isMember();
//                System.out.println("init끝!!!");
////                Handler handler = new Handler();
////                handler.postDelayed(new Runnable() {
////                    public void run() {
////                        Intent intent = null;
//////                        if(needJoin) {
//////                            intent = new Intent(Splash.this, AppInitActivity.class);
//////                        }else{
////                        intent = new Intent(AppInitActivity.this, MainActivity.class);
//////                        }
////                        startActivity(intent);
////                        //뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
////                        finish();
////                    }
////                }, 2000);
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                //Toast.makeText(Splash.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
//            }
//        };

//        new TedPermission(this)
//                .setPermissionListener(permissionlistener)
//                .setDeniedMessage("권한을 허용하지 않으면 서비스를 이용할 수 없습니다.\n\n[설정]메뉴에서 권한을 확인해 주세요.")
//                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.GET_ACCOUNTS)
//                .check();

//
//        //Dialog에서 보여줄 입력화면 View 객체 생성 작업
//        //Layout xml 리소스 파일을 View 객체로 부불려 주는(inflate) LayoutInflater 객체 생성
//        LayoutInflater inflater=getLayoutInflater();
//
//        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
//        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언
//        final View dialogView = inflater.inflate(R.layout.dialog_userjoin, null);
//
//        AlertDialog.Builder builder= new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert); //AlertDialog.Builder 객체 생성
//        builder.setTitle("환영합니다!"); //Dialog 제목
//        builder.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
//        builder.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
//        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            public boolean onKey(DialogInterface dialog,
//                                 int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    finish();
//                    Splash.activity.finish();
//                    dialog.dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//        builder.setPositiveButton("확인", new Dialog.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
////                EditText editText = (EditText) dialogView.findViewById(R.id.nickname);
////                String nickname = editText.getText().toString().trim();
////                if(nickname.getBytes().length <= 0){
////                    Toast toast = Toast.makeText(activity, "메세지 입력.", Toast.LENGTH_SHORT);
////                    toast.show();
////                }
//            }
//        });
//        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//                Splash.activity.finish();
//                dialog.dismiss();
//                return;
//            }
//        });
//
//        //설정한 값으로 AlertDialog 객체 생성
//        AlertDialog dialog = builder.create();
//
//        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
//        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
//
//        //Dialog 보이기
//        dialog.show();
    }
}