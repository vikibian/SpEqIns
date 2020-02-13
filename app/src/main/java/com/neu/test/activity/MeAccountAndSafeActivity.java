package com.neu.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.net.OkHttp;
import com.neu.test.net.netBeans.ResetPasswordBean;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import top.androidman.SuperButton;

public class MeAccountAndSafeActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MeAccAndSafeActivity";
    private Toolbar mToolbar;
    private TextView toolbar_TextView;
    private EditText me_ac_sa_password;
    private EditText me_ac_sa_newpassword;
    private EditText me_ac_sa_confirmnewpassword;
    private SuperButton me_ac_sa_sb_confirm;

    private String password;
    private String newpassword;
    private String confirmnewpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_account_and_safe);

        initToolBar();
        init();
        toolbar_TextView.setText("账号与安全");
    }

    private void init() {
        toolbar_TextView = findViewById(R.id.me_account_safe_toolbar_textview);
        me_ac_sa_password = findViewById(R.id.me_account_safe_et_password);
        me_ac_sa_newpassword = findViewById(R.id.me_account_safe_et_newpassword);
        me_ac_sa_confirmnewpassword = findViewById(R.id.me_account_safe_et_confirmnewpassword);
        me_ac_sa_sb_confirm = findViewById(R.id.me_account_safe_sb_confirm);

        me_ac_sa_sb_confirm.setOnClickListener(this);

    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.me_account_safe_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();//back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_account_safe_sb_confirm:
                if (checkPassword()){
                    compare();
                }else {
                    Toasty.warning(this,"密码填写不正确，请重新填写！",Toast.LENGTH_SHORT,true).show();
                }

                break;
            default:
                break;
        }

    }

    private boolean checkPassword() {
        boolean valid = true;
        password = me_ac_sa_password.getText().toString();
        newpassword = me_ac_sa_newpassword.getText().toString();
        confirmnewpassword = me_ac_sa_confirmnewpassword.getText().toString();

        if (password.isEmpty() || !password.equals(LoginActivity.testinputPassword)) {
            me_ac_sa_password.setError("密码不正确");
            valid = false;
        } else {
            me_ac_sa_password.setError(null);
        }

        if (newpassword.isEmpty() || newpassword.length() < 4 || newpassword.length() > 10) {//|| password.length() < 4 || password.length() > 10
            me_ac_sa_newpassword.setError("请输入4至10个字母或数字");
            valid = false;
        } else {
            me_ac_sa_newpassword.setError(null);
        }

        if (confirmnewpassword.isEmpty() || confirmnewpassword.length() < 4 || confirmnewpassword.length() > 10 || !(confirmnewpassword.equals(password))) {
            me_ac_sa_confirmnewpassword.setError("与新密码不匹配");
            valid = false;
        } else {
            me_ac_sa_confirmnewpassword.setError(null);
        }


        return valid;
    }

    private void compare() {
        if (!password.equals(LoginActivity.testinputPassword)){
            Toasty.error(this, "密码输入错误！",Toast.LENGTH_SHORT,true).show();
        } else if(!newpassword.equals(confirmnewpassword)){
            Toasty.error(this, "新密码输入不一致！",Toast.LENGTH_SHORT,true).show();
        } else {
            getDataByPost();
        }
    }

    private void getDataByPost() {
        String url = SplashActivity.baseurl+"/resetPsdServlet";
        Log.d(TAG,"POST url: "+url);
        JSONObject jsonObject = null;

        JSONObject user = new JSONObject();
        //传入的数据不全
        try {
            user.put("USERID",LoginActivity.testuserid);
            user.put("LOGINNAME",LoginActivity.testinputName);
            user.put("LOGINPWD",me_ac_sa_newpassword.getText().toString());

            jsonObject = new JSONObject();
            jsonObject.put("user",user);
            Log.e(TAG,"jsonObject: "+ jsonObject.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, jsonObject, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                Gson gson = new Gson();
                ResetPasswordBean resetPasswordBean = gson.fromJson(s, ResetPasswordBean.class);
                ResetPasswordBean.ResultBean resultBean = resetPasswordBean.getResult();
                String message = resultBean.getMessage();


//                    Log.d(TAG,"s: " +s);
//
//                    String  messageJson = " ";
//                    try {
//                        JSONObject resultJson = new JSONObject(s);
//                        JSONObject Json = (JSONObject) resultJson.get("result");
//                        Log.d(TAG,"Json:"+Json.toString());
//                        messageJson = Json.getString("message");
//                        Log.d(TAG,"messageJson:"+messageJson);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                if (message.equals("更改成功")){
                    Toasty.success(getBaseContext(),"密码修改成功！",Toast.LENGTH_SHORT,true).show();
                    jump();
                }else if (message.equals("用户名输入错误")){
                    Toasty.warning(getBaseContext(), "用户名输入错误！",Toast.LENGTH_SHORT,true).show();
                } else if (message.equals("操作失败")){
                    Toasty.warning(getBaseContext(), "操作失败！",Toast.LENGTH_SHORT,true).show();
                }
            }
        });
    }

    private void jump() {
        setResult(RESULT_OK);
        this.finish();
    }
}
