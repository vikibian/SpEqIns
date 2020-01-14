package com.neu.test.fragment;


import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.neu.test.R;
import com.neu.test.activity.VideoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckDetailsVideoFragment extends Fragment {

    private SurfaceView mSurfView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer = null;
    private Button button_playVideo;
    private String videoPath;


    public CheckDetailsVideoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        videoPath = VideoActivity.checkDetailsVideoPath;
        Log.d("checkDetailsVideo","  videoPath: "+videoPath);

        if (videoPath!=null){
            view = inflater.inflate(R.layout.fragment_check_details_video, container, false);
            initView(view);
            mSurfaceHolder = mSurfView.getHolder();
            mMediaPlayer = new MediaPlayer();

            button_playVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo();
                }
            });
        }else{
            view = inflater.inflate(R.layout.fragment_check_details_video_blank,container,false);
        }


        return view;
    }

    private void playVideo() {
        if (videoPath != null){
            button_playVideo.setVisibility(View.INVISIBLE);
            mMediaPlayer.reset();
            Uri videoUri = Uri.parse(videoPath);
            mMediaPlayer = MediaPlayer.create(getContext(),videoUri);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDisplay(mSurfaceHolder);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    button_playVideo.setVisibility(View.VISIBLE);
                }
            });
            try {
                mMediaPlayer.prepare();
            }catch (Exception e){
                e.printStackTrace();
            }
            mMediaPlayer.start();
        }else{
            button_playVideo.setVisibility(View.INVISIBLE);
        }
    }

    private void initView(View view) {
        mSurfView = view.findViewById(R.id.check_detail_vedio_sv);
        button_playVideo = view.findViewById(R.id.check_detail_vedio_bt_play);
    }

}
