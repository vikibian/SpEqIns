package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
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

public class ReDetectionActivity extends AppCompatActivity {

    private static String TAG = "ReDetectionActivity";

    private ListView lv_redetection;
    private Button btn_add_redetection;
    private Button btn_save_redetection;
    private Button btn_sure_redetection;
    private ReDetectionActivity.ReDetectionAdapter redetectionAdapter;

    public List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list

    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType = "";
    private String title = "";
    final int RequestCor = 521;
    private int pposition;

    private Toolbar toolbar;
    private TextView toolbar_textView;
    private static boolean isSave = false;

    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_detection);

        task = (Task) getIntent().getSerializableExtra("task");
        pposition =  getIntent().getIntExtra("position",0);
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType =  task.getTASKTYPE();//getIntent().getStringExtra("tasktype");
        initToolbar();
        //初始化
        lv_redetection = findViewById(R.id.lv_redetection);
        btn_add_redetection = findViewById(R.id.btn_add_redetection);
        btn_sure_redetection = findViewById(R.id.btn_sure_redetection);
        btn_save_redetection = findViewById(R.id.btn_save_redetection);

        detectionResults = (List<DetectionResult>) getIntent().getSerializableExtra("items");
        for(int i=0;i<detectionResults.size();i++){
            detectionResults.get(i).setTASKID(task.getTASKID());
            //暂时默认 都有证据
            detectionResults.get(i).setHaveDetail(true);
        }
        Log.e(TAG," detectionResults: "+detectionResults.size());


        redetectionAdapter = new ReDetectionActivity.ReDetectionAdapter();
        lv_redetection.setAdapter(redetectionAdapter);

//        btn_sure_redetection.setOnClickListener(new View.OnClickListener() {
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
//                        Intent intent = new Intent(ReDetectionActivity.this,PDFActivity.class);
//                        intent.putExtra("listData",(Serializable)detectionResults);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });

        btn_sure_redetection.setOnClickListener(new View.OnClickListener() {
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
                            Intent intent = new Intent(ReDetectionActivity.this, PDFActivity.class);
                            intent.putExtra("listData", (Serializable) detectionResults);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        btn_save_redetection.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent = getIntent();
                        intent.putExtra("position",pposition);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
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
    toolbar = (Toolbar) findViewById(R.id.toolbar_re_detction);
      toolbar_title = findViewById(R.id.toolbar_re_detction_title);
      toolbar_subtitleLeft = findViewById(R.id.toolbar_re_detction_subtitle_left);
      toolabr_subtitleRight = findViewById(R.id.toolbar_re_detction_subtitle_right);

      toolbar_title.setTextSize(20);
      toolbar_subtitleLeft.setTextSize(13);
      toolabr_subtitleRight.setTextSize(13);

      toolbar_title.setText(getResources().getString(R.string.app_name));
      toolbar_subtitleLeft.setText(title);
      toolabr_subtitleRight.setText(taskType);
      toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

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
                Toasty.warning(ReDetectionActivity.this,"客官，网络不给力", Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                Toasty.success(ReDetectionActivity.this,response.getMessage(),Toast.LENGTH_LONG,true).show();
                List<Task> tasks = new ArrayList<Task>();
                if(response.getMessage().equals("任务提交成功")){
                }
            }
        });

    }


    class ReDetectionAdapter extends BaseAdapter {

        //private Map<Integer,Integer> hashMap = new HashMap<>();// key封装的是它爹的tag值，value封装儿子radiobutton

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ReDetectionActivity.ViewHolder viewHolder=null;

            //加载item的布局
            convertView = View.inflate(ReDetectionActivity.this, R.layout.listview_detection1, null);
            viewHolder = new ReDetectionActivity.ViewHolder();
            viewHolder.contentView= (TextView) convertView.findViewById(R.id.tv_detection_item1);
            viewHolder.rb_detection_1 = convertView.findViewById(R.id.rb_detection_1_1);
            viewHolder.rb_detection_2 = convertView.findViewById(R.id.rb_detection_2_2);
            viewHolder.btn_add_content = convertView.findViewById(R.id.btn_add_content);
            viewHolder.rg_detection_group = convertView.findViewById(R.id.rb_detection_group);
            convertView.setTag(viewHolder);

            //viewHolder.rg_detection_group.setTag(position);//给RadioGroup  弄个tag标记


//      viewHolder.rg_detection_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//          if ((Integer)group.getTag() == position){
//            boolean b = false;
//            if (checkedId  == R.id.rb_detection_1_1){
//              b = true;
//              hashMap.put((Integer) group.getTag(),R.id.rb_detection_1_1);
//            }else if (checkedId == R.id.rb_detection_2_2){
//              b = true;
//              hashMap.put((Integer) group.getTag(),R.id.rb_detection_2_2);
//            }
//
//          }
//        }
//      });

            viewHolder.contentView.setText(detectionResults.get(position).getCHECKCONTENT());
            viewHolder.rb_detection_1.setChecked(detectionResults.get(position).getSTATUS().equals("0"));
            viewHolder.rb_detection_2.setChecked(detectionResults.get(position).getSTATUS().equals("1"));




            final ReDetectionActivity.ViewHolder finalViewHolder = viewHolder;

            viewHolder.rb_detection_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detectionResults.get(position).setSTATUS("1");
                    //合格是否取证 默认取证
                    jumpToSuggesstionActivity(position);
                    Log.e("positontest",position+"+++");
                }
            });


            viewHolder.rb_detection_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    detectionResults.get(position).setSTATUS("-1");
                    jumpToSuggesstionActivity(position);
                }
            });
            final ReDetectionActivity.ViewHolder finalViewHolder1 = viewHolder;
            viewHolder.btn_add_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(detectionResults.get(position).isHaveDetail()){
                        Toast.makeText(ReDetectionActivity.this,"有证据",Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(ReDetectionActivity.this,"暂无详细信息",Toast.LENGTH_LONG).show();

                }
            });
            return convertView;
        }
    }


    private void jumpToSuggesstionActivity(int position) {

        String noItem = detectionResults.get(position).getCHECKCONTENT();
        Intent intent = new Intent(ReDetectionActivity.this, SuggestionActivity.class);
        intent.putExtra("suggestion",noItem);
        intent.putExtra("path",task.getTASKID());
        intent.putExtra("position",position);
        intent.putExtra("title",title);
        intent.putExtra("taskType",taskType);
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
                lv_redetection.setAdapter(redetectionAdapter);
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
    }
}
