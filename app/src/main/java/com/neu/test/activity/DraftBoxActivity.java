package com.neu.test.activity;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.MyListView;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SharedPreferencesUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class DraftBoxActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "DraftBoxActivity";

    private Toolbar mToolbar;
    private TextView textView_toolbar;
    private List<Task> taskList = new ArrayList<>();
//    private List<Task> selectList = new ArrayList<>();
    private List<String> selectList = new ArrayList<>();
//    private List<String> taskList = new ArrayList<>();
//    private List<DetectionResult> detectionResultList = new ArrayList<>();
    private List<List<DetectionResult>> detectionResultsLists = new ArrayList<>();
    private Button button_save;
    private Button button_generate;
    private MyListView listView;

    private DraftBoxAdapter draftBoxAdapter = new DraftBoxAdapter();
    private boolean isSelect = false;
    private PromptDialog promptDialog;
    private Map<Integer,Integer> isCheckMap = new HashMap<>();
    private Map<Integer,Integer> positionMap = new HashMap<>();
    private static int pos = 0;
    private List<Integer> integerList = new ArrayList<>();
    private ScrollView scrollView;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_box);
        promptDialog = new PromptDialog(this);
        getDatas();
        initview();
        initToolbar();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(DraftBoxActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                promptDialog.showLoading("正在读取信息...");
                Intent intent = new Intent(DraftBoxActivity.this,DraftBoxDetailsActivity.class);
                intent.putExtra("task",taskList.get(position));
                intent.putExtra("detectionresults", (Serializable) detectionResultsLists.get(position));
                startActivity(intent);
                promptDialog.dismiss();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                MessageDialog.build(DraftBoxActivity.this)
                        .setStyle(DialogSettings.STYLE.STYLE_IOS)
                        .setTitle("提示")
                        .setMessage("你确定要删除该项吗？")
                        .setOkButton("确定", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                Log.e(TAG, "onClick: 删除了" );
                                taskList.remove(position);
                                detectionResultsLists.remove(position);
                                if (isCheckMap.containsKey(position)){
                                    isCheckMap.remove(position);
                                    int index = integerList.indexOf(position);
                                    integerList.remove(index);
                                }
                                listView.setAdapter(draftBoxAdapter);
                                return false;
                            }
                        })
                        .setCancelButton("取消", new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                return false;
                            }
                        })
                        .show();
                return true;
            }
        });

    }


    private void initview() {
        button_save = findViewById(R.id.draft_box_button_save);
        button_generate = findViewById(R.id.draft_box_button_ok);
        listView = findViewById(R.id.draft_box_listview);
        linearLayout = findViewById(R.id.draft_box_lin);

        button_save.setOnClickListener(this);
        button_generate.setOnClickListener(this);

        if (taskList.size() == 0){
            linearLayout.setVisibility(View.VISIBLE);
            button_generate.setEnabled(false);
            button_save.setEnabled(false);
        }else {
            linearLayout.setVisibility(View.GONE);
            button_generate.setEnabled(true);
            button_save.setEnabled(true);
        }

        listView.setAdapter(draftBoxAdapter);

    }

    private void getDatas() {
        Type type = new TypeToken<List<Task>>(){}.getType();
        taskList = SharedPreferencesUtil.getTaskList(this,type);
        for (Task task:taskList){
            String s = new Gson().toJson(task);
            Log.e(TAG,"草稿箱测试:"+s);
        }
        Log.e(TAG,"草稿箱测试 size:"+taskList.size());

        Type type1 = new TypeToken<List<List<DetectionResult>>>(){}.getType();
        detectionResultsLists = SharedPreferencesUtil.getDetectionResultLists(this,type1);
        for (List<DetectionResult> list:detectionResultsLists){
            String str = new Gson().toJson(list);
            Log.e(TAG,"草稿箱测试2："+str);
        }
        Log.e(TAG,"草稿箱测试 2 size:"+detectionResultsLists.size());

//        for (int i=0;i<13;i++){
//            taskList.add(""+i);
//        }
    }


    private void initToolbar() {
        mToolbar = findViewById(R.id.draft_box_toolbar);
        mToolbar.setTitle(" ");
        mToolbar.setTitleTextColor(Color.WHITE);

        textView_toolbar = findViewById(R.id.draft_box_toolbar_textview);
        textView_toolbar.setText("草稿箱");

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                saveSharePreferences();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.draft_box_button_save:
                promptDialog.showLoading("保存中...");
                saveSharePreferences();
                promptDialog.dismiss();
                Toasty.success(this,"保存成功",Toasty.LENGTH_SHORT).show();
                break;
            case R.id.draft_box_button_ok:
                Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show();
                if (integerList.size() ==0){
                    Toasty.warning(this,"未选择，无法提交！",Toasty.LENGTH_SHORT).show();
                }else {
                    submit();
                }

                break;
            default:
                break;
        }
    }

    private void saveSharePreferences() {
        SharedPreferencesUtil.saveTasks(this,taskList);
        SharedPreferencesUtil.saveDetectionResults(this,detectionResultsLists);
    }

    private void submit() {
        Map<String, Object> map = new HashMap<>();
        map.put("taskList",taskList);
        map.put("DetectionResultsList",detectionResultsLists);
        String url = BaseUrl.testBaseUrl+"writeCaoGao";

        String s = new Gson().toJson(map);
        Log.e(TAG, "submit: "+s);

        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismissImmediately();
                Toasty.warning(DraftBoxActivity.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {

                if(response.getMessage().equals("草稿提交成功")){
                    Toasty.success(DraftBoxActivity.this,"成功").show();
                    Log.e(TAG,"  "+response.getMessage().toString());
                    List<Task> tasks = response.getContent();
                    Intent intent = new Intent(DraftBoxActivity.this, FragmentManagerActivity.class);
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
                    Toasty.error(DraftBoxActivity.this,"操作失败，请稍后重试").show();
                    finish();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSharePreferences();
    }

    class DraftBoxAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();;
            if (convertView == null){
                convertView = LayoutInflater.from(DraftBoxActivity.this).inflate(R.layout.draft_box_listview_item,null);
                viewHolder.item_name = convertView.findViewById(R.id.draft_box_textview_taskName);
                viewHolder.item_id = convertView.findViewById(R.id.draft_box_textview_taskID);
                viewHolder.item_checkBox = convertView.findViewById(R.id.draft_box_checkbox);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.item_checkBox.setTag(position);
            //找到需要选中的条目
            if(isCheckMap!=null && isCheckMap.containsKey(position))
            {
                viewHolder.item_checkBox.setChecked(true);
            }
            else
            {
                viewHolder.item_checkBox.setChecked(false);
            }

            viewHolder.item_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int radiaoId = Integer.parseInt(buttonView.getTag().toString());
                    if(isChecked)
                    {
                        if (isCheckMap.size()<5){
                            //将选中的放入hashmap中
                            isCheckMap.put(radiaoId, radiaoId);
                            if (integerList.contains(radiaoId)){

                            }else {
                                integerList.add(radiaoId);
                            }
                        }else {
                            buttonView.setChecked(false);
                            Toasty.warning(DraftBoxActivity.this,"对不起，对多一次添加5项",Toasty.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        //取消选中的则剔除
                        isCheckMap.remove(radiaoId);
                        if (integerList.contains(radiaoId)){
                            int index = integerList.indexOf(radiaoId);
                            integerList.remove(index);
                            Log.e(TAG, "onCheckedChanged: "+integerList.toString());
                        }
                    }
                }
            });

            viewHolder.item_name.setText(taskList.get(position).getTASKNAME());
            viewHolder.item_id.setText(taskList.get(position).getTASKID()+"");
            viewHolder.item_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSelect = true;
                }
            });


            return convertView;
        }
    }

    private class ViewHolder{
        TextView item_name;
        TextView item_id;
        CheckBox item_checkBox;
    }
}
