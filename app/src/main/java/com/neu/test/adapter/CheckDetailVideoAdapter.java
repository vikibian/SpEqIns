package com.neu.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neu.test.R;
import com.neu.test.layout.CustomVideoPlayerController;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.List;

/**
 * created by Viki on 2020/2/28
 * system login name : lg
 * created time : 18:46
 * email : 710256138@qq.com
 */
public class CheckDetailVideoAdapter extends RecyclerView.Adapter<NiceVideoViewHolder> {

    private Context mContext;
    private List<String> mVideoList;
    private int pos;

    public CheckDetailVideoAdapter(Context context,List<String> videoList){
        mContext = context;
        mVideoList = videoList;
    }

    @NonNull
    @Override
    public NiceVideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent, false);
        NiceVideoViewHolder holder = new NiceVideoViewHolder(itemView);
        CustomVideoPlayerController controller = new CustomVideoPlayerController(mContext);
        holder.setController(controller);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NiceVideoViewHolder holder, int position) {
        String video = mVideoList.get(position);
        holder.bindData(video);
        pos = position;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }



}


