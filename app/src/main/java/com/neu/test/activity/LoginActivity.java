package com.neu.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.neu.test.R;
import com.neu.test.entity.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static String inputName;
    public static String inputPassword;

    private EditText input_name;
    private EditText input_password;
    private Button bt_login;
    private Button bt_signin;

    private boolean isSuccess ;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                String s = (String) msg.obj;
                if(s.equals("成功")){
                    Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
                    finish();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    input_name.setText(null);
                    input_password.setText(null);
                }
            }
        }
    };
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
                login();
                break;
            case R.id.bt_signin:
                //sinup();
                Intent intent = new Intent(LoginActivity.this,FragmentManagerActivity.class);
                finish();
                startActivity(intent);
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
            new Thread(){
                @Override
                public void run() {
                    InternetRequest(inputName,inputPassword);
                }
            }.start();
        }


    }


    private void InternetRequest(String inputName,String inputPassword){
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        //String postUrl = "http://apis.juhe.cn/mobile/get";
        String postUrl = "http://i2hc9f.natappfree.cc/WEB1010/LoginServlet";

        Map<String,String> map = new HashMap<String, String>();
        map.put("username",inputName);
        map.put("password",inputPassword);
        JSONObject jsonObject = new JSONObject(map);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl,jsonObject,new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.e("response-->" , jsonObject.toString());
                try {
                    JSONObject result = (JSONObject) jsonObject.get("result");
                    String s1 =result.getString("isSuccess");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = s1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("suc");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response-->" , error.getMessage());
                System.out.println("fal");
            }
        }) {

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> heads = new HashMap<String,String>();
                heads.put("Accept","application/json");
                heads.put("Content-Type","application/json;charset=UTF-8");
                return heads;
            }
        };
        queue.add(jsonObjectRequest);
    }








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
