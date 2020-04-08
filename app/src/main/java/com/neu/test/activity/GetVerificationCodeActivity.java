package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hxb.verification.VerificationInputView;
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

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class GetVerificationCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GetVerificationCodeActi";
    private Toolbar mToolbar;
    private TextView toolbar_textview;
    private TextView content;
    private VerificationInputView verificationInputView;
    private TextView tip;
    private Button reget;


    private EventHandler eh; //事件接收器
    private TimeCount mTimeCount;

    private String phonenumber;

    private User user = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_verification_code);

        phonenumber = getIntent().getStringExtra("phone");
        user = LoginActivity.user;

        initview();
        initToolbar();

        SMSSDK.getVerificationCode("+86",phonenumber);//获取验证码
        mTimeCount = new TimeCount(60000, 1000);
        mTimeCount.start();


        verificationInputView.setListener(new VerificationInputView.Listener() {
            @Override
            public void onChange(String[] strings) {

            }

            @Override
            public void onComplete(String string) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG," 核验验证码和手机号");
                        SMSSDK.submitVerificationCode("+86",phonenumber,string);//提交验证
                    }
                }).start();
            }
        });

        initSms();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.me_getverifcode_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    private void initview() {
        toolbar_textview = findViewById(R.id.me_getverifcode_toolbar_textview);
        toolbar_textview.setText("获取验证码");

        content = findViewById(R.id.me_getverifcde_content);
        content.setText("已向 "+phonenumber+" 发送验证码，\n请输入验证码：");

        verificationInputView = findViewById(R.id.verification_input_view);

        tip = findViewById(R.id.me_getverifcde_tip);
        reget = findViewById(R.id.me_getverifcde_reget);


        reget.setOnClickListener(this);

    }

    private void initSms() {
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                        Log.e(TAG," 提交验证码成功");
                        resetPhone(user.getLOGINNAME(),phonenumber);

                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功
                        Log.e(TAG," 获取验证码成功");
                    }else {
                        TipDialog.show(GetVerificationCodeActivity.this,"重新获取验证码！",TipDialog.TYPE.SUCCESS);
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }

        };


        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_getverifcde_reget:
                SMSSDK.getVerificationCode("+86",phonenumber);//获取验证码
                mTimeCount = new TimeCount(60000, 1000);
                mTimeCount.start();
                reget.setVisibility(View.GONE);
                tip.setVisibility(View.VISIBLE);
                break;
        }
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

    private void resetPhone(String testinputName, String phone) {
        String url = BaseUrl.BaseUrl +"resetPhoneServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> user = new HashMap<>();
        user.put("LOGINNAME",testinputName);
        user.put("PHONE",phone);
        String stringuser = new Gson().toJson(user);

        Map<String,String> map = new HashMap<>();
        map.put("user",stringuser);
        Log.e(TAG, "resetphone: "+map.toString());

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
                        Toasty.success(getApplicationContext(),"修改成功").show();
                        finish();
                    }else if (result.get("message").equals("用户名输入错误")){
                        TipDialog.show(GetVerificationCodeActivity.this,"出现错误！",TipDialog.TYPE.ERROR);
                    }else if (result.get("message").equals("操作失败")){
                        TipDialog.show(GetVerificationCodeActivity.this,"修改失败！",TipDialog.TYPE.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            tip.setVisibility(View.VISIBLE);
            tip.setText(l/1000+"秒后重新获取。");
            reget.setVisibility(View.GONE);
        }

        @Override
        public void onFinish() {
            reget.setVisibility(View.VISIBLE);
            tip.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
