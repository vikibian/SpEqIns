package com.neu.test.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;
import com.neu.test.fragment.CheckDetailsPhotoFragment;
import com.neu.test.fragment.CheckDetailsTextFragment;
import com.neu.test.fragment.CheckDetailsVideoFragment;
import com.neu.test.fragment.TabFragment;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private static String TAG = "MyFragPageAdapter";

    private DetectionResult detectionResult;
    private Task task;
    private SearchUtil searchUtil = new SearchUtil();
    private ReloadImageAndVideo reloadImageAndVideo;

    public MyFragmentPagerAdapter(FragmentManager fm, DetectionResult detectionResult, Task task) {
        super(fm);
        this.detectionResult = detectionResult;
        this.task = task;
        reloadImageAndVideo = new ReloadImageAndVideo();
        Log.e(TAG,"检验task："+task.toString());
        Log.e(TAG,"检验detection："+detectionResult.getLOGINNAME());
        Log.e(TAG,"检验detection："+this.detectionResult.getLOGINNAME());
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
//            return new CheckDetailsPhotoFragment(reloadImageAndVideo.decodeImagePath(detectionResult.getREFJIM(),detectionResult.getLOGINNAME()));
            return new CheckDetailsPhotoFragment(detectionResult);
        } else if (position == 2) {
            return new CheckDetailsVideoFragment(
                    reloadImageAndVideo.decodeVideoPath(detectionResult.getREFJVI(),detectionResult.getLOGINNAME()));
        }else if (position==3){
            return new TabFragment();
        }
        return new CheckDetailsTextFragment(detectionResult,task);
    }

    @Override
    public int getCount() {
        return searchUtil.mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return searchUtil.mTitles[position];
    }

}
