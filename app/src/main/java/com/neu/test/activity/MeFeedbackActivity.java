package com.neu.test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseUrl;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import top.androidman.SuperButton;

public class MeFeedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MeFeedbackActivity";
    private Toolbar mToolbar;
    private TextView mTextView;
    private EditText mEdittext;
    private Button submit;

    private String string_feedback;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_feedback);

        user = LoginActivity.user;

        initToolBar();
        init();
        mTextView.setText("意见反馈");
    }

    private void init() {
        mTextView = findViewById(R.id.me_feedback_toolbar_textview);
        mEdittext = findViewById(R.id.me_feedback_edittext);

        submit = findViewById(R.id.me_feedback_submit);
        submit.setOnClickListener(this);
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
//                setResult(RESULT_OK);
                this.finish();//back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_feedback_submit:
                string_feedback = mEdittext.getText().toString();
                postFeedback(user.getLOGINNAME(),string_feedback);

                break;
        }
    }

    private void jump() {
        setResult(RESULT_OK);
        this.finish();
    }

    private void postFeedback(String testinputName, String feedback) {
        String url = BaseUrl.BaseUrl +"postFeedbackServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> feed = new HashMap<>();
        feed.put("LOGINNAME",testinputName);
        feed.put("feedback",feedback);




        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(feed), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG, "onError: "+e.toString());
            }

            @Override
            public void onResponse(String reponse, int i) {
                Log.e(TAG, "onResponse: "+reponse);
                JSONObject result = null;
                try {
                    result = new JSONObject(reponse);
                    if (result.get("message").equals("获取反馈成功")){
                        TipDialog.show(MeFeedbackActivity.this,"提交反馈成功！",TipDialog.TYPE.SUCCESS);
                        mEdittext.setText("");
                    }else if (result.get("message").equals("用户不存在")){
                        TipDialog.show(MeFeedbackActivity.this,"出现错误！",TipDialog.TYPE.ERROR);
                    }else if (result.get("message").equals("接收反馈信息失败")){
                        TipDialog.show(MeFeedbackActivity.this,"修改失败！",TipDialog.TYPE.ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
