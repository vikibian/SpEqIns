package com.neu.test.fragment;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neu.test.R;
import com.neu.test.activity.VideoActivity;
import com.neu.test.adapter.CheckDetailVideoAdapter;
import com.neu.test.adapter.NiceVideoViewHolder;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckDetailsVideoFragment extends Fragment {

    private SurfaceView mSurfView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer = null;
    private Button button_playVideo;
    private String videoPath;
    private RecyclerView mRecyclerView;
    private List<String> path = new ArrayList<>();

    public CheckDetailsVideoFragment() {
        // Required empty public constructor
    }

    public CheckDetailsVideoFragment(List<String> path) {
        // Required empty public constructor
        this.path = path;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
//        videoPath = VideoActivity.checkDetailsVideoPath;
//        Log.d("checkDetailsVideo","  videoPath: "+videoPath);
        path.add("http://39.97.108.172:8080/pic/1234567880VIDEO0.mp4");
        path.add("http://39.97.108.172:8080/pic/123456789100VIDEO0.mp4");
        if (path!=null){

            view = inflater.inflate(R.layout.fragment_check_details_video, container, false);

            init(view);

//            view = inflater.inflate(R.layout.fragment_check_details_video, container, false);
//            initView(view);
//            mSurfaceHolder = mSurfView.getHolder();
//            mMediaPlayer = new MediaPlayer();
//
//            button_playVideo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    playVideo();
//                }
//            });
        }else{
            view = inflater.inflate(R.layout.fragment_check_details_video_blank,container,false);
        }


        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    private void init(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.check_detail_video_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        CheckDetailVideoAdapter adapter = new CheckDetailVideoAdapter(getContext(), path);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                NiceVideoPlayer niceVideoPlayer = ((NiceVideoViewHolder)holder).mVideoPlayer;
                if (niceVideoPlayer == NiceVideoPlayerManager.instance().getCurrentNiceVideoPlayer()) {
                    NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
                }
            }
        });
    }

//    private void playVideo() {
//        if (videoPath != null){
//            button_playVideo.setVisibility(View.INVISIBLE);
//            mMediaPlayer.reset();
//            Uri videoUri = Uri.parse(videoPath);
//            mMediaPlayer = MediaPlayer.create(getContext(),videoUri);
//            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mMediaPlayer.setDisplay(mSurfaceHolder);
//            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    button_playVideo.setVisibility(View.VISIBLE);
//                }
//            });
//            try {
//                mMediaPlayer.prepare();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            mMediaPlayer.start();
//        }else{
//            button_playVideo.setVisibility(View.INVISIBLE);
//        }
//    }

//    private void initView(View view) {
//        mSurfView = view.findViewById(R.id.check_detail_vedio_sv);
//        button_playVideo = view.findViewById(R.id.check_detail_vedio_bt_play);
//    }

}
