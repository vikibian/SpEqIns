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
import com.neu.test.entity.DetectionResult;

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

    public CheckDetailsPhotoFragment(DetectionResult detectionResult) {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_details_photo, container, false);
        initView(view);

        //真机上的图片位置
//        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg");///storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg
//        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578971327735IMG.jpg");

        /**
         * 这是AndroidStudio模拟器上测试图片的位置
         */
        photoPaths.add("http://39.97.108.172:8080/pic/123456789103Image1.jpg");///storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg
        photoPaths.add("http://39.97.108.172:8080/pic/123456789103Image0.jpg");
        //photoPaths.add("http://39.97.108.172:8080/pic/ 123456789102Image0.jpg");

//        photoPaths.add("/storage/emulated/0/DCIM/Camera/TIM20200114164635.jpg");
//        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg");



        /**
        * 原来只显示一张图片时调用的时这个函数
        * */
        //decodePhoto();

        checkPhotoListViewAdapter = new CheckPhotoListViewAdapter(getContext(),photoPaths);
        photoCheckListview.setAdapter(checkPhotoListViewAdapter);


        return view;
    }

    private void initView(View view) {
       // mImageView = view.findViewById(R.id.check_detail_photo_iv_show);

        //对测试的ListView的id进行连接
        photoCheckListview = view.findViewById(R.id.listview_fragment_check_details_photo);
    }


}
