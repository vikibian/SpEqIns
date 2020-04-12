package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResetPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ResetPhoneActivity";
    private EditText phone;
    private EditText newphone;
    private Button change_phone;

    private TextView toolbar_textview;
    private Toolbar mToolbar;
    final int code = 404;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_phone);

        initView();
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.me_resetphone_toolbar);
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


    private void initView() {
        user = LoginActivity.user;
        toolbar_textview = findViewById(R.id.me_resetphone_toolbar_textview);
        toolbar_textview.setText("更换手机号");

        phone = findViewById(R.id.me_resetphone_phone);
        newphone = findViewById(R.id.me_resetphone_newphone);
        change_phone = findViewById(R.id.me_resetphone_change);

        phone.setText(user.getPHONE());
        phone.setTextColor(getResources().getColor(R.color.black));
        phone.setEnabled(false);

        change_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_resetphone_change:

                if (!phone.getText().toString().equals(newphone.getText().toString())){
                    if (checkPhone(newphone.getText().toString())){
                        Intent intent = new Intent(ResetPhoneActivity.this,GetVerificationCodeActivity.class);
                        intent.putExtra("phone",newphone.getText().toString());
                        startActivityForResult(intent,code);
                    }else {
                        TipDialog.show(ResetPhoneActivity.this,"您输入的手机号格式不正确",TipDialog.TYPE.ERROR);
                    }
                }else {
                    TipDialog.show(ResetPhoneActivity.this,"新老手机号一致！",TipDialog.TYPE.WARNING);
                }
                break;
        }
    }

    /**
     * 验证手机号码
     */
    public boolean checkPhone(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == code){
            if (resultCode == RESULT_OK){
                this.finish();
            }
        }

    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.e(TAG," onStart");
        // TODO Auto-generated method stub
        super.onStart();


    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG," onResume");
        initView();
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG," onpause");
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG," onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,"onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG,"onRestoreInstanceState");
    }
}
