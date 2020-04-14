package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.test.R;
import com.neu.test.adapter.RectifyListItemAdapter;
import com.neu.test.adapter.SuggestionGridViewAdapter;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.GPSUtil;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.PhoneInfoUtils;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SidebarUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class RectifyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RectifyActivity";
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
    private String isHege ="";
    private PromptDialog promptDialog;
    private PermissionUtils permissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rectify);
        promptDialog = new PromptDialog(this);
        permissionUtils = new PermissionUtils(this,RectifyActivity.this,null,null);
        gpsUtil = new GPSUtil(this);
        gpsUtil.startLocate();
        deleteIndex = -1;

        initview();

        PhoneInfoUtils phoneInfoUtils = new PhoneInfoUtils(RectifyActivity.this,this);
        phonenumber = phoneInfoUtils.getNativePhoneNumber();


        intentByPreviousActivity = getIntent();
        toolbarTitle = getIntent().getStringExtra("toolbarTitle");
        toolbarTaskType = getIntent().getStringExtra("toolbarTaskType");
        positionSelected = getIntent().getIntExtra("position",-1);
        detectionResult = (DetectionResult) getIntent().getSerializableExtra("detectionResult");
        isHege = getIntent().getStringExtra("isHege");
        Log.e(TAG, "onCreate: ishege "+isHege);

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
        //设置建议的内容
        //如果是整改界面则  只显示内容不让输入
        if (toolbarTaskType.equals(searchUtil.recify)){
            editText_rectify_content.setHint("");
            editText_rectify_content.setText(detectionResult.getSUGGESTION());
            editText_rectify_content.setEnabled(false);
        }
        editText_rectify_action.setHint("请输入整改措施...");
        editText_rectify_result.setHint("请输入整改情况...");
        Log.e(TAG, "initRecifyContent: "+detectionResult.getSTATUS() );
        Log.e(TAG, "initRecifyContent: "+detectionResult.getCHANGEDIMAGE() );
        Log.e(TAG, "initRecifyContent: "+detectionResult.getCHANGEDVIDEO() );
        Log.e(TAG, "initRecifyContent: "+detectionResult.getREFJVI());
        Log.e(TAG, "initRecifyContent: "+detectionResult.getREFJIM());
        Log.e(TAG, "initRecifyContent: "+detectionResult.getISHAVEDETAIL());
        Log.e(TAG, "initRecifyContent: "+detectionResult.getISCHANGED());
        //对保存的内容进行查看

        if ((detectionResult.getISCHANGED().equals(searchUtil.changed))){
            editText_rectify_action.setText(detectionResult.getCHANGEDACTION());
            textView_finish_time.setText(detectionResult.getCHANGEDFINISHTIME());
            editText_rectify_result.setText(detectionResult.getCHANGEDRESULT());
            String pathOfImage = "";
            String pathOfVideo = "";
            pathOfImage = detectionResult.getCHANGEDIMAGE();
            pathOfVideo = detectionResult.getCHANGEDVIDEO();

            //将设置图片地址的代码放到一个统一的文件里面 在SuggestionActivity中也一样
            ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();
            pathlistOfPhoto = reloadImageAndVideo.getPathlist(pathOfImage,pathOfVideo,detectionResult.getLOGINNAME());

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
                SidebarUtils.initSelectRecityTime(RectifyActivity.this,textView_finish_time);
                break;
            case R.id.rectify_result_item_submit_button:
                //在点击提交之前把整改的内容 添加到选中的detectionResult的相应属性中取
                if (!textView_finish_time.getText().toString().isEmpty()){

                    if (pathlistOfPhoto.size() == 0){
                        setDetectionResult();
                        intentByPreviousActivity.putExtra("detectionResult",detectionResult);
                        intentByPreviousActivity.putExtra("position",positionSelected);
                        setResult(RESULT_OK,intentByPreviousActivity);
                        finish();
                    }else {
                        promptDialog.showLoading("上传图片中...");
                        setDetectionResult();
                        postFiles("text",pathlistOfPhoto);
                    }

                }else {
                    Toasty.info(getApplicationContext(),"对不起，您没有选择时间！",Toasty.LENGTH_SHORT).show();
                }
                break;
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
        //Log.e(TAG, " pathlistOfPhoto: "+ pathOfPhotos.size());
        okHttp.postFilesByPost(url, taskItem, pathOfPhotos, new FileResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                promptDialog.dismiss();
                System.out.println(e.getMessage());
                Toasty.warning(RectifyActivity.this,"客官，网络不给力",Toast.LENGTH_SHORT,true).show();
            }

            @Override
            public void onResponse(FilePathResult response, int id) {
                if (response.getMessage() != null){
                    Log.d(TAG," getMessage: "+response.getMessage());//文件长传成功
                    if (response.getMessage().equals("结果上传成功")) {//Result upload Success!
                        Log.d(TAG," 文件上传成功");//文件长传成功
                        Toasty.success(RectifyActivity.this,"文件上传成功！",Toast.LENGTH_SHORT,true).show();
                        Log.e(TAG," imagenumber: "+response.imageNumber);
                        Log.e(TAG," videonumber: "+response.videoNumber);
                        intentByPreviousActivity.putExtra("detectionResult",detectionResult);
                        intentByPreviousActivity.putExtra("position",positionSelected);

                        setResult(RESULT_OK,intentByPreviousActivity);
                        finish();
                    } else if (response.getMessage().equals("结果上传失败")){
                        promptDialog.dismiss();
                        Toasty.error(RectifyActivity.this, "文件上传失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void setDetectionResult() {
        //设置地理位置和手机号
        //Log.e(TAG,"   local:  "+gpsUtil.getLocal());  通过这个获取地理位置

        detectionResult.setCHANGEDACTION(editText_rectify_action.getText().toString());
        detectionResult.setCHANGEDFINISHTIME(textView_finish_time.getText().toString());
        detectionResult.setCHANGEDRESULT(editText_rectify_result.getText().toString());
        detectionResult.setSUGGESTION(editText_rectify_content.getText().toString());
        detectionResult.setISHAVEDETAIL(searchUtil.haveDetail);
        //整改合格指的是立即整改合格，所以照片和视频都放在这个属性里
        detectionResult.setISCHANGED(searchUtil.changed);
        detectionResult.setCHANGEDIMAGE(ImagePath);
        detectionResult.setCHANGEDVIDEO(VideoPath);

        detectionResult.setPHONE(phonenumber);
        detectionResult.setLONGITUDE(gpsUtil.getLongitude());
        detectionResult.setLATITUDE(gpsUtil.getLatitude());

        if (isHege.equals(searchUtil.recifyQualify)){
            detectionResult.setSTATUS(searchUtil.recifyQualify);
        }else if (isHege.equals(searchUtil.nohege)){
            detectionResult.setSTATUS(searchUtil.nohege);
        }


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
                break;
            case 2:
                if (permissionUtils.canGoNextstep()){
                    Intent intentall = new Intent(RectifyActivity.this,PhotoVideoActivity.class);
                    intentall.putExtra("username",detectionResult.getLOGINNAME());
                    startActivityForResult(intentall,REQUEST_TEST);
                }else {
                    permissionUtils.getPermission();
                }

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

    @Override
    protected void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
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
                Log.e(TAG, "onActivityResult: "+ImagePath );

            }

        }

    }



}
