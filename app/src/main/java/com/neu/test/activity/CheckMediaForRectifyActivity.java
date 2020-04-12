package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neu.test.R;
import com.neu.test.entity.DetectionResult;
import com.neu.test.layout.MyGridView;
import com.neu.test.util.BaseUrl;
import com.neu.test.util.ReloadImageAndVideo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class CheckMediaForRectifyActivity extends AppCompatActivity {
    private static final String TAG = "CheckMediaForRectifyAct";
    private LinearLayout check_image_lin;
    private MyGridView check_image_gridview;
    private LinearLayout check_video_lin;
    private MyGridView check_video_gridview;
    private RecyclerView check_video_recyclerview;
    private TextView check_media_title;
    private TextView check_media_content;
    private TextView check_media_yinhuanlevel;
    private TextView check_media_changway;
    private Button check_media_back;

    private DetectionResult detectionResult = new DetectionResult();

    private List<String> imageList = new ArrayList<>();
    private List<String> videoList = new ArrayList<>();
    private List<String> mediaList = new ArrayList<>();

//    private ChangeImageAdapter changeImageAdapter;
//    private ChangeVideoAdapter changeImageAdapter_video;
//    private ChangeVideoAdapter changeVideoAdapter;
    private ChangeMediaAdapter changeMediaAdapter;

    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView toolbar_subtitleLeft;
    private TextView toolabr_subtitleRight;
    private String taskType = "";
    private String title = "";

    private static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_media_for_rectify);
        detectionResult = (DetectionResult) getIntent().getSerializableExtra("detectionResult");
        Log.e(TAG, "onCreate: detectionResult  "+detectionResult.getLOGINNAME());

        title = getIntent().getStringExtra("title");
        taskType = getIntent().getStringExtra("taskType");
        initToolbar();
        initView();

        ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();

        imageList = reloadImageAndVideo.decodeImagePath(detectionResult.getREFJIM(),detectionResult.getLOGINNAME());
        videoList = reloadImageAndVideo.decodeVideoPath(detectionResult.getREFJVI(),detectionResult.getLOGINNAME());

        for (int i = 0;i<(imageList.size()+videoList.size());i++){
            if (i<imageList.size()){
                mediaList.add(imageList.get(i));
            }else if ((i>=imageList.size())&&(i<(imageList.size()+videoList.size()))){
                mediaList.add(videoList.get((i-imageList.size())));
            }
        }
        Log.e(TAG,"  media大小："+mediaList.size());

        if (mediaList.size() == 0){
            check_image_lin.setVisibility(View.GONE);
        }else {
            changeMediaAdapter = new ChangeMediaAdapter(mediaList);
            check_image_gridview.setAdapter(changeMediaAdapter);
        }

        check_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((imageList.size()<=position)&&(position<(imageList.size()+videoList.size()))){
                    Intent intent2 = new Intent(CheckMediaForRectifyActivity.this,ShowVideoActivity.class);
                    Log.e(TAG, "onClick: 视频地址   "+videoList);
                    intent2.putExtra("video", (Serializable) videoList);
                    intent2.putExtra("position",(position-imageList.size()));
                    startActivity(intent2);
                }else {
                    initImageView(position);
                }


                //展示选中图片
                Toast.makeText(CheckMediaForRectifyActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });

//        if (imageList.size() == 0){
//            check_image_lin.setVisibility(View.GONE);
//        }else {
//            changeImageAdapter = new ChangeImageAdapter(imageList);
//            check_image_gridview.setAdapter(changeImageAdapter);
//        }
//        Log.e(TAG,"  地址："+detectionResult.getREFJVI());

//        if (videoList.size() == 0){
//            check_video_lin.setVisibility(View.GONE);
//        }else {
//            check_video_lin.setVisibility(View.VISIBLE);
//            //下面的代码是以Recyclerview显示饺子视频时的代码
////            changeVideoAdapter = new ChangeVideoAdapter(this);
////            check_video_recyclerview.setAdapter(changeVideoAdapter);
//            changeImageAdapter_video = new ChangeVideoAdapter(videoList);
//            check_video_gridview.setAdapter(changeImageAdapter_video);
//        }

//        check_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                initImageView(position);
//                //展示选中图片
//                Toast.makeText(CheckMediaForRectifyActivity.this, ""+position, Toast.LENGTH_SHORT).show();
//            }
//        });

//        check_video_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(CheckMediaForRectifyActivity.this, ""+position, Toast.LENGTH_SHORT).show();
//                Intent intent2 = new Intent(CheckMediaForRectifyActivity.this,ShowVideoActivity.class);
//                Log.e(TAG, "onClick: 视频地址   "+videoList);
//                intent2.putExtra("video", (Serializable) videoList);
//                intent2.putExtra("position",position);
//                startActivity(intent2);
//            }
//        });

//        //使用recyclerview播放视频时的监听 目前使用上面video_gridview的监听了
//        check_video_recyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
//            @Override
//            public void onChildViewAttachedToWindow(@NonNull View view) {
//
//            }
//
//            @Override
//            public void onChildViewDetachedFromWindow(@NonNull View view) {
//                Jzvd jzvd = view.findViewById(R.id.jz_video);
//                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
//                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
//                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
//                        Jzvd.releaseAllVideos();
//                    }
//                }
//            }
//        });

        check_media_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);//不设置RESULT_OK的原因是会出现bug
                finish();
            }
        });

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_checkmedia);
        toolbar_title = findViewById(R.id.toolbar_checkmedia_title);
        toolbar_title.setTextSize(18);
        toolbar.setTitle("");
        toolbar_title.setText("附件内容显示");

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initView() {
        check_image_lin = findViewById(R.id.check_image_lin);
        check_image_gridview = findViewById(R.id.check_image_gridview);

        check_video_gridview = findViewById(R.id.check_video_gridview);

        check_video_lin = findViewById(R.id.check_video_lin);
        check_video_recyclerview = findViewById(R.id.check_video_recycleview);
        check_video_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        check_media_title = findViewById(R.id.check_media_title_textview);
        check_media_title.setText(detectionResult.getJIANCHAXIANGTITLE());

        check_media_content = findViewById(R.id.check_media_content_textview);
        check_media_content.setText(detectionResult.getSUGGESTION());
//        check_media_content.setText("整改问题描述整改问题描述整改问题描述整改问题描述整改问题描述");

        check_media_yinhuanlevel = findViewById(R.id.check_media_yinhuanlevel_textview);
        check_media_yinhuanlevel.setText(detectionResult.getYINHUANLEVEL());

        check_media_changway = findViewById(R.id.check_media_changway_textview);
        check_media_changway.setText(detectionResult.getCHANGEDWAY());

        check_media_back = findViewById(R.id.check_media_back_button);

    }


    private void initImageView(int position) {
        final WindowManager windowManager = getWindowManager();
        final RelativeLayout relativeLayout = new RelativeLayout(this);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //FLAG_LAYOUT_IN_SCREEN
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        layoutParams.format = PixelFormat.RGBA_8888;//让背景透明，放大过程可以看到当前界面
        layoutParams.verticalMargin = 0;
        windowManager.addView(relativeLayout,layoutParams);

        final PhotoView photoview = new PhotoView(this);
        photoview.enable();
        photoview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.addView(photoview,params);
        relativeLayout.setFocusableInTouchMode(true);

        Glide
                .with(relativeLayout.getContext())
                .load(imageList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(photoview);

        photoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(relativeLayout);
            }
        });

        relativeLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != windowManager && null != relativeLayout) {
                        windowManager.removeView(relativeLayout);
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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



//

    class ChangeMediaAdapter extends BaseAdapter{

        private List<String> pathlists;

        public ChangeMediaAdapter(List<String> list){
            pathlists = new ArrayList<>();
            this.pathlists = list;

        }

        @Override
        public int getCount() {
            return pathlists.size();
        }

        @Override
        public Object getItem(int position) {
            return pathlists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChangeMediaViewHolder changeMediaViewHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fujian_layout_redetection2,null);
                changeMediaViewHolder = new ChangeMediaViewHolder(convertView);
                convertView.setTag(changeMediaViewHolder);
            }else{
                changeMediaViewHolder = (ChangeMediaViewHolder) convertView.getTag();
            }

            if ((imageList.size()<=position)&&(position<(imageList.size()+videoList.size()))){
                changeMediaViewHolder.changeImagePre.setVisibility(View.VISIBLE);
            }else {
                changeMediaViewHolder.changeImagePre.setVisibility(View.INVISIBLE);
            }

            Glide
                    .with(getApplicationContext())
                    .load(pathlists.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(changeMediaViewHolder.changeImage);


            return convertView;
        }


    }

    class ChangeMediaViewHolder {
        private ImageView changeImage;
        private ImageView changeImagePre;

        public ChangeMediaViewHolder(View view){
            changeImage = view.findViewById(R.id.fujian_gridview_item_photo);
            changeImagePre = view.findViewById(R.id.fujian_gridview_item_photo_pre);
        }
    }


//    class ChangeVideoAdapter extends RecyclerView.Adapter<ChangeVideoAdapter.ChangeVideoViewHolder>{
//        Context context;
//
//        public ChangeVideoAdapter(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public ChangeVideoAdapter.ChangeVideoViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
//            ChangeVideoViewHolder holder = new ChangeVideoViewHolder(LayoutInflater.from(
//                  context  ).inflate(R.layout.checkmedia_video_layout, parent,
//                    false));
//
//            return holder;
//        }
//
//        @SuppressLint("LongLogTag")
//        @Override
//        public void onBindViewHolder( ChangeVideoAdapter.ChangeVideoViewHolder holder, int position) {
//            holder.jzvd.setUp(videoList.get(position),"",Jzvd.SCREEN_NORMAL);
//        }
//
//        @Override
//        public int getItemCount() {
//            return videoList.size();
//        }
//
//        class ChangeVideoViewHolder extends RecyclerView.ViewHolder {
//            private Jzvd jzvd;
//
//            public ChangeVideoViewHolder(View view){
//                super(view);
//                jzvd = view.findViewById(R.id.jz_video);
//            }
//        }
//    }



    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }


}
