package com.neu.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.test.R;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    public String signupName;
    public String signupEmail;
    public String signupMobile;
    public String signupPassword;
    public String signupRePassword;

    private EditText signup_name;
    private EditText signup_email;
    private EditText signup_mobile;
    private EditText signup_password;
    private EditText signup_repassword;

    private Button bt_signup;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        init();
        getContent();
    }

    private void getContent() {
        signupName = signup_name.getText().toString();
        signupEmail = signup_email.getText().toString();
        signupMobile = signup_mobile.getText().toString();
        signupPassword = signup_password.getText().toString();
        signupRePassword = signup_repassword.getText().toString();

    }

    private void init() {
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_mobile = findViewById(R.id.signup_mobile);
        signup_password = findViewById(R.id.signup_password);
        signup_repassword = findViewById(R.id.signup_rePassword);
        bt_signup = findViewById(R.id.bt_signup);


        bt_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_signup:
                createAccount();
                break;
        }
    }

    private void createAccount() {
        if(isValidate()){

            successCreated();

        }else{
            signupFailed();
            return;
        }
    }

    private void signupFailed() {
        Toast.makeText(getBaseContext(), "用户信息填写不全，创建失败！", Toast.LENGTH_LONG).show();

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
                }, 3000);
    }

    private boolean isValidate() {
        boolean valid = true;

        String name = signup_name.getText().toString();
        String email = signup_email.getText().toString();
        String mobile = signup_mobile.getText().toString();
        String password = signup_password.getText().toString();
        String repassword = signup_repassword.getText().toString();

        if (name.isEmpty() ) {
            signup_name.setError("请输入用户名");
            valid = false;
        } else {
            signup_name.setError(null);
        }

//        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            signup_email.setError("请输入合法的用户邮箱");
//            valid = false;
//        } else {
//            signup_email.setError(null);
//        }
//
//        if (mobile.isEmpty() || mobile.length() != 10) {
//            signup_mobile.setError("请输入合法的电话号码");
//            valid = false;
//        } else {
//            signup_mobile.setError(null);
//        }
//
//        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
//            signup_password.setError("between 4 and 10 alphanumeric characters");
//            valid = false;
//        } else {
//            signup_password.setError(null);
//        }
//
//        if (repassword.isEmpty() || repassword.length() < 4 || repassword.length() > 10 || !(repassword.equals(password))) {
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
}
