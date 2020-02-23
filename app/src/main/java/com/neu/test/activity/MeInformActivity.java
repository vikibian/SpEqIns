package com.neu.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperTextView;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import top.androidman.SuperButton;

public class MeInformActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "MeInformActivity";

    private Toolbar mToolbar;
    private TextView toolbar_TextView;
    private SuperTextView stv_name;
    private SuperTextView stv_email;
    private SuperTextView stv_telephone;
    private SuperButton sb_back;


    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_inform);

        initToolBar();
        init();
        toolbar_TextView.setText("个人信息");

        getUserInfoByPost(LoginActivity.inputName);


        //showText();
    }

    private void getUserInfoByPost(String testinputName) {
        String url = BaseUrl.BaseUrl +"getUserInfo";
        Log.d(TAG,"POST url: "+url);

        Map<String, String> user = new HashMap<>();
        user.put("username",testinputName);

        Log.e(TAG,"user: "+ user.toString());
        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(user), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e("error"," "+e.getMessage());
//                Toast.makeText(LoginActivity.this,"客官，网络不给力",Toast.LENGTH_LONG).show();
                Toasty.warning(MeInformActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();

            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if (response.getMessage().equals("获取数据成功")) {
                    Log.d(TAG," 获取数据成功");
                    showText();
                } else if (response.getMessage().equals("操作失败")){
                    Toasty.error(MeInformActivity.this, "获取个人信息失败！",Toast.LENGTH_SHORT,true).show();
                } else if (response.getMessage().equals("用户不存在")){
                    Toasty.warning(MeInformActivity.this,"用户不存在！",Toast.LENGTH_LONG,true).show();
                }
            }
        });
    }

    private void showText() {
        stv_name.setCenterString(LoginActivity.inputName);
        //目前数据库里面还没有邮箱和电话
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
