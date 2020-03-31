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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.layout.SlideLayout;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.net.callback.ListDetectionResultCallBack;
import com.neu.test.net.callback.ListTaskCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.ToastUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;

public class ReDetectionActivity extends AppCompatActivity implements View.OnClickListener {

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


    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType ;
    private String title ;
    final int RequestCor = 521;
    final int RectifyCode = 600;
    private int pposition;


    DetctionActivity.ViewHolder viewHolder=null;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detction);

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

                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        });

        button_left = findViewById(R.id.button_selector_left);
        button_right = findViewById(R.id.button_selector_right);
        button_middle = findViewById(R.id.button_selector_middle);
        button_left.setOnClickListener(this);
        button_middle.setOnClickListener(this);
        button_right.setOnClickListener(this);

        data = task.getTASKID()+task.getDEVID()+task.getTASKTYPE()+task.getLOGINNAME();
        sp = getSharedPreferences(data, Context.MODE_PRIVATE);

        detectionResults = (List<DetectionResult>) getIntent().getSerializableExtra("items");
        splitDatas();
        Log.e(TAG," detectionResults: "+detectionResults.size());

        ToastUtil.showNumber(getApplicationContext(),detectionResults.size()+"");



        tv_totalitem.setText("------共"+detectionResults.size()+"项--已完成"+donepositions.size()+"项-------");
        setEnable(button_left);//默认选择未操作的
        detectionAdapter = new ReDetectionActivity.ReDetectionAdapter(detectionundo,undo);
        lv_detection.setAdapter(detectionAdapter);


        btn_sure_detection.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                List<DetectionResult> list = detectionResults.stream().filter(dete -> dete.getSTATUS().equals("2")).collect(Collectors.toList());
                if (list.size() != 0) {
                    Toasty.warning(ReDetectionActivity.this, "有未操作项，无法提交", Toast.LENGTH_LONG).show();
                } else {
                    String string = new Gson().toJson(detectionResults);
                    String url = BaseUrl.BaseUrl+"setItemResult";
                    OkHttp okHttp = new OkHttp();
                    okHttp.postBypostString(url, string, new FileResultCallBack() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            Log.e("error", e.toString());
                        }

                        @Override
                        public void onResponse(FilePathResult filePathResult, int i) {
                            Log.e("message", filePathResult.getMessage());
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            Intent intent = new Intent(ReDetectionActivity.this, PDFActivity.class);
                            intent.putExtra("listData", (Serializable) detectionResults);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        btn_save_detection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                      editorSave();
                      intent1.putExtra("position",pposition);
                      intent1.putExtra("isDoing",isDoing);
                      intent1.putExtra("where","finish");
                        setResult(RESULT_OK,intent1);
                        finish();
                    }
                });
            }
        });
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
                setResult(RESULT_CANCELED);//不设置RESULT_OK的原因是会出现bug
                this.finish();
                return true;
            case R.id.action_add_item://搜索，根据需要显示/隐藏下载按钮
                LayoutInflater layoutInflater = LayoutInflater.from(ReDetectionActivity.this);
                final View textEntryView = layoutInflater.inflate(R.layout.layout_add_item, null);
                AlertDialog dlg = new AlertDialog.Builder(ReDetectionActivity.this)
                        .setView(textEntryView)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText add_title = (EditText) textEntryView.findViewById(R.id.add_title);
                                EditText add_schedule = (EditText) textEntryView.findViewById(R.id.add_schedule);
                                EditText add_law = (EditText) textEntryView.findViewById(R.id.add_law);
                                DetectionResult detectionResult = new DetectionResult();
                                detectionResult.setRUNWATERNUM(detectionResults.get(0).getRUNWATERNUM());
                                detectionResult.setJIANCHAXIANGTITLE(add_title.getText().toString());
                                detectionResult.setCHECKCONTENT(add_schedule.getText().toString());
                                detectionResult.setLAW(add_law.getText().toString());
                                detectionResult.setTASKID(detectionResults.get(0).getTASKID());
                                detectionResult.setDEVID(detectionResults.get(0).getDEVID());
                                detectionResult.setDEVCLASS(detectionResults.get(0).getDEVCLASS());
                                detectionResult.setLOGINNAME(detectionResults.get(0).getLOGINNAME());
                                ToastUtil.showNumber(getApplicationContext(),detectionResults.size()+"");
                                detectionadd.add(detectionResult);
                                Log.e(TAG, "onClick: addpositions:"+addpositions.size());
                                addpositions.put(addpositions.size(),detectionResults.size()-1);

                                //更新
                                detectionAdapter.notifyDataSetChanged();

//                                lv_detection.setAdapter(detectionAdapter);

                                setEnable(button_right);
                                reflashList(detectionadd,add);
                                select = "right";
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dlg.show();
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
        detectionAdapter = new ReDetectionActivity.ReDetectionAdapter(listtoflash,flag);
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

            //加载item的布局
            convertView = View.inflate(ReDetectionActivity.this, R.layout.test_list_item2, null);
            viewHolder = new DetctionActivity.ViewHolder();
            viewHolder.detction_item_text_leftnum= (TextView) convertView.findViewById(R.id.detction_item_text_leftnum_2);
            viewHolder.detction_item_text_context= (TextView) convertView.findViewById(R.id.detction_item_text_context_2);
            viewHolder.rectify_item_status_rectified = convertView.findViewById(R.id.rectify_item_status_rectified_2);
            viewHolder.rectify_item_status_rectifyliving = convertView.findViewById(R.id.rectify_item_status_rectifyliving_2);
            viewHolder.rectify_item_status_unrectify = convertView.findViewById(R.id.rectify_item_status_unrectify_2);
            viewHolder.detction_item_image_right = convertView.findViewById(R.id.detction_item_image_right_2);
            viewHolder.ll_test = convertView.findViewById(R.id.ll_ttt);
            convertView.setTag(viewHolder);
            //检查项标题
            viewHolder.detction_item_text_context.setText(listdetectionresult.get(position).getJIANCHAXIANGTITLE());

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


            viewHolder.rectify_item_status_unrectify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listdetectionresult.get(position).getSTATUS().equals("0")){
                        viewHolder.rectify_item_status_unrectify.setChecked(true);
                        viewHolder.rectify_item_status_rectified.setChecked(false);
                        viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                        Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                        reflashList(listdetectionresult,flag);
                    }else{

                        jumpToSuggesstionActivity(getIndex(flag,position),"0");
                    }
                }
            });


            viewHolder.rectify_item_status_rectified.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listdetectionresult.get(position).getSTATUS().equals("1")){
                        viewHolder.rectify_item_status_unrectify.setChecked(false);
                        viewHolder.rectify_item_status_rectified.setChecked(true);
                        viewHolder.rectify_item_status_rectifyliving.setChecked(false);
                        Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                        reflashList(listdetectionresult,flag);
                    }else{
                        //页面跳转
                        //detectionResults.get(position).setSTATUS("1");

                        jumpToSuggesstionActivity( getIndex(flag,position),"1");
                    }
                }
            });

            viewHolder.rectify_item_status_rectifyliving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listdetectionresult.get(position).getSTATUS().equals("3")){
                        viewHolder.rectify_item_status_unrectify.setChecked(false);
                        viewHolder.rectify_item_status_rectified.setChecked(false);
                        viewHolder.rectify_item_status_rectifyliving.setChecked(true);
                        Toasty.warning(ReDetectionActivity.this,"已选择，无法重复操作");
                        reflashList(listdetectionresult,flag);
                    }else{
                        //页面跳转  现场整改
                        jumpToRectifyResultActivity( getIndex(flag,position));
                        reflashList(listdetectionresult,flag);
                    }
                }
            });
            //跳转至 已操作界面
            viewHolder.detction_item_image_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示箭头后判断跳转的是SuggestionActivity界面还是整改界面
                    if ((listdetectionresult.get(position).getISCHANGED().equals(searchUtil.changed))
                            && listdetectionresult.get(position).getSTATUS().equals(searchUtil.recifyQualify)){
                        jumpToRectifyResultActivity(position);
                    }else {
                        jumpToSuggesstionActivity( getIndex(flag,position),listdetectionresult.get(position).getSTATUS());
                    }
                }
            });

            viewHolder.detction_item_text_context.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            viewHolder.detction_item_text_context.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    dlg = new Dialog(ReDetectionActivity.this,R.style.FullScreen);
                    View textEntryView = View.inflate(ReDetectionActivity.this,R.layout.show_law_and_other, null);
                    TextView tv_paichaxize = textEntryView.findViewById(R.id.tv_paichaxize);
                    TextView tv_laws = textEntryView.findViewById(R.id.tv_laws);
                    Button btn_cancel = textEntryView.findViewById(R.id.btn_cancel);
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dlg.dismiss();
                        }
                    });
                    tv_paichaxize.setText(detectionResults.get(position).getCHECKCONTENT());
                    tv_laws.setText(detectionResults.get(position).getLAW());
                    dlg.setContentView(textEntryView);
                    dlg.show();
                    return true;
                }
            });
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

    private void jumpToRectifyResultActivity(int position) {
        Intent intent = new Intent(ReDetectionActivity.this,RectifyResultActivity.class);
        DetectionResult test = new DetectionResult();
//        test.setJIANCHAXIANGTITLE("测试");//测试代码
//        test.setLOGINNAME(LoginActivity.inputName);//测试代码
//        detectionResults.add(test);
        intent.putExtra("detectionResult",  detectionResults.get(position));
        intent.putExtra("position",position);
        intent.putExtra("toolbarTitle",title);
        intent.putExtra("toolbarTaskType",taskType);
        startActivityForResult(intent,RectifyCode);
    }



    private void jumpToSuggesstionActivity(int position,String status) {
        //listData.get(position).setResultStatus(status);
        // String isHege = listData.get(position).getResultStatus();
        //String noItem = listData.get(position).getJIANCHAXIANGCONTENT();
        //String devclass = listData.get(position).getDEVCLASS();
        Intent intent = new Intent(ReDetectionActivity.this, SuggestionActivity.class);
        intent.putExtra("detectionResult",detectionResults.get(position));
        //intent.putExtra("suggestion",noItem);
        //intent.putExtra("path",task.getTASKID());
        intent.putExtra("position",position);
        intent.putExtra("title",title);//需要传递status
        intent.putExtra("taskType",taskType);
        intent.putExtra("status",status);
        Log.e("status1",status);
        //intent.putExtra("isHege",isHege);
        startActivityForResult(intent,RequestCor);
    }


    //接收图片视频的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
          isDoing = true;
            if(requestCode == RequestCor){
                int position = data.getIntExtra("position",0);
                Log.e("position",position+"-");
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

//                lv_detection.setAdapter(detectionAdapter);
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

//                lv_detection.setAdapter(detectionAdapter);
                judgeSelect();
            }
        }
    }

    private void judgeSelect() {
        detectionundo.clear();
        detectiondone.clear();
        donepositions.clear();
        undopositions.clear();
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
        public TextView detction_item_text_context;
        public TextView detction_item_text_leftnum;
        public CheckBox rectify_item_status_unrectify;
        public CheckBox rectify_item_status_rectified;
        public CheckBox rectify_item_status_rectifyliving;
        public ImageView detction_item_image_right;
        public LinearLayout ll_test;
    }



    @Override
    public void onResume() {
        super.onResume();
        lv_detection.setAdapter(detectionAdapter);
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
    Map<String, String> map = new HashMap<>();
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

}