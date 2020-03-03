package com.neu.test.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.JianChaItem;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.SlideLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;


public class DetctionActivity extends AppCompatActivity {
    private static String TAG = "DetctionActivity";

    private ListView lv_detection;
    private Button btn_add_detection;
    private Button btn_save_detection;
    private Button btn_sure_detection;
    private DetectionAdapter detectionAdapter;
    private DetectionAdapter1 detectionAdapter1;
    //public List<DetectionItem> listData ;; //检测项数据

    public List<JianChaItem> listData  = new ArrayList<JianChaItem>();; //检测项数据
    public List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list

    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType;
    private String title;
    final int RequestCor = 521;
    private int pposition;

    //private  String  isHege="合格";
    ViewHolder viewHolder=null;
    View view1;

    private Toolbar toolbar;
    private TextView toolbar_textView;
    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;
    private static boolean isSave = false;
    private  Intent intent1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detction);


        intent1 = getIntent();
//        title = getIntent().getStringExtra("TITLE");
//        userName = getIntent().getStringExtra("userName");
//        task = (Task) getIntent().getSerializableExtra("task");
//        taskType = getIntent().getStringExtra("tasktype");
        task = (Task) getIntent().getSerializableExtra("task");
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType = task.getTASKTYPE();
        pposition = getIntent().getIntExtra("position",0);


        initToolbar();

        Log.e(TAG,"获取TAsk "+task.getTASKID());
        Log.e(TAG,"获取title "+title);
        Log.e(TAG,"获取TAsk "+taskType);
        //setTitle(title);

        lv_detection = findViewById(R.id.lv_detection);
        btn_add_detection = findViewById(R.id.btn_add_detection);
        btn_sure_detection = findViewById(R.id.btn_sure_detection);
        btn_save_detection = findViewById(R.id.btn_save_detection);


        //String position = "position";
        listData = (List<JianChaItem>) getIntent().getSerializableExtra("items");
        Log.e(TAG," listData: "+listData.size());

        for (int position=0;position<listData.size();position++){
            DetectionResult detectionResult = new DetectionResult();
            detectionResult.setCHECKCONTENT(listData.get(position).getXIANGMUMINGCHENG());//检查内容
//            detectionResult.setSTATUS("1");//检查结果
            detectionResult.setTASKID(task.getTASKID());
            detectionResult.setDEVCLASS(task.getDEVCLASS());
            detectionResults.add(detectionResult);
        }

        detectionAdapter = new DetectionAdapter();
        lv_detection.setAdapter(detectionAdapter);

        detectionAdapter1 = new DetectionAdapter1();
        lv_detection.setAdapter(detectionAdapter1);


        btn_add_detection.setOnClickListener(new View.OnClickListener() {
            String itemString;
            @Override
            public void onClick(View v) {

                Log.e(TAG," 保存");

                LayoutInflater layoutInflater = LayoutInflater.from(DetctionActivity.this);
                final View textEntryView = layoutInflater.inflate(R.layout.detection_dialog, null);
                AlertDialog dlg = new AlertDialog.Builder(DetctionActivity.this)
                        .setTitle("输入你要添加的检测项")
                        .setView(textEntryView)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText item = (EditText) textEntryView.findViewById(R.id.et_add_item);
                                itemString = item.getText().toString();
                                JianChaItem testItem = new JianChaItem();
                                testItem.setXIANGMUMINGCHENG(itemString);
                                listData.add(testItem);
                                //更新
                                detectionAdapter.notifyDataSetChanged();
                                lv_detection.invalidate();

                                //postTaskAndGetResult();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dlg.show();

            }
        });

        btn_sure_detection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String string = new Gson().toJson(detectionResults);
//                String url = BaseUrl.BaseUrl+"setItemResult";
//                OkHttp okHttp = new OkHttp();
//                okHttp.postBypostString(url, string, new FileResultCallBack() {
//                    @Override
//                    public void onError(Call call, Exception e, int i) {
//                        Log.e("error",e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(FilePathResult filePathResult, int i) {
//                        Log.e("message",filePathResult.getMessage());
//                        Intent intent = new Intent(DetctionActivity.this,PDFActivity.class);
//                        intent.putExtra("listData",(Serializable)detectionResults);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                });
//            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                List<DetectionResult> list = detectionResults.stream().filter(dete -> dete.getSTATUS().equals("2")).collect(Collectors.toList());
                if(list.size() != 0) {
                    Toasty.warning(DetctionActivity.this,"有未操作项，无法提交",Toast.LENGTH_LONG).show();
                }else{
                    String string = new Gson().toJson(detectionResults);
                    String url = BaseUrl.BaseUrl+"setItemResult";
                    OkHttp okHttp = new OkHttp();
                    okHttp.postBypostString(url, string, new FileResultCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Log.e("error",e.toString());
                        }

                        @Override
                        public void onResponse(FilePathResult filePathResult, int i) {
                            Log.e("message",filePathResult.getMessage());
                            Intent intent = new Intent(DetctionActivity.this,PDFActivity.class);
                            intent.putExtra("listData",(Serializable)detectionResults);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        //暂存
        btn_save_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = new Gson().toJson(detectionResults);
                String url = BaseUrl.BaseUrl+"setItemResult";
                OkHttp okHttp = new OkHttp();
                okHttp.postBypostString(url, string, new FileResultCallBack() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        Log.e("error",e.toString());
                    }

                    @Override
                    public void onResponse(FilePathResult filePathResult, int i) {
                        intent1.putExtra("position",pposition);
                        Log.e("message",filePathResult.getMessage());
                        setResult(RESULT_OK,intent1);
                        finish();
//            Intent intent = new Intent(DetctionActivity.this,PDFActivity.class);
//            intent.putExtra("listData",(Serializable)detectionResults);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
                    }
                });

            }
        });


//        btn_sure_detection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean clickFlag = true;
//                //对是否全选全部标记是否合格进行判断
//                Log.e(TAG," 测试合格情况没有选择 listData "+listData.size());
//                for (int j=0;j<listData.size();j++){
//                    Log.e(TAG," 测试合格情况没有选择 listData的内容 "+listData.get(j).getResultStatus());
//                    if (listData.isEmpty()|| listData.get(j).getResultStatus() == null){
//                        clickFlag = false;
//                    }
//                }
//                //如果合格情况都已经选择了则进行跳转操作
//                if (clickFlag){
//                    Intent intent = new Intent(DetctionActivity.this,PDFActivity.class);
//                    intent.putExtra("listData",(Serializable)listData);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Log.e(TAG, "  "+task.toString());
//
//                    boolean flag = true;
//                    for (int i=0;i<listData.size();i++){
//                        if (listData.get(i).getResultStatus() == "不合格"){
//                            flag = false;
//                        }
//                    }
//                    if (flag){
//                        getSelectedItemByPost(task.getTASKID(),task.getDEVID(),"1");
//                    }else {
//                        getSelectedItemByPost(task.getTASKID(),task.getDEVID(),"-1");
//                    }
//                } else {
//                    Toasty.warning(DetctionActivity.this,"请选择合格情况！",Toast.LENGTH_SHORT).show();
//                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//                    vibrator.vibrate(100);
//                }
//
//            }
//        });

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

        toolbar_title.setTextSize(20);
        toolbar_subtitleLeft.setTextSize(13);
        toolabr_subtitleRight.setTextSize(13);

        toolbar_title.setText(getResources().getString(R.string.app_name));
        toolbar_subtitleLeft.setText(title);
        toolabr_subtitleRight.setText(taskType);
        toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

        //getSupportActionBar.setDisplayHomeAsUpEnabled(true);

//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);//让原始的toolbar的title不显示，可以使用
//        }

    }


    private void getSelectedItemByPost(String TASKID, String DEVID, String HEGE) {
        url = BaseUrl.BaseUrl + "setTaskResultServlet";
        Log.d(TAG,"POST url: "+url);

        Map<String, String> map = new HashMap<>();
        map.put("TASKID",TASKID);
        map.put("DEVID",DEVID);
        map.put("HEGE",HEGE);
        map.put("username",userName);

        OkHttp okHttp = new OkHttp();
        okHttp.postBypostString(url, new Gson().toJson(map), new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Toasty.warning(DetctionActivity.this,"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                Toasty.success(DetctionActivity.this,response.getMessage(),Toast.LENGTH_LONG,true).show();
                List<Task> tasks = new ArrayList<Task>();
                if(response.getMessage().equals("任务提交成功")){
//                    if(response.getContent().size()>0) {
//                        tasks = response.getContent();
//                    }
//                    //Toast.makeText(DetctionActivity.this,"成功",Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(DetctionActivity.this,FragmentManagerActivity.class);
//                    intent.putExtra("userTask", (Serializable) tasks);
//                    intent.putExtra("userName",userName);
//                    startActivity(intent);
//                    finish();
                }
            }
        });

    }

//    private void postTaskAndGetResult() {
//
//        String url = SplashActivity.baseurl+"/addTaskServlet";
//        Log.d(TAG,"POST url: "+url);
//
//
//        JSONObject tSchedule = new JSONObject();
//        JSONObject jsonObject = null;
//        try {
//            tSchedule.put("TASKID","123461");
//            tSchedule.put("DEVCLASS","3000");
//            tSchedule.put("DEVID","12345");
//            tSchedule.put("LOGINNAME",LoginActivity.testinputName);
//            tSchedule.put("TASKTYPE",taskTypes[taskType].toString());
//
//            jsonObject = new JSONObject();
//            jsonObject.put("tSchedule", tSchedule);
//            Log.e(TAG,"tSchedule: "+ jsonObject.toString());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        OkHttp okHttp = new OkHttp();
//        okHttp.postBypostString(url, jsonObject.toString(), new StringCallback() {
//            @Override
//            public void onError(Call call, Exception e, int i) {
//                Log.e(TAG,"error: "+e.toString());
//                Log.e(TAG,"i: "+i);
//            }
//
//            @Override
//            public void onResponse(String s, int i) {
//                //测试Gson
//                Gson gson = new Gson();
//
//
////                if(message.equals("添加任务成功")){
////                    String itemString;
////                    EditText item = (EditText) textEntryView.findViewById(R.id.et_add_item);
////                    itemString = item.getText().toString();
////                    DetectionItem testItem = new DetectionItem(itemString);
////                    listData.add(testItem);
////                    detectionAdapter.notifyDataSetChanged();
////                    lv_detection.invalidate();
////                    Toasty.success(DetctionActivity.this,"添加任务成功！",Toast.LENGTH_SHORT,true).show();
////                } else if(message.equals("操作失败")){
////                    Toasty.error(DetctionActivity.this, "添加任务失败！", Toast.LENGTH_LONG,true).show();
////                }
//
//            }
//        });
//
//    }

    public void getData(){
        listData = new ArrayList<JianChaItem>();

    }


    class DetectionAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            Log.d(TAG,"listData adapter: "+listData.size());
            return listData.size();
        }
        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            //如果没有复用的
            if(convertView == null){
                //加载item的布局
                convertView = View.inflate(DetctionActivity.this, R.layout.listview_detection, null);
                viewHolder = new ViewHolder();
                viewHolder.contentView= (TextView) convertView.findViewById(R.id.tv_detection_item);
                viewHolder.menuView = (TextView) convertView.findViewById(R.id.tv_detection_menu);
                //viewHolder.rg_detection = convertView.findViewById(R.id.rg_detection);
                viewHolder.rb_detection_1 = convertView.findViewById(R.id.rb_detection_1);
                viewHolder.rb_detection_2 = convertView.findViewById(R.id.rb_detection_2);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.contentView.setText(listData.get(position).getXIANGMUMINGCHENG());
            viewHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View v) {
                    jumpToSuggesstionActivity(position);
                    Toast.makeText(DetctionActivity.this, "click "+((TextView)v).getText(), Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.rb_detection_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listData.get(position).setResultStatus("不合格");
                }
            });

            viewHolder.rb_detection_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //listData.get(position).setResultStatus("合格");
                }
            });
            viewHolder.menuView.setOnClickListener(new View.OnClickListener() {

                @Override

                public void onClick(View v) {

                    listData.remove(position);
                    detectionAdapter.notifyDataSetChanged();
                    lv_detection.invalidate();
                }
            });
            SlideLayout slideLayout = (SlideLayout) convertView;
            slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());
            return convertView;
        }
    }

    class DetectionAdapter1 extends BaseAdapter{

        private Map<Integer,Integer> hashMap = new HashMap<>();// key封装的是它爹的tag值，value封装儿子radiobutton

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            view1 = convertView;
            //加载item的布局
            convertView = View.inflate(DetctionActivity.this, R.layout.listview_detection1, null);
            viewHolder = new ViewHolder();
            viewHolder.contentView= (TextView) convertView.findViewById(R.id.tv_detection_item1);
            viewHolder.rb_detection_1 = convertView.findViewById(R.id.rb_detection_1_1);
            viewHolder.rb_detection_2 = convertView.findViewById(R.id.rb_detection_2_2);
            viewHolder.btn_add_content = convertView.findViewById(R.id.btn_add_content);
            viewHolder.rg_detection_group = convertView.findViewById(R.id.rb_detection_group);
            convertView.setTag(viewHolder);

            viewHolder.rg_detection_group.setTag(position);//给RadioGroup  弄个tag标记
            if(hashMap.containsKey(position))
            {
                viewHolder.rg_detection_group.check(hashMap.get(position));
            }
            else
            {
                viewHolder.rg_detection_group.clearCheck();
            }

            viewHolder.rg_detection_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if ((Integer)group.getTag() == position){
                        boolean b = false;
                        if (checkedId  == R.id.rb_detection_1_1){
                            b = true;
                            hashMap.put((Integer) group.getTag(),R.id.rb_detection_1_1);
                        }else if (checkedId == R.id.rb_detection_2_2){
                            b = true;
                            hashMap.put((Integer) group.getTag(),R.id.rb_detection_2_2);
                        }

                    }
                }
            });

            viewHolder.contentView.setText(listData.get(position).getXIANGMUMINGCHENG());
            //未选择是否合格
            if(!(viewHolder.rb_detection_1.isChecked()||viewHolder.rb_detection_2.isChecked())){
                //需要取证显示 取证按钮
                if(listData.get(position).isSHIFOUHEGEQUZHENG()){
                    //viewHolder.btn_add_content.setVisibility(View.VISIBLE);
                }else {//合格不取证
                    detectionResults.get(position).setSTATUS("0");
                    viewHolder.rb_detection_1.setChecked(true);
                }
            }
            if(detectionResults.get(position).isHaveDetail()){
                viewHolder.btn_add_content.setVisibility(View.VISIBLE);
            }else{
                viewHolder.btn_add_content.setVisibility(View.INVISIBLE);
            }

            final ViewHolder finalViewHolder = viewHolder;

            viewHolder.rb_detection_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detectionResults.get(position).setSTATUS("0");
                    if(listData.get(position).isSHIFOUHEGEQUZHENG()){
                        jumpToSuggesstionActivity(position);
                    }
                }
            });


            viewHolder.rb_detection_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detectionResults.get(position).setSTATUS("1");
                    jumpToSuggesstionActivity(position);
                }
            });
            final ViewHolder finalViewHolder1 = viewHolder;
            viewHolder.btn_add_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(detectionResults.get(position).isHaveDetail()){
                        Toast.makeText(DetctionActivity.this,"有证据",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(DetctionActivity.this,"暂无详细信息",Toast.LENGTH_LONG).show();
                    //jumpToSuggesstionActivity(position);
                }
            });
            return convertView;
        }
    }


    private void jumpToSuggesstionActivity(int position) {
        //listData.get(position).setResultStatus(status);
        // String isHege = listData.get(position).getResultStatus();
        String noItem = listData.get(position).getXIANGMUMINGCHENG();
        //String devclass = listData.get(position).getDEVCLASS();
        Intent intent = new Intent(DetctionActivity.this, SuggestionActivity.class);
        intent.putExtra("suggestion",noItem);
        intent.putExtra("path",task.getTASKID());
        intent.putExtra("position",position);
        intent.putExtra("title",title);//需要传递status
        intent.putExtra("taskType",taskType);
        intent.putExtra("status",detectionResults.get(position).getSTATUS());
        //intent.putExtra("isHege",isHege);
        startActivityForResult(intent,RequestCor);
    }


    //接收图片视频的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == RequestCor){
                int position = data.getIntExtra("position",0);
                Log.e("position",position+"-");
                int imageNumber = data.getIntExtra("imageNumber",0);
                Log.e("imageNumber",imageNumber+"-");
                int videoNumber = data.getIntExtra("videoNumber",0);
                Log.e("videoNumber",videoNumber+"-");
                String content = data.getStringExtra("content");
                Log.e("content",content+"-");
                detectionResults.get(position).setREFJIM(task.getTASKID()+position+"-"+imageNumber);
                detectionResults.get(position).setREFJVI(task.getTASKID()+position+"-"+videoNumber);
                detectionResults.get(position).setSUGGESTION(content);
                detectionResults.get(position).setHaveDetail(true);
            }
        }
    }

    public SlideLayout slideLayout = null;
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener {
        @Override
        public void onOpen(SlideLayout layout) {
            slideLayout = layout;
        }
        @Override
        public void onMove(SlideLayout layout) {
            if (slideLayout != null && slideLayout !=layout)
            {
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (slideLayout == layout)
            {
                slideLayout = null;
            }
        }
    }



    static class ViewHolder
    {
        public TextView contentView;
        public TextView menuView;
        public RadioButton rb_detection_1;
        public RadioButton rb_detection_2;
        public RadioGroup rg_detection_group;

        public Button btn_add_content;
        public CheckBox checkBox;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onstart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,"onstop");
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
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG," onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG,"onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG,"onRestoreInstanceState");
    }

}
