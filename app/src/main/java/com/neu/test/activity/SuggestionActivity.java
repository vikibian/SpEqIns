package com.neu.test.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.neu.test.R;
import com.neu.test.adapter.SuggestionGridViewAdapter;
import com.neu.test.entity.LocationService;
import com.neu.test.entity.Result;
import com.neu.test.entity.Task;
import com.neu.test.net.OkHttp;
import com.neu.test.net.callback.ListTaskCallBack;

import com.neu.test.util.BaseUrl;
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

public class SuggestionActivity extends AppCompatActivity {
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

    final int RequestCor = 521;
    final int maxNum = 500;
    final int REQUEST_VIDEO = 99;

    //测试多张图片显示，测试Gridview组件
    private GridView gridView;
    private SuggestionGridViewAdapter suggestionGridViewAdapter;
    public  List<String> pathlistOfPhoto = new ArrayList<>();
    //测试视频和图片的显示分开，因为在缩小时他们的方向不同，旋转的角度不同
    public static String videoPath = new String();



    private   List<String> testpathlistOfPhoto = new ArrayList<>();
    private String testvideoPath = " ";

    private String devclass;//接收从DetctionActivity传过来的DEVCLASS
    private String suggestion;//接收从DetctionActivity传过来的检查项

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        //定位
        tv_suggetion = findViewById(R.id.tv_suggetion);//显示不合格项
        //btn_get_gps = findViewById(R.id.btn_get_gps);//获取定位按钮
        btn_submit_suggestion = findViewById(R.id.btn_submit_suggestion);

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

        //测试上传文件
        testpathlistOfPhoto.add("/storage/emulated/0/DCIM/Camera/1581774108382IMG.jpg");
        testpathlistOfPhoto.add("/storage/emulated/0/DCIM/Camera/1581774121524IMG.jpg");
        testpathlistOfPhoto.add("/storage/emulated/0/DCIM/Camera/1581774131951IMG.jpg");
        testpathlistOfPhoto.add("/storage/emulated/0/DCIM/Video/202021595650VIDEO.mp4");

        //这是在使用一张图片时的代码
//        iv_photo = findViewById(R.id.iv_photo);//显示拍摄图片
//        iv_video = findViewById(R.id.iv_video);//显示视频第一帧
//        iv_plus = findViewById(R.id.iv_plus);//显示视频第一帧


        tv_mygps = findViewById(R.id.tv_mygps);
        et_suggestion = findViewById(R.id.et_suggestion);//建议文本框
        tv_num = findViewById(R.id.tv_num);//计算字数框
        tv_mygps.setMovementMethod(ScrollingMovementMethod.getInstance());//显示定位  里面的参数给TextView添加滚动条  设置滚动方式
        Log.e("ERROR","Suggesion crate");

        Intent intent = getIntent();
        suggestion = intent.getStringExtra("suggestion");
        devclass = intent.getStringExtra("DEVCLASS");
//        tv_suggetion.setText(suggestion);  //写下不合格的检查项

        String isHege = intent.getStringExtra("isHege");
        tv_suggetion.setText(suggestion+"----"+isHege);  //写下不合格的检查项



        btn_submit_suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //用来对没有选择图片或视频进行提醒 若没选 软件则会崩溃
                if (pathlistOfPhoto.size() == 0) {
                    Toasty.warning(SuggestionActivity.this,"没有选择图片或视频！",Toast.LENGTH_LONG,true).show();

                    postFiles(et_suggestion.getText().toString(),null);
                }else {
                    //提交数据   上传图片
                    postFiles(et_suggestion.getText().toString(),pathlistOfPhoto);
                }
                //退出activity
                finish();
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

    }

    private void postFiles(String text, List<String> pathOfPhotos) {
//        Map<String, File> fileMap = new LinkedHashMap<>();
//
//        for (int i=0;i<testpathlistOfPhoto.size();i++){
//            File file = new File(testpathlistOfPhoto.get(i));
//            Log.d(TAG," pathlistOfPhoto: "+testpathlistOfPhoto.get(i));
////            int num = testpathlistOfPhoto.size()-1;
////            Log.d(TAG," num: "+num);
//            int length = testpathlistOfPhoto.get(i).length();
//            Log.d(TAG," length: "+length);
//            String suffix = testpathlistOfPhoto.get(i).substring(length -3,length);
//            Log.d(TAG," i: "+i);
//
//            if (suffix.equals("mp4")){
//                fileMap.put("videoFile",file);
//            }else {
//                fileMap.put("imageFile",file);
//            }
//
//            Log.e(TAG," filemap.size: "+fileMap.size());
//        }


        String url = BaseUrl.BaseUrl+"testServlet";
        Log.d(TAG,"POST url: "+url);

        Map<String,String> taskItem = new HashMap<String, String>();
        taskItem.put("CHECKCONTENT",text);
        taskItem.put("DEVCLASS",devclass);

        Log.d(TAG,"taskItem: "+taskItem.toString());
        JSONObject taskJson = new JSONObject(taskItem);
        Log.d(TAG,"taskJson: "+taskJson.toString());

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("taskItem",taskJson.toString());



        OkHttp okHttp = new OkHttp();
        //Log.e(TAG, " pathlistOfPhoto: "+ pathOfPhotos.size());
        okHttp.postFilesByPost(url, resultMap, pathOfPhotos, new ListTaskCallBack() {
            @Override
            public void onError(Call call, Exception e, int i) {
                System.out.println(e.getMessage());
                Toasty.warning(SuggestionActivity.this,"客官，网络不给力",Toast.LENGTH_LONG,true).show();
            }

            @Override
            public void onResponse(Result<List<Task>> response, int id) {
                if (response.getMessage() != null){
                        Log.d(TAG," getMessage: "+response.getMessage());//文件长传成功
                    if (response.getMessage().equals("??????")) {//Result upload Success!
                        Log.d(TAG," 文件上传成功");//文件长传成功
                        Toasty.success(SuggestionActivity.this,"文件上传成功！",Toast.LENGTH_SHORT,true).show();
                        if (response.getContent() == null){
                            Log.d(TAG," 文件上传成功" + "但是数据为0");
                        }
                    } else if (response.getMessage().equals("结果上传失败")){
                        Toasty.error(SuggestionActivity.this, "获取个人信息失败！", Toast.LENGTH_SHORT).show();
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
                Intent intentPicture = new Intent(SuggestionActivity.this,PictureActivity.class);
                startActivityForResult(intentPicture,RequestCor);
                break;
            case 3:
                Intent intentVideo = new Intent(SuggestionActivity.this,VideoActivity.class);
                startActivityForResult(intentVideo,REQUEST_VIDEO);
                break;
            case 4:
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
            if (requestCode == REQUEST_VIDEO) {

                imgString = data.getStringExtra("imagePath");
                testvideoPath =data.getStringExtra("path");
                videoString =data.getStringExtra("path");
                Log.d(TAG," videopath: "+testvideoPath);
                //不需要旋转90度  需要在设置图片的时候进行判断
                videoPath = imgString;
                //在此处需要更新图片数组
                //pathlistOfPhoto.add(imgString);

                pathlistOfPhoto.add(videoString);

                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,1);
                gridView.setAdapter(suggestionGridViewAdapter);

//                final String imgString = data.getStringExtra("imagePath");
//                String videoString = data.getStringExtra("path");
//                iv_video.setVisibility(View.VISIBLE);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        iv_video.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Bitmap bitmap = BitmapFactory.decodeFile(imgString);
//                                    if(bitmap == null){
//                                        Log.e ("ERROR","Bitmap失败");
//                                    }
//                                    if(bitmap != null){
//                                        Log.e ("ERROR","Bitmap成功");
//                                        int scale = reckonThumbnail(bitmap.getWidth(),bitmap.getHeight(),125,125);
//                                        Bitmap bitmap1 = PicZoom(bitmap,bitmap.getWidth()/scale,bitmap.getHeight()/scale);
//                                        bitmap.recycle();
//                                        bitmap = null;
//                                        iv_video.setImageBitmap(bitmap1);
//                                        iv_video.setPivotX(iv_photo.getWidth()/2);
//                                        iv_video.setPivotY(iv_photo.getHeight()/2);
//                                        iv_video.setRotation(0);
//                                        iv_video.setVisibility(View.VISIBLE);
//
//                                        Log.e ("ERROR","Bitmap更换成功");
//                                    }
//
//                                }
//                                catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                    }
//                }).start();

            }
            if (requestCode == RequestCor) {
                Log.e("ERROR","对接成功");
                imgString = data.getStringExtra("imagePath");
                Log.e("图片地址","  "+imgString);
                //在此处需要更新图片数组
                pathlistOfPhoto.add(imgString);
                Log.e("图片地址","  "+pathlistOfPhoto.get(0));
                suggestionGridViewAdapter = new SuggestionGridViewAdapter(getApplicationContext(), pathlistOfPhoto,0);
                gridView.setAdapter(suggestionGridViewAdapter);


//                final String imgString = data.getStringExtra("imagePath");
//                iv_photo.setVisibility(View.VISIBLE);
//                Log.e("ERROR",imgString);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        iv_photo.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    Bitmap bitmap = BitmapFactory.decodeFile(imgString);
//                                    if(bitmap == null){
//                                        Log.e ("ERROR","Bitmap失败");
//                                    }
//                                    if(bitmap != null){
//                                        Log.e ("ERROR","Bitmap成功");
//                                        int scale = reckonThumbnail(bitmap.getWidth(),bitmap.getHeight(),125,125);
//                                        Bitmap bitmap1 = PicZoom(bitmap,bitmap.getWidth()/scale,bitmap.getHeight()/scale);
//                                        bitmap.recycle();
//                                        bitmap = null;
//                                        iv_photo.setImageBitmap(bitmap1);
//                                        iv_photo.setPivotX(iv_photo.getWidth()/2);//设置锚点
//                                        iv_photo.setPivotY(iv_photo.getHeight()/2);
//                                        iv_photo.setRotation(90);
//                                        iv_photo.setVisibility(View.VISIBLE);
//
//                                        Log.e ("ERROR","Bitmap更换成功");
//                                    }
//
//                                }
//                                catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//
//                    }
//                }).start();

            }
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
                                tv_mygps.setText(s);
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
        Log.e("ERROR","Su onStop()");
        super.onStop();
    }

    @Override
    protected void onStart() {
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

//        btn_get_gps.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (btn_get_gps.getText().toString().equals(getString(R.string.startlocation))) {
//                    locationService.start();// 定位SDK
//                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
//                    btn_get_gps.setText(getString(R.string.stoplocation));
//                } else {
//                    locationService.stop();
//                    btn_get_gps.setText(getString(R.string.startlocation));
//                }
//            }
//        });
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

}
