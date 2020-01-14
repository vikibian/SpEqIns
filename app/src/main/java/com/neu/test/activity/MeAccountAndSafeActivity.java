package com.neu.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.neu.test.R;

import top.androidman.SuperButton;

public class MeAccountAndSafeActivity extends AppCompatActivity implements View.OnClickListener{
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
                compare();
                break;
            default:
                break;
        }

    }

    private void compare() {
        password = me_ac_sa_password.getText().toString();
        newpassword = me_ac_sa_newpassword.getText().toString();
        confirmnewpassword = me_ac_sa_confirmnewpassword.getText().toString();
        if(newpassword.equals(confirmnewpassword)){
            Toast.makeText(this,"密码修改成功！",Toast.LENGTH_LONG).show();
            jump();
        }
    }

    private void jump() {
        setResult(RESULT_OK);
        this.finish();
    }
}
