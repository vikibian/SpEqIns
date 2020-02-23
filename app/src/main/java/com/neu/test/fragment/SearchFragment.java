package com.neu.test.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.activity.DetctionActivity;
import com.neu.test.activity.FragmentManagerActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SidebarUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "SearchFragment";

    private DrawerLayout mDrawerLayout;
    private RelativeLayout searchRightDrawer;
    private FloatingNavigationView mFloatingNavigationView;
    private TextView searchTtoolbarTextview;
    private boolean isDirection_right =false;
    private Button search_select_time;
    private Button search_bt_confirm;
    private Button search_bt_concel;
    private TextView selectStartTime;
    private TextView selectEndtTime;

    private boolean flag_check=false;//判断是否已经根据条件选择了查询项，


    private MaterialBetterSpinner MBsp_Type;
    private MaterialBetterSpinner MBsp_Qualify;
    private MaterialBetterSpinner MBsp_Checked;
    private MaterialBetterSpinner MBsp_User;

    ArrayAdapter<String> adapterDeviceType;
    ArrayAdapter<String> adapterDeviceQualify;
    ArrayAdapter<String> adapterDeviceChecked;
    ArrayAdapter<String> adapterDeviceUser;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initActivity(view);

        selectStartTime.setText(SidebarUtils.getSystemTime());
        selectEndtTime.setText(SidebarUtils.getSystemTime());

        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDirection_right){
                    mDrawerLayout.openDrawer(searchRightDrawer);
                    isDirection_right = true;
                }else {
                    mDrawerLayout.closeDrawer(searchRightDrawer);
                    isDirection_right = false;
                }
            }
        });

        initsidebar();

        if (flag_check){
            initFragment();
        }

        //initFragment();



        return view;
    }

    private void initFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PanelFragment fragment = new PanelFragment();
        transaction.replace(R.id.fragment_main,fragment);
        transaction.commit();

    }

    private void initsidebar() {
        String[] deviceType = {"电梯","起重机","压力容器"};
        String[] deviceQualify = {"合格","不合格"};
        String[] deviceChecked = {"已检查","未检查","待复查"};
        String[] deviceUser = {"admin","operator"};

        adapterDeviceType = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceType);
        adapterDeviceType.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Type.setAdapter(adapterDeviceType);
        MBsp_Type.setText(deviceType[0]);

        adapterDeviceQualify = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceQualify);
        adapterDeviceQualify.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Qualify.setAdapter(adapterDeviceQualify);
        MBsp_Qualify.setText(deviceQualify[0]);

        adapterDeviceChecked = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceChecked);
        adapterDeviceChecked.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Checked.setAdapter(adapterDeviceChecked);
        MBsp_Checked.setText(deviceChecked[0]);

        adapterDeviceUser = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceUser);
        adapterDeviceUser.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_User.setAdapter(adapterDeviceUser);
        MBsp_User.setText(deviceUser[0]);


    }

    private void initActivity(View view ) {
        searchTtoolbarTextview = view.findViewById(R.id.textview_opeator_name);
        mDrawerLayout = view.findViewById(R.id.search_drawerLayout);
        searchRightDrawer = view.findViewById(R.id.search_right_drawer);
        mFloatingNavigationView = view.findViewById(R.id.search_fb);
        //search_select_time = findViewById(R.id.sideber_admin_time);//Button点击
        search_bt_confirm = view.findViewById(R.id.sidebar_admin_confirm);
        search_bt_concel = view.findViewById(R.id.sidebar_admin_concel);
        selectStartTime = view.findViewById(R.id.select_starttime);
        selectEndtTime = view.findViewById(R.id.select_endtime);

        MBsp_Type = view.findViewById(R.id.MBsp_deviceType);
        MBsp_Qualify = view.findViewById(R.id.MBsp_deviceQualify);
        MBsp_Checked = view.findViewById(R.id.MBsp_deviceChecked);
        MBsp_User = view.findViewById(R.id.MBsp_deviceUser);

        //设置操作人员
        searchTtoolbarTextview.setText(LoginActivity.inputName);


        //search_select_time.setOnClickListener(this);//时间选择Button点击事件设置
        search_bt_confirm.setOnClickListener(this);
        search_bt_concel.setOnClickListener(this);
        selectStartTime.setOnClickListener(this);
        selectEndtTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_starttime:
                SidebarUtils.initSelectStartTime(getActivity(), selectStartTime);
                break;
            case R.id.select_endtime:
                SidebarUtils.initSelectEndTime(getActivity(),selectEndtTime);
                break;
            case R.id.sidebar_admin_confirm:
                String starttime = selectStartTime.getText().toString();
                String endtime = selectEndtTime.getText().toString();
                try {
                    if (SidebarUtils.isStartBeforeEnd(starttime,endtime)){
                        mDrawerLayout.closeDrawer(searchRightDrawer);

                        getSearchedData();
//                        //下面是显示搜索结果 并收起侧滑界面  要放在网络post请求之后
//                        initFragment();
//                        isDirection_right = false;
//                        flag_check=true;
                    }else {
                        //Toast.makeText(getContext(),"结束日期早于开始日期，请重新选择！",Toast.LENGTH_LONG).show();
                        Toasty.warning(getContext(),"结束日期早于开始日期，请重新选择！",Toast.LENGTH_LONG,true).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.sidebar_admin_concel:
                mDrawerLayout.closeDrawer(searchRightDrawer);
                initFragment();
                isDirection_right = false;
                break;

        }

    }

    private void getSearchedData() {
        String url = BaseUrl.BaseUrl+"selectItemResultServlet";
        Map<String, String> searchmap = new HashMap<>();
        searchmap.put("taskID","1affb4ca-1b34-4d99-9222-5ce1ed62afa5");
        searchmap.put("DEVID","123456");

//        searchmap.put("StartedTime","12345");//起始时间
//        searchmap.put("EndTime","12345");//终止时间
//        searchmap.put("DEVCLASS","12345");//设备种类
//        searchmap.put("DEVCLASS","12345");//合格情况
//        searchmap.put("DEVCLASS","12345");//检查情况
//        searchmap.put("DEVCLASS","12345");//检查人员

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(searchmap), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e(TAG," error: "+e.getMessage());
                Toasty.warning(getContext(),"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("获取成功")){
//                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    Toasty.success(getContext(),"搜索数据成功!",Toast.LENGTH_LONG,true).show();
                    List<Task> tasks = response.getContent();
                    if(response.getContent().size()==0){
//                        Toast.makeText(LoginActivity.this,"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");
                        tasks = new ArrayList<Task>();
                    }
                    Toast.makeText(getContext(),"成功",Toast.LENGTH_LONG).show();
                    //下面是显示搜索结果 并收起侧滑界面  要放在网络post请求之后
                    initFragment();
                    isDirection_right = false;
                    flag_check=true;
                }
                else {
                    Toasty.error(getContext(),"搜索数据失败!",Toast.LENGTH_LONG,true).show();

                }
            }
        });
    }
}
