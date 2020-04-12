package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.util.TextInfo;
import com.kongzue.dialog.v3.InputDialog;
import com.kongzue.dialog.v3.TipDialog;
import com.neu.test.R;
import com.neu.test.adapter.SuggestionGridViewAdapter;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.GPSUtil;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.PhoneInfoUtils;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;
import com.neu.test.util.SuggestionActivitySaveDataUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class RectifyResultActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RectifyResultActivity";
    private static final int REQUEST_CODE_CHOOSE = 23;
    private GridView gridView;
    private SuggestionGridViewAdapter suggestionGridViewAdapter;
    public List<String> pathlistOfPhoto = new ArrayList<>();

    private  List<String> testpathlistOfPhoto = new ArrayList<>();

    private TextView textView_item_title;

    private EditText editText_rectify_action;
    private TextView textView_finish_time;
    private EditText editText_rectify_result;
    private EditText editText_rectify_content;
    private Button button_submit;

    private LinearLayout recify_result_way_checkbox_lin;
    private int deleteIndex;

    private String testvideoPath = " ";
    private final int GET_PERMISSION_REQUEST = 110; //权限申请自定义码
    private boolean granted = false;
    final int RequestCor = 521;
    final int maxNum = 500;
    final int REQUEST_TEST = 66;
    public int position = 0;

    private String ImagePath = new String();
    private String VideoPath = new String();
    public DetectionResult detectionResult;
    private Intent intentByPreviousActivity;
    private int positionSelected;//获取从DetctionActivity中被点击的DetectionResult，当结束整改后将position返回 并更新

    private Toolbar toolbar;
    private TextView toolbar_textView;
    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;

    private String toolbarTitle ="";
    private String toolbarTaskType="";
    //将status的固定值设置在一个固定的位置
    private SearchUtil searchUtil = new SearchUtil();
    private String phonenumber ="";
    private GPSUtil gpsUtil;
    private String way = "立即整改";
    private String status = "3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify_result);
        gpsUtil = new GPSUtil(this);
        gpsUtil.startLocate();
        deleteIndex = -1;

        initview();

        PhoneInfoUtils phoneInfoUtils = new PhoneInfoUtils(RectifyResultActivity.this,this);
        phonenumber = phoneInfoUtils.getNativePhoneNumber();


        intentByPreviousActivity = getIntent();
        detectionResult = (DetectionResult) getIntent().getSerializableExtra("detectionResult");
        positionSelected = getIntent().getIntExtra("position",-1);
        status = getIntent().getStringExtra("status");
        toolbarTitle = getIntent().getStringExtra("toolbarTitle");
        toolbarTaskType = getIntent().getStringExtra("toolbarTaskType");


        initRecifyContent();


        initToolbar();

        suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
        gridView.setAdapter(suggestionGridViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        gridView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//                menu.add(0,1,0,"相册");
                menu.add(0,2,0,"相机");
                menu.add(0,3,0,"取消");
                menu.add(0,4,0,"删除");
            }
        });
    }

    private void initRecifyContent() {
        //设置标题
        textView_item_title.setText(detectionResult.getJIANCHAXIANGTITLE());//detectionResult.getJIANCHAXIANGTITLE()
        editText_rectify_content.setHint("请输入问题描述...");
        editText_rectify_action.setHint("请输入整改措施...");
        editText_rectify_result.setHint("请输入整改情况...");
        Log.e(TAG, "initRecifyContent: "+detectionResult.getSTATUS() );
        Log.e(TAG, "initRecifyContent: "+detectionResult.getCHANGEDIMAGE() );
        Log.e(TAG, "initRecifyContent: "+detectionResult.getCHANGEDVIDEO() );
        Log.e(TAG, "initRecifyContent: "+detectionResult.getREFJVI());
        Log.e(TAG, "initRecifyContent: "+detectionResult.getREFJIM());
        Log.e(TAG, "initRecifyContent: "+positionSelected);

        //对保存的内容进行查看
        if (detectionResult.getSTATUS().equals(searchUtil.recifyQualify)){
            if ((detectionResult.getISHAVEDETAIL().equals(searchUtil.haveDetail))
                    &&(detectionResult.getISCHANGED().equals(searchUtil.changed))){
//                editText_rectify_way.setText(detectionResult.getCHANGEDWAY());
                //设置建议的内容
                editText_rectify_content.setText(detectionResult.getSUGGESTION());
                editText_rectify_action.setText(detectionResult.getCHANGEDACTION());
                textView_finish_time.setText(detectionResult.getCHANGEDFINISHTIME());
                editText_rectify_result.setText(detectionResult.getCHANGEDRESULT());

                ImagePath = detectionResult.getCHANGEDIMAGE();
                VideoPath = detectionResult.getCHANGEDVIDEO();
                Log.e(TAG, "initRecifyContent: image: "+ImagePath);
                Log.e(TAG, "initRecifyContent: video: "+VideoPath);
                //将设置图片地址的代码放到一个统一的文件里面 在SuggestionActivity中也一样
                ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();
                pathlistOfPhoto = reloadImageAndVideo.getPathlist(ImagePath,VideoPath,detectionResult.getLOGINNAME());

            }
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_rectify);

        toolbar_title = findViewById(R.id.toolbar_rectify_title);
        toolbar_subtitleLeft = findViewById(R.id.toolbar_rectify_subtitle_left);
        toolabr_subtitleRight = findViewById(R.id.toolbar_rectify_subtitle_right);

        toolbar_title.setTextSize(18);
        toolbar_subtitleLeft.setTextSize(13);
        toolabr_subtitleRight.setTextSize(13);

        toolbar_title.setText(toolbarTaskType+"             ");
        toolbar_subtitleLeft.setText(toolbarTitle);
        toolabr_subtitleRight.setText(toolbarTaskType);
        toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initview() {
        gridView = findViewById(R.id.rectify_result_gridview);
        textView_item_title = findViewById(R.id.rectify_result_item_title_textview);
//        editText_rectify_way = findViewById(R.id.rectify_result_item_way_textview);
        editText_rectify_action = findViewById(R.id.rectify_result_item_action_edittext);
        textView_finish_time = findViewById(R.id.rectify_result_item_finishtime_textview);
        editText_rectify_result = findViewById(R.id.rectify_result_item_rectifyresult);
        button_submit = findViewById(R.id.rectify_result_item_submit_button);

        editText_rectify_content = findViewById(R.id.rectify_result_item_content_edittext);


        textView_finish_time.setOnClickListener(this);
        button_submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rectify_result_item_finishtime_textview:
                SidebarUtils.initSelectRecityTime(RectifyResultActivity.this,textView_finish_time);
                break;
            case R.id.rectify_result_item_submit_button:
                //在点击提交之前把整改的内容 添加到选中的detectionResult的相应属性中取
                if (!textView_finish_time.getText().toString().isEmpty()){
                    Log.e(TAG, "onClick: 点击"+textView_finish_time.getText().toString());
                    if (pathlistOfPhoto.size() == 0){
                        setDetectionResult();
                        intentByPreviousActivity.putExtra("detectionResult",detectionResult);
                        intentByPreviousActivity.putExtra("position",positionSelected);
                        setResult(RESULT_OK,intentByPreviousActivity);
                        finish();
                    }else {
                        setDetectionResult();
                        postFiles("text",pathlistOfPhoto);
                    }
                }else {
                    Toasty.info(getApplicationContext(),"对不起，您没有选择时间！",Toasty.LENGTH_SHORT).show();
                }
                break;
//            case R.id.rectify_result_qualified:
//                recify_result_unqualified.setChecked(false);
//                recify_result_way_textview.setVisibility(View.VISIBLE);
//                recify_result_way_textview.setText(way);
//                recify_result_way_checkbox_lin.setVisibility(View.INVISIBLE);
//                recify_result_way_limit.setChecked(false);
//                recify_result_way_stop.setChecked(false);
//                break;
//            case R.id.rectify_result_unqualified:
//                recify_result_qualified.setChecked(false);
//                recify_result_way_textview.setVisibility(View.INVISIBLE);
//                recify_result_way_checkbox_lin.setVisibility(View.VISIBLE);
//                break;
//            case R.id.rectify_result_way_limit:
//                recify_result_way_stop.setChecked(false);
//                Toast.makeText(getApplicationContext(),recify_result_way_limit.getText(),Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.rectify_result_way_stop:
//                recify_result_way_limit.setChecked(false);
//                Toast.makeText(getApplicationContext(),recify_result_way_stop.getText(),Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    private void postFiles(String text, List<String> pathOfPhotos) {

        String url = BaseUrl.BaseUrl+"testServlet";
        Log.d(TAG,"POST url: "+url);

        Map<String,String> taskItem = new HashMap<String, String>();
        taskItem.put("path",detectionResult.getLOGINNAME());



        OkHttp okHttp = new OkHttp();
        Log.e(TAG, " pathlistOfPhoto: "+ pathOfPhotos.size());
        okHttp.postFilesByPost(url, taskItem, pathOfPhotos, new FileResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Toasty.warning(RectifyResultActivity.this,"客官，网络不给力",Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onResponse(FilePathResult response, int id) {
                if (response.getMessage() != null){
                    Log.d(TAG," getMessage: "+response.getMessage());//文件长传成功
                    if (response.getMessage().equals("结果上传成功")) {//Result upload Success!
                        Log.d(TAG," 文件上传成功");//文件长传成功
                        Toasty.success(RectifyResultActivity.this,"文件上传成功！",Toast.LENGTH_SHORT,true).show();
                        Log.e(TAG," imagenumber: "+response.imageNumber);
                        Log.e(TAG," videonumber: "+response.videoNumber);
                        intentByPreviousActivity.putExtra("detectionResult",detectionResult);
                        intentByPreviousActivity.putExtra("position",positionSelected);
                        Log.e(TAG, "onResponse: 整改图片删除测试"+detectionResult.getCHANGEDIMAGE());
                        Log.e(TAG, "onResponse: 整改图片删除测试"+detectionResult.getSTATUS());
                        setResult(RESULT_OK,intentByPreviousActivity);
                        finish();
                    } else if (response.getMessage().equals("结果上传失败")){
                        Toasty.error(RectifyResultActivity.this, "文件上传失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void setDetectionResult() {

        //已经选择不合格 但是需要重新选择整改合格
        if (status.equals(searchUtil.recifyQualify)&&(detectionResult.getSTATUS().equals(searchUtil.nohege))){
            detectionResult.setYINHUANLEVEL("");
        }

        //整改合格默认的整改方式是：立即整改
        detectionResult.setCHANGEDWAY(way);
        //设置地理位置和手机号
        //Log.e(TAG,"   local:  "+gpsUtil.getLocal());  通过这个获取地理位置

        detectionResult.setCHANGEDACTION(editText_rectify_action.getText().toString());
        detectionResult.setCHANGEDFINISHTIME(textView_finish_time.getText().toString());
        detectionResult.setCHANGEDRESULT(editText_rectify_result.getText().toString());
        detectionResult.setSUGGESTION(editText_rectify_content.getText().toString());
        detectionResult.setISHAVEDETAIL(searchUtil.haveDetail);
        //整改合格指的是立即整改合格，所以照片和视频都放在这个属性里
        detectionResult.setISCHANGED(searchUtil.changed);
        detectionResult.setSTATUS(searchUtil.recifyQualify);
        detectionResult.setCHANGEDIMAGE(ImagePath);
        detectionResult.setCHANGEDVIDEO(VideoPath);

        detectionResult.setPHONE(phonenumber);
        detectionResult.setLONGITUDE(gpsUtil.getLongitude());
        detectionResult.setLATITUDE(gpsUtil.getLatitude());


    }

    /**
     * 虽然menu选项在SuggestionGridViewAdapter.java文件中但是一样可以使用这里的选择事件按钮
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 1:
                Log.e(TAG," item  "+item.getTitle());
                startPhotoGallery();
                break;
            case 2:
                Intent intentall = new Intent(RectifyResultActivity.this,PhotoVideoActivity.class);
                intentall.putExtra("username",detectionResult.getLOGINNAME());
                startActivityForResult(intentall,REQUEST_TEST);
                break;
            case 3:
                break;
            case 4:
                if (deleteIndex>=0&&deleteIndex<pathlistOfPhoto.size()){
                    pathlistOfPhoto.remove(deleteIndex);
                    suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,1);
                    gridView.setAdapter(suggestionGridViewAdapter);
                }else {
                    Toast.makeText(getApplicationContext(),"此图片不能删除",Toast.LENGTH_SHORT).show();
                }
                break;


        }
        return super.onContextItemSelected(item);
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
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * @date : 2020/2/23
     * @time : 20:55
     * @author : viki
     * @description :获取相册
     */

    private void startPhotoGallery() {

        Matisse.from(RectifyResultActivity.this)
                .choose(MimeType.ofAll())//图片类型
                .countable(true)//true:选中后显示数字;false:选中后显示对号
                .maxSelectable(5)//可选的最大数
                //.capture(true)//选择照片时，是否显示拍照
                //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；
                // 参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                //"com.neu.testimageload.fileprovider"
                .captureStrategy(new CaptureStrategy(true, "com.neu.test.fileprovider"))
                .imageEngine(new GlideEngine())//图片加载引擎
                .forResult(REQUEST_CODE_CHOOSE);//
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
                testvideoPath = data.getStringExtra("VideoPath");
                videoString = data.getStringExtra("VideoPath");
                //不需要旋转90度  需要在设置图片的时候进行判断

                //在此处需要更新图片数组
                if (!(imgString.equals(""))) {
                    ImagePath += imgString + ",";
                    imgString = Environment.getExternalStorageDirectory() + "/DCIM/" + detectionResult.getLOGINNAME() + "/Photo/" + imgString;
//                    imgString = Environment.getExternalStorageDirectory() + "/DCIM/" + LoginActivity.inputName + "/Photo/" + imgString;
                    pathlistOfPhoto.add(imgString);
                }
                if (!(testvideoPath.equals(""))) {
                    VideoPath += videoString + ",";
                    videoString = Environment.getExternalStorageDirectory() + "/DCIM/" + detectionResult.getLOGINNAME() + "/Video/" + videoString;
//                    videoString = Environment.getExternalStorageDirectory() + "/DCIM/" + LoginActivity.inputName + "/Video/" + videoString;
                    pathlistOfPhoto.add(videoString);
                }
                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto, 1);
                gridView.setAdapter(suggestionGridViewAdapter);

            }

            //Masstise返回的图片数据
            if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
                Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
                Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));
                Log.e("Matisse", "Use the selected photos with original: "+String.valueOf(Matisse.obtainOriginalState(data)));
                for (int i = 0;i<Matisse.obtainPathResult(data).size();i++){
                    pathlistOfPhoto.add(Matisse.obtainPathResult(data).get(i));
                }

                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
                gridView.setAdapter(suggestionGridViewAdapter);
            }
        }

    }
}
