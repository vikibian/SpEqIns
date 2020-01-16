package com.neu.test.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.neu.test.R;
import com.neu.test.activity.PictureActivity;
import com.neu.test.activity.SuggestionActivity;
import com.neu.test.activity.VideoActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuggestionGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> photoList ;
    private int flag;
    private static int pos = 1000;

    //记录List的大小
    private int num ;

    //调用函数  之后可以把函数移动到这个文件里面
    private SuggestionActivity suggestionActivity = new SuggestionActivity();

    //设置视频第一帧图片的旋转方向
    private String videoPath = new String();

    public SuggestionGridViewAdapter(Context applicationContext, List<String> pathlistOfPhoto, int flag){
        this.context = applicationContext;
        this.num = pathlistOfPhoto.size();
        this.flag = flag;
        //this.photoList = pathlistOfPhoto;
        //Collections.reverse(photoList);


        photoList = new ArrayList<>();
        for (int i=0;i<pathlistOfPhoto.size();i++){
            photoList.add(pathlistOfPhoto.get(i).toString());
        }
        Collections.reverse(photoList);
        //获取drawble目录下的plus图片  但是好像获取的文件有问题
        Resources resources = context.getResources();
        String path = resources.getResourceTypeName(R.drawable.plus) + "/" + resources.getResourceEntryName(R.drawable.plus)+".png";
        photoList.add(path);


    }



    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public Object getItem(int position) {
        return photoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.gridview_item,null);
            viewHolder = new ViewHolder(convertView);


            if (position == photoList.size()-1){
                Bitmap  bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.plus);
                viewHolder.imageView.setImageBitmap(bitmap1);
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.imageView.post(new Runnable() {
                            @Override
                            public void run() {

                                Bitmap bitmap = BitmapFactory.decodeFile(photoList.get(position));
                                Log.e("多张图片显示","  : "+"  位置："+position);//+photoList.get(position)
                                if(bitmap == null){
                                    Log.e ("ERROR","Bitmap失败");

                                }
                                if(bitmap != null){
                                    //Log.e ("ERROR","Bitmap成功");
                                    int scale = suggestionActivity.reckonThumbnail(bitmap.getWidth(),bitmap.getHeight(),125,125);
                                    Bitmap bitmap1 = suggestionActivity.PicZoom(bitmap,bitmap.getWidth()/scale,bitmap.getHeight()/scale);
                                    bitmap.recycle();
                                    bitmap = null;
                                    viewHolder.imageView.setImageBitmap(bitmap1);
                                    viewHolder.imageView.setPivotX(viewHolder.imageView.getWidth()/2);//设置锚点
                                    viewHolder.imageView.setPivotY(viewHolder.imageView.getHeight()/2);


//                                    if (flag == 1){
//                                        viewHolder.imageView.setRotation(0);
//                                        flag = 0 ;
//                                    }else if (flag == 0){
//                                        viewHolder.imageView.setRotation(90);
//                                    }

                                    //利用视频第一帧图片的路径进行判断
                                    if (suggestionActivity.videoPath == photoList.get(position)){
                                        viewHolder.imageView.setRotation(0);
                                    }else {
                                        viewHolder.imageView.setRotation(90);
                                    }

                                    viewHolder.imageView.setVisibility(View.VISIBLE);


                                }

                            }
                        });
                    }
                }).start();
            }


            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0,1,0,"相册");
                menu.add(0,2,0,"拍照");
                menu.add(0,3,0,"录像");
                menu.add(0,4,0,"取消");
            }
        });


        return convertView;
    }



    class ViewHolder{
        ImageView imageView;

        public ViewHolder(View view){
            imageView = view.findViewById(R.id.gridview_item_photo);
        }
    }
}
