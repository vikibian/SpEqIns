package com.neu.test.adapter;

import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.neu.test.activity.DraftTaskActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.DoubleData;
import com.neu.test.fragment.CheckDetailsVideoFragment;
import com.neu.test.fragment.DraftTaskFragment;
import com.neu.test.fragment.YinhuanFragment;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDoubleDataCallBack;
import com.neu.test.util.BaseUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    private FragmentManager fm;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;

    private SparseArray<String> mTags = new SparseArray<>();
    private final String[] mTitles = {"任务起草","隐患上报"};
    private List<Fragment> mFragmentList = new ArrayList<>();

    public TemporaryTaskFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
      this.fm = fm;
      mFragmentList.clear();
      mFragmentList.add(new DraftTaskFragment());
      mFragmentList.add(new YinhuanFragment());
      Log.e("ttttttttt",mFragmentList.size()+"-+");
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
      Log.e("ttttttttt",position+"+");
       return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
//
//  @NonNull
//  @Override
//  public Object instantiateItem(@NonNull ViewGroup container, int position) {
//     /* if(mCurTransaction == null){
//        mCurTransaction = fm.beginTransaction();
//      }
//      final long itemId = getItemId(position);
//      String name = makeFragmentName(container.getId(),itemId);
//      Fragment fragment = fm.findFragmentByTag(name);
//      if(fragment != null){
//        mCurTransaction.attach(fragment);
//      }else{
//        fragment = getItem(position);
//        mCurTransaction.add(container.getId(),fragment,makeFragmentName(container.getId(),itemId));
//      }
//
//    Log.e("tttttttt",position+"---+");
//    return fragment;*/
//     mTags.put(position,makeFragmentName(container.getId(),position));
//     return super.instantiateItem(container,position);
//  }
//
//  @Override
//  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//    /*if(mCurTransaction == null){
//      mCurTransaction = fm.beginTransaction();
//    }
//    mCurTransaction.detach((Fragment) object);*/
//    mTags.remove(position);
//    super.destroyItem(container,position,object);
//  }
//
//
//  public String makeFragmentName(int viewId,long position){
//      return "android:switcher:"+viewId+":"+position;
//  }
}
