package com.neu.test.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.allen.library.SuperTextView;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import top.androidman.SuperButton;

public class MeInformActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "MeInformActivity";

    private QMUIGroupListView groupListView ;
    private Toolbar mToolbar;
    private TextView toolbar_TextView;
    private User userInfo = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_inform);

        initview();
        initToolBar();

        userInfo = LoginActivity.user;
//        userInfo.setUSERNAME("username");
//        userInfo.setUSEUNITNAME("USEUNITNAME");
//        userInfo.setPHONE("PHONE");
//        userInfo.setIDCARD("IDCARD");
//        userInfo.setEMAIL("EMAIL");
        initGroupListview();
    }

//    private void getUserInfoByPost(String testinputName) {
//        String url = BaseUrl.BaseUrltest +"getUserInfo";
//        Log.d(TAG,"POST url: "+url);
//
//
//        Map<String, String> user = new HashMap<>();
//        user.put("username",testinputName);
//
//        Log.e(TAG,"user: "+ user.toString());
//        OkHttp okHttp = new OkHttp();
//        okHttp.postBypostString(url, new Gson().toJson(user), new UserInfomCallBack() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                Log.e(TAG, "onError: "+e.toString());
//            }
//
//            @Override
//            public void onResponse(Result<User> response, int id) {
//                if (response.getMessage().equals("获取数据成功")) {
//                    Log.d(TAG," 获取数据成功");
//                    userInfo = response.getContent();
//                    initGroupListview();
//                } else if (response.getMessage().equals("操作失败")){
//                    Toasty.error(MeInformActivity.this, "获取个人信息失败！",Toast.LENGTH_SHORT,true).show();
//                } else if (response.getMessage().equals("用户不存在")){
//                    Toasty.warning(MeInformActivity.this,"用户不存在！",Toast.LENGTH_LONG,true).show();
//                }
//            }
//        });
//    }

    private void initGroupListview() {
        int height = QMUIResHelper.getAttrDimen(this, com.qmuiteam.qmui.R.attr.qmui_list_item_height);

        QMUICommonListItemView Item0 = groupListView.createItemView(
                null,
                "用户名称",
                userInfo.getLOGINNAME(),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        Item0.setTag(0);

        QMUICommonListItemView Item1 = groupListView.createItemView(
                null,
                "姓名",
                userInfo.getUSERNAME(),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        Item1.setTag(1);

        QMUICommonListItemView Item2 = groupListView.createItemView(
                null,
                "工作单位",
                userInfo.getUSEUNITNAME(),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        Item2.setTag(2);

        QMUICommonListItemView Item3 = groupListView.createItemView(
                null,
                "手机号",
                userInfo.getPHONE(),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item3.setTag(3);

        QMUICommonListItemView Item4 = groupListView.createItemView(
                null,
                "证件号",
                userInfo.getIDCARD(),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        Item4.setTag(4);

        QMUICommonListItemView Item5 = groupListView.createItemView(
                null,
                "邮箱",
                userInfo.getEMAIL(),
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        Item5.setTag(5);

        int size = QMUIDisplayHelper.dp2px(this, 20);
        QMUIGroupListView.newSection(this)
                .setUseDefaultTitleIfNone(false) //默认标题内容
                .setUseTitleViewForSectionSpace(false) //取消标题显示
//                .setTitle("Section 2: 自定义右侧 View/红点/new 提示")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(Item0, this)
                .addItemView(Item1, this)
                .addItemView(Item2, this)
                .addItemView(Item3, this)
                .addItemView(Item4, this)
                .addItemView(Item5, this)
                //   .setOnlyShowStartEndSeparator(true)
                .addTo(groupListView);

    }

    private void initview() {
        groupListView = findViewById(R.id.me_inform_ListView);
        toolbar_TextView = findViewById(R.id.me_inform_toolbar_textview);

        toolbar_TextView.setText("个人信息");
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
//                setResult(RESULT_OK);
                this.finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        if(v instanceof QMUICommonListItemView){
            switch ((int)v.getTag()){
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    intent = new Intent(MeInformActivity.this, ResetPhoneActivity.class);
                    startActivity(intent);
                    break;
                case 4:

                    break;
                case 5:
                    intent = new Intent(MeInformActivity.this, ResetEmailActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}
