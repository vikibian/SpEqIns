package com.neu.test.adapter;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.neu.test.activity.DraftTaskActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.DoubleData;
import com.neu.test.fragment.CheckDetailsVideoFragment;
import com.neu.test.fragment.DraftTaskFragment;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDoubleDataCallBack;
import com.neu.test.util.BaseUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

/**
 * created by Viki on 2020/5/8
 * system login name : lg
 * created time : 21:28
 * email : 710256138@qq.com
 */
public class TemporaryTaskFragmentAdapter extends FragmentPagerAdapter {
    private final String[] mTitles = {"任务起草","隐患上报"};

    public TemporaryTaskFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new DraftTaskFragment();
        }else if (position == 1){
            return new CheckDetailsVideoFragment();
        }
        return new DraftTaskFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

}
