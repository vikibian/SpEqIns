package com.neu.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseUrl;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "LoginActivity";

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

    private boolean isSuccess ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {
        input_name = findViewById(R.id.input_name);
        input_password = findViewById(R.id.input_password);
        bt_login = (Button)findViewById(R.id.bt_login);
        bt_signin = findViewById(R.id.bt_signin);

        bt_login.setOnClickListener(this);
        bt_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_login:
                //先注销  方便测试
                login();

                //临时添加方便测试
//                Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
//                finish();
//                startActivity(intent);
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
//                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    Toasty.success(LoginActivity.this,"登陆成功!",Toast.LENGTH_LONG,true).show();
                    List<Task> tasks = response.getContent();
                    if(response.getContent().size()==0){
//                        Toast.makeText(LoginActivity.this,"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");
                        tasks = new ArrayList<Task>();
                    }
                    Toast.makeText(LoginActivity.this,"成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
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
