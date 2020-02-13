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
import com.neu.test.net.netBeans.LoginBean;
import com.neu.test.net.OkHttp;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "LoginActivity";

    public static String inputName;
    public static String inputPassword ;//修改密码界面会用到  先为该参数赋值 方便后面的测试
    public static int userid ;//在修改密码界面会用的到

    public static String testinputName = "test";
    public static String testinputPassword = "test";//修改密码界面会用到  先为该参数赋值 方便后面的测试
    public static int testuserid = 1;//在修改密码界面会用的到

    private EditText input_name;
    private EditText input_password;
    private Button bt_login;
    private Button bt_signin;

    private boolean isSuccess ;

//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if(msg.what == 1){
//                String s = (String) msg.obj;
//                Log.d(TAG,"msg.obj: "+ s);
//                if(s.equals("登录成功")){
//                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
//                    finish();
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
//                    input_name.setText(null);
//                    input_password.setText(null);
//                }
//            }
//        }
//    };


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
                //login();

                //临时添加方便测试
                Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
                finish();
                startActivity(intent);
                break;
            case R.id.bt_signin:
                sinup();
//                Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
//                finish();
//                startActivity(intent);
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
//            new Thread(){
//                @Override
//                public void run() {
//                    InternetRequest(inputName,inputPassword);
//                }
//            }.start();

            getDataBypost();
        }


    }

    private void getDataBypost() {
        String url = SplashActivity.baseurl+"/loginServlet";
        Log.d(TAG,"POST url: "+url);


        JSONObject user = new JSONObject();
        try {
            user.put("username",inputName);
            user.put("password",inputPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG,"user: "+ user.toString());
        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, user, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG,"error: "+e.toString());
                Log.e(TAG,"i: "+i);
            }

            @Override
            public void onResponse(String s, int i) {
                //测试Gson
                Gson gson = new Gson();
                LoginBean loginBean = gson.fromJson(s, LoginBean.class);

                LoginBean.ResultBean resultBean = loginBean.getResult();
                LoginBean.ResultBean.ContentBean contentBean = resultBean.getContent();

                userid = contentBean.getUSERID();
                String message = resultBean.getMessage();


//                Log.d(TAG,"s: " +s);
//                String  messageJson = " ";
//                try {
//                    JSONObject resultJson = new JSONObject(s);
//                    JSONObject userJson = (JSONObject) resultJson.get("result");
//                    Log.d(TAG,"userJson:"+userJson.toString());
//                    messageJson = userJson.getString("message");
//                    Log.d(TAG,"messageJson:"+messageJson);
//
//                    //获取userid
//                    JSONObject content = (JSONObject) userJson.get("content");
//                    userid = content.getInt("USERID");
//                    Log.d(TAG,"userid:"+userid);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                if(message.equals("登录成功")){
                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
                    finish();
                    startActivity(intent);
                } else if(message.equals("密码错误")){
                    Toasty.error(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG,true).show();
                    input_name.setText(null);
                    input_password.setText(null);
                }

            }
        });
    }


//    private void InternetRequest(String inputName,String inputPassword){
//        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//        //String postUrl = "http://apis.juhe.cn/mobile/get";
//        //String postUrl = "http://i2hc9f.natappfree.cc/WEB1010/LoginServlet";
//        String postUrl = SplashActivity.baseurl+"/loginServlet";
//
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("username",inputName);
//        map.put("password",inputPassword);
//        JSONObject jsonObject = new JSONObject(map);
//        Log.d(TAG,"map: "+map.toString());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,jsonObject,new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                Log.e("response-->" , jsonObject.toString());
//                try {
//                    JSONObject result = (JSONObject) jsonObject.get("result");
//                    String s1 = result.getString("message");
//                    Log.d(TAG,"s1: "+s1);
//                    Message message = Message.obtain();
//                    message.what = 1;
//                    message.obj = s1;
//                    handler.sendMessage(message);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("suc");
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("response-->" , error.getMessage());
//                System.out.println("fal");
//            }
//        }) {
//
//            @Override
//            public Map<String,String> getHeaders() throws AuthFailureError {
//                HashMap<String,String> heads = new HashMap<String,String>();
//                heads.put("Accept","application/json");
//                heads.put("Content-Type","application/json;charset=UTF-8");
//                return heads;
//            }
//        };
//        queue.add(jsonObjectRequest);
//    }


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
