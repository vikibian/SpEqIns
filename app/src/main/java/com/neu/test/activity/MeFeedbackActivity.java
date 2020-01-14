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

public class MeFeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mTextView;
    private EditText mEdittext;
    private SuperButton mSuperButton;

    private String string_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_feedback);

        initToolBar();
        init();
        mTextView.setText("意见反馈");
    }

    private void init() {
        mTextView = findViewById(R.id.me_feedback_toolbar_textview);
        mEdittext = findViewById(R.id.me_feedback_edittext);
        mSuperButton = findViewById(R.id.me_feedback_sb_submit);

        mSuperButton.setOnClickListener(this);
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.me_feedback_toolbar);
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
            case R.id.me_feedback_sb_submit:
                string_feedback = mEdittext.getText().toString();
                Toast.makeText(MeFeedbackActivity.this,"意见提交成功！",Toast.LENGTH_LONG);
                jump();
                break;
        }
    }

    private void jump() {
        setResult(RESULT_OK);
        this.finish();
    }
}
