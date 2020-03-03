package com.neu.test.fragment;


import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SidebarUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

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
    private MaterialBetterSpinner MBsp_taskType;

    ArrayAdapter<String> adapterDeviceType;
    ArrayAdapter<String> adapterDeviceQualify;
    ArrayAdapter<String> adapterDeviceChecked;
    ArrayAdapter<String> adapterDeviceUser;
    ArrayAdapter<String> adapterTaskType;

    private List<Task> tasks  = new ArrayList<Task>();;
    private SmartTable<Task> taskSmartTable;
    Column<String> checkdate;
    Column<String> deadline;
    Column<String> devclass;
    Column<String> devid;
    Column<String> latitude;
    Column<String> place;
    Column<String> result;
    Column<String> taskID;
    Column<String> tasktype;
    Column<String> danwei;
    Column<String> nextInsertTime;



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

//        if (flag_check){
//            initFragment();
//        }

        //initFragment();



        return view;
    }

    private void initFragment(List<Task> tasks) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        PanelFragment fragment = new PanelFragment(tasks);
        transaction.replace(R.id.fragment_main,fragment);
        transaction.commit();

    }

    private void initsidebar() {
        String[] deviceType = {"电梯","起重机","压力容器"};
        String[] deviceQualify = {"合格","不合格"};
        String[] deviceChecked = {"已检查","未检查","待复查"};
        String[] deviceUser = {"admin","operator"};
        String[] taskType = {"自查","复查","下派","随机"};

        adapterDeviceType = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceType);
        adapterDeviceType.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Type.setAdapter(adapterDeviceType);
        MBsp_Type.setText(deviceType[0]);

        adapterDeviceQualify = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceQualify);
        adapterDeviceQualify.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Qualify.setAdapter(adapterDeviceQualify);
        MBsp_Qualify.setText(deviceQualify[0]);

        adapterTaskType = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,taskType);
        adapterTaskType.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_taskType.setAdapter(adapterTaskType);
        MBsp_taskType.setText(taskType[0]);

//        adapterDeviceChecked = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceChecked);
//        adapterDeviceChecked.setDropDownViewResource(R.layout.my_spinner_item);
//        MBsp_Checked.setAdapter(adapterDeviceChecked);
//        MBsp_Checked.setText(deviceChecked[0]);
//
//        adapterDeviceUser = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,deviceUser);
//        adapterDeviceUser.setDropDownViewResource(R.layout.my_spinner_item);
//        MBsp_User.setAdapter(adapterDeviceUser);
//        MBsp_User.setText(deviceUser[0]);


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
        taskSmartTable = view.findViewById(R.id.result_table);

        MBsp_Type = view.findViewById(R.id.MBsp_deviceType);
        MBsp_Qualify = view.findViewById(R.id.MBsp_deviceQualify);
//        MBsp_Checked = view.findViewById(R.id.MBsp_deviceChecked);
//        MBsp_User = view.findViewById(R.id.MBsp_deviceUser);
        MBsp_taskType = view.findViewById(R.id.MBsp_taskType);

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
                //initFragment(tasks);
                isDirection_right = false;
                break;

        }

    }

    private void getSearchedData() {
        Log.e(TAG," Type : "+MBsp_Type.getText());
        Log.e(TAG," Qualify : "+MBsp_Qualify.getText());

        String hege = new String();
        if (MBsp_Qualify.getText().equals("合格")){
            hege = "1";
        }else if(MBsp_Qualify.getText().equals("不合格")){
            hege = "-1";
        }else{
            hege = "0";
        }

        String url = BaseUrl.BaseUrl+"selectUserResultServlet";
        Map<String, String> searchmap = new HashMap<>();
        searchmap.put("LOGINNAME",LoginActivity.inputName);
        searchmap.put("DEVCLASS","3000");
        searchmap.put("RESULT",hege);
        searchmap.put("TASKTYPE",MBsp_taskType.getText().toString());
//        searchmap.put("taskID","1affb4ca-1b34-4d99-9222-5ce1ed62afa5");
//        searchmap.put("DEVID","123456");

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
                System.out.println(e.toString());
                Log.e(TAG," error: "+e.toString());
                Toasty.warning(getContext(),"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("获取任务成功")){
//                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_LONG).show();
                    Toasty.success(getContext(),"搜索数据成功!",Toast.LENGTH_LONG,true).show();

                    if(response.getContent().size()==0){
                        Toast.makeText(getContext(),"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");

                    }else {
                        Log.e("TAG"," response.getContent: "+"有数据");
                        tasks = response.getContent();
                        initTable(tasks);
                        Log.e("TAG"," Content: "+tasks.toString());
                        Log.e("TAG"," 接收task: "+tasks.size());
                    }
                    Toast.makeText(getContext(),"成功",Toast.LENGTH_LONG).show();
                    //下面是显示搜索结果 并收起侧滑界面  要放在网络post请求之后

                    //initFragment(tasks);
                    isDirection_right = false;
                    flag_check=true;
                }
                else {
                    Toasty.error(getContext(),"搜索数据失败!",Toast.LENGTH_LONG,true).show();

                }
            }
        });
    }

    private void initTable( List<Task> tasksList) {
        checkdate = new Column<String>("检查日期","CHECKDATE");
        checkdate.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                Toast.makeText(getContext(),"点击了"+value+" "+position,Toast.LENGTH_SHORT).show();
                jumpToShowSearchedResultFragment(position);
            }
        });

        deadline = new Column<String>("截止日期","DEADLINE");
        deadline.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        devclass = new Column<String>("设备类型","DEVCLASS");
        devclass.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        devid = new Column<String>("设备ID","DEVID");
        devid.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        latitude = new Column<String>("纬度","LATITUDE");
        latitude.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        place = new Column<String>("地址","PLACE");
        place.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        result = new Column<String>("检查结果","RESULT");
        result.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        taskID = new Column<String>("任务ID","TASKID");
        taskID.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        tasktype = new Column<String>("任务类型","TASKTYPE");
        tasktype.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });
        danwei = new Column<String>("检查单位","USEUNITNAME");
        danwei.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        nextInsertTime = new Column<String>("下次检查时间","NEXT_INSSEIFTIME");
        nextInsertTime.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        final TableData<Task> tableData = new TableData<Task>("测试标题", tasksList,checkdate,
                deadline,devclass, devid, latitude, place,result, taskID, tasktype, danwei);
        taskSmartTable.getConfig().setShowTableTitle(false);
        taskSmartTable.getConfig().setShowXSequence(false);
        taskSmartTable.setTableData(tableData);

//        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
        taskSmartTable.getConfig().setMinTableWidth(1024);       //设置表格最小宽度
        FontStyle style = new FontStyle();
        style.setTextSize(30);
        taskSmartTable.setZoom(true);
        taskSmartTable.getConfig().setContentStyle(style);       //设置表格主题字体样式
        taskSmartTable.getConfig().setColumnTitleStyle(style);   //设置表格标题字体样式
        taskSmartTable.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {     //设置隔行变色
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if(cellInfo.row%2 ==1) {
                    return ContextCompat.getColor(getContext(), R.color.grey);      //需要在 app/res/values 中添加 <color name="tableBackground">#d4d4d4</color>
                }else{
                    return TableConfig.INVALID_COLOR;
                }
            }
        });
        taskSmartTable.getConfig().setMinTableWidth(1024);   //设置最小宽度

    }

    private void jumpToShowSearchedResultFragment(int position) {
        Bundle bundle = new Bundle();
        Log.e(TAG,"发送数据显示："+tasks.get(position).getCHECKDATE());
        Log.e(TAG,"发送数据显示："+position);
        Log.e(TAG,"发送数据显示："+tasks.size());
        bundle.putSerializable("tasks",tasks.get(position));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        ShowSearchedResultFragment showSearchedResultFragment = new ShowSearchedResultFragment();
        showSearchedResultFragment.setArguments(bundle);
        transaction.replace(R.id.fl_content,showSearchedResultFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();

    }



    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG," onStart");
        //Log.e(TAG," tasks.size: "+tasks.size());

//        View view = (View) LayoutInflater.from(getContext()).inflate(R.layout.fragment_panel, null, false);
//
//        //初始化
//        taskSmartTable = view.findViewById(R.id.table);
        if (tasks.size()!=0){
            initTable(tasks);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG," onResume");

    }



}
