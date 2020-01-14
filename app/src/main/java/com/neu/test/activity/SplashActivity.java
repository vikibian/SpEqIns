package com.neu.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.test.R;

import me.wangyuwei.particleview.ParticleView;

public class SplashActivity extends AppCompatActivity {
    ParticleView mPvSplash;
    private FrameLayout frameLayout_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        initData();
        initAnimation();
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        frameLayout_splash.startAnimation(alphaAnimation);
    }

    private void initData() {
        //tv_version_name.setText("版本信息"+ getVersionName());
        mPvSplash.startAnim();

        //splash动话监听

        mPvSplash.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(intent);
                finish();
            }

        });
    }

    private void initUI() {
        frameLayout_splash=(FrameLayout) findViewById(R.id.frameLayout_splash);
        mPvSplash = (ParticleView) findViewById(R.id.pv_splash);
    }
}
