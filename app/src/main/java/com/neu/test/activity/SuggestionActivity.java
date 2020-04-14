package com.neu.test.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.neu.test.R;
import com.neu.test.adapter.SuggestionGridViewAdapter;
import com.neu.test.entity.DetectionResult;
import com.neu.test.entity.FilePathResult;
import com.neu.test.entity.LocationService;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;

import com.neu.test.util.BaseActivity;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.GPSUtil;
import com.neu.test.util.PermissionUtils;
import com.neu.test.util.PhoneInfoUtils;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SuggestionActivitySaveDataUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;

public class SuggestionActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "SuggestionActivity";
    private static final int REQUEST_CODE_CHOOSE = 23;

    private ImageView iv_photo;
    private ImageView iv_video;
    private ImageView iv_plus;
    private LocationService locationService;
    private TextView tv_suggetion;
    //private Button btn_get_gps;//获取定位
    private Button btn_submit_suggestion;//提交结果
    private TextView tv_mygps;//显示定位
    private EditText et_suggestion;
    private TextView tv_num;
//    private TextView textview_phoneNumber;
    private LinearLayout suggestion_dangerandrecify;
    private CheckBox suggestion_danger_great;
    private CheckBox suggestion_danger_normal;
//    private CheckBox suggestion_recify_way_now;
    private CheckBox suggestion_recify_way_limit;
    private CheckBox suggestion_recify_way_stop;

    final int RequestCor = 521;
    final int maxNum = 500;
    final int REQUEST_TEST = 66;
    public int position = 0;

    private String ImagePath = "";
    private String VideoPath = "";
    private DetectionResult detectionResult;
    private Intent intent;

    //测试多张图片显示，测试Gridview组件
    private GridView gridView;
    private SuggestionGridViewAdapter suggestionGridViewAdapter;
    public  List<String> pathlistOfPhoto = new ArrayList<>();
    //测试视频和图片的显示分开，因为在缩小时他们的方向不同，旋转的角度不同
    public static String videoPath = new String();

    private  List<String> testpathlistOfPhoto = new ArrayList<>();
    private String testvideoPath = " ";
    private int deleteIndex;

    //设置toolbar
    private Toolbar toolbar_suggestion;
    private TextView toolbar_title;
    private TextView toolabr_subtitleLeft;
    private TextView toolbar_subtitleRight;


    private String devclass = "";//接收从DetctionActivity传过来的DEVCLASS
    private String suggestion = "";//接收从DetctionActivity传过来的检查项
    private String title = "";//接收从DetctionActivity传过来的标题
    private String taskType = "";
    private String Path = "";
    private String status = "0";//该属性代表的是点击的哪个状态
    private String phonenumber = "";
    private SuggestionActivitySaveDataUtil saveDataUtil ;
    private SearchUtil searchUtil = new SearchUtil();
    private GPSUtil gpsUtil;
    private PermissionUtils permissionUtils;
    private PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        promptDialog = new PromptDialog(this);
        permissionUtils = new PermissionUtils(this,SuggestionActivity.this,null,null);
        gpsUtil = new GPSUtil(this);
        gpsUtil.startLocate();
        saveDataUtil = new SuggestionActivitySaveDataUtil(SuggestionActivity.this);
        deleteIndex = -1;
        init();

        PhoneInfoUtils phoneInfoUtils = new PhoneInfoUtils(SuggestionActivity.this,this);
        phonenumber = phoneInfoUtils.getNativePhoneNumber();
        Log.e(TAG, "onCreate: shoujihao "+phonenumber);



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

        btn_submit_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (suggestion_dangerandrecify.getVisibility() == View.VISIBLE){
                    if ((!suggestion_danger_great.isChecked())&&(!suggestion_danger_normal.isChecked())){
                        Toasty.info(getApplicationContext(),"对不起，您没有选择隐患等级！",Toasty.LENGTH_SHORT).show();
                    }else {
                        if ((!suggestion_recify_way_limit.isChecked())&&
                                (!suggestion_recify_way_stop.isChecked())){
                            Toasty.info(getApplicationContext(),"对不起，您没有选择整改方式！",Toasty.LENGTH_SHORT).show();
                        }else {
                            submit();
                        }
                    }
                }else {
                    submit();
                }
            }
        });


        et_suggestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_num.setText("剩余字数："+ (maxNum-s.length()));
            }
        });


        if(pathlistOfPhoto.size()>0){
            suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,1);
            gridView.setAdapter(suggestionGridViewAdapter);
        }

    }

    private void submit(){
        if (pathlistOfPhoto.size() == 0) {
            Toasty.warning(SuggestionActivity.this,"没有选择图片或视频！", Toast.LENGTH_LONG,true).show();
            resetAttribute();
            intent.putExtra("detectionResult",detectionResult);
            intent.putExtra("position",position);
            intent.putExtra("imageNumber",0);
            intent.putExtra("videoNumber",0);
            intent.putExtra("status",status);
            Log.e("status7",status);
            intent.putExtra("content",et_suggestion.getText().toString());
            intent.putExtra("phone",phonenumber);
            intent.putExtra("longitude",gpsUtil.getLongitude());
            intent.putExtra("laitude",gpsUtil.getLatitude());


            if (status.equals(searchUtil.nohege)){
                setLevelAndWay();
            }

            setResult(RESULT_OK,intent);
            finish();
        }else {
            promptDialog.showLoading("上传图片中...");
            //提交数据   上传图片
            postFiles(et_suggestion.getText().toString(),pathlistOfPhoto);
        }
    }

    private void init() {
//        textview_phoneNumber = findViewById(R.id.suggestion_phonenumber);
        et_suggestion = findViewById(R.id.et_suggestion);//建议文本框
        tv_num = findViewById(R.id.tv_num);//计算字数框
        //定位
        tv_suggetion = findViewById(R.id.tv_suggetion);//显示不合格项
        //btn_get_gps = findViewById(R.id.btn_get_gps);//获取定位按钮
        btn_submit_suggestion = findViewById(R.id.btn_submit_suggestion);
        toolbar_suggestion = findViewById(R.id.toolbar_suggestion);
        suggestion_dangerandrecify = findViewById(R.id.suggestion_dangerandrecify);
        suggestion_danger_great = findViewById(R.id.suggestion_danger_great);
        suggestion_danger_normal = findViewById(R.id.suggestion_danger_normal);
//        suggestion_recify_way_now = findViewById(R.id.suggestion_rectify_way_now);
        suggestion_recify_way_limit = findViewById(R.id.suggestion_rectify_way_limit);
        suggestion_recify_way_stop = findViewById(R.id.suggestion_rectify_way_stop);

        suggestion_danger_great.setOnClickListener(this);
        suggestion_danger_normal.setOnClickListener(this);
//        suggestion_recify_way_now.setOnClickListener(this);
        suggestion_recify_way_limit.setOnClickListener(this);
        suggestion_recify_way_stop.setOnClickListener(this);


        //获取drawble目录下的plus图片  但是好像获取的文件有问题， 设置一个空的String字符也可以 在Adapter设置图片显示的那里已经实现了
        Resources resources = getResources();
        //String path = resources.getResourceTypeName(R.drawable.plus) + "/" + resources.getResourceEntryName(R.drawable.plus)+".png";
        String path = resources.getResourceTypeName(R.drawable.plus1) + "/" + resources.getResourceEntryName(R.drawable.plus1)+".png";

        pathlistOfPhoto = new ArrayList<>();
        //测试多张图片显示效果
        gridView = findViewById(R.id.suggestion_gridview);
        suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
        gridView.setAdapter(suggestionGridViewAdapter);
        //Collections.reverse(pathlistOfPhoto);



        intent = getIntent();
        position = intent.getIntExtra("position",0);
        status = intent.getStringExtra("status");
        Log.e(TAG,"  接收的stus："+status);
        detectionResult = (DetectionResult) intent.getSerializableExtra("detectionResult");
        ImagePath = detectionResult.getREFJIM();
        VideoPath = detectionResult.getREFJVI();
        et_suggestion.setText(detectionResult.getSUGGESTION());


        //将设置图片地址的代码放到一个统一的文件里面 在SuggestionActivity中也一样
        ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();
        pathlistOfPhoto = reloadImageAndVideo.getPathlist(ImagePath,VideoPath,detectionResult.getLOGINNAME());


        tv_suggetion.setText(detectionResult.getJIANCHAXIANGTITLE());  //写下不合格的检查项

        //设置标题栏
        title = intent.getStringExtra("title");
        taskType = intent.getStringExtra("taskType");
        Log.e(TAG,"获取title："+title);
        Log.e(TAG,"获取taskType："+taskType);
//

        if (status.equals(searchUtil.nohege)){
            suggestion_dangerandrecify.setVisibility(View.VISIBLE);
            et_suggestion.setHint("请输入不合格原因...");

            if (!detectionResult.getYINHUANLEVEL().isEmpty()){
                if (detectionResult.getYINHUANLEVEL().equals("重大隐患")){
                    suggestion_danger_great.setChecked(true);
                    suggestion_danger_normal.setChecked(false);
                }else if (detectionResult.getYINHUANLEVEL().equals("一般隐患")){
                    suggestion_danger_normal.setChecked(true);
                    suggestion_danger_great.setChecked(false);
                }

                if (detectionResult.getCHANGEDWAY().equals("限期整改")){
                    suggestion_recify_way_limit.setChecked(true);
                    suggestion_recify_way_stop.setChecked(false);
                } else if (detectionResult.getCHANGEDWAY().equals("停产整改")){
                    suggestion_recify_way_limit.setChecked(false);
                    suggestion_recify_way_stop.setChecked(true);
                }

            }

        }else if (status.equals(searchUtil.hege)){
            suggestion_dangerandrecify.setVisibility(View.GONE);
            et_suggestion.setHint("请输入合格描述...");
        }

        toolbar_title = findViewById(R.id.toolbar_suggestion_title);
        toolabr_subtitleLeft = findViewById(R.id.toolbar_suggestion_subtitle_left);
        toolbar_subtitleRight = findViewById(R.id.toolbar_suggestion_subtitle_right);
        toolbar_title.setTextSize(18);
        toolabr_subtitleLeft.setTextSize(13);
        toolbar_subtitleRight.setTextSize(13);
        toolbar_title.setText(taskType+"           ");//加空格的原因是让其显示居中
        toolabr_subtitleLeft.setText(title);
        toolbar_subtitleRight.setText(taskType);
        toolbar_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

        setSupportActionBar(toolbar_suggestion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.suggestion_danger_great:
                suggestion_danger_normal.setChecked(false);
                break;
            case R.id.suggestion_danger_normal:
                suggestion_danger_great.setChecked(false);
                break;
//            case R.id.suggestion_rectify_way_now:
//                suggestion_recify_way_limit.setChecked(false);
//                suggestion_recify_way_stop.setChecked(false);
//                break;
            case R.id.suggestion_rectify_way_limit:
//                suggestion_recify_way_now.setChecked(false);
                suggestion_recify_way_stop.setChecked(false);
                break;
            case R.id.suggestion_rectify_way_stop:
                suggestion_recify_way_limit.setChecked(false);
//                suggestion_recify_way_now.setChecked(false);
                break;
        }
    }

    private void postFiles(String text, List<String> pathOfPhotos) {

        String url = BaseUrl.BaseUrl+"testServlet";
        Log.d(TAG,"POST url: "+url);

        Map<String,String> taskItem = new HashMap<String, String>();
        taskItem.put("path",detectionResult.getLOGINNAME());

        OkHttp okHttp = new OkHttp();
        okHttp.postFilesByPost(url, taskItem, pathOfPhotos, new FileResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                promptDialog.dismiss();
                Toasty.warning(SuggestionActivity.this,"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(FilePathResult response, int id) {
                if (response.getMessage() != null){
                        Log.d(TAG," getMessage: "+response.getMessage());//文件长传成功
                    if (response.getMessage().equals("结果上传成功")) {//Result upload Success!
                        Log.d(TAG," 文件上传成功");//文件长传成功
                        Toasty.success(SuggestionActivity.this,"文件上传成功！",Toast.LENGTH_SHORT,true).show();
                        resetAttribute();
                        intent.putExtra("detectionResult",detectionResult);
                        intent.putExtra("position",position);
                        intent.putExtra("imageNumber",response.imageNumber);
                        intent.putExtra("videoNumber",response.videoNumber);
                        intent.putExtra("content",et_suggestion.getText().toString());
                        intent.putExtra("ImagePath",ImagePath);
                        intent.putExtra("VideoPath",VideoPath);
                        intent.putExtra("status",status);

                        intent.putExtra("phone",phonenumber);
                        intent.putExtra("longitude",gpsUtil.getLongitude());
                        intent.putExtra("laitude",gpsUtil.getLatitude());


                        if (status.equals(searchUtil.nohege)){
                            setLevelAndWay();
                        }

                        setResult(RESULT_OK,intent);
                        finish();
                    } else if (response.getMessage().equals("结果上传失败")){
                        promptDialog.dismiss();
                        Toasty.error(SuggestionActivity.this, "文件上传失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void resetAttribute() {
        //已经选择不合格 但是需要重新选择合格
        if (status.equals(searchUtil.hege)&&(detectionResult.getSTATUS().equals(searchUtil.nohege))){
            detectionResult.setYINHUANLEVEL("");
            detectionResult.setCHANGEDWAY("");
        }
        //已经选择整改合格 但是需要重新选择合格
        if (status.equals(searchUtil.hege)&&(detectionResult.getSTATUS().equals(searchUtil.recifyQualify))){
            detectionResult.setCHANGEDACTION("");
            detectionResult.setCHANGEDWAY("");
            detectionResult.setCHANGEDFINISHTIME("");
            detectionResult.setCHANGEDRESULT("");
        }
        //已经选择整改合格 但是需要重新选择不合格
        if (status.equals(searchUtil.nohege)&&(detectionResult.getSTATUS().equals(searchUtil.recifyQualify))){
            detectionResult.setCHANGEDACTION("");
            detectionResult.setCHANGEDFINISHTIME("");
            detectionResult.setCHANGEDRESULT("");
        }

    }

    private void setLevelAndWay() {
        String yinhuanlevel = "";
        if (suggestion_danger_great.isChecked()){
            yinhuanlevel = suggestion_danger_great.getText().toString();
        }else if (suggestion_danger_normal.isChecked()){
            yinhuanlevel = suggestion_danger_normal.getText().toString();
        }
        String changway = "";
        if (suggestion_recify_way_limit.isChecked()){
            changway = suggestion_recify_way_limit.getText().toString();
        }else if (suggestion_recify_way_stop.isChecked()){
            changway = suggestion_recify_way_stop.getText().toString();
        }
        intent.putExtra("yinhuanlevel",yinhuanlevel);
        intent.putExtra("changway",changway);
        Log.e(TAG, "  隐患等级: "+yinhuanlevel);
        Log.e(TAG, "  整改方式: "+changway);
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
                if(permissionUtils.canGoNextstep()){
                    Intent intentall = new Intent(SuggestionActivity.this,PhotoVideoActivity.class);
                    intentall.putExtra("username",detectionResult.getLOGINNAME());
                    startActivityForResult(intentall,REQUEST_TEST);
                }else{
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

    /**
     * @date : 2020/2/23
     * @time : 20:55
     * @author : viki
     * @description :获取相册
     */

    private void startPhotoGallery() {

        Matisse.from(SuggestionActivity.this)
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
                    pathlistOfPhoto.add(imgString);
                }
                if (!(testvideoPath.equals(""))) {
                    VideoPath += videoString + ",";
                    videoString = Environment.getExternalStorageDirectory() + "/DCIM/" + detectionResult.getLOGINNAME() + "/Video/" + videoString;
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
    //            List<Uri> result = Matisse.obtainResult(data);
    //            textView.setText(result.toString());
            }
        }

    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.e(TAG," onStart");
        // TODO Auto-generated method stub
        super.onStart();


    }


    @Override
    public void onResume() {
        super.onResume();
        promptDialog.dismissImmediately();
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
