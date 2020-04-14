package com.neu.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.neu.test.R;
import com.neu.test.util.BaseActivity;

public class MeAboutActivity extends BaseActivity {
    private Toolbar mToolbar;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_about);

        initToolBar();
        init();
        mTextView.setText("关于软件");
    }

    private void init() {
        mTextView = findViewById(R.id.me_about_toolbar_textview);
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.me_about_toolbar);
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
}
