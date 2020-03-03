package com.neu.test.adapter;

import android.util.Log;

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
import com.neu.test.util.SearchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private static String TAG = "MyFragPageAdapter";

    private DetectionResult detectionResult;
    private Task task;
    private SearchUtil searchUtil = new SearchUtil();

    public MyFragmentPagerAdapter(FragmentManager fm, DetectionResult detectionResult, Task task) {
        super(fm);
        this.detectionResult = detectionResult;
        this.task = task;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new CheckDetailsPhotoFragment(decodePhotoPath(detectionResult));
        } else if (position == 2) {
            return new CheckDetailsVideoFragment(decodeVideoPath(detectionResult));
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

    /**
     * @date : 2020/2/29
     * @time : 16:10
     * @author : viki
     * @description : 对返回的图片地址进行解析 使其变为网络地址
     */

    public List<String> decodePhotoPath(DetectionResult detectionResult){
        //detectionResult.setREFJIM("123456789100-0");//123456789100-1  1234567872-1
        List<String> photoPath = new ArrayList<>();
        String path = detectionResult.getREFJIM();
        Log.e(TAG," path: "+path);
        if (path!=" "&&path!=null){
            String[] splitpath = path.split("-");
            Log.e(TAG,"splitpath.size:"+splitpath.length);
            if (splitpath.length == 2){
                Log.e(TAG," 1: "+splitpath[0]);
                Log.e(TAG," 2: "+splitpath[1]);
                int photoNum = Integer.valueOf(splitpath[1]);
                Log.e(TAG," photoNum: "+photoNum);
                if (photoNum!=0){
                    for (int i=0;i<photoNum;i++){
                        String httpPath = "http://39.97.108.172:8080/pic/"+splitpath[0]+"Image"+i+".jpg";
                        Log.e(TAG,"httppath: "+httpPath);
                        photoPath.add(httpPath);
                    }
                }else {
                    Log.e(TAG,"没有图片");
                }
            }
        }
        return photoPath;
    }

    public List<String> decodeVideoPath(DetectionResult detectionResult){
//        detectionResult.setREFJVI("1234567881-2");//1234567881VIDEO1.mp4
        List<String> videoPath = new ArrayList<>();
        String path = detectionResult.getREFJVI();
        Log.e(TAG," path: "+path);
        if (path!=null&&path!=" "){
            String[] splitpath = path.split("-");
            if (splitpath.length == 2){
                Log.e(TAG," 1: "+splitpath[0]);
                Log.e(TAG," 2: "+splitpath[1]);
                int videoNum = Integer.valueOf(splitpath[1]);
                Log.e(TAG," videoNum: "+videoNum);
                if (videoNum!=0){
                    for (int i=0;i<videoNum;i++){
                        String httpPath = "http://39.97.108.172:8080/pic/"+splitpath[0]+"VIDEO"+i+".mp4";
                        Log.e(TAG,"httppath: "+httpPath);
                        videoPath.add(httpPath);
                    }
                }else {
                    Log.e(TAG,"没有视频");
                }
            }
        }
        return videoPath;
    }
}
