package com.neu.test.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.neu.test.R;
import com.neu.test.activity.BoSourceActivity;
import com.neu.test.activity.DraftTaskActivity;
import com.neu.test.activity.FragmentManagerActivity;
import com.neu.test.activity.LoginActivity;
import com.neu.test.activity.TreeListActivity;
import com.neu.test.activity.TreeListActivity3;
import com.neu.test.entity.DetailTask;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.DoubleData;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.SimpleToolbar;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListDoubleDataCallBack;
import com.neu.test.net.callback.ListIDetailTaskCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.tree.TreeNode;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;


public class DraftTaskFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "DraftTaskActivity";
    private static final int JUMP_TREELIST_LOOK = 12;
    private static final int JUMP_TREELIST_SET = 11;
    private EditText editText_taskID;
    private MaterialSpinner materialSpinner_taskSource;
    private EditText editText_devID;
    private MaterialSpinner materialSpinner_devType;
    private TextView textView_companyName;
    private TextView textView_person;
    private TextView zq_tv;
    private Button button_ok;
    private Button button_getCheckItem;
    private Button draft_task_button_lookFangAn;
    private Button draft_task_button_donwload;
    private SearchUtil searchUtil;
    private TextView textView_deadline;
    private EditText editText_declocation;
    private MaterialSpinner materialSpinner_checkperiod;
    private MaterialSpinner draft_task_spinner_fangan;

    private Task task;
    private String task_type = "日常";//任务类型/任务来源
    private String task_devclass = "10000";//设备种类
    private String FName = "自定义";//设备种类

    private List<DetectionResult> detectionResults1 = new ArrayList<>();//有父节点数据
    private List<DetectionResult> detectionResults2 = new ArrayList<>();//无父节点
    private SharedPreferences sharedPreferences;
    private String spKey;
    List<String> cycle =new ArrayList<>();
    List<String> faName =new ArrayList<>();

    private PromptDialog promptDialog;

    Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_draft_task, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        SimpleToolbar simple_toolbar = activity.findViewById(R.id.simple_toolbar);
        simple_toolbar.setVisibility(View.VISIBLE);
        promptDialog = new PromptDialog(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareData(view);
            }
        }).start();
        searchUtil = new SearchUtil();
        initView(view);

        materialSpinner_checkperiod.setItems(cycle);

        materialSpinner_taskSource.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.e(TAG," 测试materialSpinner_taskSource："+searchUtil.taskTypeNew[position]+" position:"+position);
                task_type = searchUtil.taskTypeNew[position];
            }
        });
        draft_task_spinner_fangan.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(item.toString().equals("自定义")){
                    button_getCheckItem.setVisibility(View.VISIBLE);
                    draft_task_button_lookFangAn.setVisibility(View.GONE);
                    draft_task_button_donwload.setVisibility(View.GONE);
                    materialSpinner_checkperiod.setVisibility(View.VISIBLE);
                    zq_tv.setVisibility(View.VISIBLE);
                }else{
                    String value1 = sharedPreferences.getString(spKey+draft_task_spinner_fangan.getText().toString()+"1","");
                    String value2 = sharedPreferences.getString(spKey+draft_task_spinner_fangan.getText().toString()+"2","");
                    materialSpinner_checkperiod.setVisibility(View.GONE);
                    zq_tv.setVisibility(View.GONE);
                    if(value1.equals("")){
                        button_getCheckItem.setVisibility(View.GONE);
                        draft_task_button_lookFangAn.setVisibility(View.GONE);
                        draft_task_button_donwload.setVisibility(View.VISIBLE);
                    }else{
                        //gson转换
                        button_getCheckItem.setVisibility(View.GONE);
                        draft_task_button_lookFangAn.setVisibility(View.VISIBLE);
                        draft_task_button_donwload.setVisibility(View.GONE);
                        detectionResults1.clear();
                        detectionResults2.clear();
                        detectionResults1.addAll(getList(new ArrayList<DetectionResult>(),sharedPreferences.getString(value1,"[]")));
                        detectionResults2.addAll(getList(new ArrayList<DetectionResult>(),sharedPreferences.getString(value2,"[]")));
                    }


                }
            }
        });
        materialSpinner_devType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Log.e(TAG," 测试materialSpinner_devType："+searchUtil.classofdevForDraft[position]+" position:"+position+"  text:"+materialSpinner_devType.getText());
                task_devclass = searchUtil.classofdevForDraft[position];
            }
        });


        return view;
    }

    private void initView(View view) {
//        intent = getActivity().getIntent();
//        faName.add(FName);
//        faName.addAll(intent.getStringArrayListExtra("nameList"));
//        cycle = intent.getStringArrayListExtra("cycleList");
        editText_taskID = view.findViewById(R.id.draft_task_edittext_taskID);
        materialSpinner_taskSource = view.findViewById(R.id.draft_task_spinner_tasksource);
        editText_devID = view.findViewById(R.id.draft_task_edittext_devID);
        materialSpinner_devType = view.findViewById(R.id.draft_task_spinner_devtype);
        textView_companyName = view.findViewById(R.id.draft_task_textview_companyname);
        textView_person = view.findViewById(R.id.draft_task_textview_person);
        zq_tv = view.findViewById(R.id.zq_tv);
        button_ok = view.findViewById(R.id.draft_task_button_ok);
        draft_task_button_donwload = view.findViewById(R.id.draft_task_button_donwload);
        button_getCheckItem = view.findViewById(R.id.draft_task_button_getCheckItem);
        draft_task_button_lookFangAn = view.findViewById(R.id.draft_task_button_lookFangAn);
        textView_deadline = view.findViewById(R.id.draft_task_textview_deadline);
        materialSpinner_checkperiod = view.findViewById(R.id.draft_task_spinner_checkperiod);
        draft_task_spinner_fangan = view.findViewById(R.id.draft_task_spinner_fangan);

        button_ok.setOnClickListener(this);
        button_getCheckItem.setOnClickListener(this);
        draft_task_button_donwload.setOnClickListener(this);
        draft_task_button_lookFangAn.setOnClickListener(this);
        textView_deadline.setOnClickListener(this);

        List<String> list_tasksource = new ArrayList<>();
        list_tasksource = Arrays.asList(new String[]{"无","日常","企业专项","政府专项"});
        materialSpinner_taskSource.setItems(list_tasksource);
        List<String> list_devtype = new ArrayList<>();
        list_devtype = Arrays.asList(searchUtil.deviceTypeForDraft);
        materialSpinner_devType.setItems(list_devtype);
        draft_task_spinner_fangan.setItems(faName);
        textView_companyName.setText(LoginActivity.user.getUSEUNITNAME());
        textView_person.setText(LoginActivity.user.getUSERNAME());

        task = new Task();
        task.setUSEUNITNAME(LoginActivity.user.getUSEUNITNAME());
        task.setLOGINNAME(LoginActivity.user.getUSERNAME());
        spKey = LoginActivity.user.getUSERNAME()+ LoginActivity.user.getUSEUNITNAME();
        sharedPreferences = getActivity().getSharedPreferences(spKey, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.draft_task_button_ok:
                if(detectionResults2 == null){
                    Toasty.error(getContext(),"请选择检查项").show();
                }else if(detectionResults2.size() == 0){
                    Toasty.error(getContext(),"请选择检查项").show();
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
                    Toasty.error(getContext(),"请输入有效任务名称").show();
                }else if(devID.equals("")||(!isNumeric(devID))){
                    Toasty.error(getContext(),"请输入有效设备号").show();
                }else if(textView_deadline.getText().toString().equals("")){
                    Toasty.error(getContext(),"请选择截止时间").show();
                } else{
                    promptDialog.showLoading("任务项获取中 ...");
                    BigInteger bigInteger = new BigInteger(devID);
                    long devid = bigInteger.longValue();
                    jumpToGetItems(taskName,devid);
                }

                break;
            case R.id.draft_task_textview_deadline:
                Log.e(TAG,"截止时间点击了");
                SidebarUtils.initSelectRecityTime(getActivity(),textView_deadline);
                break;
            case R.id.draft_task_button_lookFangAn:
                Toasty.success(getActivity(),"查看检查方案");
                lookFanganData();
                break;
            case R.id.draft_task_button_donwload:
                String taskName2 = editText_taskID.getText().toString().trim();
                String devID2 = editText_devID.getText().toString().trim();
                if(taskName2.equals("")){
                    Toasty.error(getContext(),"请输入有效任务名称").show();
                }else if(devID2.equals("")||(!isNumeric(devID2))){
                    Toasty.error(getContext(),"请输入有效设备号").show();
                }else if(textView_deadline.getText().toString().equals("")){
                    Toasty.error(getContext(),"请选择截止时间").show();
                } else{
                    BigInteger bigInteger = new BigInteger(devID2);
                    long devid = bigInteger.longValue();
                    promptDialog.showLoading("检查方案下载中 ...");
                    jumpToDownload(taskName2,devid);
                }
        }
    }

    //提交任务
    public void draftTask(){
        if(materialSpinner_taskSource.getText().toString().equals("无")){

            Intent intent = new Intent(getContext(), BoSourceActivity.class);
            intent.putExtra("items", (Serializable) detectionResults2);
            intent.putExtra("task", (Serializable) task);
            startActivity(intent);
        }else{
            submitData();
        }
    }
    public void jumpToGetItems(String taskName,long devID){
        Date date = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String taskid = sd2.format(date);
        task.setDEVID(devID);
        task.setTASKNAME(taskName);
        BigInteger bigInteger = new BigInteger(taskid);
        task.setTASKID(bigInteger.longValue());
        task.setTASKTYPE(materialSpinner_taskSource.getText().toString());
        task.setDEVCLASS(task_devclass);
        task.setRESULT("2");
        task.setRUNWATERNUM(taskid);
        task.setDEADLINE(textView_deadline.getText().toString());
        //task.setPLACE(editText_declocation.getText().toString().trim());
        getDetctionData();
    }

    public void jumpToDownload(String taskName,long devID){
        Date date = new Date();
        SimpleDateFormat sd2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String taskid = sd2.format(date);
        task.setDEVID(devID);
        task.setTASKNAME(taskName);
        BigInteger bigInteger = new BigInteger(taskid);
        task.setTASKID(bigInteger.longValue());
        task.setTASKTYPE(materialSpinner_taskSource.getText().toString());
        task.setDEVCLASS(task_devclass);
        task.setRESULT("2");
        task.setRUNWATERNUM(taskid);
        task.setDEADLINE(textView_deadline.getText().toString());
        //task.setPLACE(editText_declocation.getText().toString().trim());
        dwonloadFanganData();
    }


    private void getDetctionData() {
        Map<String, Object> map = new HashMap<>();
        map.put("username",task.getLOGINNAME());
        map.put("DEVCLASS",task.getDEVCLASS());
        map.put("UNIT",LoginActivity.user.getUSEUNITNAME());
        map.put("DEVID",task.getDEVID());
        map.put("ZHOUQI",materialSpinner_checkperiod.getText());
        String url = BaseUrl.BaseUrl+"getItemLists";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListIDetailTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismissImmediately();
                Toasty.warning(getContext(),"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<DetailTask>> response, int id) {
                if(response.getMessage().equals("获取检查项成功")) {
                    List<DetailTask> items = response.getContent();
                    //List<DetectionResult> dd = new ArrayList<>();
                    if(items.size() >0) {
                        detectionResults1.clear();
                        List<DetectionResult> detectionResults = creatDetectionResultList(items);
                        detectionResults1.addAll(detectionResults);
                        Intent intent = new Intent(getContext(), TreeListActivity.class);
                        intent.putExtra("DetectionResults", (Serializable) detectionResults1);
                        startActivityForResult(intent,JUMP_TREELIST_SET);
                        promptDialog.dismissImmediately();
                    } else{
                        promptDialog.dismissImmediately();
                        Toasty.error(getContext(),"暂无可选项").show();
                    }
                }else if(response.getMessage().equals("设备号不存在")){
                    promptDialog.dismissImmediately();
                    Toasty.error(getContext(),"设备号输入有误").show();
                }else{
                    promptDialog.dismissImmediately();
                    Toasty.error(getContext(),"获取检查项失败，请稍后重试").show();
                }
            }
        });
    }


    public List<DetectionResult> creatDetectionResultList(List<DetailTask> items){
        //父节点

        DetectionResult detectionResult1 = new DetectionResult(0, TreeNode.ROOT_PARENT_ID,true, "");
        detectionResult1.setJIANCHAXIANGTITLE("通用");
        DetectionResult detectionResult2 = new DetectionResult(1, TreeNode.ROOT_PARENT_ID,true, "");
        detectionResult2.setJIANCHAXIANGTITLE(materialSpinner_devType.getText().toString());
        //子节点
        List<DetectionResult> detectionResults = new ArrayList<>();

        for (int position=0;position<items.size();position++){
            if(items.get(position).getDEVCLASS().equals("10000")){
                if(!detectionResults.contains(detectionResult1)){
                    detectionResults.add(detectionResult1);
                }
                Log.e("tttttttt",position+"通用");
                DetectionResult detectionResult = new DetectionResult(position+2,0,false,items.get(position).getJIANCHAXIANGTITLE());
                detectionResult.setCHECKCONTENT(items.get(position).getJIANCHAXIANGCONTENT());//检查内容
                detectionResult.setJIANCHAXIANGBIANHAO(items.get(position).getJIANCHAXIANGID());//检查编号
                Log.e("编号",items.get(position).getJIANCHAXIANGID());
                detectionResult.setLOGINNAME(items.get(position).getLOGINNAME());//检查人员
                detectionResult.setTASKID(task.getTASKID());
                detectionResult.setDEVID(task.getDEVID());
                detectionResult.setDEVCLASS(items.get(position).getDEVCLASS());
                detectionResult.setRUNWATERNUM(task.getRUNWATERNUM());
                detectionResult.setQIYEMINGCHENG(task.getUSEUNITNAME());
                detectionResult.setJIANCHAXIANGTITLE(items.get(position).getJIANCHAXIANGTITLE());
                detectionResult.setLAW(items.get(position).getLAW());
                detectionResult.setSTATUS("2");
                detectionResults.add(detectionResult);
            }else{
                if(!detectionResults.contains(detectionResult2)){
                    detectionResults.add(detectionResult2);
                }
                Log.e("tttttttt",position+"sp");
                DetectionResult detectionResult = new DetectionResult(position+2,1,false,items.get(position).getJIANCHAXIANGTITLE());
                detectionResult.setCHECKCONTENT(items.get(position).getJIANCHAXIANGCONTENT());//检查内容
                detectionResult.setJIANCHAXIANGBIANHAO(items.get(position).getJIANCHAXIANGID());//检查编号
                Log.e("编号",items.get(position).getJIANCHAXIANGID());
                detectionResult.setLOGINNAME(items.get(position).getLOGINNAME());//检查人员
                detectionResult.setTASKID(task.getTASKID());
                detectionResult.setDEVID(task.getDEVID());
                detectionResult.setDEVCLASS(items.get(position).getDEVCLASS());
                detectionResult.setRUNWATERNUM(task.getRUNWATERNUM());
                detectionResult.setQIYEMINGCHENG(task.getUSEUNITNAME());
                detectionResult.setJIANCHAXIANGTITLE(items.get(position).getJIANCHAXIANGTITLE());
                detectionResult.setLAW(items.get(position).getLAW());
                detectionResult.setSTATUS("2");
                detectionResults.add(detectionResult);
            }

        }
        return  detectionResults;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if (requestCode == JUMP_TREELIST_SET){
                detectionResults2.clear();
                detectionResults2.addAll((Collection<? extends DetectionResult>) data.getSerializableExtra("DList"));
            }
        }
    }



    private void submitData() {

        Map<String, Object> map = new HashMap<>();
        map.put("task",task);
        map.put("DetectionResults",detectionResults2);
        String url = BaseUrl.BaseUrl+"customerDraftTask";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismissImmediately();
                Toasty.warning(getContext(),"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {

                if(response.getMessage().equals("任务提交成功")){
                    Toasty.success(getContext(),"成功").show();
                    List<Task> tasks = response.getContent();
                    Intent intent = new Intent(getContext(), FragmentManagerActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    String result = new Gson().toJson(tasks);
                    Log.e(TAG, "onResponse: "+result );
                    intent.putExtra("userName",LoginActivity.inputName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    promptDialog = null;

                }else{
                    promptDialog = null;
                    Toasty.error(getContext(),"操作失败，请稍后重试").show();

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


    private void dwonloadFanganData() {
        Map<String, Object> map = new HashMap<>();
        map.put("username",task.getLOGINNAME());
        map.put("fanganname",draft_task_spinner_fangan.getText().toString());
        map.put("unitname",LoginActivity.user.getUSEUNITNAME());
        String url = BaseUrl.BaseUrl+"getFanganItems";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListIDetailTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismissImmediately();
                Toasty.warning(getContext(),"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<DetailTask>> response, int id) {
                if(response.getMessage().equals("操作成功")) {
                    List<DetailTask> items = response.getContent();
                    if(items.size() >0) {
                        detectionResults1.clear();
                        detectionResults2.clear();
                        List<DetectionResult> detectionResults = creatDetectionResultList(items);
                        List<DetectionResult> detection_r = new ArrayList<>();
                        for(int i=0;i<items.size();i++){
                            DetectionResult detectionResult = new DetectionResult();
                            detectionResult.setCHECKCONTENT(items.get(i).getJIANCHAXIANGCONTENT());//检查内容
                            detectionResult.setJIANCHAXIANGBIANHAO(items.get(i).getJIANCHAXIANGID());//检查编号
                            Log.e("编号",items.get(i).getJIANCHAXIANGID());
                            detectionResult.setLOGINNAME(items.get(i).getLOGINNAME());//检查人员
                            detectionResult.setTASKID(task.getTASKID());
                            detectionResult.setDEVID(task.getDEVID());
                            detectionResult.setDEVCLASS(items.get(i).getDEVCLASS());
                            detectionResult.setRUNWATERNUM(task.getRUNWATERNUM());
                            detectionResult.setQIYEMINGCHENG(task.getUSEUNITNAME());
                            detectionResult.setJIANCHAXIANGTITLE(items.get(i).getJIANCHAXIANGTITLE());
                            detectionResult.setLAW(items.get(i).getLAW());
                            detectionResult.setSTATUS("2");
                            detection_r.add(detectionResult);
                        }
                        detectionResults1.addAll(detectionResults);
                        detectionResults2.addAll(detection_r);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(spKey+draft_task_spinner_fangan.getText().toString()+"1",new Gson().toJson(detectionResults1));
                        editor.putString(spKey+draft_task_spinner_fangan.getText().toString()+"2",new Gson().toJson(detection_r));
                        editor.commit();
                        button_getCheckItem.setVisibility(View.GONE);
                        draft_task_button_lookFangAn.setVisibility(View.VISIBLE);
                        draft_task_button_donwload.setVisibility(View.GONE);
                        promptDialog.dismissImmediately();
                    } else{
                        promptDialog.dismissImmediately();
                        Toasty.error(getContext(),"暂无可选项").show();
                    }
                }else{
                    promptDialog.dismissImmediately();
                    Toasty.error(getContext(),"获取检查项失败，请稍后重试").show();
                }
            }
        });
    }

    private void lookFanganData(){
        Intent intent = new Intent(getContext(), TreeListActivity3.class);
        intent.putExtra("DetectionResults", (Serializable) detectionResults1);
        startActivity(intent);
    }

    public <T> T getList(T content,String key){

        Gson gson = new Gson();
        Type type = content.getClass();
        T list = gson.fromJson(key,type);
        return list;
    }

    private void prepareData(View view) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", LoginActivity.user.getUSERNAME());
        map.put("unitname",LoginActivity.user.getUSEUNITNAME());

        String url = BaseUrl.BaseUrl+"getDraftData";
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListDoubleDataCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Toasty.warning(getActivity(),"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(DoubleData<ArrayList<String>, ArrayList<String>> response, int id) {
                if(response.getMessage().equals("获取信息成功")) {
                    cycle = response.getCycle();
                    faName = response.getFAName();
                    Log.e(TAG,"cycle:"+cycle.toString());
                    Log.e(TAG,"fName:"+faName.toString());
                    materialSpinner_checkperiod.setItems(cycle);
                    draft_task_spinner_fangan.setItems(faName);
                }else{
                    Toasty.error(getActivity(),"获取检查项失败，请稍后重试").show();
                }
            }
        });
    }
}
