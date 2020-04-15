package com.neu.test.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.ResultForTaskAndUser;
import com.neu.test.entity.Task;
import com.neu.test.entity.User;
import com.neu.test.net.callback.ListTaskAndUserCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.SuggestionActivitySaveDataUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.richinfo.dualsim.TelephonyManagement;
import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;



public class LoginActivity extends BaseActivity implements View.OnClickListener,OnRequestPermissionsResultCallback{
    // 定义 字段
    //region
    private static String TAG = "LoginActivity";
    private final int SDK_PERMISSION_REQUEST = 127;
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private String permissionInfo;//权限信息
    public static String inputName; //
    public static String inputPassword;//修改密码界面会用到  先为该参数赋值 方便后面的测试
    public static int userid;//在修改密码界面会用的到

    private EditText input_name; //输入用户名
    private EditText input_password; //输入密码
    private Button bt_login; //登录
    private Button bt_signin; //注册
    String url;  //登录的接口网址
    public static User user = new User(); //其他页面使用
    private SharedPreferences sharedPreferences; //缓存
    //endregion
    TelephonyManager telephonyManager;
    PermissionUtils permissionUtils;

    PromptDialog promptDialog;
    boolean autoLogin = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //试图初始化
        initView();
        //参数初始化
        initParams();
        //设置监听
        setListener();
        //获取权限
//        permissionUtils = new PermissionUtils(this,this,null,null);
//        permissionUtils.getPermission();

    }

    private void initView() {
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_signin = findViewById(R.id.bt_signin);
    }

    private void initParams() {
        promptDialog = new PromptDialog(this);
        permissionUtils = new PermissionUtils(this,LoginActivity.this,null,null);
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //用户信息缓存
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", ""); //姓名
        String password = sharedPreferences.getString("password", ""); //密码
        autoLogin = sharedPreferences.getBoolean("isAuto",false);
        if (name != "" || password != "") { //不为空，即有缓存，从缓存提取
            input_name.setText(name);
            input_password.setText(password);
            if (permissionUtils.canGoNextstep()){
                if(autoLogin){
                    promptDialog.showLoading("正在登录 ... ");
                    login();
                }else{
                    //获取权限
                    permissionUtils.getPermission();
                }
            }
        }else{
            //获取权限;
            permissionUtils.getPermission();
        }
    }

    private void setListener() {
        bt_login.setOnClickListener(this);
        bt_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                bt_signin.setClickable(false);
                bt_login.setClickable(false);
                if(permissionUtils.canGoNextstep()){
                    TelephonyManagement telephonyManagement = TelephonyManagement.getInstance();
                    TelephonyManagement.TelephonyInfo telephonyInfo = telephonyManagement.getTelephonyInfo(LoginActivity.this);
                    String simInfo = telephonyInfo.getSubscriberIdBySlotId(0)+","+telephonyInfo.getSubscriberIdBySlotId(1);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("simInfo",simInfo);
                    editor.putBoolean("isAuto",true);
                    editor.commit();
                    login();//登陆
                }else{
                    permissionUtils.getPermission();
                }
                break;
            case R.id.bt_signin:
                bt_login.setClickable(false);
                bt_signin.setClickable(false);
//                promptDialog.showLoading("加载中 ... ");
                sinup(); //注册
                Log.e("tttttt1","6666");
                break;
        }
    }

    //登陆
    private void login() {
        if (!isValidae()) { //有效性判断
            bt_login.setClickable(true);
            bt_signin.setClickable(true);
            return;
        } else {
            inputName = input_name.getText().toString().trim();
            inputPassword = input_password.getText().toString().trim();
            promptDialog.showLoading("正在登录 ... ");
            getTasksAndUserBypost(); //获取用户及任务信息
        }
    }

    //注册
    private void sinup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        //String sss1 = "";
    /*TelephonyManagement telephonyManagement = TelephonyManagement.getInstance();
    TelephonyManagement.TelephonyInfo telephonyInfo = telephonyManagement.getTelephonyInfo(LoginActivity.this);

      sss1 = telephonyInfo.isDualSIM()+"-"+telephonyInfo.getSubscriberIdBySlotId(0)+"-"+telephonyInfo.getSubscriberIdBySlotId(1);
    input_name.setText(sss1);*/

    }

    //获取用户信息和任务list
    private void getTasksAndUserBypost() {
        url = BaseUrl.BaseUrl+"getUserAndTask";
        Map<String, String> map = new HashMap<>();
        map.put("username",inputName);
        map.put("password",inputPassword);
        String gson = new Gson().toJson(map);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, gson, new ListTaskAndUserCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismiss();
                bt_login.setClickable(true);
                bt_signin.setClickable(true);
                Log.e("error"," "+e.toString());
                Toasty.warning(LoginActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(ResultForTaskAndUser<List<Task>, User> response, int i) {
                if(response.getMessage().equals("登录成功")){
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("name",inputName);
                    edit.putString("password",inputPassword);
                    edit.commit();
                    Toasty.success(LoginActivity.this,"登陆成功!",Toast.LENGTH_LONG,true).show();
                    List<Task> tasks = response.getContent();
                    if(response.getContent().size()==0){
                        Log.e("TAG"," response.getContent: "+"无数据");
                        tasks = new ArrayList<Task>();
                    }
                    user = response.getUserInfo();
                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    intent.putExtra("userName",inputName);
                    startActivity(intent);
                    finish();
                }
                else {
                    promptDialog.dismiss();
                    bt_login.setClickable(true);
                    bt_signin.setClickable(true);
                    Toasty.error(LoginActivity.this,"用户名或密码错误!",Toast.LENGTH_LONG,true).show();
                }
            }
        });
    }

    /**
     * 判断输入框输入的内容是否合法
     * @return
     */
    private boolean isValidae() {
        boolean valid = true;

        String name = input_name.getText().toString();
        String password = input_password.getText().toString();

        if (name.isEmpty() ) {
            input_name.setError("请输入用户名");
            valid = false;
        } else {
            //input_name.setError(null);
        }

        if (password.isEmpty() ) {
            input_password.setError("请输入密码");
            valid = false;
        } else {
            //input_password.setError(null);
        }

        return valid;
    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    //按键可点击
    @Override
    protected void onResume() {
        if(!autoLogin){
            promptDialog.dismissImmediately();
            super.onResume();
            Log.e("tttt","onstart");
            bt_login.setClickable(true);
            bt_signin.setClickable(true);
        }else{
            super.onResume();
        }
    }
}
