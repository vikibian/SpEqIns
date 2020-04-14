package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


import com.google.android.material.tabs.TabLayout;
import com.neu.test.R;
import com.neu.test.adapter.MyFragmentPagerAdapter;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;
import com.neu.test.util.BaseActivity;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

public class CheckDetailsActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;

    private DetectionItem1 detectionItem;
    private DetectionResult detectionResult;

    private TextView mTextView;
    private Toolbar mToolbar;
    private Task task;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_details);


        Intent intent = getIntent();
        detectionResult = (DetectionResult) intent.getSerializableExtra("detectionResult");
        task = (Task) intent.getSerializableExtra("task");
        initToolbar();
        //初始化视图
        initViews();

    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.check_detail_toolbar);
        mTextView = findViewById(R.id.check_detail_toolbar_textview);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        mTextView.setText("检查详情");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    private void initViews() {

        mTextView = findViewById(R.id.check_detail_toolbar_textview);

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = findViewById(R.id.viewpager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),detectionResult,task);
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        four = mTabLayout.getTabAt(3);



        //设置Tab的图标，假如不需要则把下面的代码删去
//        one.setIcon(R.mipmap.ic_launcher);
//        two.setIcon(R.mipmap.ic_launcher);
//        three.setIcon(R.mipmap.ic_launcher);
//        four.setIcon(R.mipmap.ic_launcher);


    }

    //界面左上角返回箭头
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

    //与视频播放界面相关
    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
