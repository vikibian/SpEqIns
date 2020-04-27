package com.neu.test.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;

import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.FindPassWordUtil;
import com.neu.test.util.SendMailUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class FindPassWordByEmailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "FindPassWordByEmailActi";
    private Toolbar mToolbar;
    private TextView toolbar_textview;

    private EditText editText_newpasswd;
    private ImageView iv_showNewPassword;//密码是否明文显示
    private Boolean showNewPassword = true;
    private EditText editText_varcode;
    private Button button_getVarCode;
    private Button button_submit;
    private TimeCount mTimeCount;
    private String loginname = "";
    private String email = "";
    private FindPassWordUtil findPassWordUtil;
    private String code ;
    private PromptDialog promptDialog;
    private boolean isSuccess ;
    private final int resetCount = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word_by_email);

        mTimeCount = new TimeCount(300000, 1000);
        promptDialog = new PromptDialog(this);
        Intent intent = getIntent();
        loginname = intent.getStringExtra("loginname");
        email = intent.getStringExtra("email");
        Log.e(TAG,"loginname:"+loginname);
        Log.e(TAG,"email:"+email);
        findPassWordUtil = new FindPassWordUtil();
        initview();
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.findpasswordbyemail_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    private void initview() {
        toolbar_textview = findViewById(R.id.findpasswordbyemail_toolbar_textview);
        toolbar_textview.setText("找回密码");

        editText_newpasswd = findViewById(R.id.findpasswordbyemail_newpassword);
        editText_newpasswd.setHint("请输入密码...");
        iv_showNewPassword = findViewById(R.id.findpasswordbyemail_showNewPassword);
        iv_showNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
        iv_showNewPassword.setOnClickListener(this);

        editText_varcode = findViewById(R.id.findpasswordbyemail_varcode);
        editText_varcode.setHint("请输入验证码...");

        button_getVarCode = findViewById(R.id.findpasswordbyemail_getvarcode);
        button_getVarCode.setOnClickListener(this);

        button_submit = findViewById(R.id.findpasswordbyemail_change);
        button_submit.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findpasswordbyemail_showNewPassword:
                if (showNewPassword) {// 显示密码
                    iv_showNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_grey_500_24dp));
                    editText_newpasswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    editText_newpasswd.setSelection(editText_newpasswd.getText().toString().length());
                    showNewPassword = !showNewPassword;
                } else {// 隐藏密码
                    iv_showNewPassword.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
                    editText_newpasswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    editText_newpasswd.setSelection(editText_newpasswd.getText().toString().length());
                    showNewPassword = !showNewPassword;
                }
                break;
            case R.id.findpasswordbyemail_getvarcode:
                mTimeCount.start();
                sendCodeToEmail(email);
                break;
            case R.id.findpasswordbyemail_change:
                Log.e(TAG,"密码格式："+editText_newpasswd.getText().toString());
                Log.e(TAG,"密码格式："+(!editText_newpasswd.getText().toString().isEmpty()));
                Log.e(TAG,"密码格式："+(!(editText_newpasswd.getText().toString().length() < 4)));
                Log.e(TAG,"密码格式："+(!(editText_newpasswd.getText().toString().length() > 10)));
                Log.e(TAG,"密码格式："+(findPassWordUtil.checkPassword(editText_newpasswd.getText().toString())));
                if ((!editText_newpasswd.getText().toString().isEmpty())
                        && (!(editText_newpasswd.getText().toString().length() < 4))
                        && (!(editText_newpasswd.getText().toString().length() > 10))
                        && (findPassWordUtil.checkPassword(editText_newpasswd.getText().toString()))){
                    Log.e(TAG,"code:"+code);
                    if (!mTimeCount.isCountFinish()){
                        if (editText_varcode.getText().toString().equals(code)){
                            resetPassword(loginname,editText_newpasswd.getText().toString());
                        }else {
                            TipDialog.show(FindPassWordByEmailActivity.this,"验证码不正确!", TipDialog.TYPE.WARNING);
                        }
                    }else {
                        mTimeCount.isCountFinish = false;
                        TipDialog.show(FindPassWordByEmailActivity.this,"验证码已过期!", TipDialog.TYPE.WARNING);
                    }

                }else {
                    TipDialog.show(FindPassWordByEmailActivity.this,"密码格式不正确!", TipDialog.TYPE.WARNING);
                }

                break;
        }
    }

    private void resetPassword(String testinputName, String pwd) {
        promptDialog.showLoading("新密码提交中...");
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
                promptDialog.dismiss();
                TipDialog.show(FindPassWordByEmailActivity.this,"网络出现错误！",TipDialog.TYPE.ERROR);
            }

            @Override
            public void onResponse(String reponse, int i) {
                Log.e(TAG, "onResponse: "+reponse);
                JSONObject result = null;
                promptDialog.dismiss();
                try {
                    result = new JSONObject(reponse);
                    if (result.get("message").equals("更改成功")){
                        Toasty.success(getApplicationContext(),"密码修改成功！",Toasty.LENGTH_SHORT).show();
                        mTimeCount.cancel();
                        setResult(RESULT_OK);
                        finish();
                    }else if (result.get("message").equals("用户名输入错误")){
                        TipDialog.show(FindPassWordByEmailActivity.this,"出现错误！",TipDialog.TYPE.ERROR);
                    }else if (result.get("message").equals("操作失败")){
                        TipDialog.show(FindPassWordByEmailActivity.this,"修改失败！",TipDialog.TYPE.ERROR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendCodeToEmail(String email) {
        if (findPassWordUtil.isNetworkConnected(getApplicationContext())){
            code = findPassWordUtil.getRandomCode();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSuccess =  SendMailUtil.send(email,"[特种设备企业自查系统]找回密码!","您正在修改用户 "+loginname+" 的登录密码，请在规定的时间内输入以下的验证码："+code
                            +", 若不是本人操作，请及时确定账户安全。");
                    if (!isSuccess){
                        Message msg = Message.obtain();
                        msg.what = resetCount;
                        handler.sendMessage(msg);
                        TipDialog.show(FindPassWordByEmailActivity.this,"邮件发送失败！", TipDialog.TYPE.ERROR);
                    }
                }
            }).start();
        }else {
            Toast.makeText(FindPassWordByEmailActivity.this, "网络不可获取！", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case resetCount:
                    mTimeCount.setCountFinish(false);
                    button_getVarCode.setClickable(true);
                    button_getVarCode.setBackground(getResources().getDrawable(R.drawable.bg_button));
                    button_getVarCode.setText("获取验证码");
                    mTimeCount.cancel();
                    break;
            }
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        private boolean isCountFinish = false;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            button_getVarCode.setClickable(false);
            button_getVarCode.setBackground(getResources().getDrawable(R.drawable.bg_button_unselectable));
            button_getVarCode.setText(l/1000 + "秒后重新获取");
            Log.e(TAG, "onTick: "+(l/1000));
        }

        @Override
        public void onFinish() {
            button_getVarCode.setClickable(true);
            button_getVarCode.setBackground(getResources().getDrawable(R.drawable.bg_button));
            button_getVarCode.setText("获取验证码");
            isCountFinish = true;
        }

        public void setCountFinish(boolean countFinish) {
            isCountFinish = countFinish;
        }

        public boolean isCountFinish() {
            return isCountFinish;
        }
    }

}
