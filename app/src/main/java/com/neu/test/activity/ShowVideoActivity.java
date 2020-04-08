package com.neu.test.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lmx.library.media.VideoPlayAdapter;
import com.lmx.library.media.VideoPlayRecyclerView;
import com.neu.test.R;
import com.neu.test.layout.VideoLoadingProgressbar;
import com.neu.test.util.VideoPlayer;

import java.util.ArrayList;
import java.util.List;

public class ShowVideoActivity extends AppCompatActivity {
    private VideoPlayRecyclerView mRvVideo;
    private ShowVideoAdapter adapter;
    private List<String> videoPath = new ArrayList<>();
    private VideoPlayer videoPlayer;
    ImageButton ibzanting ;
    ImageButton ibbofang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        videoPath = (List<String>) getIntent().getSerializableExtra("video");
//        videoPath.add("http://39.97.108.172:8080/pic/yuhang/%20video_1584256348490.mp4");
//        videoPath.add("http://39.97.108.172:8080/pic/yuhang/video_1584540967093.mp4");
//        videoPath.add("http://39.97.108.172:8080/pic/yuhang/video_1584582023465.mp4");
//        videoPath.add("http://39.97.108.172:8080/pic/video_1584240973852.mp4");
        initView();
    }

    private void initView() {
        findViewById(R.id.ibBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ibzanting = findViewById(R.id.ibzanting);
        ibbofang = findViewById(R.id.ibbofang);
        ibzanting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.start();
                ibzanting.setVisibility(View.GONE);
                ibbofang.setVisibility(View.VISIBLE);
            }
        });

        ibbofang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoPlayer.pause();
                ibzanting.setVisibility(View.VISIBLE);
                ibbofang.setVisibility(View.GONE);
            }
        });


        mRvVideo = findViewById(R.id.rvVideo);
        adapter = new ShowVideoAdapter(ShowVideoActivity.this);
        mRvVideo.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.release();
    }

    class ShowVideoAdapter extends VideoPlayAdapter<ViewHolder> {

        private Context mContext;

        private int mCurrentPosition;
        private ViewHolder mCurrentHolder;
        private TextureView textureView;

        public ShowVideoAdapter(Context mContext) {
            this.mContext = mContext;
            videoPlayer = new VideoPlayer();
            textureView = new TextureView(mContext);
            videoPlayer.setTextureView(textureView);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_video_show, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RequestOptions options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            Glide.with(mContext).load(R.drawable.timg).apply(options).into(holder.ivCover);
        }

        @Override
        public int getItemCount() {
            return videoPath.size();
        }

        @Override
        public void onPageSelected(int itemPosition, View itemView) {
            mCurrentPosition = itemPosition;
            mCurrentHolder = new ViewHolder(itemView);
            playVideo();
        }
        private void playVideo() {
            videoPlayer.reset();
            mCurrentHolder.pbLoading.setVisibility(View.VISIBLE);
            videoPlayer.setOnStateChangeListener(new VideoPlayer.OnStateChangeListener() {
                @Override
                public void onReset() {
                    Log.e("ShowVideo","onReset");
                    mCurrentHolder.ivCover.setVisibility(View.VISIBLE);
                    mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
                    ibzanting.setVisibility(View.VISIBLE);
                    ibbofang.setVisibility(View.GONE);
                    ibzanting.setClickable(false);
                }

                @Override
                public void onRenderingStart() {
                    Log.e("ShowVideo","onRenderingStart");
                    mCurrentHolder.ivCover.setVisibility(View.GONE);
                    mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
                    ibzanting.setClickable(true);
                    ibzanting.setVisibility(View.GONE);
                    ibbofang.setVisibility(View.VISIBLE);
                }

                @Override
                public void onProgressUpdate(float per) {
                }

                @Override
                public void onPause() {
                    Log.e("ShowVideo","onPause");
                    mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
                    ibzanting.setVisibility(View.VISIBLE);
                    ibbofang.setVisibility(View.GONE);
                }

                @Override
                public void onStop() {
                    Log.e("ShowVideo","onStop");
                    mCurrentHolder.ivCover.setVisibility(View.VISIBLE);
                    mCurrentHolder.pbLoading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onComplete() {

                    Log.e("ShowVideo","onComplete");
                    //videoPlayer.start();
                    videoPlayer.pause();
                    ibzanting.setVisibility(View.VISIBLE);
                    ibbofang.setVisibility(View.GONE);
                    //videoPlayer.start();
                }
            });
            if (textureView.getParent() != mCurrentHolder.flVideo) {
                if (textureView.getParent() != null) {
                    ((FrameLayout) textureView.getParent()).removeView(textureView);
                }
                mCurrentHolder.flVideo.addView(textureView);
            }
//            Log.e("视频测试  ",videoPath.get(mCurrentPosition));
            videoPlayer.setDataSource(videoPath.get(mCurrentPosition));
            videoPlayer.prepare();
        }

        public void release() {
            videoPlayer.release();
        }

    }
    class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout flVideo;
        private ImageView ivCover;
        private VideoLoadingProgressbar pbLoading;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            flVideo = itemView.findViewById(R.id.flVideo);
            ivCover = itemView.findViewById(R.id.ivCover);
            pbLoading = itemView.findViewById(R.id.pbLoading);
        }
    }
}
