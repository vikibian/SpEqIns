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

            decodePhoto(paths.get(position),viewHolder);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        return convertView;
    }

    class ViewHolder{
        ImageView imageView;

        public ViewHolder(View convertView){
            imageView = convertView.findViewById(R.id.check_detail_photo_iv_show);

        }
    }


    //有参的函数
    public void decodePhoto(final String path,final ViewHolder viewHolder) {
        viewHolder.imageView.setVisibility(View.VISIBLE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                viewHolder.imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/1573743302500IMG.jpg");//photoPath
                            //Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/1573743302500IMG.jpg");//photoPath
                            //Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/IMG_20191110_164646.jpg");//photoPath
                            Bitmap bitmap = BitmapFactory.decodeFile(path);//photoPath

                            bitmap = adjustPhotoRotation(bitmap,90);
                            Log.d("path","  "+path);
                            Log.d("bitmap","  bitmap.width: "+bitmap.getWidth()+"  bitmap.getheight: "+bitmap.getHeight());

                            if (bitmap == null){
                                Toast.makeText(context,"图片加载失败",Toast.LENGTH_SHORT).show();
                            }
                            if(bitmap != null){
                                int[] scales;
                                scales = getScreenWithandHeight();
                                int scale = getProportion(bitmap.getWidth(),bitmap.getHeight(),scales[0],scales[1]);
                                //Bitmap bitmap1 =PicZoom(bitmap,bitmap.getWidth()/scale,bitmap.getHeight()/scale);
                                Log.d("screen","  screen.width: "+scales[0]+"  screen.height: "+scales[1]);
                                Log.d("screen","  scale: "+(bitmap.getWidth()/scales[0])+"  "+(bitmap.getWidth()/scales[1]));

                                Bitmap bitmap1 =PicZoom(bitmap,scales[0],scales[1]);
                                Log.d("bitmap1","  bitmap1.width: "+bitmap1.getWidth()+"  botmap1.height: "+bitmap1.getHeight());

                                viewHolder.imageView.setImageBitmap(bitmap1);
                                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER);
                                //mImageView.setRotation(90);
                                viewHolder.imageView.setVisibility(View.VISIBLE);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }


    /**
     * 得到图片的缩放比例
     * @param oldWidth 之前图片的宽度
     * @param oldHeight 之前图片的高度
     * @param newWidth 新图片的宽度
     * @param newHeight 新图片的高度
     * @return
     */
    private int getProportion(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        if ((oldHeight > newHeight && oldWidth > newWidth)
                || (oldHeight <= newHeight && oldWidth > newWidth)) {
            int be = (int) (oldWidth / (float) newWidth);
            if (be <= 1)
                be = 1;
            return be;
        } else if (oldHeight > newHeight && oldWidth <= newWidth) {
            int be = (int) (oldHeight / (float) newHeight);
            if (be <= 1)
                be = 1;
            return be;
        }
        return  1;
    }

    /**
     * 图片的放大
     * @param bitmap 需要放大的图片
     * @param width 宽度
     * @param height 高度
     * @return
     */
    private Bitmap PicZoom(Bitmap bitmap, int width, int height) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Log.d("bitmap-zoom","  bitmap.width: "+bitmap.getWidth()+"  bitmap.getheight: "+bitmap.getHeight());


        Matrix matrix = new Matrix();
        Log.d("Matrix","  width/bitmapHeight: "+(float)width/bitmapHeight+"  height/bitmapWidth: "+(float)height/bitmapWidth);
        matrix.setScale((float)width/bitmapWidth,(float)height/bitmapHeight);

        return Bitmap.createBitmap(bitmap,0,0,(bitmapWidth),(bitmapHeight),matrix,true);
    }

    /**
     * 获取屏幕的宽度和高度 位显示图片做铺垫
     * @return 返回屏幕宽度和高度的数组
     */
    private int[] getScreenWithandHeight(){
        int[] scale = new int[2];
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        scale[0] = windowManager.getDefaultDisplay().getWidth();
        scale[1] = windowManager.getDefaultDisplay().getHeight();

        return scale;
    }

    /**
     * 调整图片的角度，因为经过前面拍照后的图片是有一个角度差，所以需要在显示之前将图片进行旋转
     * @param bm  图片资源
     * @param orientationDegree  旋转角度
     * @return
     */
    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
    {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        Log.d("x1: "," "+x1);
        Log.d("y1: "," "+y1);
        Log.d("targetX: "," "+targetX);
        Log.d("targetY: "," "+targetY);


        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);


        return bm1;
    }
}
