package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;

public class CheckMediaForRectifyActivity extends AppCompatActivity {
    private static final String TAG = "CheckMediaForRectifyAct";
    private LinearLayout check_image_lin;
    private MyGridView check_image_gridview;
    private LinearLayout check_video_lin;
//    private MyGridView check_video_gridview;
    private RecyclerView check_video_recyclerview;
    private TextView check_media_content;

    private DetectionResult detectionResult = new DetectionResult();

    private List<String> imageList = new ArrayList<>();
    private List<String> videoList = new ArrayList<>();

    private ChangeImageAdapter changeImageAdapter;
    private ChangeVideoAdapter changeVideoAdapter;
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


        imageList = decodeChaneImagePath(detectionResult);
        if (imageList.size() == 0){
            check_image_lin.setVisibility(View.GONE);
        }else {
            changeImageAdapter = new ChangeImageAdapter();
            check_image_gridview.setAdapter(changeImageAdapter);
        }
        videoList = decodeChaneVideoPath(detectionResult);
        if (videoList.size() == 0){
            check_video_lin.setVisibility(View.GONE);
        }else {
            check_video_lin.setVisibility(View.VISIBLE);
            changeVideoAdapter = new ChangeVideoAdapter(this);
            check_video_recyclerview.setAdapter(changeVideoAdapter);
        }

        check_image_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                initImageView(position);
                //展示选中图片
                Toast.makeText(CheckMediaForRectifyActivity.this, ""+position, Toast.LENGTH_SHORT).show();
            }
        });


        check_video_recyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                Jzvd jzvd = view.findViewById(R.id.jz_video);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())) {
                    if (Jzvd.CURRENT_JZVD != null && Jzvd.CURRENT_JZVD.screen != Jzvd.SCREEN_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_checkmedia);
        toolbar_title = findViewById(R.id.toolbar_checkmedia_title);
        toolbar_subtitleLeft = findViewById(R.id.toolbar_checkmedia_subtitle_left);
        toolabr_subtitleRight = findViewById(R.id.toolbar_checkmedia_subtitle_right);

        toolbar_title.setTextSize(20);
        toolbar_subtitleLeft.setTextSize(13);
        toolabr_subtitleRight.setTextSize(13);

        toolbar_title.setText(taskType+"    ");
        toolbar_subtitleLeft.setText(title);
        toolabr_subtitleRight.setText(taskType);
        toolabr_subtitleRight.setTextColor(getResources().getColor(R.color.yellow));

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initView() {
        check_image_lin = findViewById(R.id.check_image_lin);
        check_image_gridview = findViewById(R.id.check_image_gridview);

        check_video_lin = findViewById(R.id.check_video_lin);
        check_video_recyclerview = findViewById(R.id.check_video_recycleview);
        check_video_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        check_media_content = findViewById(R.id.check_media_content_textview);
        check_media_content.setText(detectionResult.getSUGGESTION());

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


    public List<String> decodeChaneImagePath(DetectionResult detectionResult) {
//        detectionResult.setCHANGEDIMAGE("image_202031610246.jpg,image_202031662445.jpg,image_202031610246.jpg,image_202031662445.jpg");//image_202031610246.jpg,image_202031662445.jpg  image_202031610246.jpg,image_202031662445.jpg,image_202031610246.jpg,image_202031662445.jpg
        List<String> paths = new ArrayList<>();
        String imagePath = "";
        imagePath = detectionResult.getREFJIM();
        if ((!imagePath.equals(""))&&(!imagePath.equals(null))){
            String[] splitpath = imagePath.split(",");
            for (int i=0;i<splitpath.length;i++){
                String httppath = BaseUrl.pathOfPhotoAndVideo+ detectionResult.getLOGINNAME()+"/"+splitpath[i];
                paths.add(httppath);
            }
        }else {

        }
        return paths;
    }

    private List<String> decodeChaneVideoPath(DetectionResult detectionResult) {
//        detectionResult.setCHANGEDVIDEO("video_1584540967093.mp4,video_1584582023465.mp4,video_1584540967093.mp4,video_1584582023465.mp4");
        List<String> paths = new ArrayList<>();
        String videoPath = "";
        videoPath = detectionResult.getREFJVI();
        if ((!videoPath.equals(""))&&(!videoPath.equals(null))){
            String[] splitpath = videoPath.split(",");
            for (int i=0;i<splitpath.length;i++){
                String httppath = BaseUrl.pathOfPhotoAndVideo+ detectionResult.getLOGINNAME()+"/"+splitpath[i];
                paths.add(httppath);
                Log.e(TAG, "decodeChaneVideoPath: httppath  "+httppath );
            }
        }else {

        }
        return paths;
    }

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



    class ChangeImageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return imageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChangeImageViewHolder changeImageViewHolder;
            if (convertView == null){

                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fujian_layout_redetection,null);
                changeImageViewHolder = new ChangeImageViewHolder(convertView);
                convertView.setTag(changeImageViewHolder);
            }else{
                changeImageViewHolder = (ChangeImageViewHolder) convertView.getTag();
            }

            Glide
                    .with(getApplicationContext())
                    .load(imageList.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(changeImageViewHolder.changeImage);

            return convertView;
        }


    }

    class ChangeImageViewHolder {
        private PhotoView changeImage;

        public ChangeImageViewHolder(View view){
            changeImage = view.findViewById(R.id.fujian_gridview_item_photo);
        }
    }


    class ChangeVideoAdapter extends RecyclerView.Adapter<ChangeVideoAdapter.ChangeVideoViewHolder>{
        Context context;

        public ChangeVideoAdapter(Context context) {
            this.context = context;
        }

        @Override
        public ChangeVideoAdapter.ChangeVideoViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            ChangeVideoViewHolder holder = new ChangeVideoViewHolder(LayoutInflater.from(
                  context  ).inflate(R.layout.checkmedia_video_layout, parent,
                    false));

            return holder;
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onBindViewHolder( ChangeVideoAdapter.ChangeVideoViewHolder holder, int position) {
            holder.jzvd.setUp(videoList.get(position),"",Jzvd.SCREEN_NORMAL);
        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }

        class ChangeVideoViewHolder extends RecyclerView.ViewHolder {
            private Jzvd jzvd;

            public ChangeVideoViewHolder(View view){
                super(view);
                jzvd = view.findViewById(R.id.jz_video);
            }
        }
    }



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
