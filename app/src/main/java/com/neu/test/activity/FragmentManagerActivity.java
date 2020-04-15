package com.neu.test.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.neu.test.R;
import com.neu.test.entity.Task;
import com.neu.test.fragment.CheckFragment;
import com.neu.test.fragment.MeFragment;
import com.neu.test.fragment.SearchFragment;
import com.neu.test.layout.BottomBarItem;
import com.neu.test.layout.BottomBarLayout;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.PermissionUtils;

import java.util.ArrayList;
import java.util.List;


public class FragmentManagerActivity extends BaseActivity {

    private final String TAG = "FragmentManager";


    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    private List<Fragment> mFragmentList = new ArrayList<>();

    private FrameLayout mFlContent;

    public BottomBarLayout mBottomBarLayout;
    private int preposition = 0;

    private RotateAnimation mRotateAnimation;

    private List<Task> selfTasks = new ArrayList<Task>(); //自查任务
    private List<Task> reselfTasks = new ArrayList<Task>(); //复查任务
    private List<Task> kingTasks = new ArrayList<Task>(); //上级任务
    private List<Task> randomTasks = new ArrayList<Task>();  //随机任务
    private List<Task> allTasks ;  //随机任务
    String userName;

    private SimpleToolbar simpleToolbar;
    private Handler mHandler = new Handler();
    private PermissionUtils permissionUtils;
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_manager);

        initView();
        intent = getIntent();
        //获得任务
//        getTaskByPost();

        initData(0);
        initListener();
        getPersimmions();

    }


    private void initView() {

        mFlContent = (FrameLayout) findViewById(R.id.fl_content);

        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);
        //绑定自定义的toolbar组件设置toolbar上面的文字
        simpleToolbar = findViewById(R.id.simple_toolbar);
        simpleToolbar.setLeftTitleText(getResources().getString(R.string.app_name));
        simpleToolbar.setRightTitleText(LoginActivity.user.getUSERNAME());

    }






    private void initData(int position) {
        mFragmentList.clear();
        allTasks = new ArrayList<>();
        permissionUtils = new PermissionUtils(this,this,null,null);
        permissionUtils.getPermission();

        String[] taskType = {"自查","整改","下派","临时"};
        allTasks = (List<Task>) intent.getSerializableExtra("userTask");
        String all = new Gson().toJson(allTasks);
        Log.e(TAG, "initData: "+all+"  "+allTasks.size() );
        userName = intent.getStringExtra("userName");
        selfTasks.clear();
        reselfTasks.clear();
        kingTasks.clear();
        randomTasks.clear();
        for (int i=0; i<allTasks.size();i++){
            if (allTasks.get(i).getTASKTYPE().equals("自查")){
                selfTasks.add(allTasks.get(i));
            }
            else if (allTasks.get(i).getTASKTYPE().equals("整改")){
                reselfTasks.add(allTasks.get(i));
            }
            else if (allTasks.get(i).getTASKTYPE().equals("下派")){
                kingTasks.add(allTasks.get(i));
            }else {
                randomTasks.add(allTasks.get(i));
            }
        }

        //如果变换页面下的排列将下面的顺序重新排列一下即可

         CheckFragment checkFragment = new CheckFragment(selfTasks,mBottomBarLayout,taskType[0]);
         mFragmentList.add(checkFragment);


        CheckFragment reCheck = new CheckFragment(reselfTasks,mBottomBarLayout,taskType[1]);
        mFragmentList.add(reCheck);


        CheckFragment taskFrag = new CheckFragment(kingTasks,mBottomBarLayout,taskType[2]);
        mFragmentList.add(taskFrag);


        CheckFragment randomFrag = new CheckFragment(randomTasks,mBottomBarLayout,taskType[3]);
        mFragmentList.add(randomFrag);

        SearchFragment searchFragment = new SearchFragment();
        mFragmentList.add(searchFragment);


        MeFragment meFragment = new MeFragment();
        mFragmentList.add(meFragment);


        changeFragment(position); //默认显示第一页

    }


    private void initListener() {

        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition, final int currentPosition) {
                Log.i("MainActivity", "position: " + currentPosition);
                preposition = currentPosition;
                changeFragment(currentPosition);

            }

        });





        mBottomBarLayout.setUnread(0, selfTasks.size());//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, reselfTasks.size());//设置第二个页签的未读数
        mBottomBarLayout.setUnread(2, kingTasks.size());//设置第二个页签的未读数
        mBottomBarLayout.setUnread(3, randomTasks.size());//设置第二个页签的未读数


        // mBottomBarLayout.showNotify(2);//设置第三个页签显示提示的小红点

        //mBottomBarLayout.setMsg(3, "NEW");//设置第四个页签显示NEW提示文字

    }



    private void changeFragment(int currentPosition) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fl_content, mFragmentList.get(currentPosition));

        transaction.commit();

    }



    /**

     * 停止首页页签的旋转动画

     */

    private void cancelTabLoading(BottomBarItem bottomItem) {

        Animation animation = bottomItem.getImageView().getAnimation();

        if (animation != null) {

            animation.cancel();

        }

    }




    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }

            //摄像头权限
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.intent = intent;
        int currentposition = mBottomBarLayout.getCurrentItem();
        initData(currentposition);
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
//        initListener();
    }

    @Override
    public void onBackPressed() {

        MessageDialog.build(FragmentManagerActivity.this)
                .setStyle(DialogSettings.STYLE.STYLE_IOS)
                .setTitle("提示")
                .setMessage("你要退出当前应用吗？")
                .setOkButton("确定", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        FragmentManagerActivity.super.onBackPressed();
                        return false;
                    }
                })
                .setCancelButton("取消", new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        return false;
                    }
                })
                .show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MeFragment fragment = new MeFragment();

        if (requestCode== MeFragment.REQUEST_CODE_MEINFORM){
            if (resultCode ==RESULT_OK){
                transaction.replace(R.id.fl_content, fragment);
                transaction.commit();
            }
        }

        if (requestCode == MeFragment.REQUEST_CODE_ACCOUNTANDSAFE){
            if (requestCode == RESULT_OK){
                transaction.replace(R.id.fl_content, fragment);
                transaction.commit();
            }
        }

        if (requestCode == MeFragment.REQUEST_CODE_FEEDBACK){
            if (requestCode == RESULT_OK){
                transaction.replace(R.id.fl_content, fragment);
                transaction.commit();
            }
        }

        if (requestCode == MeFragment.REQUEST_CODE_ABOUT){
            if(requestCode == RESULT_OK){
                transaction.replace(R.id.fl_content,fragment);
                transaction.commit();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}