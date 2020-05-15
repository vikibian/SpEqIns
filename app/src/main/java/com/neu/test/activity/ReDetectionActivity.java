package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.CheckLists;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.MyListView;
import com.neu.test.layout.MyTextView;
import com.neu.test.layout.SlideLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.net.callback.ListCheckListCallBack;
import com.neu.test.net.callback.ListDetectionResultCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.ClickUtil;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.ToastUtil;
import com.xiaweizi.marquee.MarqueeTextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.richinfo.dualsim.TelephonyManagement;
import es.dmoral.toasty.Toasty;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class ReDetectionActivity extends BaseActivity implements View.OnClickListener {

    private static String TAG = "ReDetectionActivity";

    private ListView lv_detection;
    private Button btn_add_detection;
    private Button btn_save_detection;
    private Button btn_sure_detection;
    private TextView tv_totalitem;
    private boolean isDoing = false;
    private ReDetectionActivity.ReDetectionAdapter detectionAdapter;

    public List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list
    public List<DetectionResult> detectionundo = new ArrayList<DetectionResult>();//检查结果list
    public List<DetectionResult> detectiondone = new ArrayList<DetectionResult>();//检查结果list
    public List<DetectionResult> detectionadd = new ArrayList<DetectionResult>();//检查结果list

    private Map<Integer,Integer> undopositions = new HashMap<>();
    private Map<Integer,Integer> donepositions = new HashMap<>();
    private Map<Integer,Integer> addpositions = new HashMap<>();

    private MyListView listView;
    private ArrayAdapter adapter;
    private List<String> titleData = new ArrayList<>();
    private List<CheckLists> checkLists = new ArrayList<>();

    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType ;
    private String title ;
    final int RequestCor = 521;
    final int RectifyCode = 600;
    private int pposition;


    ViewHolder viewHolder=null;
    View view1;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private Toolbar toolbar;
    private TextView toolbar_textView;
    private static boolean isSave = false;

    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;
    private  Intent intent1;
    private SearchUtil searchUtil = new SearchUtil();

    private Button button_left;
    private Button button_right;
    private Button button_middle;

    private String undo = "undo";
    private String done = "done";
    private String add = "add";
    private static String select = "left";

    String data ;
    SharedPreferences sp;
    Dialog dlg;
    PromptDialog promptDialog;
    private PermissionUtils permissionUtils;
    private  boolean[] isFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detction);

        promptDialog = new PromptDialog(this);
        permissionUtils = new PermissionUtils(this,ReDetectionActivity.this,null,null);
        intent1 = getIntent();
        task = (Task) getIntent().getSerializableExtra("task");
        pposition =  getIntent().getIntExtra("position",0);
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType =  task.getTASKTYPE();//getIntent().getStringExtra("tasktype");
        initToolbar();
        //初始化
        lv_detection = findViewById(R.id.lv_detection);
//        btn_add_detection = findViewById(R.id.btn_add_detection);
        btn_sure_detection = findViewById(R.id.btn_sure_detection);
        btn_save_detection = findViewById(R.id.btn_save_detection);
        tv_totalitem = findViewById(R.id.tv_totalitem);

        mWaveSwipeRefreshLayout = findViewById(R.id.main_swipe);

        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mWaveSwipeRefreshLayout.setRefreshing(false);
                getSaveData(task);
//                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        });

        button_left = findViewById(R.id.button_selector_left);
        button_right = findViewById(R.id.button_selector_right);
        button_middle = findViewById(R.id.button_selector_middle);
        button_left.setOnClickListener(this);
        button_middle.setOnClickListener(this);
        button_right.setOnClickListener(this);

        data = task.getTASKID()+task.getDEVID()+task.getTASKTYPE()+task.getLOGINNAME()+task.getRUNWATERNUM();
        sp = getSharedPreferences(data, Context.MODE_PRIVATE);

        detectionResults = (List<DetectionResult>) getIntent().getSerializableExtra("items");
        splitDatas();
        Log.e(TAG," detectionResults: "+detectionResults.size());

//        ToastUtil.showNumber(getApplicationContext(),detectionResults.size()+"");

        isFirst = new boolean[detectionResults.size()];
        reflashTotalItem();
        setEnable(button_left);//默认选择未操作的
        detectionAdapter = new ReDetectionActivity.ReDetectionAdapter(detectionundo,undo);
        lv_detection.setAdapter(detectionAdapter);


        btn_sure_detection.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                promptDialog.showLoading("提交结果中...");
                List<DetectionResult> list = new ArrayList<>();
                for(int i=0;i<detectionResults.size();i++){
                    if (detectionResults.get(i).getSTATUS().equals("2")){
                        list.add(detectionResults.get(i));
                    }
                }

//                List<DetectionResult> list = detectionResults.stream().filter(dete -> dete.getSTATUS().equals("2")).collect(Collectors.toList());
                if (list.size() != 0) {
                    promptDialog.dismiss();
                    Toasty.warning(ReDetectionActivity.this, "有未操作项，无法提交", Toast.LENGTH_LONG).show();
                } else {
                    Map<String,Object> map = new HashMap<>();
                    map.put("detectionResults",detectionResults);
                    map.put("TASK",task);
                    map.put("role",LoginActivity.user.getROLEID());
                    String gson = new Gson().toJson(map);
                    String url = BaseUrl.BaseUrl+"setResultFull";
                    OkHttp okHttp = new OkHttp();
                    okHttp.postBypostString(url, gson, new FileResultCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Log.e("error", e.toString());
                            promptDialog.dismiss();
                            Toasty.warning(ReDetectionActivity.this,"客官，网络不给力。");
                        }

                        @Override
                        public void onResponse(FilePathResult filePathResult, int i) {
                            Log.e("message", filePathResult.getMessage());
                            if (filePathResult.getMessage().equals("任务提交成功")){
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.clear();
                                Toasty.success(getApplicationContext(),"提交成功！").show();
//                                Intent intent = new Intent(ReDetectionActivity.this, PDFActivity.class);
//                                intent.putExtra("listData", (Serializable) detectionResults);
//                                intent.putExtra("task",task);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);

                                getDataBypost();



                            }else {
                                promptDialog.dismiss();
                                Toasty.error(ReDetectionActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btn_save_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.showLoading("保存中...");
                Map<String,Object> map = new HashMap<>();
                map.put("detectionResults",detectionResults);
                map.put("TASKTYPE",task.getTASKTYPE());
                String gson = new Gson().toJson(map);
                String url = BaseUrl.BaseUrl+"saveResultFull";
                OkHttp okHttp = new OkHttp();
                okHttp.postBypostString(url, gson, new FileResultCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Log.e("error",e.toString());
                        promptDialog.dismiss();
                        Toasty.warning(ReDetectionActivity.this,"客官，网络不给力。");
                    }

                    @Override
                    public void onResponse(FilePathResult filePathResult, int i) {
                        Log.e("message",filePathResult.getMessage());
                        if (filePathResult.getMessage().equals("任务提交成功")){
                            promptDialog.dismiss();
                            for(int mm= 0;mm<detectionResults.size();mm++){
                                if(detectionResults.get(mm).getJIANCHAXIANGBIANHAO().contains("?")){
                                    int size = detectionResults.get(mm).getJIANCHAXIANGBIANHAO().length();
                                    detectionResults.get(mm).setJIANCHAXIANGBIANHAO(detectionResults.get(mm).getJIANCHAXIANGBIANHAO().substring(0,size-1));
                                }
                            }
                            editorSave();
                            Toasty.success(getApplicationContext(),"保存成功！").show();
                            intent1.putExtra("position",pposition);
                            intent1.putExtra("isDoing",isDoing);
                            intent1.putExtra("where","finish");
                            setResult(RESULT_OK,intent1);
                            promptDialog = null;
                            finish();
                        }else {
                            promptDialog.dismiss();
                            Toasty.error(ReDetectionActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void reflashTotalItem() {
//        tv_totalitem.setText("---------待操作"+(undopositions.size())+"项--已完成"+donepositions.size()+"项---------");
        if (undopositions.size() != 0){
            button_left.setText("未操作("+undopositions.size()+")");
        }else if (undopositions.size() == 0){
            button_left.setText("未操作");
        }

        if (donepositions.size() != 0){
            button_middle.setText("已操作("+donepositions.size()+")");
        }else if (donepositions.size() == 0){
            button_middle.setText("已操作");
        }

        if (addpositions.size() != 0){
            button_right.setText("添加项("+addpositions.size()+")");
        }else if (addpositions.size() == 0){
            button_right.setText("添加项");
        }

    }

    private void splitDatas() {
        for (int i =0;i<detectionResults.size();i++){
            int undo = 0;
            int done = 0;
            //如果STATUS不为2  表示该项还没有操作  则添加到未操作的组里  并存储其在原来项的list的位置
            if (!detectionResults.get(i).getSTATUS().equals("2")){
                detectiondone.add(detectionResults.get(i));
                donepositions.put(donepositions.size(),i);
            }else {
                detectionundo.add(detectionResults.get(i));
                undopositions.put(undopositions.size(),i);
            }
        }
    }

    /**
     * @date : 2020/2/22
     * @time : 9:51
     * @author : viki
     * @description : 设置toolbar让中间显示文字
     */

  public void initToolbar() {
      toolbar = (Toolbar) findViewById(R.id.toolbar_detction);
      toolbar_title = findViewById(R.id.toolbar_detction_title);
      toolbar_subtitleLeft = findViewById(R.id.toolbar_detction_subtitle_left);
      toolabr_subtitleRight = findViewById(R.id.toolbar_detction_subtitle_right);

      toolbar_title.setTextSize(18);
      toolbar_subtitleLeft.setTextSize(13);
      toolabr_subtitleRight.setTextSize(13);

      toolbar_title.setText(taskType+"    ");//加空格的原因是让其显示居中
      toolbar_subtitleLeft.setText(title);
      toolabr_subtitleRight.setText(taskType);
      toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);

  }

    //初始化Menu的布局
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dynamic, menu);
        return true;
    }

    //Menu的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.action_add_item://搜索，根据需要显示/隐藏下载按钮
                LayoutInflater layoutInflater = LayoutInflater.from(ReDetectionActivity.this);
                final View textEntryView = layoutInflater.inflate(R.layout.layout_add_item, null);

                EditText add_title = (EditText) textEntryView.findViewById(R.id.add_title);
                EditText add_schedule = (EditText) textEntryView.findViewById(R.id.add_schedule);
                EditText add_law = (EditText) textEntryView.findViewById(R.id.add_law);
                Button btn_add_cancel = textEntryView.findViewById(R.id.btn_add_cancel);
                Button btn_add_add = textEntryView.findViewById(R.id.btn_add_add);
                TextView tv_tips = textEntryView.findViewById(R.id.tv_tips);
                tv_tips.setVisibility(View.GONE);

                listView = textEntryView.findViewById(R.id.lv_search);
                adapter = new ArrayAdapter<String>(ReDetectionActivity.this,android.R.layout.simple_list_item_1,titleData);
                listView.setAdapter((ListAdapter) adapter);

                View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(v.getId() == R.id.add_schedule ||v.getId() == R.id.add_law){
                            if(hasFocus){
                                listView.setVisibility(View.GONE);
                            }
                        }
                    }
                };
                add_schedule.setOnFocusChangeListener(onFocusChangeListener);
                add_law.setOnFocusChangeListener(onFocusChangeListener);

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.e("tttttttbeTtChanged:",s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int size = s.length();
                        if(size>1){
                            getTitleDataList(s.toString(),task.getDEVCLASS());
                        }else {
                            listView.setVisibility(View.GONE);
                        }
                        Log.e("tttttttonTextChanged:",s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                };

                add_title.addTextChangedListener(textWatcher);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        add_title.removeTextChangedListener(textWatcher);
                        add_title.setText(titleData.get(position));
                        add_schedule.setText(checkLists.get(position).getMONITORITEMS());
                        add_title.addTextChangedListener(textWatcher);
                        listView.setVisibility(View.GONE);
                    }
                });

                AlertDialog dlg = new AlertDialog.Builder(ReDetectionActivity.this)
                        .setView(textEntryView)
                        .create();
                dlg.show();

                btn_add_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dlg.dismiss();
                    }
                });
                btn_add_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!(add_title.getText().toString().trim().equals("") || add_law.getText().toString().trim().equals("") || add_schedule.getText().toString().trim().equals(""))){
                            DetectionResult detectionResult = new DetectionResult();
                            detectionResult.setRUNWATERNUM(detectionResults.get(0).getRUNWATERNUM());
                            detectionResult.setJIANCHAXIANGBIANHAO("add_"+detectionResults.size()+"?");
                            detectionResult.setJIANCHAXIANGTITLE(add_title.getText().toString().trim());
                            detectionResult.setCHECKCONTENT(add_schedule.getText().toString().trim());
                            detectionResult.setLAW(add_law.getText().toString().trim());
//                            detectionResult.setLATITUDE(gpsUtil.getLatitude());
//                            detectionResult.setLONGITUDE(gpsUtil.getLongitude());
                            detectionResult.setTASKID(detectionResults.get(0).getTASKID());
                            detectionResult.setDEVID(detectionResults.get(0).getDEVID());
                            detectionResult.setDEVCLASS(detectionResults.get(0).getDEVCLASS());
                            detectionResult.setLOGINNAME(detectionResults.get(0).getLOGINNAME());
                            detectionResult.setQUZHENG("一律取证");
                            detectionResult.setQIYEMINGCHENG(detectionResults.get(0).getQIYEMINGCHENG());
//                            ToastUtil.showNumber(getApplicationContext(),detectionResults.size()+"");
                            detectionadd.add(detectionResult);
                            detectionResults.add(detectionResult);
                            Log.e(TAG, "onClick: addpositions:"+addpositions.size());
                            addpositions.put(addpositions.size(),detectionResults.size()-1);

                            //更新
                            detectionAdapter.notifyDataSetChanged();

//                                lv_detection.setAdapter(detectionAdapter);
                            reflashTotalItem();
                            setEnable(button_right);
                            reflashList(detectionadd,add);
                            select = "right";
                            dlg.dismiss();
                        }else{
                            //未起作用
                            tv_tips.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_selector_left:
                setEnable(button_left);
                reflashList(detectionundo,undo);
                select = "left";
                break;
            case R.id.button_selector_middle:
                setEnable(button_middle);
                reflashList(detectiondone,done);
                select = "middle";
                break;
            case R.id.button_selector_right:
                setEnable(button_right);
                reflashList(detectionadd,add);
                select = "right";
                break;
        }
    }

    private void reflashList(List<DetectionResult> listtoflash, String flag) {
        boolean isDo = false;
        for (int i=0;i<detectionadd.size();i++){
            if (!detectionadd.get(i).getSTATUS().equals("2")){
                isDo = true;
            }
        }
        if (isDo){
            detectionadd.clear();
        }
        detectionAdapter = new ReDetectionAdapter(listtoflash,flag);
        lv_detection.setAdapter(detectionAdapter);
    }

    private void setEnable(Button btn) {
        List<Button> buttonList=new ArrayList<>();
        if (buttonList.size()==0){
            buttonList.add(button_left);
            buttonList.add(button_right);
            buttonList.add(button_middle);
        }
        for (int i = 0; i <buttonList.size() ; i++) {
            buttonList.get(i).setEnabled(true);
        }
        btn.setEnabled(false);
    }


    class ReDetectionAdapter extends BaseAdapter {

        private Map<Integer,Integer> hashMap = new HashMap<>();// key封装的是它爹的tag值，value封装儿子radiobutton
        private List<DetectionResult> listdetectionresult = new ArrayList<>();
        private String flag ;
        public ReDetectionAdapter(List<DetectionResult> listdetectionresult,String flag){
            this.listdetectionresult = listdetectionresult;
            this.flag = flag;
        }

        public ReDetectionAdapter(){

        }

        @Override
        public int getCount() {
            return listdetectionresult.size();
        }

        @Override
        public Object getItem(int position) {
            return listdetectionresult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            String result = new Gson().toJson(listdetectionresult.get(position));
            Log.e(TAG,"positon:"+position+"  result:"+result);

            //加载item的布局
            convertView = View.inflate(ReDetectionActivity.this, R.layout.test_list_item2, null);
            viewHolder = new ViewHolder();
            viewHolder.detction_item_text_leftnum=  convertView.findViewById(R.id.detction_item_text_leftnum_2);
            viewHolder.detction_item_text_context=  convertView.findViewById(R.id.detction_item_text_context_2);
            viewHolder.rectify_item_status_rectified = convertView.findViewById(R.id.rectify_item_status_rectified_2);
            viewHolder.rectify_item_status_rectifyliving = convertView.findViewById(R.id.rectify_item_status_rectifyliving_2);
            viewHolder.rectify_item_status_unrectify = convertView.findViewById(R.id.rectify_item_status_unrectify_2);
            viewHolder.detction_item_image_right = convertView.findViewById(R.id.detction_item_image_right_2);
            viewHolder.ll_test = convertView.findViewById(R.id.ll_ttt);
            viewHolder.detction_item_image_error = convertView.findViewById(R.id.detction_item_imageview_error_2);

            convertView.setTag(viewHolder);
            //检查项标题
          /*List<String> ll = new ArrayList<>();
          ll.clear();
          ll.add(listdetectionresult.get(position).getJIANCHAXIANGTITLE()+"555");
          viewHolder.detction_item_text_context.setContent(ll);*/
           viewHolder.detction_item_text_context.setText(listdetectionresult.get(position).getJIANCHAXIANGTITLE());
           if(!isFirst[position]){
             viewHolder.detction_item_text_context.startScroll();
             isFirst[position] = true;
           }


            //检查任务的number设置
            viewHolder.detction_item_text_leftnum.setText(position+1+"");

            //详细按钮的显示问题
            if(listdetectionresult.get(position).getISHAVEDETAIL().equals("1")){
                viewHolder.detction_item_image_right.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.detction_item_image_right.setVisibility(View.INVISIBLE);
            }

            Log.e(TAG,"检测添加选项 re："+listdetectionresult.get(position).getJIANCHAXIANGTITLE());

            //初始化选择状态
            if(listdetectionresult.get(position).getSTATUS().equals("0")){
                viewHolder.rectify_item_status_unrectify.setChecked(true);
                viewHolder.rectify_item_status_rectified.setChecked(false);
                viewHolder.rectify_item_status_rectifyliving.setChecked(false);
            }

            else if(listdetectionresult.get(position).getSTATUS().equals("1")){
                viewHolder.rectify_item_status_unrectify.setChecked(false);
                viewHolder.rectify_item_status_rectified.setChecked(true);
                viewHolder.rectify_item_status_rectifyliving.setChecked(false);
            }
            else if (listdetectionresult.get(position).getSTATUS().equals("3")){
                viewHolder.rectify_item_status_unrectify.setChecked(false);
                viewHolder.rectify_item_status_rectified.setChecked(false);
                viewHolder.rectify_item_status_rectifyliving.setChecked(true);
            }

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ClickUtil.isFastClick()){
                        return;
                    }
                    switch (v.getId()){
                        case R.id.rectify_item_status_unrectify_2:
                            if(listdetectionresult.get(position).getSTATUS().equals("0")){
                                viewHolder.rectify_item_status_unrectify.setChecked(true);
                                viewHolder.rectify_item_status_rectified.setChecked(false);
                                viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                                Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                                reflashList(listdetectionresult,flag);
                            }else{
                                if (listdetectionresult.get(position).getQUZHENG().equals("一律取证")){
                                    promptDialog.showLoading("加载中 ... ");
                                    jumpToSuggesstionActivity(getIndex(flag,position),"0");
                                }
                            }
                            break;
                        case R.id.rectify_item_status_rectified_2:
                            if(listdetectionresult.get(position).getSTATUS().equals("1")){
                                viewHolder.rectify_item_status_unrectify.setChecked(false);
                                viewHolder.rectify_item_status_rectified.setChecked(true);
                                viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                                Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                                reflashList(listdetectionresult,flag);
                            }else{
                                //页面跳转
                                //detectionResults.get(position).setSTATUS("1");
                                if (!listdetectionresult.get(position).getQUZHENG().equals("不用取证")){
                                    promptDialog.showLoading("加载中 ... ");
                                    jumpToSuggesstionActivity( getIndex(flag,position),"1");
                                }
                            }
                            break;
                        case  R.id.rectify_item_status_rectifyliving_2:
                            if(listdetectionresult.get(position).getSTATUS().equals("3")){
                                viewHolder.rectify_item_status_unrectify.setChecked(false);
                                viewHolder.rectify_item_status_rectified.setChecked(false);
                                viewHolder.rectify_item_status_rectifyliving.setChecked(true);
                                Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                                reflashList(listdetectionresult,flag);
                            }else{
                                //页面跳转  现场整改
                                if (!listdetectionresult.get(position).getQUZHENG().equals("不用取证")){
                                    promptDialog.showLoading("加载中 ... ");
                                    jumpToRectifyResultActivity( getIndex(flag,position),"3");
                                    reflashList(listdetectionresult,flag);
                                }
                            }
                            break;
                        case R.id.detction_item_image_right_2:
                            //显示箭头后判断跳转的是SuggestionActivity界面还是整改界面
                            if ((listdetectionresult.get(position).getISCHANGED().trim().equals("1"))
                                    &&(listdetectionresult.get(position).getSTATUS().equals(searchUtil.recifyQualify))){
                                promptDialog.showLoading("加载中 ... ");
                                jumpToRectifyResultActivity(getIndex(flag,position),searchUtil.recifyQualify);
                            }else if (listdetectionresult.get(position).getISCHANGED().trim().equals(searchUtil.unchanged)){
                                promptDialog.showLoading("加载中 ... ");
                                jumpToSuggesstionActivity( getIndex(flag,position),listdetectionresult.get(position).getSTATUS());
                            }
                            break;
                        case R.id.detction_item_imageview_error_2:
                            dlg = new Dialog(ReDetectionActivity.this,R.style.FullScreen);
                            View textEntryView = View.inflate(ReDetectionActivity.this,R.layout.show_law_and_other, null);
                            TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
                            TextView tv_paichaxxiangmu = textEntryView.findViewById(R.id.tv_paichaxxiangmu);
                            TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
                            Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dlg.dismiss();
                                }
                            });
//                    tv_paichaxize.setText(detectionResults.get(position).getCHECKCONTENT());
//                    tv_laws.setText(detectionResults.get(position).getLAW());
                            tv_paichaxize.setText(detectionResults.get(getIndex(flag,position)).getCHECKCONTENT());
                            tv_paichaxxiangmu.setText(detectionResults.get(getIndex(flag,position)).getJIANCHAXIANGTITLE());
                            tv_laws.setText(detectionResults.get(getIndex(flag,position)).getLAW());
                            dlg.setContentView(textEntryView);
                            dlg.show();
                            break;

                    }
                }
            };

            viewHolder.rectify_item_status_unrectify.setOnClickListener(clickListener);
            viewHolder.rectify_item_status_rectified.setOnClickListener(clickListener);
            viewHolder.rectify_item_status_rectifyliving.setOnClickListener(clickListener);
            //跳转至 已操作界面
            viewHolder.detction_item_image_right.setOnClickListener(clickListener);
            viewHolder.detction_item_image_error.setOnClickListener(clickListener);

            return convertView;
        }
    }

    private int getIndex(String flag, int position) {
        int index =0;
        if (flag.equals(undo)){
            index = undopositions.get(position);
        } else if (flag.equals(done)){
            index = donepositions.get(position);
        } else if (flag.equals(add)){
            index = addpositions.get(position);
        }

        return index;
    }

    private void jumpToRectifyResultActivity(int position,String status) {
      if (permissionUtils.canGoNextstep()){
          Intent intent = new Intent(ReDetectionActivity.this,RectifyResultActivity.class);
          DetectionResult test = new DetectionResult();
//        test.setJIANCHAXIANGTITLE("测试");//测试代码
//        test.setLOGINNAME(LoginActivity.inputName);//测试代码
//        detectionResults.add(test);
          intent.putExtra("detectionResult",  detectionResults.get(position));
          intent.putExtra("position",position);
          intent.putExtra("status",status);
          intent.putExtra("toolbarTitle",title);
          intent.putExtra("toolbarTaskType",taskType);
          startActivityForResult(intent,RectifyCode);
      }else {
          promptDialog.dismiss();
          Toast.makeText(this, "没有获取相应权限！", Toast.LENGTH_SHORT).show();
      }

    }



    private void jumpToSuggesstionActivity(int position,String status) {
        if(permissionUtils.canGoNextstep()){
            Intent intent = new Intent(ReDetectionActivity.this, SuggestionActivity.class);
            intent.putExtra("detectionResult",detectionResults.get(position));
            intent.putExtra("position",position);
            intent.putExtra("title",title);//需要传递status
            intent.putExtra("taskType",taskType);
            intent.putExtra("status",status);
            Log.e("status1",status);
            startActivityForResult(intent,RequestCor);
        }else{
            promptDialog.dismiss();
            Toast.makeText(this, "没有获取相应权限！", Toast.LENGTH_SHORT).show();
        }

    }


    //接收图片视频的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
          isDoing = true;
            if(requestCode == RequestCor){
                DetectionResult getdata = (DetectionResult) data.getSerializableExtra("detectionResult");

                int position = data.getIntExtra("position",0);
                Log.e("position",position+"-");

                detectionResults.set(position,getdata);

                int imageNumber = data.getIntExtra("imageNumber",0);
                Log.e("imageNumber",imageNumber+"-");
                int videoNumber = data.getIntExtra("videoNumber",0);
                Log.e("videoNumber",videoNumber+"-");
                String content = data.getStringExtra("content");
                Log.e("content",content+"-");
                String status = data.getStringExtra("status");

                String imagePath = data.getStringExtra("ImagePath");
                String videoPath = data.getStringExtra("VideoPath");
                if(imageNumber > 0){
                    //detectionResults.get(position).setREFJIM(task.getTASKID() + position + "-" + imageNumber);
                    detectionResults.get(position).setREFJIM(imagePath);
                    detectionResults.get(position).setISHAVEDETAIL("1");
                }
                if(videoNumber > 0){
                    //detectionResults.get(position).setREFJVI(task.getTASKID() + position + "-" + videoNumber);
                    detectionResults.get(position).setREFJVI(videoPath);
                    detectionResults.get(position).setISHAVEDETAIL("1");
                }
                //if(!(content.trim().toString().equals(""))){
                detectionResults.get(position).setSUGGESTION(content);
                detectionResults.get(position).setISHAVEDETAIL("1");
                detectionResults.get(position).setSTATUS(status);
                Log.e("status6",detectionResults.get(position).getSTATUS());
                //设置整改的属性为空
                detectionResults.get(position).setCHANGEDWAY("");
                detectionResults.get(position).setCHANGEDACTION("");
                detectionResults.get(position).setCHANGEDFINISHTIME("");
                detectionResults.get(position).setCHANGEDRESULT("");
                detectionResults.get(position).setCHANGEDIMAGE("");
                detectionResults.get(position).setCHANGEDVIDEO("");
                detectionResults.get(position).setISCHANGED(searchUtil.unchanged);

                if (status.equals(searchUtil.nohege)){
                    String yinhuanlevel = data.getStringExtra("yinhuanlevel");
                    String changway = data.getStringExtra("changway");
                    detectionResults.get(position).setYINHUANLEVEL(yinhuanlevel);
                    detectionResults.get(position).setCHANGEDWAY(changway);
                }

                String phone = data.getStringExtra("phone");
                detectionResults.get(position).setPHONE(phone);
                String longitude = data.getStringExtra("longitude");
                detectionResults.get(position).setLONGITUDE(longitude);
                String latitude = data.getStringExtra("latitude");
                detectionResults.get(position).setLATITUDE(latitude);
                Log.e(TAG," 手机号："+phone);
                Log.e(TAG," 经度："+longitude);
                Log.e(TAG," 纬度："+latitude);

//                lv_detection.setAdapter(detectionAdapter);
                String result = new Gson().toJson(detectionResults.get(position));
                Log.e(TAG,"结果(suggestion)："+result);
                Log.e(TAG,"结果(select)："+select);
                judgeSelect();
            }

            if (requestCode == RectifyCode){
                int pos = data.getIntExtra("position",-1);
                DetectionResult getdata = (DetectionResult) data.getSerializableExtra("detectionResult");
                detectionResults.set(pos,getdata);
                Log.e(TAG," way: "+detectionResults.get(pos).getCHANGEDWAY());
                Log.e(TAG," action: "+detectionResults.get(pos).getCHANGEDACTION());
                Log.e(TAG," finish time: "+detectionResults.get(pos).getCHANGEDFINISHTIME());
                Log.e(TAG," result: "+detectionResults.get(pos).getCHANGEDRESULT());
                Log.e(TAG,"imagepath: "+detectionResults.get(pos).getCHANGEDIMAGE());
                Log.e(TAG,"videopath: "+detectionResults.get(pos).getCHANGEDVIDEO());
                Log.e(TAG,"status: "+detectionResults.get(pos).getSTATUS());
                Log.e(TAG,"pos: "+pos);
                String result = new Gson().toJson(detectionResults.get(pos));
                Log.e(TAG,"结果(整改合格)："+result);
                judgeSelect();
            }
        }
    }

    private void judgeSelect() {
        detectionundo.clear();
        detectiondone.clear();
        donepositions.clear();
        undopositions.clear();
        detectionadd.clear();
        addpositions.clear();
        splitDatas();

        if (select.equals("left")){
            reflashList(detectionundo,undo);
        } else if (select.equals("middle")){
            reflashList(detectiondone,done);
        } else if (select.equals("right")){
            reflashList(detectionadd,add);
        }
    }



    static class ViewHolder{
        public MarqueeTextView detction_item_text_context;
        public TextView detction_item_text_leftnum;
        public CheckBox rectify_item_status_unrectify;
        public CheckBox rectify_item_status_rectified;
        public CheckBox rectify_item_status_rectifyliving;
        public ImageView detction_item_image_right;
        public LinearLayout ll_test;

        public ImageView detction_item_image_error;
    }



    @Override
    public void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
        lv_detection.setAdapter(detectionAdapter);
        reflashTotalItem();
        Log.e(TAG," onResume");
    }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    editorSave();
    intent1.putExtra("position",pposition);
    intent1.putExtra("isDoing",isDoing);
    intent1.putExtra("where","back");
    setResult(RESULT_OK,intent1);
    finish();
    Log.e(TAG,"onback");

  }

  public void editorSave(){
    Gson gson = new Gson();
    String json = gson.toJson(detectionResults);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString("detectionResultList",json);
    editor.commit();
  }

  public void refleshData(){
    Map<String, Object> map = new HashMap<>();
    map.put("taskID",task.getTASKID());
    map.put("DEVID",task.getDEVID());
    map.put("USERNAME",task.getLOGINNAME());
    String url = BaseUrl.BaseUrl+"getSaveResult";
    Log.e(TAG,"url: "+url);
    OkHttp okHttp=new OkHttp();
    okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
      @Override
      public void onError(Call call, Exception e, int i) {
        Log.e(TAG,"onError: "+e.toString());
        Toasty.warning(ReDetectionActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
      }

      @Override
      public void onResponse(Result<List<DetectionResult>> listResult, int i) {
        if (listResult.getMessage().equals("获取成功")) {
          List<DetectionResult> list = listResult.getContent();
          editorSharedPreferences(list,"detectionResultList");
        }
      }
    });

  }

  public <T> void editorSharedPreferences(T value,String key){
    String json2 = new Gson().toJson(value);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(key,json2);
    editor.commit();
  }

    public void getTitleDataList(String content,String Devclass){
        Map<String, String> map = new HashMap<>();
        map.put("content",content);
        map.put("DEVCLASS",Devclass);

        String url = BaseUrl.BaseUrl+"findMonitorContent";
        Log.e(TAG,"url: "+url);
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListCheckListCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                Log.e(TAG,"onError: "+e.toString());
                Toasty.warning(ReDetectionActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

//            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Result<List<CheckLists>> listResult, int i) {
                Log.e("tttt",listResult.getMessage());
                if (listResult.getMessage().equals("操作成功")) {
                    List<CheckLists> list = listResult.getContent();
                    checkLists.addAll(list);
                    titleData.clear();
                    for (int j=0;j<list.size();j++){
                        titleData.add(list.get(j).getMONITORCONTENT());
                    }
//                    titleData = list.stream().map(CheckLists::getMONITORCONTENT).collect(Collectors.toList());
                    Log.e("tttt",titleData.size()+"");
                    adapter = new ArrayAdapter<String>(ReDetectionActivity.this,android.R.layout.simple_list_item_1,titleData);
                    listView.setAdapter(adapter);
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getSaveData(final Task task) {
        Map<String, Object> map = new HashMap<>();
        map.put("taskID",task.getTASKID());
        map.put("DEVID",task.getDEVID());
        map.put("USERNAME",LoginActivity.user.getUSERNAME());
        map.put("RUNWATERNUM",task.getRUNWATERNUM());
        map.put("unitname",LoginActivity.user.getUSEUNITNAME());
        String url = BaseUrl.BaseUrl+"getSaveResult";
        Log.e(TAG,"url: "+url);
        OkHttp okHttp=new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListDetectionResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                mWaveSwipeRefreshLayout.setRefreshing(false);
                Log.e(TAG,"onError: "+e.toString());
                Toasty.warning(ReDetectionActivity.this,"客官，网络不给力!",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<DetectionResult>> listResult, int i) {
                Log.e(TAG,listResult.getMessage());
                if (listResult.getMessage().equals("获取成功")) {
                    Log.e("ssssize",listResult.getContent().size()+"");
                    if(listResult.getContent().size() == 0){
                        //不变化
                        Toasty.warning(ReDetectionActivity.this,"无变化");
                    }else{
                        editorSharedPreferences(listResult.getContent(),"detectionResultList");

                        detectionResults.clear();
                        detectiondone.clear();
                        detectionundo.clear();
                        detectionadd.clear();
                        undopositions.clear();
                        donepositions.clear();
                        addpositions.clear();

                        Log.e("ssssize",detectionResults.size()+"");
                        detectionResults.addAll(listResult.getContent());
                        Log.e("ssssize",detectionResults.size()+"");
                        splitDatas();
                        reflashTotalItem();
//                        setEnable(button_left);

                        detectionAdapter.notifyDataSetChanged();
                        lv_detection.setAdapter(detectionAdapter);
                    }
                }
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getDataBypost() {
        String url = BaseUrl.BaseUrl+"getFullyTSchedule";
        Log.d(TAG,"POST url: "+url);
        Map<String, Object> map = new HashMap<>();
        map.put("username",LoginActivity.user.getUSERNAME());
        map.put("unitname",LoginActivity.user.getUSEUNITNAME());
        map.put("role",LoginActivity.user.getROLEID());
//        map.put("password",inputPassword);
        Log.e(TAG,"map: "+ map.toString());


        String gson = new Gson().toJson(map);
        Log.e(TAG,"gson: "+ gson);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, gson, new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Log.e("error"," "+e.toString());
//                Toast.makeText(LoginActivity.this,"客官，网络不给力",Toast.LENGTH_LONG).show();
                Toasty.warning(ReDetectionActivity.this,"客官，网络不给力!", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if(response.getMessage().equals("获取任务成功")){
                    List<Task> tasks = response.getContent();
                    if(response.getContent().size()==0){
//                        Toast.makeText(LoginActivity.this,"无数据",Toast.LENGTH_LONG).show();
                        Log.e("TAG"," response.getContent: "+"无数据");
                        tasks = new ArrayList<Task>();
                    }
//                    Toast.makeText(ReDetectionActivity.this,"成功",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(ReDetectionActivity.this,FragmentManagerActivity.class);
//                    intent.putExtra("userTask", (Serializable) tasks);
//                    Log.e("TAG"," tasks: "+tasks.size());
//                    intent.putExtra("userName",LoginActivity.inputName);
//                    startActivity(intent);
//                    finish();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(ReDetectionActivity.this, FragmentManagerActivity.class);
                    intent.putExtra("userTask", (Serializable) tasks);
                    String result = new Gson().toJson(tasks);
                    Log.e(TAG, "onResponse: "+result );
                    intent.putExtra("userName",LoginActivity.inputName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    promptDialog.dismissImmediately();
                    startActivity(intent);
                    promptDialog =null;
                    finish();
                }
                else {
                    Intent intent = new Intent(ReDetectionActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    promptDialog = null;
                    finish();
                }
            }
        });
    }

}
