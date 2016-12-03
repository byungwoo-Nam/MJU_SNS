package com.example.mju_sns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mju_sns.util.config.app.AppBoot;
import com.example.mju_sns.util.config.app.URLConnector;
import com.example.mju_sns.util.fcm.FcmStarter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Splash extends Activity {

    public Activity activity;
    AppBoot appBoot;
    ProgressDialog progressDialog;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        activity = this;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("연결중입니다...");

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                settingStart();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한을 허용하지 않으면 서비스를 이용할 수 없습니다.\n\n[설정]메뉴에서 권한을 확인해 주세요.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.GET_ACCOUNTS)
                .check();
    }

    private void settingStart(){
        progressDialog.show();
        // 모든것은 fcmStarter로 부터... app setting도 여기 안에
        new FcmStarter(activity);
    }

    public void settingEnd(AppBoot appBoot){
        progressDialog.dismiss();
        this.appBoot = appBoot;
        if(this.appBoot.getIsMember()){
            // 기존회원
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            // 신규등록
            joinStart();
        }
    }

    private void joinStart(){
        //Dialog에서 보여줄 입력화면 View 객체 생성 작업
        //Layout xml 리소스 파일을 View 객체로 부불려 주는(inflate) LayoutInflater 객체 생성
        LayoutInflater inflater=getLayoutInflater();

        //res폴더>>layout폴더>>dialog_addmember.xml 레이아웃 리소스 파일로 View 객체 생성
        //Dialog의 listener에서 사용하기 위해 final로 참조변수 선언
        final View dialogView = inflater.inflate(R.layout.dialog_userjoin, null);

        AlertDialog.Builder builder= new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert); //AlertDialog.Builder 객체 생성
        builder.setTitle("환영합니다!"); //Dialog 제목
        builder.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
        builder.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
        builder.setPositiveButton("확인", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                return;
            }
        });

        //설정한 값으로 AlertDialog 객체 생성
        final AlertDialog dialog = builder.create();

        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정

        //back키 설정
        dialog.setCancelable(false);

        //Dialog 보이기
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) dialogView.findViewById(R.id.nickname);
                String nickname = editText.getText().toString().trim();
                if (nickname.getBytes().length <= 0) {
                    Toast toast = Toast.makeText(activity, "올바른 닉네임을 입력해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }else if(!nicknameDuplicationCheck(nickname)){
                    Toast toast = Toast.makeText(activity, "이미 등록된 닉네임 입니다. 다시 입력해 주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    if(joinMember(nickname)){
                        SharedPreferences prefs = activity.getSharedPreferences("mju_sns", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("isMember", "true");
                        editor.commit();
                        dialog.dismiss();
                        intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    private boolean nicknameDuplicationCheck(String nickname) {
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        try {
            param.put("mode", "nicknameCheck");
            param.put("nickname", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = urlConnector.getData(param, true);
        if(result!=""){
//            Gson gson = new Gson();
//            gson.fromJson(result, Users.class);
            return Integer.parseInt(result) > 0 ? false : true;
        }else{
            return false;
        }
    }

    private boolean joinMember(String nickname){
        URLConnector urlConnector = new URLConnector();
        JSONObject param = new JSONObject();
        try {
            param.put("mode", "joinMember");
            param.put("id", this.appBoot.getUuid());
            param.put("gcm_id", this.appBoot.getFcmToken());
            param.put("nickname", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = urlConnector.getData(param, true);
        return Integer.parseInt(result)==1 ? true : false;
    }
}
