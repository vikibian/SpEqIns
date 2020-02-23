package com.neu.test.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetectionItem;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.SlideLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;


public class DetctionActivity extends AppCompatActivity {
    private static String TAG = "DetctionActivity";

    private ListView lv_detection;
    private Button btn_add_detection;
    private Button btn_sure_detection;
    DetectionAdapter detectionAdapter;
    public List<DetectionItem> listData  = new ArrayList<>();; //检测项数据

    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType;
    private String title;

    private Toolbar toolbar;
    private TextView toolbar_textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detction);



        title = getIntent().getStringExtra("TITLE");
        userName = getIntent().getStringExtra("userName");
        task = (Task) getIntent().getSerializableExtra("task");
        taskType = getIntent().getStringExtra("tasktype");

        initToolbar();

        Log.e(TAG,"获取TAsk "+task.getTASKID());
        //setTitle(title);

        lv_detection = findViewById(R.id.lv_detection);
        btn_add_detection = findViewById(R.id.btn_add_detection);
        btn_sure_detection = findViewById(R.id.btn_sure_detection);


        //String position = "position";
        listData = (List<DetectionItem>) getIntent().getSerializableExtra("items");
        detectionAdapter = new DetectionAdapter();
        lv_detection.setAdapter(detectionAdapter);


//        lv_detection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(DetctionActivity.this,position,Toast.LENGTH_SHORT).show();
//            }
//        });



//        lv_detection.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
//            @Override
//            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.setHeaderTitle("确定删除该检查项吗");
//                menu.add(0, 0, 0, "删除此项");
//                menu.add(0, 1, 0, "取消删除");
//            }
//        });

        btn_add_detection.setOnClickListener(new View.OnClickListener() {
            String itemString;
            @Override
            public void onClick(View v) {

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
                                DetectionItem testItem = new DetectionItem();
                                testItem.setCHECKCONTENT(itemString);
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
            @Override
            public void onClick(View v) {
                boolean clickFlag = true;
                //对是否全选全部标记是否合格进行判断
                Log.e(TAG," 测试合格情况没有选择 listData "+listData.size());
                for (int j=0;j<listData.size();j++){
                    Log.e(TAG," 测试合格情况没有选择 listData的内容 "+listData.get(j).getResultStatus());
                    if (listData.isEmpty()|| listData.get(j).getResultStatus() == null){
                        clickFlag = false;
                    }
                }
                //如果合格情况都已经选择了则进行跳转操作
                if (clickFlag){
                    Intent intent = new Intent(DetctionActivity.this,PDFActivity.class);
                    intent.putExtra("listData",(Serializable)listData);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.e(TAG, "  "+task.toString());

                    boolean flag = true;
                    for (int i=0;i<listData.size();i++){
                        if (listData.get(i).getResultStatus() == "不合格"){
                            flag = false;
                        }
                    }
                    if (flag){
                        getSelectedItemByPost(task.getTASKID(),task.getDEVID(),"1");
                    }else {
                        getSelectedItemByPost(task.getTASKID(),task.getDEVID(),"-1");
                    }
                } else {
                    Toasty.warning(DetctionActivity.this,"请选择合格情况！",Toast.LENGTH_SHORT).show();
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(100);
                }

            }
        });

    }

    /**
     * @date : 2020/2/22
     * @time : 9:51
     * @author : viki
     * @description : 设置toolbar让中间显示文字
     */

    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setSubtitle("SubTitle");
//        toolbar.setTitle("");
        toolbar_textView = (TextView) findViewById(R.id.toolbar_title);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar_textView.setText(taskType);
        setSupportActionBar(toolbar);
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
        listData = new ArrayList<DetectionItem>();
//        listData.add(new DetectionItem("现场人员是否具有有效证件。","合格"));
//        listData.add(new DetectionItem("是否有使用登记标志，并按规定固定在电梯的显著位置，是否在下次检验期限内。","合格"));
//        listData.add(new DetectionItem("安全注意事项和警示标志是否置于易于为乘客注意的显著位置。","合格"));
//        listData.add(new DetectionItem("电梯内设置的报警装置是否可靠，联系是否畅通。","合格"));
//        listData.add(new DetectionItem("抽查呼层、楼层等显示信号系统功能是否有效，指示是否正确。","合格"));
//        listData.add(new DetectionItem("门防夹保护装置是否有效。","合格"));
//        listData.add(new DetectionItem("自动扶梯和自动人行道入口处急停开关是否有效。","合格"));
//        listData.add(new DetectionItem("限速器校验报告是否在有效期内。","合格"));
//        listData.add(new DetectionItem("是否有有效的维保合同，维保资质及人员资质是否满足要求。","合格"));
//        listData.add(new DetectionItem("是否有维保记录，并经安全管理人签字确认，维保周期是否符合规定。","合格"));


    }

//    private void listGotData() {
//
//        Log.d(TAG,"  listGotData  ");
//
//        for (int i =0;i<contentBeans.size();i++){
//            Log.d(TAG, "  contentBeans: "+contentBeans.get(i).getCHECKCONTENT()+"  i: "+i);
////            listData.add(new DetectionItem(contentBeans.get(i).getCHECKCONTENT(),"合格"));
//        }
//    }

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

            viewHolder.contentView.setText(listData.get(position).getCHECKCONTENT());

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
                    listData.get(position).setResultStatus("不合格");


                }
            });

            viewHolder.rb_detection_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listData.get(position).setResultStatus("合格");
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

    private void jumpToSuggesstionActivity(int position) {
        //listData.get(position).setResultStatus(status);
        String noItem = listData.get(position).getCHECKCONTENT();
        String devclass = listData.get(position).getDEVCLASS();
        Intent intent = new Intent(DetctionActivity.this, SuggestionActivity.class);
        intent.putExtra("suggestion",noItem);
        intent.putExtra("DEVCLASS",devclass);
        startActivity(intent);
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
       // public RadioButton rg_detection;


    }

}
