package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.neu.test.R;
import com.neu.test.entity.DetailTask;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListIDetailTaskCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.tree.TreeNode;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class DraftTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DraftTaskActivity";
    private static final int JUMP_TREELIST = 11;
    private EditText editText_taskID;
    private MaterialSpinner materialSpinner_taskSource;
    private EditText editText_devID;
    private MaterialSpinner materialSpinner_devType;
    private TextView textView_companyName;
    private TextView textView_person;
    private Button button_ok;
    private Button button_getCheckItem;
    private SearchUtil searchUtil;
    private Toolbar mToolbar;
    private TextView textView_toolbar;
    private TextView textView_deadline;
    private EditText editText_declocation;
    private MaterialSpinner materialSpinner_checkitemstandard;

    private Task task;
    private String task_type = "日常";//任务类型/任务来源
    private String task_devclass = "10000";//设备种类

    private List<DetectionResult> detectionResults;

    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_task);
        searchUtil = new SearchUtil();
        promptDialog = new PromptDialog(this);
        task = new Task();
        initView();

        initToolbar();

        materialSpinner_taskSource.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.e(TAG," 测试materialSpinner_taskSource："+searchUtil.taskTypeNew[position]+" position:"+position);
                task_type = searchUtil.taskTypeNew[position];
            }
        });

        materialSpinner_devType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.e(TAG," 测试materialSpinner_devType："+searchUtil.classofdevForDraft[position]+" position:"+position+"  text:"+materialSpinner_devType.getText());
                task_devclass = searchUtil.classofdevForDraft[position];
            }
        });

        materialSpinner_checkitemstandard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.e(TAG," 测试materialSpinner_checkitemstandard：  text:"+materialSpinner_checkitemstandard.getText());
            }
        });

    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.draft_task_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        textView_toolbar = findViewById(R.id.draft_task_toolbar_textview);
        textView_toolbar.setText("任务起草");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示箭头
    }

    private void initView() {
        editText_taskID = findViewById(R.id.draft_task_edittext_taskID);
        materialSpinner_taskSource = findViewById(R.id.draft_task_spinner_tasksource);
        editText_devID = findViewById(R.id.draft_task_edittext_devID);
        materialSpinner_devType = findViewById(R.id.draft_task_spinner_devtype);
        textView_companyName = findViewById(R.id.draft_task_textview_companyname);
        textView_person = findViewById(R.id.draft_task_textview_person);
        button_ok = findViewById(R.id.draft_task_button_ok);
        button_getCheckItem = findViewById(R.id.draft_task_button_getCheckItem);
        textView_deadline = findViewById(R.id.draft_task_textview_deadline);
//        editText_declocation = findViewById(R.id.draft_task_edittext_devlocation);
        materialSpinner_checkitemstandard = findViewById(R.id.draft_task_spinner_checkitemstandard);

        button_ok.setOnClickListener(this);
        button_getCheckItem.setOnClickListener(this);
        textView_deadline.setOnClickListener(this);

        List<String> list_tasksource = new ArrayList<>();
        list_tasksource = Arrays.asList(searchUtil.taskTypeNew);
        materialSpinner_taskSource.setItems(list_tasksource);
        List<String> list_devtype = new ArrayList<>();
        list_devtype = Arrays.asList(searchUtil.deviceTypeForDraft);
        materialSpinner_devType.setItems(list_devtype);
        String[] strings = {"国标","市标","其他"};
        List<String> listOfStandard = Arrays.asList(strings);
        materialSpinner_checkitemstandard.setItems(listOfStandard);
        textView_companyName.setText(LoginActivity.user.getUSEUNITNAME());
        textView_person.setText(LoginActivity.user.getUSERNAME());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.draft_task_button_ok:
                if(detectionResults == null){
                    Toasty.error(this,"请选择检查项").show();
                }else if(detectionResults.size() == 0){
                    Toasty.error(this,"请选择检查项").show();
                }else {
                    promptDialog.showLoading("任务生成中 ...");
                    //提交任务
                    draftTask();
                }

                break;
            case R.id.draft_task_button_getCheckItem:
                String taskName = editText_taskID.getText().toString().trim();
                String devID = editText_devID.getText().toString().trim();
                if(taskName.equals("")){
                    Toasty.error(this,"请输入有效任务名称").show();
                }else if(devID.equals("")||(!isNumeric(devID))){
                    Toasty.error(this,"请输入有效设备号").show();
                }else if(textView_deadline.getText().toString().equals("")){
                    Toasty.error(this,"请选择截止时间").show();
                }else {
                    promptDialog.showLoading("任务项获取中 ...");
                    BigInteger bigInteger = new BigInteger(devID);
                    long devid = bigInteger.longValue();
                    jumpToGetItems(taskName,devid);
                }

                break;
            case R.id.draft_task_textview_deadline:
                Log.e(TAG,"截止时间点击了");
                SidebarUtils.initSelectRecityTime(DraftTaskActivity.this,textView_deadline);
                break;
        }
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


    //提交任务
    public void draftTask(){
        submitData();
    }
    public void jumpToGetItems(String taskName,long devID){
        Date date = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String taskid = sd2.format(date);
        task.setDEVID(devID);
        task.setTASKNAME(taskName);
        BigInteger bigInteger = new BigInteger(taskid);
        task.setTASKID(bigInteger.longValue());
        task.setTASKTYPE(task_type);
        task.setDEVCLASS(task_devclass);
        task.setRESULT("2");
        task.setUSEUNITNAME(LoginActivity.user.getUSEUNITNAME());
        task.setLOGINNAME(LoginActivity.user.getLOGINNAME());
        task.setRUNWATERNUM(task.getLOGINNAME()+taskid);
        task.setDEADLINE(textView_deadline.getText().toString());
//        task.setPLACE(editText_declocation.getText().toString().trim());
        getDetctionData();
    }




    private void getDetctionData() {
        Map<String, String> map = new HashMap<>();
        map.put("username",task.getLOGINNAME());
        map.put("DEVCLASS",task.getDEVCLASS());
        String url = BaseUrl.BaseUrl+"getItemLists";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListIDetailTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismissImmediately();
                Log.e("tttttttttttt",e.getMessage());
                Log.e("tttttttttttt",e.toString());
                Toasty.warning(DraftTaskActivity.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<DetailTask>> response, int id) {
                if(response.getMessage().equals("获取检查项成功")) {
                    List<DetailTask> items = response.getContent();
                    List<DetectionResult> dd = new ArrayList<>();
                    DetectionResult detectionResult = new DetectionResult(0, TreeNode.ROOT_PARENT_ID,true, "");
                    detectionResult.setJIANCHAXIANGTITLE(searchUtil.getDevclassToType(task_devclass));
                    dd.add(detectionResult);
                    List<DetectionResult> detectionResults = creatDetectionResultList(items);
                    dd.addAll(detectionResults);
                    if(dd.size()>1){
                        Intent intent = new Intent(DraftTaskActivity.this,TreeListActivity.class);
                        intent.putExtra("DetectionResults", (Serializable) dd);
                        startActivityForResult(intent,JUMP_TREELIST);
                        promptDialog.dismissImmediately();

                    }else{
                        promptDialog.dismissImmediately();
                        Toasty.error(DraftTaskActivity.this,"暂无可选项").show();
                    }
                }else if(response.getMessage().equals("设备不存在")){
                    promptDialog.dismissImmediately();
                    Toasty.error(DraftTaskActivity.this,"设备号输入有误").show();
                }else{
                    promptDialog.dismissImmediately();
                    Toasty.error(DraftTaskActivity.this,"获取检查项失败，请稍后重试").show();
                }
            }
        });
    }


    public List<DetectionResult> creatDetectionResultList(List<DetailTask> items){
        List<DetectionResult> detectionResults = new ArrayList<>();
        for (int position=0;position<items.size();position++){
            DetectionResult detectionResult = new DetectionResult(position+1,0,false,items.get(position).getJIANCHAXIANGTITLE());
            detectionResult.setCHECKCONTENT(items.get(position).getJIANCHAXIANGCONTENT());//检查内容
            detectionResult.setJIANCHAXIANGBIANHAO(items.get(position).getJIANCHAXIANGID());//检查编号
            Log.e("编号",items.get(position).getJIANCHAXIANGID());
            detectionResult.setLOGINNAME(items.get(position).getLOGINNAME());//检查人员
            detectionResult.setTASKID(task.getTASKID());
            detectionResult.setDEVID(task.getDEVID());
            detectionResult.setDEVCLASS(task.getDEVCLASS());
            detectionResult.setRUNWATERNUM(task.getRUNWATERNUM());
            detectionResult.setJIANCHAXIANGTITLE(items.get(position).getJIANCHAXIANGTITLE());
            detectionResult.setLAW(items.get(position).getLAW());
            detectionResult.setSTATUS("2");
            detectionResults.add(detectionResult);
        }
        return  detectionResults;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            detectionResults = (List<DetectionResult>) data.getSerializableExtra("DList");
        }
    }




    private void submitData() {
        String taskString = new Gson().toJson(task);
        Log.e(TAG,"task:"+taskString);
        Map<String, Object> map = new HashMap<>();
        map.put("task",task);
        map.put("DetectionResults",detectionResults);
        String url = BaseUrl.BaseUrl+"customerDraftTask";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismissImmediately();
                Toasty.warning(DraftTaskActivity.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {

                if(response.getMessage().equals("任务提交成功")){
                    Toasty.success(DraftTaskActivity.this,"成功").show();
                    List<Task> tasks = response.getContent();
                    Intent intent = new Intent(DraftTaskActivity.this, FragmentManagerActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    String result = new Gson().toJson(tasks);
                    Log.e(TAG, "onResponse: "+result );
                    intent.putExtra("userName",LoginActivity.inputName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    promptDialog = null;
                    finish();
                }else{
                    promptDialog = null;
                    Toasty.error(DraftTaskActivity.this,"操作失败，请稍后重试").show();
                    finish();
                }

            }
        });
    }

    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[0-9]{1,18}");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
}