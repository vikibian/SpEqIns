package com.neu.test.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class SignupActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignupActivity";
    private static int userId = 0;
    private static String result = " ";//Post请求返回的结果

    final int selectFlag = 111;
    private String company ="";

    public String signupName;
//    public String signupEmail;
    public String signupLoginname;
    public String signupMobile;
    public String signupPassword;
    public String signupRePassword;

    private EditText signup_name;
//    private EditText signup_email;
    private EditText signup_loginname;
    private EditText signup_mobile;
    private EditText signup_password;
    private EditText signup_repassword;


    private Button bt_signup;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private TextView textView_company;
    private EditText ed_validatenum;
    private Button bt_getvalidatenum;
    private EventHandler eh; //事件接收器
    private TimeCount mTimeCount;
    private boolean isGetValidatenum = false;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        promptDialog = new PromptDialog(this);
        init();
        getContent();
        initSms();
    }

    private void initSms() {
        eh = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                        Log.e(TAG," 提交验证码成功");
//                        if (isGetValidatenum){
//                            createAccount();
//                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createAccount();
                            }
                        });
//                        createAccount();
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){ //获取验证码成功
                        Log.e(TAG," 获取验证码成功");
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){ //返回支持发送验证码的国家列表

                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }

        };


        SMSSDK.registerEventHandler(eh); //注册短信回调
    }


    private void getContent() {
        signupName = signup_name.getText().toString();
//        signupEmail = signup_email.getText().toString();
        signupLoginname = signup_loginname.getText().toString();
        signupMobile = signup_mobile.getText().toString();
        signupPassword = signup_password.getText().toString();
        signupRePassword = signup_repassword.getText().toString();

    }

    private void init() {
        signup_name = findViewById(R.id.signup_name);
//        signup_email = findViewById(R.id.signup_email);
        signup_loginname = findViewById(R.id.signup_loginname);
        signup_mobile = findViewById(R.id.signup_mobile);
        signup_password = findViewById(R.id.signup_password);
        signup_repassword = findViewById(R.id.signup_repassword);
        bt_signup = findViewById(R.id.bt_signup);
        textView_company = findViewById(R.id.signup_company);
        ed_validatenum = findViewById(R.id.signup_validateNum);
        bt_getvalidatenum = findViewById(R.id.signup_getvalidateNum);
        mTimeCount = new TimeCount(60000, 1000);


        bt_signup.setOnClickListener(this);
        textView_company.setOnClickListener(this);
        bt_getvalidatenum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_signup:
                if (!signup_mobile.getText().toString().trim().equals("")
                        &&checkTel(signup_mobile.getText().toString().trim())) {
                    Log.e(TAG," 手机号码合格");
                    if (!ed_validatenum.getText().toString().trim().equals("")) {//checkTel(signup_mobile.getText().toString().trim())
//                            SMSSDK.submitVerificationCode("+86",signup_mobile.getText().toString().trim(),ed_validatenum.getText().toString().trim());//提交验证
                        Log.e(TAG,"验证码合格");
                        if (isValidate()) {
                            Log.e(TAG, " 其他合格");
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e(TAG," 核验验证码和手机号");
                                    SMSSDK.submitVerificationCode("+86",signup_mobile.getText().toString(),ed_validatenum.getText().toString());//提交验证
                                }
                            }).start();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"手机号输入不正确",Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG," onclick");

                break;
            case R.id.signup_company:
                Intent intent = new Intent(SignupActivity.this,SelectCompanyActivity.class);
                startActivityForResult(intent,selectFlag);
                break;
            case R.id.signup_getvalidateNum:
                SMSSDK.getSupportedCountries();//获取短信目前支持的国家列表
                if(!signup_mobile.getText().toString().trim().equals("")){
                    if (checkTel(signup_mobile.getText().toString().trim())) {
                        SMSSDK.getVerificationCode("+86",signup_mobile.getText().toString());//获取验证码
                        mTimeCount.start();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void createAccount() {
        getDataByPost();
    }

    private void getDataByPost() {
        promptDialog.showLoading("用户注册中...");
        String url = BaseUrl.BaseUrl +"registerServlet";
        Log.d(TAG,"POST url: "+url);

        User userentity = new User();
        userentity.setLOGINNAME(signup_loginname.getText().toString());
        userentity.setUSERNAME(signup_name.getText().toString());
        userentity.setUSEUNITNAME(textView_company.getText().toString());
        userentity.setPHONE(signup_mobile.getText().toString());
        userentity.setLOGINPWD(signup_password.getText().toString());
//        map.put("USERID",signup_name.getText().toString());
//        map.put("LOGINNAME",signup_name.getText().toString());
//        map.put("LOGINPWD",signup_password.getText().toString());
//        map.put("UNTITD","");
//        map.put("DEVCLASS","");
//        Log.e(TAG,"map: "+ map.toString());
        Map<String, User> map = new HashMap<>();
        map.put("user",userentity);
        String user = new Gson().toJson(map);
        Log.e(TAG,"map: "+ user.toString());



        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, user, new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e("error"," "+e.getMessage());
//                Toast.makeText(LoginActivity.this,"客官，网络不给力",Toast.LENGTH_LONG).show();
                promptDialog.dismiss();
                Toasty.warning(SignupActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("注册成功")){
//                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    Toasty.success(SignupActivity.this,"注册成功!",Toast.LENGTH_LONG,true).show();
                    successCreated();

                } else if (response.getMessage().equals("用户名已注册")){
                    promptDialog.dismiss();
                    Toasty.warning(getBaseContext(), "该用户名已注册！",Toast.LENGTH_SHORT,true).show();
                } else if (response.getMessage().equals("提交失败")){
                    promptDialog.dismiss();
                    Toasty.warning(getBaseContext(), "创建用户信息失败！",Toast.LENGTH_SHORT,true).show();
                }

            }
        });


    }


    private void signupFailed() {
        Toasty.warning(getBaseContext(), "用户信息填写不全，创建失败！", Toast.LENGTH_LONG,true).show();
        bt_signup.setEnabled(true);
    }

    private void successCreated() {
        bt_signup.setEnabled(false);

        //设置加载的bar
        LinearLayout signupLinear = findViewById(R.id.signup_lin);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;

        progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(params);

        progressBar.setVisibility(View.VISIBLE);
        signupLinear.addView(progressBar);

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        login();
                    }
                }, 300);
    }

    private boolean isValidate() {
        boolean valid = true;

        String name = signup_name.getText().toString();
//        String email = signup_email.getText().toString();
        String loginname = signup_loginname.getText().toString();
        String mobile = signup_mobile.getText().toString();
        String password = signup_password.getText().toString();
        String repassword = signup_repassword.getText().toString();

        if (name.isEmpty() ) {
            signup_name.setError("请输入姓名");
            valid = false;
        } else {
            signup_name.setError(null);
        }

        if (loginname.isEmpty()){
            Toast.makeText(getApplicationContext(),"请选择单位",Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (loginname.isEmpty() ) {
            signup_loginname.setError("请输入用户名");
            valid = false;
        } else {
            signup_loginname.setError(null);
        }


        if (mobile.isEmpty() || mobile.length() != 11||!checkTel(mobile.trim())) {//|| mobile.length() != 11
            signup_mobile.setError("请输入合法的手机号码");
            valid = false;
        } else {
            signup_mobile.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {//|| password.length() < 4 || password.length() > 10
            signup_password.setError("请输入4到10位密码");
            valid = false;
        } else {
            signup_password.setError(null);
        }

        if (repassword.isEmpty()|| repassword.length() < 4 || repassword.length() > 10 || !(repassword.equals(password))) {// || repassword.length() < 4 || repassword.length() > 10 || !(repassword.equals(password))
            signup_repassword.setError("两次密码不匹配");
            valid = false;
        } else {
            signup_repassword.setError(null);
        }

        return valid;
    }

    private void onSignupSuccess() {
        //bt_signup.setEnabled(true);
        promptDialog.dismiss();
        setResult(RESULT_OK, null);
        finish();
    }

    private void login() {
        promptDialog.dismiss();
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);

        finish();
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    /**
     * 正则匹配手机号码
     * @param tel
     * @return
     */
    public boolean checkTel(String tel){
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        outState.putInt(TAG, userId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == selectFlag){
                company = data.getStringExtra("company");
                Log.e(TAG,"得到数据："+company);
                textView_company.setText(company);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
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
            bt_getvalidatenum.setClickable(false);
            bt_getvalidatenum.setText("重新获取("+l/1000 +")");
        }

        @Override
        public void onFinish() {
            bt_getvalidatenum.setClickable(true);
            bt_getvalidatenum.setText("获取验证码");
        }
    }
}
