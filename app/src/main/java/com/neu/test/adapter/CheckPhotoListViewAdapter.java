package com.neu.test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neu.test.R;

import java.util.ArrayList;
import java.util.List;

public class CheckPhotoListViewAdapter extends BaseAdapter {
    private List<String> paths = new ArrayList<>();
    private String photoPath;
    private Context context;


    public CheckPhotoListViewAdapter(Context context,List<String> photoPaths){
        this.paths = photoPaths;
        this.context = context;
    }


    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_photo_check,null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        decodePhoto(paths.get(position),viewHolder);

        return convertView;
    }

    class ViewHolder{
        ImageView imageView;
        PhotoView photoView;

        public ViewHolder(View convertView){
            //imageView = convertView.findViewById(R.id.check_detail_photo_iv_show);
            photoView = convertView.findViewById(R.id.photoview_img);
            photoView.enable();
        }
    }


    //有参的函数
    public void decodePhoto(final String path,final ViewHolder viewHolder) {
        //viewHolder.imageView.setVisibility(View.VISIBLE);

        Glide
                .with(context)
                .load(path)
                .centerCrop()
                //.placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(viewHolder.photoView);

    }


}
