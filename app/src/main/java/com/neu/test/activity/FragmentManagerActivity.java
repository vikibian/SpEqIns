package com.neu.test.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.neu.test.R;
import com.neu.test.entity.Device;
import com.neu.test.fragment.CheckFragment;
import com.neu.test.fragment.MeFragment;
import com.neu.test.fragment.SearchFragment;
import com.neu.test.layout.BottomBarItem;
import com.neu.test.layout.BottomBarLayout;

import java.util.ArrayList;
import java.util.List;



public class FragmentManagerActivity extends AppCompatActivity {


    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;

    private List<Fragment> mFragmentList = new ArrayList<>();

    private FrameLayout mFlContent;

    public BottomBarLayout mBottomBarLayout;

    private RotateAnimation mRotateAnimation;
    private List<Device> dataDevice;
    private List<Device> dataRedevice;
    private List<Device> dataTask;
    private List<Device> dataRandom;
    int dataFlag ;
    int hegeFlag;

    private Handler mHandler = new Handler();



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_manager);

        initView();
        getData();
        initData();
        initListener();
        getPersimmions();

    }


    private void initView() {

        mFlContent = (FrameLayout) findViewById(R.id.fl_content);

        mBottomBarLayout = (BottomBarLayout) findViewById(R.id.bbl);

    }


    public void getData(){
        dataDevice = new ArrayList<Device>();
        dataDevice.add(new Device("电梯11","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯12","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯13","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯14","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯15","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯16","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯17","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯18","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯19","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯20","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯21","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯22","沈阳市和平区","2019-09-23"));
        dataDevice.add(new Device("电梯23","沈阳市和平区","2019-09-23"));



        dataRedevice = new ArrayList<Device>();
        dataRedevice.add(new Device("起重机12","沈阳市和平区","2019-09-23"));
        dataRedevice.add(new Device("电梯13","沈阳市沈河区","2019-09-21"));
        dataRedevice.add(new Device("起重机14","沈阳市和平区","2019-09-23"));
        dataRedevice.add(new Device("电梯15","沈阳市沈河区","2019-09-21"));
        dataRedevice.add(new Device("起重机16","沈阳市和平区","2019-09-23"));
        dataRedevice.add(new Device("电梯17","沈阳市沈河区","2019-09-21"));
        dataRedevice.add(new Device("起重机18","沈阳市和平区","2019-09-23"));
        dataRedevice.add(new Device("电梯19","沈阳市沈河区","2019-09-21"));
        dataRedevice.add(new Device("起重机20","沈阳市和平区","2019-09-23"));
        dataRedevice.add(new Device("电梯21","沈阳市沈河区","2019-09-21"));
        dataRedevice.add(new Device("起重机22","沈阳市和平区","2019-09-23"));
        dataRedevice.add(new Device("电梯23","沈阳市沈河区","2019-09-21"));


        dataTask = new ArrayList<Device>();
        dataTask.add(new Device("起重机01","沈阳市浑南区","2019-09-28"));
        dataTask.add(new Device("电梯34","沈阳市浑南区","2019-09-28"));
        dataTask.add(new Device("起重机02","沈阳市浑南区","2019-09-28"));
        dataTask.add(new Device("电梯51","沈阳市浑南区","2019-09-28"));
        dataTask.add(new Device("起重机03","沈阳市浑南区","2019-09-28"));

        dataRandom = new ArrayList<Device>();
        dataRandom.add(new Device("起重机88","沈阳市浑南区","2019-09-30"));
        dataRandom.add(new Device("电梯888","沈阳市沈河区","2019-09-30"));
        dataRandom.add(new Device("起重机89","沈阳市和平区","2019-09-30"));
        dataRandom.add(new Device("电梯899","沈阳市皇姑区","2019-09-30"));
        dataRandom.add(new Device("起重机90","沈阳市铁西区","2019-09-30"));
        dataRandom.add(new Device("起重机91","沈阳市于洪区","2019-09-30"));
        dataRandom.add(new Device("电梯991","沈阳市辽中区","2019-09-30"));




    }

    private void initData() {


        Intent intent = getIntent();
        if (intent != null) {
            Log.e("ERROR", "onstart in");
            int i;
            dataFlag = intent.getIntExtra("position",-3);
            i = intent.getIntExtra("taskType",-1);
            hegeFlag = intent.getIntExtra("hegeFlag",-3);
            if(dataFlag+hegeFlag>-1){
                switch (i) {
                    case 0:
                        if (hegeFlag == 0)
                            dataRedevice.add(dataDevice.get(dataFlag));
                        dataDevice.remove(dataFlag);
                        break;

                    case 1:
                        if (hegeFlag == 1)
                            dataRedevice.remove(dataFlag);
                        break;
                    case 2:
                        if (hegeFlag == 0)
                            dataRedevice.add(dataTask.get(dataFlag));
                        dataTask.remove(dataFlag);
                        break;
                    case 3:
                        if (hegeFlag == 0)
                            dataRedevice.add(dataRandom.get(dataFlag));
                        dataRandom.remove(dataFlag);
                        break;
                }
            }
        }

         CheckFragment checkFragment = new CheckFragment(dataDevice,mBottomBarLayout,0);
         mFragmentList.add(checkFragment);

//        SearchFragment searchFragment = new SearchFragment();
//
////        Bundle bundle2 = new Bundle();
////
////        bundle2.putString(TabFragment.CONTENT, "查询");
////
////        videoFragment.setArguments(bundle2);
//
//        mFragmentList.add(searchFragment);



        CheckFragment reCheck = new CheckFragment(dataRedevice,mBottomBarLayout,1);
        mFragmentList.add(reCheck);

        CheckFragment taskFrag = new CheckFragment(dataTask,mBottomBarLayout,2);
        mFragmentList.add(taskFrag);


        CheckFragment randomFrag = new CheckFragment(dataRandom,mBottomBarLayout,3);
        mFragmentList.add(randomFrag);

        SearchFragment searchFragment = new SearchFragment();

//        Bundle bundle2 = new Bundle();
//
//        bundle2.putString(TabFragment.CONTENT, "查询");
//
//        videoFragment.setArguments(bundle2);

        mFragmentList.add(searchFragment);


        MeFragment meFragment = new MeFragment();

//        Bundle bundle4 = new Bundle();
//
//        bundle4.putString(TabFragment.CONTENT, "我的");
//
//        meFragment.setArguments(bundle4);

        mFragmentList.add(meFragment);




        changeFragment(0); //默认显示第一页

    }


    private void initListener() {

        mBottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {

            @Override

            public void onItemSelected(final BottomBarItem bottomBarItem, int previousPosition, final int currentPosition) {

                Log.i("MainActivity", "position: " + currentPosition);



                changeFragment(currentPosition);



                if (currentPosition == 0) {

                    //如果是第一个，即首页

                    if (previousPosition == currentPosition) {

                        //如果是在原来位置上点击,更换首页图标并播放旋转动画

                        if (mRotateAnimation != null && !mRotateAnimation.hasEnded()){

                            //如果当前动画正在执行

                            return;

                        }



                        bottomBarItem.setSelectedIcon(R.mipmap.tab_loading);//更换成加载图标 setResId



                        //播放旋转动画

                        if (mRotateAnimation == null) {

                            mRotateAnimation = new RotateAnimation(0, 360,

                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,

                                    0.5f);

                            mRotateAnimation.setDuration(800);

                            mRotateAnimation.setRepeatCount(-1);

                        }

                        ImageView bottomImageView = bottomBarItem.getImageView();

                        bottomImageView.setAnimation(mRotateAnimation);

                        bottomImageView.startAnimation(mRotateAnimation);//播放旋转动画



                        //模拟数据刷新完毕

                        mHandler.postDelayed(new Runnable() {

                            @Override

                            public void run() {

                                boolean tabNotChanged = mBottomBarLayout.getCurrentItem() == currentPosition; //是否还停留在当前页签

                                bottomBarItem.setSelectedIcon(R.mipmap.tab_home_selected);//更换成首页原来选中图标

                                cancelTabLoading(bottomBarItem);

                            }

                        }, 3000);

                        return;

                    }

                }



                //如果点击了其他条目

                BottomBarItem bottomItem = mBottomBarLayout.getBottomItem(0);

                bottomItem.setSelectedIcon(R.mipmap.tab_home_selected);//更换为原来的图标

                cancelTabLoading(bottomItem);//停止旋转动画

            }

        });





        mBottomBarLayout.setUnread(0, dataDevice.size());//设置第一个页签的未读数为20
        mBottomBarLayout.setUnread(1, dataRedevice.size());//设置第二个页签的未读数
        mBottomBarLayout.setUnread(2, dataTask.size());//设置第二个页签的未读数
        mBottomBarLayout.setUnread(3, dataRandom.size());//设置第二个页签的未读数


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


//
//    @Override
//
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_demo, menu);
//
//        return true;
//
//    }
//
//
//
//    @Override
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        switch (id) {
//
//            case R.id.action_clear_unread:
//
//                mBottomBarLayout.setUnread(0, 0);
//
//                mBottomBarLayout.setUnread(1, 0);
//
//                break;
//
//            case R.id.action_clear_notify:
//
//                mBottomBarLayout.hideNotify(2);
//
//                break;
//
//            case R.id.action_clear_msg:
//
//                mBottomBarLayout.hideMsg(3);
//
//                break;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//
//    }



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

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

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
}