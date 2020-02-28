package com.neu.test.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.DetectionItem1;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Task;
import com.neu.test.fragment.CheckDetailsPhotoFragment;
import com.neu.test.fragment.CheckDetailsTextFragment;
import com.neu.test.fragment.CheckDetailsVideoFragment;
import com.neu.test.fragment.TabFragment;
import com.neu.test.util.ResultBean;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private DetectionResult detectionResult;
    private Task task;
    private String[] mTitles = new String[]{"文字", "图片", "视频"};

    public MyFragmentPagerAdapter(FragmentManager fm, DetectionResult detectionResult, Task task) {
        super(fm);
        this.detectionResult = detectionResult;
        this.task = task;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new CheckDetailsPhotoFragment(detectionResult);
        } else if (position == 2) {
            return new CheckDetailsVideoFragment();
        }else if (position==3){
            return new TabFragment();
        }
        return new CheckDetailsTextFragment(detectionResult,task);
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
