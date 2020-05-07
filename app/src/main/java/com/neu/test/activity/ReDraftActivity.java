package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.MyListView;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SharedPreferencesUtil;
import com.neu.test.util.SidebarUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class ReDraftActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "ReDraftActivity";
    private final static int REDRAFT_CODE = 223;
    private Toolbar mToolbar;
    private TextView textView_toolbar;
    private Task task;
    private List<DetectionResult> detectionResults = new ArrayList<>();
    private TextView textView_taskName;
    private TextView textView_taskType;
    private TextView textView_taskID;
    private TextView textView_devID;
    private TextView textView_startTime;
    private TextView textView_deadLine;
    private TextView textView_devClass;
    private TextView textView_devLocation;
    private TextView textView_companyName;
    private TextView textView_person;
    private Button button_sure;
    private Button button_back;

    private SearchUtil searchUtil;
    private MyListView listView;
    private ReDraftAdapter reDraftAdapter = new ReDraftAdapter();
    private boolean isChnage = false;
//    public static boolean isSubmit ;

    private PromptDialog promptDialog;
    public static List<Task> tasksList = new ArrayList<>();
    private Task newTask = new Task();
    private List<List<DetectionResult>> detecttionResultLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_draft);
        promptDialog = new PromptDialog(this);
        Type type = new TypeToken<List<Task>>(){}.getType();
        tasksList = SharedPreferencesUtil.getTaskList(this,type);
        Type type1 = new TypeToken<List<List<DetectionResult>>>(){}.getType();
        detecttionResultLists = SharedPreferencesUtil.getDetectionResultLists(this,type1);

//        SharedPreferences sharedPreferences = getSharedPreferences("ReDraftActivityForTask",MODE_PRIVATE);
//        String taskstring = sharedPreferences.getString("tasks", "[]");
//        if (!"[]".equals(taskstring)){
//            Type type = new TypeToken<List<Task>>(){}.getType();
//            tasksList = new Gson().fromJson(taskstring, type);
//            for (int i=0;i<tasksList.size();i++){
//                String st = new Gson().toJson(tasksList.get(i));
//                Log.e(TAG,"oncreate: st"+st);
//            }
//        }

        for (int i=0;i<tasksList.size();i++){
                String st = new Gson().toJson(tasksList.get(i));
                Log.e(TAG,"oncreate: st:"+st);
        }
        Log.e(TAG,"oncreate: size1:"+tasksList.size());

        for (int i=0;i<detecttionResultLists.size();i++){
            String st = new Gson().toJson(detecttionResultLists.get(i));
            Log.e(TAG,"oncreate: detecttionResultLists:"+st);
        }
        Log.e(TAG,"oncreate: size2:"+detecttionResultLists.size());

        getIntentData();
        initview();
        initToolbar();
        initData();
    }

    private void getIntentData() {
        searchUtil = new SearchUtil();
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");
        detectionResults.clear();
        detectionResults = (List<DetectionResult>) intent.getSerializableExtra("redraftdetection");
        String taskstring = new Gson().toJson(task);
        String resultString = new Gson().toJson(detectionResults);
        Log.e(TAG, "getIntentData: task:"+taskstring);
        Log.e(TAG, "getIntentData:detection:"+resultString );
    }

    private void initview() {
        textView_taskName = findViewById(R.id.redraft_task_textview_taskName);
        textView_taskType = findViewById(R.id.redraft_task_textview_taskType);
        textView_taskID = findViewById(R.id.redraft_task_textview_taskID);
        textView_devID = findViewById(R.id.redraft_task_textview_devID);
        textView_startTime = findViewById(R.id.redraft_task_textview_starttime);
        textView_deadLine = findViewById(R.id.redraft_task_textview_deadline);
        textView_devClass = findViewById(R.id.redraft_task_textview_devtype);
        textView_devLocation = findViewById(R.id.redraft_task_textview_devlocation);
        textView_companyName = findViewById(R.id.redraft_task_textview_companyname);
        textView_person = findViewById(R.id.redraft_task_textview_person);

        textView_startTime.setOnClickListener(this);
        textView_deadLine.setOnClickListener(this);

        button_sure = findViewById(R.id.redraft_task_button_ok);
        button_sure.setOnClickListener(this);

        button_back = findViewById(R.id.redraft_task_button_back);
        button_back.setOnClickListener(this);

        listView = findViewById(R.id.redraft_listview);
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.redraft_task_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        textView_toolbar = findViewById(R.id.redraft_task_toolbar_textview);
        textView_toolbar.setText("起草不合格项");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        textView_taskName.setText(task.getTASKNAME());
        textView_taskType.setText(task.getTASKTYPE());
        textView_taskID.setText(String.valueOf(task.getTASKID()));
        textView_devID.setText(String.valueOf(task.getDEVID()));
        textView_devClass.setText(searchUtil.getDevclassToType(task.getDEVCLASS()));
        textView_devLocation.setText(task.getPLACE());
        textView_companyName.setText(task.getUSEUNITNAME());
        textView_person.setText(task.getLOGINNAME());

        listView.setAdapter(reDraftAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.redraft_task_button_ok:
                String starttime = textView_startTime.getText().toString();
                String endtime = textView_deadLine.getText().toString();
//                if (TextUtils.isEmpty(starttime)){
//                    Toasty.error(this,"请输入任务开始时间").show();
//                }
//                else
                if (TextUtils.isEmpty(endtime)){
                    Toasty.error(this,"请输入任务截止时间").show();
                } else {
                    setTask(endtime);
                    if (isTaskRepeat()){
                        Toasty.error(this,"该任务已操作，勿重复操作！").show();
                    }else {

                        promptDialog.showLoading("正在保存到草稿箱...");
                        tasksList.add(newTask);
                        SharedPreferencesUtil.saveTasks(this,tasksList);

                        for (int i=0;i<detectionResults.size();i++){
                            detectionResults.get(i).setRUNWATERNUM(newTask.getRUNWATERNUM());
                            detectionResults.get(i).setISHAVEDETAIL("0");
                            detectionResults.get(i).setSTATUS("2");
                        }
                        detecttionResultLists.add(detectionResults);
                        SharedPreferencesUtil.saveDetectionResults(this,detecttionResultLists);
                        promptDialog.dismiss();
                        Toasty.success(this,"任务已保存到草稿箱").show();
                    }
                }
                break;
            case R.id.redraft_task_button_back:
                finish();
                break;
            case R.id.redraft_task_textview_starttime:
                SidebarUtils.initSelectRecityTime(ReDraftActivity.this,textView_startTime);
                break;
            case R.id.redraft_task_textview_deadline:
                SidebarUtils.initSelectRecityTime(ReDraftActivity.this,textView_deadLine);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @return 重复的话返回true
     */
    private boolean isTaskRepeat() {
        boolean flag = false;
        for (int i =0;i<tasksList.size();i++){
            if (newTask.getTASKID() == tasksList.get(i).getTASKID()){
                flag = true;
            }
            if (newTask.getDEVID() == tasksList.get(i).getDEVID()){
                flag = true;
            }
        }
        return flag;
    }

    private void setTask(String endtime) {
        Date date = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String taskid = sd2.format(date);
        newTask.setDEVID(task.getDEVID());
        newTask.setTASKNAME(task.getTASKNAME());
        BigInteger bigInteger = new BigInteger(taskid);

        newTask.setTASKID(task.getTASKID());
        newTask.setTASKTYPE("整改");
        newTask.setDEVCLASS(task.getDEVCLASS());
        newTask.setRESULT("2");
        newTask.setRUNWATERNUM(taskid);
        newTask.setDEADLINE(endtime);
        newTask.setLOGINNAME(LoginActivity.user.getUSERNAME());
        newTask.setUSEUNITNAME(LoginActivity.user.getUSEUNITNAME());

    }

    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
        for (int i=0;i<detectionResults.size();i++){
            String resultString = new Gson().toJson(detectionResults.get(i));
            Log.e(TAG, "onResume:detection:"+resultString );
        }
        listView.setAdapter(reDraftAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }



    class ReDraftAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return detectionResults.size();
        }

        @Override
        public Object getItem(int position) {
            return detectionResults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();;
            if (convertView == null){
                convertView = LayoutInflater.from(ReDraftActivity.this).inflate(R.layout.redraft_listview_item,null);
                viewHolder.item_name = convertView.findViewById(R.id.redraft_listview_item_textview_name);
                viewHolder.item_status = convertView.findViewById(R.id.redraft_listview_item_textview_status);
                viewHolder.item_image = convertView.findViewById(R.id.redraft_listview_item_circleimage);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.item_name.setText(detectionResults.get(position).getJIANCHAXIANGTITLE());
            viewHolder.item_status.setText(searchUtil.getQualityFromNUm(detectionResults.get(position).getSTATUS()));
            viewHolder.item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLaw(detectionResults.get(position));
                }
            });
            return convertView;
        }
    }

    private class ViewHolder{
        TextView item_name;
        TextView item_status;
        ImageView item_image;
    }

    private void showLaw(DetectionResult detectionResult) {
        Dialog dlg = new Dialog(ReDraftActivity.this,R.style.FullScreen);
        View textEntryView = View.inflate(ReDraftActivity.this,R.layout.show_law_and_other, null);
        TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
        TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
        Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        tv_paichaxize.setText(detectionResult.getCHECKCONTENT());
        tv_laws.setText(detectionResult.getLAW());
        dlg.setContentView(textEntryView);
        dlg.show();
    }
}
