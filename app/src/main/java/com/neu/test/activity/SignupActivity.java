package com.neu.test.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
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
import com.neu.test.util.BaseUrl;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

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
//    public String signupRePassword;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if (savedInstanceState != null) {
            Log.d(TAG,"userID "+userId);
            userId = savedInstanceState.getInt(TAG);
        }

        init();
        getContent();
    }

    private void getContent() {
        signupName = signup_name.getText().toString();
//        signupEmail = signup_email.getText().toString();
        signupLoginname = signup_loginname.getText().toString();
        signupMobile = signup_mobile.getText().toString();
        signupPassword = signup_password.getText().toString();
//        signupRePassword = signup_repassword.getText().toString();

    }

    private void init() {
        signup_name = findViewById(R.id.signup_name);
//        signup_email = findViewById(R.id.signup_email);
        signup_loginname = findViewById(R.id.signup_loginname);
        signup_mobile = findViewById(R.id.signup_mobile);
        signup_password = findViewById(R.id.signup_password);
//        signup_repassword = findViewById(R.id.signup_rePassword);
        bt_signup = findViewById(R.id.bt_signup);
        textView_company = findViewById(R.id.signup_company);




        bt_signup.setOnClickListener(this);
        textView_company.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_signup:
                Log.d(TAG," onclick");
                createAccount();
                break;
            case R.id.signup_company:
                Intent intent = new Intent(SignupActivity.this,SelectCompanyActivity.class);
                startActivityForResult(intent,selectFlag);
                break;
        }
    }

    private void createAccount() {
        if(isValidate()){
            //上传数据
            getDataByPost();


        }else{
            signupFailed();
            return;
        }
    }

    private void getDataByPost() {
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

//        Map<String, String> usermap = new HashMap<>();
//        usermap.put("user",user);


        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(user), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e("error"," "+e.getMessage());
//                Toast.makeText(LoginActivity.this,"客官，网络不给力",Toast.LENGTH_LONG).show();
                Toasty.warning(SignupActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("注册成功")){
//                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    Toasty.success(SignupActivity.this,"注册成功!",Toast.LENGTH_LONG,true).show();

                    if(response.getContent().size()==0){
                        Toast.makeText(SignupActivity.this,"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");

                    }
                    successCreated();
                    Toasty.success(getBaseContext(),"注册成功！",Toast.LENGTH_SHORT,true).show();
                } else if (response.getMessage().equals("用户名已注册")){
                    Toasty.warning(getBaseContext(), "该用户名已注册！",Toast.LENGTH_SHORT,true).show();
                } else if (response.getMessage().equals("提交失败")){
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

        //设置加载的dialog
//        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
//                R.style.Widget_AppCompat_ProgressBar_Horizontal);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Creating Account...");


        //progressDialog.show();

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

                        //progressDialog.dismiss();

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
//        String repassword = signup_repassword.getText().toString();

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

//
//        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {//|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()
//            signup_email.setError("请输入合法的用户邮箱");
//            valid = false;
//        } else {
//            signup_email.setError(null);
//        }

        if (mobile.isEmpty() || mobile.length() != 11) {//|| mobile.length() != 11
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

//        if (repassword.isEmpty()|| repassword.length() < 4 || repassword.length() > 10 || !(repassword.equals(password))) {// || repassword.length() < 4 || repassword.length() > 10 || !(repassword.equals(password))
//            signup_repassword.setError("Password Do not match");
//            valid = false;
//        } else {
//            signup_repassword.setError(null);
//        }

        return valid;
    }

    private void onSignupSuccess() {
        //bt_signup.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    private void login() {
        Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(TAG, userId);
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
}
