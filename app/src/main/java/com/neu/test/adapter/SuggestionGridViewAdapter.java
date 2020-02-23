package com.neu.test.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.neu.test.R;
import com.neu.test.activity.PictureActivity;
import com.neu.test.activity.SuggestionActivity;
import com.neu.test.activity.VideoActivity;
import com.neu.test.util.ImageUtil;

import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuggestionGridViewAdapter extends BaseAdapter {
    private static String TAG = "SuggestionAdapter";

    private Context context;
    private List<String> photoList ;
    private int flag;

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
            //Log.d ("photoList position"," "+photoList.get(i));
            photoList.add(pathlistOfPhoto.get(i).toString());
        }
        Collections.reverse(photoList);
        //photoList.add("/storage/emulated/0/DCIM/Camera/1581774108382IMG.jpg");
        //获取drawble目录下的plus图片  但是好像获取的文件有问题
        Resources resources = context.getResources();
        String path = resources.getResourceTypeName(R.drawable.plus3) + "/" + resources.getResourceEntryName(R.drawable.plus3)+".png";
        photoList.add(path);
        Log.e ("plus2 "," "+path);
        Log.e ("photoList size"," "+photoList.size());


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
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Log.e("图片地址：",photoList.get(position));

        if (position == (photoList.size()-1)){
            Log.e ("position"," "+(photoList.size()-1));
            Log.e ("== "," 判断加号图片"+(photoList.size()-1));
            //Glide.with(context).load(R.drawable.plus).into(viewHolder.imageView);
            Glide.with(context).load(R.drawable.plus3).into(viewHolder.imageView);

        }else if (position<(photoList.size()-1)){
            Log.e ("Glide"," Glide显示图片:"+position);
            int length = photoList.get(position).length();
            String suffix = photoList.get(position).substring( length-3,length);

            if (suffix.equals("mp4")) {
                Log.e (TAG," 视频地址: "+photoList.get(position));

                String name = getVideoName(photoList.get(position));
                String imPath =  Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/"+name+"IMG.jpg";
                File imFile = new File(imPath);
                if (imFile.exists()){
                    Log.e(TAG," 文件存在！");

                    Bitmap bitmap = waterMaskVideoPhoto(name,imPath);

                    Glide
                        .with(context)
                        .load(bitmap)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(viewHolder.imageView);
                }else {
                    Log.e(TAG," 文件不存在！");
                    Glide
                            .with(context)
                            .load(photoList.get(position))
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(viewHolder.imageView);
                }

            } else if (suffix.equals("jpg")||suffix.equals("png")){
                Log.e(TAG,"jpg suffix: "+suffix);
                Glide
                        .with(context)
                        .load(photoList.get(position))
                        .transform(new RotateTransformation(context, 0f))
                        .into(viewHolder.imageView);
            }else {
                Log.e(TAG,"else suffix: "+suffix);
                //加载其他图片类型
                Glide
                        .with(context)
                        .load(photoList.get(position))
                        .transform(new RotateTransformation(context, 0f))
                        .into(viewHolder.imageView);
            }

        }

        //Glide.with(context).load(R.drawable.plus).into(viewHolder.imageView);

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

    /**
     * @date : 2020/2/23
     * @time : 21:51
     * @author : viki
     * @description : 判断是不是系统中早已存在的图片
     * @param videopath
     */

    private String getVideoName(String videopath) {
        String name = videopath.substring(videopath.lastIndexOf("/")+1,
                videopath.lastIndexOf("V"));
        Log.e(TAG,"视频地址：lastIndex / "+videopath.lastIndexOf("/")+1);
        Log.e(TAG,"视频地址：lastIndex . "+videopath.lastIndexOf(".")+1);
        Log.e(TAG,"视频地址：name: "+name);
        return name;
    }

    /**
     * @date : 2020/2/23
     * @time : 21:24
     * @author : viki
     * @description : 添加水印图片
     */

    private Bitmap waterMaskVideoPhoto(String name, String imagepath) {

        Bitmap bitmap = null;

        try {


            Bitmap src = BitmapFactory.decodeFile(imagepath);
            //Bitmap src = BitmapFactory.decodeResource(context.getResources(),R.drawable.plus);
            Bitmap mask = BitmapFactory.decodeResource(context.getResources(),R.mipmap.watermask);
            Log.e(TAG," mask宽度： "+mask.getWidth());
            Log.e(TAG," mask高度度： "+mask.getHeight());

            Bitmap suoxiao = ThumbnailUtils.extractThumbnail(mask,200,200);
            Log.e(TAG," 缩小后mask宽度： "+suoxiao.getWidth());
            Log.e(TAG," 缩小后mask高度度： "+suoxiao.getHeight());

            bitmap = ImageUtil.createWaterMaskCenter(src,suoxiao);
        } catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }


    class ViewHolder{
        ImageView imageView;

        public ViewHolder(View view){
            imageView = view.findViewById(R.id.gridview_item_photo);
        }
    }

    static class RotateTransformation extends BitmapTransformation {

        private float rotateRotationAngle = 0f;

        public RotateTransformation(Context context, float rotateRotationAngle) {
            super();

            this.rotateRotationAngle = rotateRotationAngle;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Matrix matrix = new Matrix();

            matrix.postRotate(rotateRotationAngle);

            return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        }

        public String getId() {
            return "rotate" + rotateRotationAngle;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}
