package com.neu.test.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SuggestionActivitySaveDataUtil;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,
        OnRequestPermissionsResultCallback {
    private static String TAG = "LoginActivity";
    public final static int REQUEST_READ_PHONE_STATE = 1;
    public static String inputName;
    public static String inputPassword ;//修改密码界面会用到  先为该参数赋值 方便后面的测试
    public static int userid ;//在修改密码界面会用的到

    public static String testinputName = "test";
    public static String testinputPassword = "test";//修改密码界面会用到  先为该参数赋值 方便后面的测试
    public static String testuserid = "1";//在修改密码界面会用的到

    private EditText input_name; //输入用户名
    private EditText input_password; //输入密码
    private Button bt_login; //登录
    private Button bt_signin; //注册
    String url;  //登录的接口网址

    private SharedPreferences sharedPreferences;

    private boolean isSuccess ;
//    private SuggestionActivitySaveDataUtil saveDataUtil ;
//    public static String phoneNumber ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        saveDataUtil = new SuggestionActivitySaveDataUtil(getApplicationContext());
        init();
    }

    private void init() {
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_signin = findViewById(R.id.bt_signin);

      //缓存
        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
      String name = sharedPreferences.getString("name","");
      String password = sharedPreferences.getString("password","");
      if(name != ""|| password != ""){
        input_name.setText(name);
        input_password.setText(password);
      }


      bt_login.setOnClickListener(this);
        bt_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_signin:
                sinup();
                break;
        }
    }

    private void sinup() {
        Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    }

    private void login() {
        if(!isValidae()){

            return;
        }
        else{
            inputName = input_name.getText().toString().trim();
            inputPassword = input_password.getText().toString().trim();

            getDataBypost();
        }


    }

    private void getDataBypost() {
        url = BaseUrl.BaseUrl+"loginServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> map = new HashMap<>();
        map.put("username",inputName);
        map.put("password",inputPassword);
        Log.e(TAG,"map: "+ map.toString());


        String gson = new Gson().toJson(map);
        Log.e(TAG,"gson: "+ gson);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, gson, new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e("error"," "+e.toString());
//                Toast.makeText(LoginActivity.this,"客官，网络不给力",Toast.LENGTH_LONG).show();
                Toasty.warning(LoginActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("登录成功")){

                  SharedPreferences.Editor edit = sharedPreferences.edit();
                  edit.putString("name",inputName);
                  edit.putString("password",inputPassword);
                  edit.commit();
//                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
//                    phoneNumber = saveDataUtil.load(inputName);
                    Toasty.success(LoginActivity.this,"登陆成功!",Toast.LENGTH_LONG,true).show();
                    List<Task> tasks = response.getContent();
                    if(response.getContent().size()==0){
//                        Toast.makeText(LoginActivity.this,"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");
                        tasks = new ArrayList<Task>();
                    }
                    Toast.makeText(LoginActivity.this,"成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
//                    Intent intent = new Intent(LoginActivity.this,SuggestionActivity.class);
//                    Intent intent = new Intent(LoginActivity.this,RectifyResultActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    Log.e("TAG"," tasks: "+tasks.size());
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
}
