package com.neu.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperTextView;
import com.neu.test.R;

import top.androidman.SuperButton;

public class MeInformActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView toolbar_TextView;
    private SuperTextView stv_name;
    private SuperTextView stv_email;
    private SuperTextView stv_telephone;
    private SuperButton sb_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_inform);

        initToolBar();
        init();
        toolbar_TextView.setText("个人信息");
        showText();
    }

    private void showText() {
        stv_name.setCenterString("admin");
        stv_email.setCenterString("123");
        stv_telephone.setCenterString("123");
    }

    private void init() {
        toolbar_TextView = findViewById(R.id.me_inform_toolbar_textview);
        stv_name = findViewById(R.id.meinform_stv_name);
        stv_email = findViewById(R.id.meinform_stv_emai);
        stv_telephone = findViewById(R.id.meinform_stv_telephone);
        sb_back = findViewById(R.id.meinform_sb_back);

        sb_back.setOnClickListener(this);
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.me_inform_toolbar);
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
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.meinform_sb_back:
                setResult(RESULT_OK);
                this.finish();

                break;
            default:
                break;
        }
    }
}
