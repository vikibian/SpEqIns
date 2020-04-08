package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseUrl;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ResetPasswordActivity";
    private Toolbar mToolbar;
    private TextView toolbar_TextView;

    private EditText et_passwd;
    private ImageView iv_showPassword;//密码是否明文显示
    private Boolean showPassword = true;

    private EditText et_newpasswd;
    private ImageView iv_showNewPassword;//密码是否明文显示
    private Boolean showNewPassword = true;

    private EditText et_renewpasswd;
    private ImageView iv_showReNewPassword;//密码是否明文显示
    private Boolean showReNewPassword = true;

    private Button submit;

    private User userInfo = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        Log.e(TAG, "onCreate: "+LoginActivity.inputPassword);
        userInfo = LoginActivity.user;


        initview();
        initToolBar();
    }

    private void initview() {
        toolbar_TextView = findViewById(R.id.me_resetpassword_toolbar_textview);
        toolbar_TextView.setText("修改密码");

        et_passwd = (EditText) findViewById(R.id.me_resetpassword_passwd);
        iv_showPassword = (ImageView) findViewById(R.id.me_resetpassword_showPassword);
        iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
        iv_showPassword.setOnClickListener(this);

        et_newpasswd = findViewById(R.id.me_resetpassword_newpasswd);
        iv_showNewPassword = findViewById(R.id.me_resetpassword_showNewPassword);
        iv_showNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
        iv_showNewPassword.setOnClickListener(this);

        et_renewpasswd = findViewById(R.id.me_resetpassword_renewpasswd);
        iv_showReNewPassword = findViewById(R.id.me_resetpassword_showReNewPassword);
        iv_showReNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
        iv_showReNewPassword.setOnClickListener(this);

        submit = findViewById(R.id.me_resetpassword_submit);
        submit.setOnClickListener(this);

    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.me_resetpassword_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                setResult(RESULT_OK);
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_resetpassword_showPassword:
                if (showPassword) {// 显示密码
                    iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_grey_500_24dp));
                    et_passwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_passwd.setSelection(et_passwd.getText().toString().length());
                    showPassword = !showPassword;
                } else {// 隐藏密码
                    iv_showPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
                    et_passwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_passwd.setSelection(et_passwd.getText().toString().length());
                    showPassword = !showPassword;
                }
                break;

            case R.id.me_resetpassword_showNewPassword:
                if (showNewPassword) {// 显示密码
                    iv_showNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_grey_500_24dp));
                    et_newpasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_newpasswd.setSelection(et_newpasswd.getText().toString().length());
                    showNewPassword = !showNewPassword;
                } else {// 隐藏密码
                    iv_showNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
                    et_newpasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_newpasswd.setSelection(et_newpasswd.getText().toString().length());
                    showNewPassword = !showNewPassword;
                }
                break;
            case R.id.me_resetpassword_showReNewPassword:
                if (showReNewPassword) {// 显示密码
                    iv_showReNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_grey_500_24dp));
                    et_renewpasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_renewpasswd.setSelection(et_renewpasswd.getText().toString().length());
                    showReNewPassword = !showReNewPassword;
                } else {// 隐藏密码
                    iv_showReNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
                    et_renewpasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_renewpasswd.setSelection(et_renewpasswd.getText().toString().length());
                    showReNewPassword = !showReNewPassword;
                }
                break;
            case R.id.me_resetpassword_submit:
                judgePasswordAndPost();
                break;
            default:
                break;
        }

    }

    private void judgePasswordAndPost() {
        Log.e(TAG, "judgePasswordAndPost: "+et_passwd.getText().toString());
        Log.e(TAG, "judgePasswordAndPost: "+et_newpasswd.getText().toString());
        Log.e(TAG, "judgePasswordAndPost: "+et_renewpasswd.getText().toString());
        if (et_passwd.getText().toString().equals(userInfo.getLOGINPWD())){
            if (et_newpasswd.getText().toString().isEmpty()
                    || et_newpasswd.getText().toString().length() < 4
                    || et_newpasswd.getText().toString().length() > 10) {
                Toasty.info(ResetPasswordActivity.this,"对不起，新密码格式不正确！",Toasty.LENGTH_SHORT).show();
            } else {

                if (et_newpasswd.getText().toString().equals(et_renewpasswd.getText().toString())){

                    resetPassword(userInfo.getLOGINNAME(),et_newpasswd.getText().toString());

                }else {
                    Toasty.info(ResetPasswordActivity.this,"对不起，两次新密码不匹配！",Toasty.LENGTH_SHORT).show();
                }

            }

        }else {
            Toasty.info(ResetPasswordActivity.this,"对不起，您输入的密码不正确！",Toasty.LENGTH_SHORT).show();
        }
    }

    private void resetPassword(String testinputName, String pwd) {
        String url = BaseUrl.BaseUrl +"resetPasswordServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> user = new HashMap<>();
        user.put("LOGINNAME",testinputName);
        user.put("LOGINPWD",pwd);
        String stringuser = new Gson().toJson(user);

        Map<String,String> map = new HashMap<>();
        map.put("user",stringuser);
        Log.e(TAG, "resetPassword: "+map.toString());

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG, "onError: "+e.toString());
            }

            @Override
            public void onResponse(String reponse, int i) {
                Log.e(TAG, "onResponse: "+reponse);
                JSONObject result = null;
                try {
                    result = new JSONObject(reponse);
                    if (result.get("message").equals("更改成功")){
                        TipDialog.show(ResetPasswordActivity.this,"修改密码成功！",TipDialog.TYPE.SUCCESS);
                    }else if (result.get("message").equals("用户名输入错误")){
                        TipDialog.show(ResetPasswordActivity.this,"出现错误！",TipDialog.TYPE.ERROR);
                    }else if (result.get("message").equals("操作失败")){
                        TipDialog.show(ResetPasswordActivity.this,"修改失败！",TipDialog.TYPE.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
