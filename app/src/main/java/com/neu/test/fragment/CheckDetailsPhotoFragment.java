package com.neu.test.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.neu.test.R;
import com.neu.test.activity.PictureActivity;
import com.neu.test.adapter.CheckPhotoListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckDetailsPhotoFragment extends Fragment {

    private ImageView mImageView;
    private String photoPath;

    //测试ListView
    private ListView photoCheckListview;
    private List<String> photoPaths = new ArrayList<>();
    private CheckPhotoListViewAdapter checkPhotoListViewAdapter;


    public CheckDetailsPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_details_photo, container, false);
        initView(view);

        PictureActivity pictureActivity = new PictureActivity();
        photoPath = pictureActivity.checkdetailsPhoto;

        //真机上的图片位置
        //photoPaths.add("/storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg");///storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg
        //photoPaths.add("/storage/emulated/0/DCIM/Camera/1578971327735IMG.jpg");

        /**
         * 这是AndroidStudio模拟器上测试图片的位置
         */
        photoPaths.add("/storage/emulated/0/DCIM/Camera/IMG_20200113_014453.jpg");///storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg
        photoPaths.add("/storage/emulated/0/DCIM/Camera/IMG_20200114_081231.jpg");
        photoPaths.add("/storage/emulated/0/DCIM/Camera/IMG_20200114_081242.jpg");
        photoPaths.add("/storage/emulated/0/DCIM/Camera/TIM20200114164635.jpg");
        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg");



        /**
        * 原来只显示一张图片时调用的时这个函数
        * */
        //decodePhoto();

        checkPhotoListViewAdapter = new CheckPhotoListViewAdapter(getContext(),photoPaths);
        photoCheckListview.setAdapter(checkPhotoListViewAdapter);


        return view;
    }

    private void initView(View view) {
        mImageView = view.findViewById(R.id.check_detail_photo_iv_show);

        //对测试的ListView的id进行连接
        photoCheckListview = view.findViewById(R.id.listview_fragment_check_details_photo);
    }


    //这个函数已经弃用
    //无参的decodePhoto函数
//    public void decodePhoto() {
//        mImageView.setVisibility(View.VISIBLE);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mImageView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                                //Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/DCIM/Camera/1573743302500IMG.jpg");//photoPath
//                                Bitmap bitmap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/1573743302500IMG.jpg");//photoPath
//                                //Bitmap bitmap = BitmapFactory.decodeFile(photoPath);//photoPath
//                                bitmap = adjustPhotoRotation(bitmap,90);
//                                Log.d("path","  "+photoPath);
//                                Log.d("bitmap","  bitmap.width: "+bitmap.getWidth()+"  bitmap.getheight: "+bitmap.getHeight());
//
//                                if (bitmap == null){
//                                    Toast.makeText(getContext(),"图片加载失败",Toast.LENGTH_SHORT).show();
//                                }
//                                if(bitmap!= null){
//                                    int[] scales;
//                                    scales=getScreenWithandHeight();
//                                    int scale = getProportion(bitmap.getWidth(),bitmap.getHeight(),scales[0],scales[1]);
//                                    //Bitmap bitmap1 =PicZoom(bitmap,bitmap.getWidth()/scale,bitmap.getHeight()/scale);
//                                    Log.d("screen","  screen.width: "+scales[0]+"  screen.height: "+scales[1]);
//                                    Log.d("screen","  scale: "+(bitmap.getWidth()/scales[0])+"  "+(bitmap.getWidth()/scales[1]));
//
//                                    Bitmap bitmap1 =PicZoom(bitmap,scales[0],scales[1]);
//                                    Log.d("bitmap1","  bitmap1.width: "+bitmap1.getWidth()+"  botmap1.height: "+bitmap1.getHeight());
//
//                                    mImageView.setImageBitmap(bitmap1);
//                                    mImageView.setScaleType(ImageView.ScaleType.CENTER);
//                                    //mImageView.setRotation(90);
//                                    mImageView.setVisibility(View.VISIBLE);
//                                }
//                        }catch (Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        }).start();
//    }


//    private int getProportion(int oldWidth, int oldHeight, int newWidth, int newHeight) {
//        if ((oldHeight > newHeight && oldWidth > newWidth)
//                || (oldHeight <= newHeight && oldWidth > newWidth)) {
//            int be = (int) (oldWidth / (float) newWidth);
//            if (be <= 1)
//                be = 1;
//            return be;
//        } else if (oldHeight > newHeight && oldWidth <= newWidth) {
//            int be = (int) (oldHeight / (float) newHeight);
//            if (be <= 1)
//                be = 1;
//            return be;
//        }
//        return  1;
//    }
//
//    private Bitmap PicZoom(Bitmap bitmap, int width, int height) {
//        int bitmapWidth = bitmap.getWidth();
//        int bitmapHeight = bitmap.getHeight();
//        Log.d("bitmap-zoom","  bitmap.width: "+bitmap.getWidth()+"  bitmap.getheight: "+bitmap.getHeight());
//
//
//        Matrix matrix = new Matrix();
//        Log.d("Matrix","  width/bitmapHeight: "+(float)width/bitmapHeight+"  height/bitmapWidth: "+(float)height/bitmapWidth);
//        matrix.setScale((float)width/bitmapWidth,(float)height/bitmapHeight);
//
//        return Bitmap.createBitmap(bitmap,0,0,(bitmapWidth),(bitmapHeight),matrix,true);
//    }
//
//    private int[] getScreenWithandHeight(){
//        int[] scale = new int[2];
//        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
//        scale[0] = windowManager.getDefaultDisplay().getWidth();
//        scale[1] = windowManager.getDefaultDisplay().getHeight();
//
//        return scale;
//    }
//
//    private Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree)
//    {
//
//        Matrix m = new Matrix();
//        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
//        float targetX, targetY;
//        if (orientationDegree == 90) {
//            targetX = bm.getHeight();
//            targetY = 0;
//        } else {
//            targetX = bm.getHeight();
//            targetY = bm.getWidth();
//        }
//
//        final float[] values = new float[9];
//        m.getValues(values);
//
//        float x1 = values[Matrix.MTRANS_X];
//        float y1 = values[Matrix.MTRANS_Y];
//        Log.d("x1: "," "+x1);
//        Log.d("y1: "," "+y1);
//        Log.d("targetX: "," "+targetX);
//        Log.d("targetY: "," "+targetY);
//
//
//        m.postTranslate(targetX - x1, targetY - y1);
//
//        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
//
//        Paint paint = new Paint();
//        Canvas canvas = new Canvas(bm1);
//        canvas.drawBitmap(bm, m, paint);
//
//
//        return bm1;
//    }



}
