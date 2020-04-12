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


import es.dmoral.toasty.Toasty;
import okhttp3.Call;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener,OnRequestPermissionsResultCallback{
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
        permissionUtils = new PermissionUtils(this,this,null,null);
        permissionUtils.getPermission();
        //getPersimmions();
    }

    private void initView() {
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_signin = findViewById(R.id.bt_signin);
    }

    private void initParams() {
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        //用户信息缓存
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", ""); //姓名
        String password = sharedPreferences.getString("password", ""); //密码
        if (name != "" || password != "") { //不为空，即有缓存，从缓存提取
            input_name.setText(name);
            input_password.setText(password);
        }
    }

    private void setListener() {
        bt_login.setOnClickListener(this);
        bt_signin.setOnClickListener(this);
    }

    //获取权限
    //region
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }

            //摄像头权限
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }

            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }
    //监听点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                if(permissionUtils.canGoNextstep()){
                    login();//登陆
                }else{
                    permissionUtils.getPermission();
                }
                break;
            case R.id.bt_signin:
                sinup(); //注册
                break;
        }
    }

    //登陆
    private void login() {
        if (!isValidae()) { //有效性判断
            return;
        } else {
            inputName = input_name.getText().toString().trim();
            inputPassword = input_password.getText().toString().trim();
            getTasksAndUserBypost(); //获取用户及任务信息
        }
    }

    //注册
    private void sinup() {
//    Intent intent = new Intentntent(LoginActivity.this, SignupActivity.class);
//    startActivity(intent);
        String sss1 = "";
        permissionUtils = new PermissionUtils(this,this,null,null);
        permissionUtils.getPermission();
    /*TelephonyManagement telephonyManagement = TelephonyManagement.getInstance();
    TelephonyManagement.TelephonyInfo telephonyInfo = telephonyManagement.getTelephonyInfo(LoginActivity.this);

      sss1 = telephonyInfo.isDualSIM()+"-"+telephonyInfo.getSubscriberIdBySlotId(0)+"-"+telephonyInfo.getSubscriberIdBySlotId(1);
    input_name.setText(sss1);*/
        input_name.setText(permissionUtils.canGoNextstep()+"");
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
                    Toast.makeText(LoginActivity.this,"成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    Log.e("TAG"," tasks: "+tasks.size());
                    Log.e("TAG"," user: "+user.getLOGINNAME());
                    Log.e("TAG"," user: "+user.getLOGINPWD());
                    Log.e("TAG"," user: "+user.getUSERNAME());
                    Log.e("TAG"," user: "+user.getUSERID());
                    Log.e("TAG"," user: "+user.getUSEUNITNAME());
                    intent.putExtra("userName",inputName);
                    startActivity(intent);
                    finish();
                }
                else {
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
}
