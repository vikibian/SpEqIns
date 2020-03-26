package com.neu.test.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
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
import android.widget.VideoView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
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
import com.neu.test.entity.LocationService;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.entity.User;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.FileResultCallBack;
import com.neu.test.net.callback.ListTaskCallBack;

import com.neu.test.util.BaseUrl;
import com.neu.test.util.PhoneInfoUtils;
import com.neu.test.util.ReloadImageAndVideo;
import com.neu.test.util.SearchUtil;
import com.neu.test.util.SuggestionActivitySaveDataUtil;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;

public class SuggestionActivity extends AppCompatActivity implements View.OnClickListener {
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
    private TextView textview_phoneNumber;
    private LinearLayout suggestion_dangerandrecify;
    private CheckBox suggestion_danger_great;
    private CheckBox suggestion_danger_normal;
    private CheckBox suggestion_recify_way_now;
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
    private String status = "0";
    private String phonenumber = "";
    private SuggestionActivitySaveDataUtil saveDataUtil ;
    private SearchUtil searchUtil = new SearchUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        Log.e(TAG," oncreate");
        saveDataUtil = new SuggestionActivitySaveDataUtil(SuggestionActivity.this);
        deleteIndex = -1;
        init();

        PhoneInfoUtils phoneInfoUtils = new PhoneInfoUtils(SuggestionActivity.this,this);
        phonenumber = phoneInfoUtils.getNativePhoneNumber();

        if (!LoginActivity.phoneNumber.equals("")){
            if (phonenumber.equals(LoginActivity.phoneNumber)){
                textview_phoneNumber.setText(phonenumber);
            }else {
                textview_phoneNumber.setText(LoginActivity.phoneNumber);
            }
        }else {
            textview_phoneNumber.setText(phonenumber);
        }


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
                if (phonenumber.equals("")){
                    Toasty.info(getApplicationContext(),"对不起，您没有选择输入手机号！",Toasty.LENGTH_SHORT).show();
                }else {
                    if (suggestion_dangerandrecify.getVisibility() == View.VISIBLE){
                        if ((!suggestion_danger_great.isChecked())&&(!suggestion_danger_normal.isChecked())){
                            Toasty.info(getApplicationContext(),"对不起，您没有选择隐患等级！",Toasty.LENGTH_SHORT).show();
                        }else {
                            if ((!suggestion_recify_way_limit.isChecked())&&
                                    (!suggestion_recify_way_now.isChecked())&&
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

        textview_phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog.build(SuggestionActivity.this)
                        .setButtonTextInfo(new TextInfo().setFontColor(Color.BLACK))
                        .setTitle("提示!")
                        .setMessage("请输入您的手机号: ")
                        .setInputText(textview_phoneNumber.getText().toString())
                        .setOkButton("确定", new OnInputDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                if (inputStr.length() == 11){
                                    textview_phoneNumber.setText(inputStr);
                                    if(!inputStr.equals(LoginActivity.phoneNumber)){
                                        saveDataUtil.save(LoginActivity.inputName,inputStr);
                                    }
                                    return false;
                                }else {
                                    TipDialog.show(SuggestionActivity.this, "手机号输入错误！", TipDialog.TYPE.ERROR);
                                    return true;
                                }
                            }
                        })
                        .setStyle(DialogSettings.STYLE.STYLE_KONGZUE)
                        .setTheme(DialogSettings.THEME.LIGHT)
                        .setCancelButton("取消")
                        .setHintText("手机号")
                        .setInputInfo(new InputInfo()
                                .setMAX_LENGTH(11)
                                .setInputType(InputType.TYPE_CLASS_PHONE)
                                .setTextInfo(new TextInfo().setFontColor(Color.BLACK)
                                )
                        )
                        .setCancelable(false)
                        .show();
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
            intent.putExtra("position",position);
            intent.putExtra("imageNumber",0);
            intent.putExtra("videoNumber",0);
            intent.putExtra("status",status);
            Log.e("status7",status);
            intent.putExtra("content",et_suggestion.getText().toString());
            setResult(RESULT_OK,intent);
            finish();
        }else {
            //提交数据   上传图片
            postFiles(et_suggestion.getText().toString(),pathlistOfPhoto);
        }
    }

    private void init() {
        textview_phoneNumber = findViewById(R.id.suggestion_phonenumber);
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
        suggestion_recify_way_now = findViewById(R.id.suggestion_rectify_way_now);
        suggestion_recify_way_limit = findViewById(R.id.suggestion_rectify_way_limit);
        suggestion_recify_way_stop = findViewById(R.id.suggestion_rectify_way_stop);

        suggestion_danger_great.setOnClickListener(this);
        suggestion_danger_normal.setOnClickListener(this);
        suggestion_recify_way_now.setOnClickListener(this);
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
        Log.e("status2",status);
        detectionResult = (DetectionResult) intent.getSerializableExtra("detectionResult");
        ImagePath = detectionResult.getREFJIM();
        VideoPath = detectionResult.getREFJVI();
        et_suggestion.setText(detectionResult.getSUGGESTION());

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

        tv_suggetion.setText(detectionResult.getJIANCHAXIANGTITLE());  //写下不合格的检查项

        //设置标题栏
        title = intent.getStringExtra("title");
        taskType = intent.getStringExtra("taskType");
        Log.e(TAG,"获取title："+title);
        Log.e(TAG,"获取taskType："+taskType);
//

        if (status.equals(searchUtil.nohege)){
            suggestion_dangerandrecify.setVisibility(View.VISIBLE);
        }else if (status.equals(searchUtil.hege)){
            suggestion_dangerandrecify.setVisibility(View.GONE);
        }

        toolbar_title = findViewById(R.id.toolbar_suggestion_title);
        toolabr_subtitleLeft = findViewById(R.id.toolbar_suggestion_subtitle_left);
        toolbar_subtitleRight = findViewById(R.id.toolbar_suggestion_subtitle_right);
        toolbar_title.setTextSize(20);
        toolabr_subtitleLeft.setTextSize(13);
        toolbar_subtitleRight.setTextSize(13);
        toolbar_title.setText(getResources().getString(R.string.app_name));
        toolabr_subtitleLeft.setText(title);
        toolbar_subtitleRight.setText(taskType);
        toolbar_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));
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
            case R.id.suggestion_rectify_way_now:
                suggestion_recify_way_limit.setChecked(false);
                suggestion_recify_way_stop.setChecked(false);
                break;
            case R.id.suggestion_rectify_way_limit:
                suggestion_recify_way_now.setChecked(false);
                suggestion_recify_way_stop.setChecked(false);
                break;
            case R.id.suggestion_rectify_way_stop:
                suggestion_recify_way_limit.setChecked(false);
                suggestion_recify_way_now.setChecked(false);
                break;
        }
    }

    private void postFiles(String text, List<String> pathOfPhotos) {

        String url = BaseUrl.BaseUrl+"testServlet";
        Log.d(TAG,"POST url: "+url);

        Map<String,String> taskItem = new HashMap<String, String>();
        taskItem.put("path",detectionResult.getLOGINNAME());
//        taskItem.put("CHECKCONTENT",text);
//        taskItem.put("DEVCLASS",devclass);


        OkHttp okHttp = new OkHttp();
        //Log.e(TAG, " pathlistOfPhoto: "+ pathOfPhotos.size());
        okHttp.postFilesByPost(url, taskItem, pathOfPhotos, new FileResultCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Toasty.warning(SuggestionActivity.this,"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(FilePathResult response, int id) {
                if (response.getMessage() != null){
                        Log.d(TAG," getMessage: "+response.getMessage());//文件长传成功
                    if (response.getMessage().equals("??????")) {//Result upload Success!
                        Log.d(TAG," 文件上传成功");//文件长传成功
                        Toasty.success(SuggestionActivity.this,"文件上传成功！",Toast.LENGTH_SHORT,true).show();

                        intent.putExtra("position",position);
                        intent.putExtra("imageNumber",response.imageNumber);
                        intent.putExtra("videoNumber",response.videoNumber);
                        intent.putExtra("content",et_suggestion.getText().toString());
                        intent.putExtra("ImagePath",ImagePath);
                        intent.putExtra("VideoPath",VideoPath);
                        intent.putExtra("status",status);
                        Log.e("status3",status);
                        setResult(RESULT_OK,intent);
                        finish();
                    } else if (response.getMessage().equals("结果上传失败")){
                        Toasty.error(SuggestionActivity.this, "文件上传失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


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
                Intent intentall = new Intent(SuggestionActivity.this,PhotoVideoActivity.class);
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


    public  Bitmap PicZoom(Bitmap bitmap,int width,int height){
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale((float)width/bmpWidth,(float)height/bmpHeight);
        return Bitmap.createBitmap(bitmap,0,0,bmpWidth,bmpHeight,matrix,true);
    }

    public  int reckonThumbnail(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        if ((oldHeight > newHeight && oldWidth > newWidth)
                || (oldHeight <= newHeight && oldWidth > newWidth)) {
            int be = (int) (oldWidth / (float) newWidth);
            if (be <= 1)
                be = 1;
            return be;
        } else if (oldHeight > newHeight && oldWidth <= newWidth) {
            int be = (int) (oldHeight / (float) newHeight);
            if (be <= 1)
                be = 1;
            return be;
        }
        return  1;
    }





//   定位功能
    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        final String s = str;
        try {
            if (tv_mygps != null){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tv_mygps.post(new Runnable() {
                            @Override
                            public void run() {
                                //tv_mygps.setText(s);
                            }
                        });

                    }
                }).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        Log.e(TAG,"Su onStop()");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Log.e(TAG," onStart");
        // TODO Auto-generated method stub

        super.onStart();
        // -----------location config ------------
       // locationService = ((LocationApplication) getApplication()).locationService;
        locationService = new LocationService(getApplication());
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
       // int type = getIntent().getIntExtra("from", 0);
       // if (type == 0) {
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        //} else if (type == 1) {
           // locationService.setLocationOption(locationService.getOption());
        //  }

        locationService.start();// 定位SDK

    }


    /**
     *
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDAbstractLocationListener mListener = new BDAbstractLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
               // sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
               // sb.append(location.getTime());
              //  sb.append("\nlocType : ");// 定位类型
               // sb.append(location.getLocType());
               // sb.append("\nlocType description : ");// *****对应的定位类型说明*****
               // sb.append(location.getLocTypeDescription());
               // sb.append("\nradius : ");// 半径
                //sb.append(location.getRadius());
                //sb.append("\nCountryCode : ");// 国家码
                //sb.append(location.getCountryCode());
               // sb.append("\nCountry : ");// 国家名称
               // sb.append(location.getCountry());
               // sb.append("\ncitycode : ");// 城市编码
                //sb.append(location.getCityCode());
                //sb.append("\ncity : ");// 城市
                //sb.append(location.getCity());
               // sb.append("\nDistrict : ");// 区
                //sb.append(location.getDistrict());
               // sb.append("\nStreet : ");// 街道
              //  sb.append(location.getStreet());

                sb.append("");// 地址信息:\n
                sb.append(location.getAddrStr());

                sb.append("   经度 : ");// 经度
                sb.append(location.getLongitude());
                sb.append(" 纬度 : ");// 纬度
                sb.append(location.getLatitude());

               // sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
               // sb.append(location.getUserIndoorState());
               // sb.append("\nDirection(not all devices have value): ");
              //  sb.append(location.getDirection());// 方向
              //  sb.append("\nlocationdescribe: ");
               // sb.append(location.getLocationDescribe());// 位置语义化信息
               // sb.append("\nPoi: ");// POI信息
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }

                /*
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                 } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                */
                logMsg(sb.toString());
            }
        }

    };




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
