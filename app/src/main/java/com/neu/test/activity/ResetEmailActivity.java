package com.neu.test.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.util.BaseActivity;
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

public class ResetEmailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ResetEmailActivity";

    private Toolbar mToolbar;
    private TextView toolbar_TextView;
    private EditText old_email;
    private EditText new_email;
    private Button email_submit;

    private ImageView iv_showemail;//密码是否明文显示
    private ImageView iv_shownewemail;//密码是否明文显示

    private Boolean showemail = true;
    private Boolean shownewemail = true;
    private User userInfo = new User();
    private PromptDialog promptDialog;
    private EditText varcode;
    private Button button_getvarcode;
    private TimeCount mTimeCount;
    private FindPassWordUtil findPassWordUtil;
    private String code ;
    private boolean isSuccess ;
    private final int resetCount = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_email);
        mTimeCount = new TimeCount(300000, 1000);//300000
        findPassWordUtil = new FindPassWordUtil();
        promptDialog = new PromptDialog(this);
        userInfo = LoginActivity.user;

        initView();
        initToolBar();
    }

    private void initView() {
        toolbar_TextView = findViewById(R.id.me_resetemail_toolbar_textview);
        toolbar_TextView.setText("修改邮箱");

        old_email = findViewById(R.id.me_resetemail_older);
        new_email = findViewById(R.id.me_resetemail_newer);
        email_submit = findViewById(R.id.me_resetemail_submit);

        email_submit.setOnClickListener(this);


        iv_showemail = findViewById(R.id.me_resetemail_showemail);
        iv_showemail.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
        iv_showemail.setOnClickListener(this);

        iv_shownewemail = findViewById(R.id.me_resetemail_shownewemail);
        iv_shownewemail.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
        iv_shownewemail.setOnClickListener(this);


        old_email.setText(userInfo.getEMAIL());
        old_email.setEnabled(false);

        varcode = findViewById(R.id.me_resetemail_varcode);
        button_getvarcode = findViewById(R.id.me_resetemail_getvarcode);
        button_getvarcode.setOnClickListener(this);
    }

    private void initToolBar() {
        mToolbar = findViewById(R.id.me_resetemail_toolbar);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.me_resetemail_showemail:
                if (showemail) {// 显示密码
                    iv_showemail.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_grey_500_24dp));
                    old_email.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showemail = !showemail;
                } else {// 隐藏密码
                    iv_showemail.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
                    old_email.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showemail = !showemail;
                }
                break;

            case R.id.me_resetemail_shownewemail:
                if (shownewemail) {// 显示密码
                    iv_shownewemail.setImageDrawable(getResources().getDrawable(R.drawable.ic_vpn_key_grey_500_24dp));
                    new_email.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    new_email.setSelection(new_email.getText().toString().length());
                    shownewemail = !shownewemail;
                } else {// 隐藏密码
                    iv_shownewemail.setImageDrawable(getResources().getDrawable(R.drawable.ic_remove_red_eye_grey_500_24dp));
                    new_email.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    new_email.setSelection(new_email.getText().toString().length());
                    shownewemail = !shownewemail;
                }
                break;

            case R.id.me_resetemail_submit:
                if (!mTimeCount.isCountFinish()){
                    if (varcode.getText().toString().equals(code)){
                        resetEmail(userInfo.getLOGINNAME(),new_email.getText().toString());
                    }else {
                        TipDialog.show(ResetEmailActivity.this,"验证码不正确！",TipDialog.TYPE.ERROR);
                    }
                }else {
                    mTimeCount.setCountFinish(false);
                    TipDialog.show(ResetEmailActivity.this,"验证码已过期!", TipDialog.TYPE.WARNING);
                }
                break;

            case R.id.me_resetemail_getvarcode:
                if (checkEmail(new_email.getText().toString())){
                    if (!LoginActivity.user.getEMAIL().equals(new_email.getText().toString())){
                        mTimeCount.start();
                        sendCodeToEmail(new_email.getText().toString());
                    }else {
                        TipDialog.show(ResetEmailActivity.this,"新旧邮箱一样！",TipDialog.TYPE.ERROR);
                    }
                }else {
                    TipDialog.show(ResetEmailActivity.this,"邮箱格式不正确！",TipDialog.TYPE.ERROR);
                }

                break;
        }
    }

    private void resetEmail(String testinputName, String email) {
        promptDialog.showLoading("新邮箱提交中...");
        String url = BaseUrl.BaseUrl +"resetEmailServlet";
        Log.d(TAG,"POST url: "+url);
        Map<String, String> user = new HashMap<>();
        user.put("LOGINNAME",testinputName);
        user.put("EMAIL",email);
        String stringuser = new Gson().toJson(user);

        Map<String,String> map = new HashMap<>();
        map.put("user",stringuser);
        Log.e(TAG, "resetemail: "+map.toString());

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG, "onError: "+e.toString());
                promptDialog.dismiss();
                TipDialog.show(ResetEmailActivity.this,"网络出现错误！",TipDialog.TYPE.ERROR);
            }

            @Override
            public void onResponse(String reponse, int i) {
                Log.e(TAG, "onResponse: "+reponse);
                JSONObject result = null;
                promptDialog.dismiss();
                try {
                    result = new JSONObject(reponse);
                    if (result.get("message").equals("更改成功")){
                        LoginActivity.user.setEMAIL(new_email.getText().toString());
                        Toasty.success(ResetEmailActivity.this,"修改成功！",Toasty.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }else if (result.get("message").equals("用户名输入错误")){
                        TipDialog.show(ResetEmailActivity.this,"出现错误！",TipDialog.TYPE.ERROR);
                    }else if (result.get("message").equals("操作失败")){
                        TipDialog.show(ResetEmailActivity.this,"修改失败！",TipDialog.TYPE.ERROR);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendCodeToEmail(String email) {
        if (findPassWordUtil.isNetworkConnected(getApplicationContext())){
            code = findPassWordUtil.getRandomCode();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isSuccess = SendMailUtil.send(email,"[特种设备企业自查系统]修改邮箱!","您正在修改用户 "+LoginActivity.user.getLOGINNAME()+" 的邮箱，请在规定的时间内输入以下的验证码："+code
                            +", 若不是本人操作，请及时确定账户安全。");
                    if (!isSuccess){
                        Message msg = Message.obtain();
                        msg.what = resetCount;
                        handler.sendMessage(msg);
                        TipDialog.show(ResetEmailActivity.this,"邮件发送失败！", TipDialog.TYPE.ERROR);
                    }
                }
            }).start();
        }else {
            Toast.makeText(ResetEmailActivity.this, "网络不可获取！", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case resetCount:
                    mTimeCount.setCountFinish(false);
                    button_getvarcode.setClickable(true);
                    button_getvarcode.setBackground(getResources().getDrawable(R.drawable.bg_button));
                    button_getvarcode.setText("获取验证码");
                    mTimeCount.cancel();
                    break;
            }
        }
    };


    /***
     * 验证邮箱
     */
    public  boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        private boolean isCountFinish = false;

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }


        @Override
        public void onTick(long l) {
            button_getvarcode.setClickable(false);
            button_getvarcode.setText(l/1000 + "秒后重新获取");
            button_getvarcode.setBackground(getResources().getDrawable(R.drawable.bg_button_unselectable));
        }

        @Override
        public void onFinish() {
            button_getvarcode.setClickable(true);
            button_getvarcode.setText("获取验证码");
            button_getvarcode.setBackground(getResources().getDrawable(R.drawable.bg_button));
            isCountFinish = true;
        }

        public void setCountFinish(boolean countFinish) {
            isCountFinish = countFinish;
        }

        public boolean isCountFinish() {
            return isCountFinish;
        }
    }
}
