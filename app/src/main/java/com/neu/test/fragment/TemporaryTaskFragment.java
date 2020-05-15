package com.neu.test.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.activity.DraftTaskActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.adapter.MyFragmentPagerAdapter;
import com.neu.test.adapter.TemporaryTaskFragmentAdapter;
import com.neu.test.entity.DoubleData;
import com.neu.test.layout.BottomBarLayout;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDoubleDataCallBack;
import com.neu.test.util.BaseUrl;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class TemporaryTaskFragment extends Fragment  {

  private static final String TAG = "TemporaryTaskFragment";
  private QMUITabSegment tabSegment;

  private TabLayout mTabLayout;
  private ViewPager mViewPager;
  private TemporaryTaskFragmentAdapter myFragmentPagerAdapter;
  private PromptDialog promptDialog;

  private TabLayout.Tab one;
  private TabLayout.Tab two;
  private FrameLayout frameLayout;
  private List<Fragment> mFragmentList = new ArrayList<>();

  public TemporaryTaskFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view;
    view = inflater.inflate(R.layout.fragment_temporary_task, container, false);
    promptDialog = new PromptDialog(getActivity());

    initview(view);
    initTabSegment();

    return view;
  }

  private void initTabSegment() {
//        int normalColor = QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_gray_6);
//        int selectColor = QMUIResHelper.getAttrColor(getContext(), R.attr.qmui_config_color_blue);
//        tabSegment.setOuterNormalColor(normalColor);
//
//        QMUITabBuilder qmuiTabBuilder = tabSegment.tabBuilder();
//        QMUITab tab = qmuiTabBuilder.setText("12").build(getContext());
//
//        tabSegment.addTab(qmuiTabBuilder.setText("测试1").build(getContext()));
//        tabSegment.addTab(tab);
//        tabSegment.setMode(QMUITabSegment.MODE_FIXED);


  }

  private void initview(View view) {
//        tabSegment = view.findViewById(R.id.fragment_temp_tab);
    mFragmentList.clear();
    mFragmentList.add(new DraftTaskFragment());
    mFragmentList.add(new YinhuanFragment());
    frameLayout = view.findViewById(R.id.fl_content_temp);
    //使用适配器将ViewPager与Fragment绑定在一起
    mViewPager = view.findViewById(R.id.fragment_temp_viewpager);

    //将TabLayout与ViewPager绑定在一起
    mTabLayout = view.findViewById(R.id.fragment_temp_tabLayout);
    mTabLayout.setupWithViewPager(mViewPager);

    //指定Tab的位置
    one = mTabLayout.getTabAt(0);
    two = mTabLayout.getTabAt(1);
    changeFragment(0);

    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
        Log.e(TAG,"postion:"+tab.getPosition());
        changeFragment(tab.getPosition());
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });

  }

  private void changeFragment(int position) {
    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.fl_content_temp, mFragmentList.get(position));
    transaction.commit();
  }


  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    myFragmentPagerAdapter = new TemporaryTaskFragmentAdapter(getChildFragmentManager());
    mViewPager.setAdapter(myFragmentPagerAdapter);
  }
}
