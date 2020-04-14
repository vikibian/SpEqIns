package com.neu.test.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.test.R;
import com.neu.test.adapter.SuggestionGridViewAdapter;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class NewSuggestActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_CHOOSE = 23;
    private TextView tv_pcxmmc;
    private TextView tv_pcyq;
    private CheckBox cb_status;
    private CheckBox cb_status_0;
    private CheckBox cb_status_1;
    private EditText et_result_write;
    private GridView new_suggest_gv;
    private Button tijiao;
    private Button paizhao;

    private int position;
    private DetectionResult detectionResult;
    private Intent intent;
    public List<String> pathlistOfPhoto = new ArrayList<>();
    private int deleteIndex;
    private String Path;
    final int REQUEST_TEST = 66;
    public static String videoPath = new String();
    private  List<String> testpathlistOfPhoto = new ArrayList<String>();
    private String testvideoPath = " ";
    private SuggestionGridViewAdapter suggestionGridViewAdapter;

    private String totalPath = "";
    private String ImagePath;
    private String VideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_suggest);

        initView();
        changeView();
        setOnClickListener();
        if(detectionResult.getSUGGESTION() != null){
            et_result_write.setText(detectionResult.getSUGGESTION());
        }
        if(detectionResult.getSTATUS().equals("2")){
            cb_status.setChecked(true);
            cb_status_0.setChecked(false);
            cb_status_1.setChecked(false);
        }
        else if(detectionResult.getSTATUS().equals("1")){
            cb_status.setChecked(false);
            cb_status_0.setChecked(false);
            cb_status_1.setChecked(true);
        }
        else{
            cb_status.setChecked(false);
            cb_status_0.setChecked(true);
            cb_status_1.setChecked(false);
        }
        if(pathlistOfPhoto.size()>0){
            suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,1);
            new_suggest_gv.setAdapter(suggestionGridViewAdapter);
        }
    }


    public void initView(){
        tv_pcxmmc = findViewById(R.id.tv_pcxmmc);
        tv_pcyq = findViewById(R.id.tv_pcyq);
        cb_status = findViewById(R.id.cb_status);
        cb_status_0 = findViewById(R.id.cb_status_0);
        cb_status_1 = findViewById(R.id.cb_status_1);
        et_result_write = findViewById(R.id.et_result_write);
        new_suggest_gv = findViewById(R.id.new_suggest_gv);
        paizhao = findViewById(R.id.paizhao);
        tijiao = findViewById(R.id.tijiao);

        intent = getIntent();
        position = intent.getIntExtra("position",0);
        detectionResult = (DetectionResult) intent.getSerializableExtra("detectionResult");

        ImagePath = detectionResult.getREFJIM();
        Log.e("ImagePath",ImagePath);
        VideoPath = detectionResult.getREFJVI();

        if(!(ImagePath.equals(""))){
            String[] imgSplit = ImagePath.split(",");
            Log.e("lenth",imgSplit.length+"");
            for (int i=0;i<imgSplit.length;i++){
                String imgPath = Environment.getExternalStorageDirectory() +"/DCIM/"+detectionResult.getLOGINNAME()+"/Photo/"+imgSplit[i];
                File file = new File(imgPath);
                //本地有，从本地读取
                if(file.exists()){
                    pathlistOfPhoto.add(imgPath);
                }
                //若本地没有，从服务器获取
            }
        }
        if(!(VideoPath.equals(""))){
            String[] vdoSplit = VideoPath.split(",");
            for (int i=0;i<vdoSplit.length;i++){
                String vdoPath = Environment.getExternalStorageDirectory() +"/DCIM/"+detectionResult.getLOGINNAME()+"/Video/"+vdoSplit[i];
                File file = new File(vdoPath);
                //本地有，从本地读取
                if(file.exists()){
                    pathlistOfPhoto.add(vdoPath);
                }
            }
        }

        Path = intent.getStringExtra("path");//保存图片视频到服务器上的名
    }
    public void changeView(){
        tv_pcxmmc.setText(detectionResult.getJIANCHAXIANGTITLE());
        tv_pcyq.setText(detectionResult.getCHECKCONTENT());
    }
    public void setOnClickListener(){
        cb_status.setOnClickListener(this);
        cb_status_0.setOnClickListener(this);
        cb_status_1.setOnClickListener(this);
        paizhao.setOnClickListener(this);
        tijiao.setOnClickListener(this);
        new_suggest_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteIndex = position;
                if (pathlistOfPhoto.size()==position){
                    view.showContextMenu();
                }else {
                    view.showContextMenu();
                }


            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paizhao:
                Intent intentall = new Intent(NewSuggestActivity.this,PhotoVideoActivity.class);
                intentall.putExtra("username",detectionResult.getLOGINNAME());
                startActivityForResult(intentall,REQUEST_TEST);
                break;
            case R.id.tijiao:
                if (pathlistOfPhoto.size() == 0) {
                    Toasty.warning(NewSuggestActivity.this,"没有选择图片或视频！", Toast.LENGTH_LONG,true).show();
                    intent.putExtra("position",position);
                    intent.putExtra("imageNumber",0);
                    intent.putExtra("videoNumber",0);
                    intent.putExtra("status",detectionResult.getSTATUS());
                    intent.putExtra("content",et_result_write.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    //提交数据   上传图片
                    postFiles(et_result_write.getText().toString(),pathlistOfPhoto);
                }
                break;
            case R.id.new_suggest_gv:
                break;
            case R.id.cb_status:
                detectionResult.setSTATUS("2");
                cb_status.setChecked(true);
                cb_status_0.setChecked(false);
                cb_status_1.setChecked(false);
                break;
            case R.id.cb_status_0:
                detectionResult.setSTATUS("0");
                cb_status.setChecked(false);
                cb_status_0.setChecked(true);
                cb_status_1.setChecked(false);
                break;
            case R.id.cb_status_1:
                detectionResult.setSTATUS("1");
                cb_status.setChecked(false);
                cb_status_0.setChecked(false);
                cb_status_1.setChecked(true);
                break;
        }
    }


    private void postFiles(String text, List<String> pathOfPhotos) {

        String url = BaseUrl.BaseUrl+"testServlet";
        Map<String,String> taskItem = new HashMap<String, String>();
        //taskItem.put("path",Path+position);
        taskItem.put("path",detectionResult.getLOGINNAME());
        OkHttp okHttp = new OkHttp();
        okHttp.postFilesByPost(url, taskItem, pathOfPhotos, new FileResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Toasty.warning(NewSuggestActivity.this,"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(FilePathResult response, int id) {
                if (response.getMessage() != null){
                    if (response.getMessage().equals("??????")) {//Result upload Success!
                        Toasty.success(NewSuggestActivity.this,"文件上传成功！",Toast.LENGTH_SHORT,true).show();
                        //Intent intent = getIntent();
                        intent.putExtra("position",position);
                        intent.putExtra("imageNumber",response.imageNumber);
                        Log.e("imageNumber",response.imageNumber+"-");
                        intent.putExtra("videoNumber",response.videoNumber);
                        Log.e("position",response.videoNumber+"-");
                        intent.putExtra("content",et_result_write.getText().toString());
                        intent.putExtra("status",detectionResult.getSTATUS());
//            int imageLength = ImagePath.length();
//            int videoLength = VideoPath.length();

                        intent.putExtra("ImagePath",ImagePath);
                        intent.putExtra("VideoPath",VideoPath);

                        setResult(RESULT_OK,intent);
                        finish();
                    } else if (response.getMessage().equals("结果上传失败")){
                        Toasty.error(NewSuggestActivity.this, "失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //测试多张图片显示
        String imgString = new String();
        String videoString = new String();

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_TEST) {

                imgString = data.getStringExtra("ImagePath");
                testvideoPath =data.getStringExtra("VideoPath");
                videoString =data.getStringExtra("VideoPath");
                //不需要旋转90度  需要在设置图片的时候进行判断

                //在此处需要更新图片数组
                if(!(imgString.equals(""))){
                    ImagePath += imgString+",";
                    imgString = Environment.getExternalStorageDirectory() +"/DCIM/"+detectionResult.getLOGINNAME()+"/Photo/"+imgString;
                    pathlistOfPhoto.add(imgString);
                }
                if(!(testvideoPath.equals(""))){
                    VideoPath += videoString+",";
                    videoString = Environment.getExternalStorageDirectory() +"/DCIM/"+detectionResult.getLOGINNAME()+"/Video/"+videoString;
                    pathlistOfPhoto.add(videoString);
                }
                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,1);
                new_suggest_gv.setAdapter(suggestionGridViewAdapter);
            }
        }
        suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
        new_suggest_gv.setAdapter(suggestionGridViewAdapter);
    }
}
