package com.neu.test.activity;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.UserInfoCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.FindPassWordUtil;
import com.neu.test.util.SendMailUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class FindPassWordActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FindPassWordActivity";
    private Toolbar mToolbar;
    private TextView toolbar_textview;
    private EditText editText_loginname;

    private EditText editText_email;

    private Button button_changepassword;
    //    private TimeCount mTimeCount;
    private FindPassWordUtil findPassWordUtil ;
    private PromptDialog promptDialog;
    final int code = 405;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_word);
        promptDialog = new PromptDialog(this);
        initview();
        initToolbar();
        findPassWordUtil = new FindPassWordUtil();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.findpassword_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    private void initview() {
        toolbar_textview = findViewById(R.id.findpassword_toolbar_textview);
        toolbar_textview.setText("找回密码");

        editText_loginname = findViewById(R.id.findpassword_loginname);
        editText_email = findViewById(R.id.findpassword_email);


        editText_loginname.setHint("请输入用户名...");
        editText_email.setHint("请输入邮箱号...");


        button_changepassword = findViewById(R.id.findpassword_change);
        button_changepassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findpassword_change:
                if (!editText_loginname.getText().toString().isEmpty()){
                    if (findPassWordUtil.checkEmail(editText_email.getText().toString())){
                        getUserInfo(editText_loginname.getText().toString());
                    }else {
                        TipDialog.show(FindPassWordActivity.this,"邮箱格式不正确!", TipDialog.TYPE.WARNING);
                    }
                }else {
                    TipDialog.show(FindPassWordActivity.this,"用户名不能为空！", TipDialog.TYPE.WARNING);
                }
                break;
        }
    }


    private void getUserInfo(String loginname) {
        String url = BaseUrl.BaseUrl +"getUserInfo";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> user = new HashMap<>();
        user.put("loginname",loginname);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(user), new UserInfoCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG, "onError: "+e.toString());
                promptDialog.dismiss();
                TipDialog.show(FindPassWordActivity.this,"网络出现错误！",TipDialog.TYPE.ERROR);
            }

            @Override
            public void onResponse(Result<User> reponse, int id) {
                Log.e(TAG, "onResponse: "+reponse);
                User getUser = new User();
                if (reponse.getMessage().equals("获取数据成功")){
                    Log.e(TAG,"获取成功");
                    getUser = reponse.getContent();
                    Log.e(TAG,"邮箱："+getUser.getEMAIL().isEmpty());
                    if (!getUser.getEMAIL().isEmpty()){
                        if (getUser.getEMAIL().equals(editText_email.getText().toString())){
                            Intent intent = new Intent(FindPassWordActivity.this,FindPassWordByEmailActivity.class);
                            intent.putExtra("loginname",editText_loginname.getText().toString());
                            intent.putExtra("email",editText_email.getText().toString());
                            startActivityForResult(intent,code);
                        }else {
                            Toasty.warning(FindPassWordActivity.this,"您输入的邮箱不正确！",Toasty.LENGTH_SHORT).show();
                        }
                    }else {
                        TipDialog.show(FindPassWordActivity.this,"用户邮箱不存在！",TipDialog.TYPE.ERROR);
                    }

                }else if (reponse.getMessage().equals("用户不存在")){
                    TipDialog.show(FindPassWordActivity.this,"用户不存在！",TipDialog.TYPE.ERROR);
                }else if (reponse.getMessage().equals("操作失败")){
                    TipDialog.show(FindPassWordActivity.this,"查找用户邮箱失败！",TipDialog.TYPE.ERROR);
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
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



}
