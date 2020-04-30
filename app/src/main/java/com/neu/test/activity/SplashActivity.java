package com.neu.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.EmailInfo;
import com.neu.test.entity.Result;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.EmailInfoCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;

import me.wangyuwei.particleview.ParticleView;
import okhttp3.Call;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    ParticleView mPvSplash;
    private FrameLayout frameLayout_splash;
    //public static String baseurl = "http://localhost:8080/WEB1010";
    public static String baseurl = "http://192.168.1.6:8080/WEB1010";//使用android模拟器测试tomcat服务器时需要是用电脑的ip
    public  static  EmailInfo emailInfo = new EmailInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            Intent intent = new Intent(this,SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        setContentView(R.layout.activity_splash);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getEmailInfo();
            }
        }).start();
        initUI();
        initData();
        initAnimation();
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
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

    private void getEmailInfo() {
        String url = BaseUrl.BaseUrl +"getEmailInfo";
        Log.d(TAG,"POST url: "+url);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, "", new EmailInfoCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG, "onError: "+e.toString());
            }

            @Override
            public void onResponse(Result<EmailInfo> reponse, int id) {
                Log.e(TAG, "onResponse: "+reponse);

                if (reponse.getMessage().equals("获取数据成功")){
                    emailInfo = reponse.getContent();
                    String result = new Gson().toJson(emailInfo);
                    Log.e(TAG," 结果："+result);
                }else if (reponse.getMessage().equals("不存在")){
                    Log.e(TAG,"用户不存在！");
                }else if (reponse.getMessage().equals("操作失败")){
                    Log.e(TAG,"查找用户邮箱失败！");
                }
            }

        });
    }
}
