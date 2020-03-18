package com.neu.test.fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.table.TableData;
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.neu.test.R;
import com.neu.test.entity.Task;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@Deprecated
public class PanelFragment extends Fragment {
    private static String TAG = "PanelFragment";
    private List<Task> tasks;
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


    public PanelFragment(List<Task> tasks) {
        // Required empty public constructor
        this.tasks = tasks;
        Log.e(TAG, "接收task:"+tasks.size());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_panel, container, false);

        //初始化
        taskSmartTable = view.findViewById(R.id.table);
        initTable(tasks);

        return view;
    }

    private void initTable(List<Task> tasksList) {
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


//    @Override
//    protected void onSaveState(Bundle outState) {
//        super.onSaveState(outState);
//        outState.putSerializable("temporary", (Serializable) tasks);
//    }
//
//    @Override
//    protected void onRestoreState(Bundle savedInstanceState) {
//        super.onRestoreState(savedInstanceState);
//        tasks = (List<Task>) savedInstanceState.getSerializable("temporary");
//    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG," onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG," onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG," onStart");
        Log.e(TAG," tasks.size: "+tasks.size());

        View view = (View) LayoutInflater.from(getContext()).inflate(R.layout.fragment_panel, null, false);

        //初始化
        taskSmartTable = view.findViewById(R.id.table);
        initTable(tasks);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG," onResume");

    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG," onpause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG," onstop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG," onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG," onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG," onDetach");
    }

}
