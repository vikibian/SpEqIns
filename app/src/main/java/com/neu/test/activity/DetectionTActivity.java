package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.entity.DetailTask;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.util.BaseUrl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class DetectionTActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUESTRECTIFY = 0315;

    private ListView lv_detection_t;
    private Button btn_save_detection_t;
    private Button btn_error_detection_t;
    private Button btn_submit_detection_t;
    private DetectionTActivity.DetectionTAdapter detectionTAdapter;
    private DetectionTActivity.ViewHolder viewHolder;
    public List<DetailTask> listData  = new ArrayList<DetailTask>();; //检测项数据
    public List<DetectionResult> detectionResults = new ArrayList<DetectionResult>();//检查结果list
    public List<DetectionResult> errorDetectionResults = new ArrayList<DetectionResult>();//检查有问题的 list
    private List<Integer> numberError = new ArrayList<>();

    private String userName;
    private String url;
    private Task task = new Task();
    private String taskType;
    private String title;
    final int RequestCor = 521;
    private int pposition;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_t);
        initView();
        btn_error_detection_t.setOnClickListener(this);
        btn_save_detection_t.setOnClickListener(this);
        btn_submit_detection_t.setOnClickListener(this);
    }

    public void initView(){
        lv_detection_t = findViewById(R.id.lv_detection_t);
        btn_save_detection_t = findViewById(R.id.btn_save_detection_t);
        btn_error_detection_t = findViewById(R.id.btn_error_detection_t);
        btn_submit_detection_t = findViewById(R.id.btn_submit_detection_t);
        intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");
        title = task.getUSEUNITNAME();
        userName = task.getLOGINNAME();
        taskType = task.getTASKTYPE();
        pposition = intent.getIntExtra("position",0);
        listData = (List<DetailTask>)intent.getSerializableExtra("items");

        Toast.makeText(DetectionTActivity.this,"共"+listData.size()+"项",Toast.LENGTH_LONG).show();

        for (int position=0;position<listData.size();position++){
            DetectionResult detectionResult = new DetectionResult();
            detectionResult.setCHECKCONTENT(listData.get(position).getJIANCHAXIANGCONTENT());//检查内容
            detectionResult.setJIANCHAXIANGBIANHAO(listData.get(position).getJIANCHAXIANGID());//检查编号
            Log.e("编号",listData.get(position).getJIANCHAXIANGID());
            detectionResult.setLOGINNAME(listData.get(position).getLOGINNAME());//检查人员
            detectionResult.setTASKID(task.getTASKID());
            detectionResult.setDEVID(task.getDEVID());
            detectionResult.setDEVCLASS(task.getDEVCLASS());
            detectionResult.setJIANCHAXIANGTITLE(listData.get(position).getJIANCHAXIANGTITLE());
            detectionResult.setLAW(listData.get(position).getLAW());
            detectionResults.add(detectionResult);
        }

        detectionTAdapter = new DetectionTActivity.DetectionTAdapter();
        lv_detection_t.setAdapter(detectionTAdapter);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_save_detection_t:
                save();
                break;
            case R.id.btn_error_detection_t:
                error();
                break;
            case R.id.btn_submit_detection_t:
                submit();
                break;
        }
    }

    //提交
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void submit(){
        List<DetectionResult> list = detectionResults.stream().filter(dete -> dete.getSTATUS().equals("2")).collect(Collectors.toList());
        if(list.size() != 0) {
            Toasty.warning(DetectionTActivity.this,"有未操作项，无法提交", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(DetectionTActivity.this,PDFActivity.class);
                    intent.putExtra("listData",(Serializable)detectionResults);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }
    //暂存
    public void save(){
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
                intent.putExtra("position",pposition);
                Log.e("message",filePathResult.getMessage());
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    //跳转到整改界面
    public void error(){
        Intent intent = new Intent(DetectionTActivity.this,RectifyActivity.class);
        intent.putExtra("errorList", (Serializable) errorDetectionResults);
        startActivityForResult(intent,REQUESTRECTIFY);
    }


    class DetectionTAdapter extends BaseAdapter {

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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(DetectionTActivity.this, R.layout.test_list_item, null);
            viewHolder = new DetectionTActivity.ViewHolder();
            viewHolder.detction_item_text_context= (TextView) convertView.findViewById(R.id.detction_item_text_context);
            viewHolder.detction_item_text_law= (TextView) convertView.findViewById(R.id.detction_item_text_law);
            viewHolder.tv_item_num= (TextView) convertView.findViewById(R.id.tv_item_num);
            viewHolder.detction_item_text_num= (TextView) convertView.findViewById(R.id.detction_item_text_num);
            viewHolder.detction_item_image_check= (ImageView) convertView.findViewById(R.id.detction_item_image_check);
            viewHolder.detction_item_image_attach= (ImageView) convertView.findViewById(R.id.detction_item_image_attach);
            viewHolder.detction_item_image_right= (ImageView) convertView.findViewById(R.id.detction_item_image_right);
            convertView.setTag(viewHolder);

            viewHolder.detction_item_text_context.setText(detectionResults.get(position).getJIANCHAXIANGTITLE());
            viewHolder.detction_item_text_law.setText(detectionResults.get(position).getLAW());
            viewHolder.tv_item_num.setText(position+1+"");
            viewHolder.detction_item_text_num.setVisibility(View.INVISIBLE);
            viewHolder.detction_item_image_check.setVisibility(View.INVISIBLE);
            viewHolder.detction_item_image_attach.setVisibility(View.INVISIBLE);
            viewHolder.detction_item_image_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toasty.warning(DetectionTActivity.this,"即将跳转"+position).show();
                    jumpToSuggesstionActivity(position);
                }
            });

            viewHolder.detction_item_image_attach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toasty.warning(DetectionTActivity.this,position+"有附件，要查看吗？").show();
                }
            });

            if(!detectionResults.get(position).getSTATUS().equals("2")){
                viewHolder.detction_item_image_check.setVisibility(View.VISIBLE);
            }
            if(detectionResults.get(position).getISHAVEDETAIL().equals("1")){
                viewHolder.detction_item_image_attach.setVisibility(View.VISIBLE);
            }




            return convertView;
        }


        private void jumpToSuggesstionActivity(int position) {
            String noItem = listData.get(position).getJIANCHAXIANGCONTENT();
            Intent intent = new Intent(DetectionTActivity.this, NewSuggestActivity.class);
            intent.putExtra("suggestion",noItem);
            intent.putExtra("path",task.getTASKID());
            intent.putExtra("position",position);
            intent.putExtra("detectionResult",detectionResults.get(position));
            intent.putExtra("title",title);//需要传递status
            intent.putExtra("taskType",taskType);
            intent.putExtra("status",detectionResults.get(position).getSTATUS());
            startActivityForResult(intent,RequestCor);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCor) {
                int position = data.getIntExtra("position", 0);
                Log.e("position", position + "-");
                int imageNumber = data.getIntExtra("imageNumber", 0);
                Log.e("imageNumber", imageNumber + "-");
                int videoNumber = data.getIntExtra("videoNumber", 0);
                Log.e("videoNumber", videoNumber + "-");
                String content = data.getStringExtra("content");
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
                if(!(content.trim().toString().equals(""))){
                    detectionResults.get(position).setSUGGESTION(content);
                }
                detectionResults.get(position).setSTATUS(status);
                if(status.equals("1")){
                    if(!(errorDetectionResults.contains(detectionResults.get(position)))) {
                        errorDetectionResults.add(detectionResults.get(position));
                        numberError.add(position);
                    }

                }
                if(errorDetectionResults.size()>0){
                    btn_error_detection_t.setText("问题("+errorDetectionResults.size()+")");
                }
                lv_detection_t.setAdapter(detectionTAdapter);
            }
            if(requestCode == REQUESTRECTIFY){
                List<DetectionResult> list = (List<DetectionResult>) data.getSerializableExtra("errorList");

                Log.e("tttt",numberError.size()+"");
                for(int i=0;i<numberError.size();i++){
                    int pposition = numberError.get(i);
                    detectionResults.get(pposition).setISCHANGED(list.get(i).getISCHANGED());
                }

                lv_detection_t.setAdapter(detectionTAdapter);


            }
        }
    }

    class ViewHolder {
        private TextView detction_item_text_context;
        private TextView detction_item_text_law;
        private TextView tv_item_num;
        private TextView detction_item_text_num;
        private ImageView detction_item_image_check;
        private ImageView detction_item_image_attach;
        private ImageView detction_item_image_right;
    }
}
