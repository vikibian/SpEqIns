package com.neu.test.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.gson.reflect.TypeToken;
import com.neu.test.R;
import com.neu.test.activity.ShowSearchedResultActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.DragFloatActionButton;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;
import com.qmuiteam.qmui.widget.QMUIRadiusImageView2;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {
    private static String TAG = "SearchFragment";

    private DrawerLayout mDrawerLayout;
    private RelativeLayout searchRightDrawer;
//    private FloatingNavigationView mFloatingNavigationView;
    private DragFloatActionButton mFloatingNavigationView;
    private TextView searchTtoolbarTextview;
    private boolean isDirection_right =false;
    private Button search_select_time;
    private Button search_bt_confirm;
    private Button search_bt_concel;
    private TextView selectStartTime;
    private TextView selectEndtTime;
    private TextView noitem_textview;
    private LinearLayout showNoData;

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
    Column<String> ordernum;
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
    private SearchUtil searchUtil = new SearchUtil();
    private static boolean isSearch = false;
    private SimpleToolbar toolbar;
    private PromptDialog promptDialog;
    private PermissionUtils permissionUtils;
    private QMUIRadiusImageView2 globalBtn;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        SimpleToolbar simple_toolbar = activity.findViewById(R.id.simple_toolbar);
        simple_toolbar.setVisibility(View.VISIBLE);
        promptDialog = new PromptDialog(getActivity());
        permissionUtils = new PermissionUtils((AppCompatActivity) getActivity(),getActivity(),null,null);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initActivity(view);
        if (!isSearch){
            showNoData.setVisibility(View.VISIBLE);
            noitem_textview.setText("您还未开始查询数据");
        }
        toolbar.setVisibility(View.GONE);
        setLandscapeParams();
        selectStartTime.setText(SidebarUtils.getSystemTime());
        selectEndtTime.setText(SidebarUtils.getSystemTime());

        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFloatingNavigationView.isDrag()){
                    if(!isDirection_right){
                        mDrawerLayout.openDrawer(searchRightDrawer);
                        isDirection_right = true;
                    }else {
                        mDrawerLayout.closeDrawer(searchRightDrawer);
                        isDirection_right = false;
                    }
                }
            }
        });

        initsidebar();

        return view;
    }

    private void initsidebar() {
        adapterDeviceType = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner, searchUtil.deviceType);
        adapterDeviceType.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Type.setAdapter(adapterDeviceType);
        MBsp_Type.setText(searchUtil.deviceType[0]);

        adapterDeviceQualify = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,searchUtil.deviceQualify);
        adapterDeviceQualify.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_Qualify.setAdapter(adapterDeviceQualify);
        MBsp_Qualify.setText(searchUtil.deviceQualify[0]);

        adapterTaskType = new ArrayAdapter<String>(getActivity(),R.layout.my_spinner,searchUtil.taskType);
        adapterTaskType.setDropDownViewResource(R.layout.my_spinner_item);
        MBsp_taskType.setAdapter(adapterTaskType);
        MBsp_taskType.setText(searchUtil.taskType[0]);

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
        toolbar = getActivity().findViewById(R.id.simple_toolbar);
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

        showNoData = view.findViewById(R.id.search_fragment_noitem);
        noitem_textview = view.findViewById(R.id.search_fragment_noitem_textview);

        MBsp_Type = view.findViewById(R.id.MBsp_deviceType);
        MBsp_Qualify = view.findViewById(R.id.MBsp_deviceQualify);
//        MBsp_Checked = view.findViewById(R.id.MBsp_deviceChecked);
//        MBsp_User = view.findViewById(R.id.MBsp_deviceUser);
        MBsp_taskType = view.findViewById(R.id.MBsp_taskType);

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
                        isSearch = true;
                        promptDialog.showLoading("搜索数据中...");
                        if (permissionUtils.canGoNextstep()){
                            getSearchedData();
                        }else {
                            promptDialog.dismiss();
                            Toast.makeText(getContext(), "没有获取相应权限！", Toast.LENGTH_SHORT).show();
                        }

                    }else {
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

        String url = BaseUrl.BaseUrl+"selectUserResultServlet";
        Map<String, String> searchmap = new HashMap<>();
        searchmap.put("LOGINNAME",LoginActivity.inputName);
        searchmap.put("DEVCLASS",searchUtil.getTypeToDevclass(MBsp_Type.getText().toString()));
        searchmap.put("RESULT",searchUtil.getQualityToNum(MBsp_Qualify.getText().toString()));
        searchmap.put("TASKTYPE",MBsp_taskType.getText().toString());
        searchmap.put("startDate",selectStartTime.getText().toString());
        searchmap.put("endDate",selectEndtTime.getText().toString());
        //测试用数据
//        searchmap.put("LOGINNAME",LoginActivity.inputName);
//        searchmap.put("DEVCLASS","3000");
//        searchmap.put("RESULT","1");
//        searchmap.put("TASKTYPE","临时");
//        searchmap.put("startDate","2020-03-08");
//        searchmap.put("endDate","2020-04-09");
        Log.e(TAG," loginname: "+LoginActivity.inputName);
        Log.e(TAG," devclass: "+searchUtil.getTypeToDevclass(MBsp_Type.getText().toString()));
        Log.e(TAG," result: "+searchUtil.getQualityToNum(MBsp_Qualify.getText().toString()));
        Log.e(TAG," taskType: "+MBsp_taskType.getText().toString());
        Log.e(TAG," startDate: "+selectStartTime.getText().toString());
        Log.e(TAG," endDate: "+selectEndtTime.getText().toString());


        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(searchmap), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.toString());
                Log.e(TAG," error: "+e.toString());
                promptDialog.dismiss();
                Toasty.warning(getContext(),"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                promptDialog.dismiss();
                if(response.getMessage().equals("获取任务成功")){
                    Toasty.success(getContext(),"搜索数据成功!",Toast.LENGTH_LONG,true).show();
                    if(response.getContent().size()==0){
                        tasks.clear();
                        taskSmartTable.setVisibility(View.INVISIBLE);
                        Log.e("TAG"," response.getContent: "+"无数据");
                        showNoData.setVisibility(View.VISIBLE);
                        noitem_textview.setText("暂无数据！");

                    }else {
                        Log.e("TAG"," response.getContent: "+"有数据");
                        showNoData.setVisibility(View.INVISIBLE);
                        taskSmartTable.setVisibility(View.VISIBLE);
                        tasks.clear();
                        tasks = response.getContent();
                        initTable(tasks);
                        Log.e("TAG"," Content: "+tasks.toString());
                        String resultString = new Gson().toJson(tasks);
                        Log.e("TAG"," resultString: "+resultString);
                        Log.e("TAG"," 接收task: "+tasks.size());
                    }
                    //下面是显示搜索结果 并收起侧滑界面  要放在网络post请求之后
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

        String string = new Gson().toJson(tasksList);
        List<Task> testTasks = new Gson().fromJson(string,new TypeToken<List<Task>>(){}.getType());
        List<Integer> num = new ArrayList<>();
        for (int i=0;i<testTasks.size();i++){
            Log.e("TAG"," inittable 转换之前:  RESULT："+testTasks.get(i).getRESULT()+"  DEVCLASS: "+testTasks.get(i).getDEVCLASS());
            //将result的文本类型数字转换为文字
            testTasks.get(i).setRESULT(searchUtil.getHelpMapForResult().get(testTasks.get(i).getRESULT()));
            testTasks.get(i).setDEVCLASS(searchUtil.getMapdevclass().get(testTasks.get(i).getDEVCLASS()));
            Log.e("TAG"," inittable 转换之后:  RESULT："+testTasks.get(i).getRESULT()+"  DEVCLASS: "+testTasks.get(i).getDEVCLASS());
            num.add(i+1);
        }



        ordernum = new Column<String>("序号","FANGANBIANHAO");
        ordernum.setId(0);
        ordernum.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
                jumpToShowSearchedResultFragment(position);
            }
        });

        checkdate = new Column<String>("检查日期","CHECKDATE");
        checkdate.setOnColumnItemClickListener(new OnColumnItemClickListener<String>() {
            @Override
            public void onClick(Column<String> column, String value, String s, int position) {
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

        latitude = new Column<String>("设备注册码","DEVZHUCEMA");
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

        final TableData<Task> tableData = new TableData<Task>("测试标题", testTasks,ordernum,checkdate,
                deadline,devclass, devid, latitude, place,result, taskID, tasktype, danwei);
        taskSmartTable.getConfig().setShowTableTitle(false);
        taskSmartTable.getConfig().setShowXSequence(false);
        taskSmartTable.getConfig().setShowYSequence(false);
        taskSmartTable.setTableData(tableData);
//        table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
        taskSmartTable.getConfig().setMinTableWidth(256);       //设置表格最小宽度
        FontStyle style = new FontStyle();
        style.setTextSize(15);
        taskSmartTable.setZoom(true);
        taskSmartTable.getConfig().setContentStyle(style);       //设置表格主题字体样式
        taskSmartTable.getConfig().setColumnTitleStyle(style);   //设置表格标题字体样式
        taskSmartTable.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {     //设置隔行变色
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                Log.e(TAG,"SmartTAble信息："+taskSmartTable.getTableData().getColumnByID(0).getFieldName());

                if (cellInfo.col == 0){
                    Log.e(TAG,"SmartTAble信息1：row:"+cellInfo.row+" col:"+cellInfo.col);
                    cellInfo.set(taskSmartTable.getTableData().getColumnByID(0),null,String.valueOf(cellInfo.row+1),cellInfo.col,cellInfo.row);
//                    taskSmartTable.getTableData().getColumnByID(0).setWidth(512);
                }

                if(cellInfo.row%2 ==1) {
                    return ContextCompat.getColor(getContext(), R.color.lightgray);      //需要在 app/res/values 中添加 <color name="tableBackground">#d4d4d4</color>
                }else{
                    return TableConfig.INVALID_COLOR;
                }
            }
        });
//        taskSmartTable.getConfig().setMinTableWidth(1024);   //设置最小宽度

    }

    private void jumpToShowSearchedResultFragment(int position) {

        Intent intent = new Intent(getActivity(), ShowSearchedResultActivity.class);
        intent.putExtra("tasks",tasks.get(position));
        startActivity(intent);

    }



    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG," onStart");
        if (tasks.size()!=0){
            initTable(tasks);
        }else {
            if (!isSearch){
                showNoData.setVisibility(View.VISIBLE);
                noitem_textview.setText("您还未开始查询数据");
            }else {
                showNoData.setVisibility(View.VISIBLE);
                noitem_textview.setText("暂无数据！");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG," onResume");
        promptDialog.dismissImmediately();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        getActivity().setRequestedOrientation(//通过程序改变屏幕显示的方向
//                hidden ? ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
//                        : ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        if (hidden) {
             Log.e(TAG, "隐藏");
        } else {
              Log.e(TAG, "显示");
            //指定屏幕的方向为竖屏
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }

    @Override
    public void onConfigurationChanged( Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // 如果是橫屏時候
        try {
            // Checks the orientation of the screen
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.e(TAG, "onConfigurationChanged: "+ "ORIENTATION_LANDSCAPE");
                toolbar.setVisibility(View.GONE);
                setLandscapeParams();
            } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.e(TAG, "onConfigurationChanged: "+ "ORIENTATION_PORTRAIT");
                toolbar.setVisibility(View.VISIBLE);
                setPortraitParams();
            }
        } catch (Exception ex) {
        }
    }

    private void setPortraitParams(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width;
        int height;
        width = dm.widthPixels;
        height = dm.heightPixels;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) searchRightDrawer.getLayoutParams();
        params.width = width*4/5;
        searchRightDrawer.setLayoutParams(params);
    }

    private void setLandscapeParams(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width;
        int height;
        width = dm.widthPixels;
        height = dm.heightPixels;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) searchRightDrawer.getLayoutParams();
        params.width = width*3/5;
        searchRightDrawer.setLayoutParams(params);
    }
}
