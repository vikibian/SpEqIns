package com.neu.test.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.test.R;
import com.neu.test.layout.CustomVideoPlayerController;
import com.xiao.nicevideoplayer.NiceVideoPlayer;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.lang.reflect.Field;

/**
 * created by Viki on 2020/2/28
 * system login name : lg
 * created time : 19:07
 * email : 710256138@qq.com
 */
public class NiceVideoViewHolder extends RecyclerView.ViewHolder{

    public CustomVideoPlayerController mController;
    public NiceVideoPlayer mVideoPlayer;

    public NiceVideoViewHolder(@NonNull View itemView) {
        super(itemView);

        mVideoPlayer = (NiceVideoPlayer) itemView.findViewById(R.id.nice_video_player);
        mVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_NATIVE);
        // 将列表中的每个视频设置为默认16:9的比例
        ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
        params.width = itemView.getResources().getDisplayMetrics().widthPixels; // 宽度为屏幕宽度
        params.height = (int) (params.width * 9f / 16f);    // 高度为宽度的9/16
        mVideoPlayer.setLayoutParams(params);
    }

    public void setController(CustomVideoPlayerController controller) {
        mController = controller;
        mVideoPlayer.setController(mController);
    }

    public void bindData(String video) {
        //Video v = new Video("1",9800,"1","1");

        mController.setTitle(" ");
        //mController.setLenght(v.getLength());

//            mController.setLenght(video.getLength());
//            Glide.with(itemView.getContext())
//                    .load(R.mipmap.test)
//                    //.placeholder(R.drawable.img_default)
//                    .into(mController.imageView());
        mVideoPlayer.setUp(video, null);
    }
}
