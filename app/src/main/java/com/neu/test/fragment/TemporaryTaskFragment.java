package com.neu.test.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.activity.DraftTaskActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.adapter.MyFragmentPagerAdapter;
import com.neu.test.adapter.TemporaryTaskFragmentAdapter;
import com.neu.test.entity.DoubleData;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDoubleDataCallBack;
import com.neu.test.util.BaseUrl;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.tab.QMUITab;
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;

import java.util.ArrayList;
import java.util.HashMap;
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


    public TemporaryTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temporary_task, container, false);
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

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager = view.findViewById(R.id.fragment_temp_viewpager);
        myFragmentPagerAdapter = new TemporaryTaskFragmentAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = view.findViewById(R.id.fragment_temp_tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

    }


}
