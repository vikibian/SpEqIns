package com.neu.test.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.neu.test.R;
import com.neu.test.activity.CheckMediaForRectifyActivity;
import com.neu.test.activity.PictureActivity;
import com.neu.test.activity.ShowVideoActivity;
import com.neu.test.adapter.CheckPhotoListViewAdapter;
import com.neu.test.entity.DetectionResult;
import com.neu.test.layout.MyGridView;
import com.neu.test.util.ReloadImageAndVideo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CheckDetailsPhotoFragment extends Fragment {
    private static final String TAG = "CheckDetailsPhotoFragme";

    private ImageView mImageView;
    private String photoPath;

    //测试ListView
    private ListView photoCheckListview;
    private List<String> photoPaths = new ArrayList<>();
    private CheckPhotoListViewAdapter checkPhotoListViewAdapter;
    private DetectionResult detectionResult;
    private MyGridView gridView;
    private LinearLayout media_lin;
    private MediaAdapter mediaAdapter;
    private List<String> imageList = new ArrayList<>();
    private List<String> videoList = new ArrayList<>();
    private List<String> mediaList = new ArrayList<>();

    private MyGridView gridView_change;
    private LinearLayout media_change_lin;
    private MediaAdapter mediaAdapter_change;
    private List<String> change_imageList = new ArrayList<>();
    private List<String> change_videoList = new ArrayList<>();
    private List<String> change_mediaList = new ArrayList<>();

    private ScrollView scrollView;
    private TextView no_resource;


    public CheckDetailsPhotoFragment() {
        // Required empty public constructor
    }

    public CheckDetailsPhotoFragment(DetectionResult detectionResult) {
        this.detectionResult = detectionResult;
//        detectionResult.setREFJIM("image_202031610246.jpg,image_202031662445.jpg");
//        detectionResult.setREFJVI("video_1584540967093.mp4,video_1584582023465.mp4");
//        detectionResult.setCHANGEDIMAGE("image_202031662445.jpg,image_202031610246.jpg");
//        detectionResult.setCHANGEDVIDEO("video_1584582023465.mp4,video_1584540967093.mp4");
    }

    public CheckDetailsPhotoFragment(List<String> photoPaths) {
        // Required empty public constructor
        this.photoPaths = photoPaths;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_details_photo, container, false);
        initView(view);
        initcontent(view);

        //真机上的图片位置
//        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg");///storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg
//        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578971327735IMG.jpg");

//        photoPaths.add("/storage/emulated/0/DCIM/Camera/TIM20200114164635.jpg");
//        photoPaths.add("/storage/emulated/0/DCIM/Camera/1578984512676IMG.jpg");

//        //之前listview的代码
//        checkPhotoListViewAdapter = new CheckPhotoListViewAdapter(getContext(),photoPaths);
//        photoCheckListview.setAdapter(checkPhotoListViewAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((imageList.size()<=position)&&(position<(imageList.size()+videoList.size()))){
                    Intent intent2 = new Intent(getActivity(), ShowVideoActivity.class);
                    Log.e(TAG, "onClick: 视频地址   "+videoList);
                    intent2.putExtra("video", (Serializable) videoList);
                    intent2.putExtra("position",(position-imageList.size()));
                    startActivity(intent2);
                }else {
                    initImageView(position,imageList);
                }
                //展示选中图片
                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });

        gridView_change.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((change_imageList.size()<=position)&&(position<(change_imageList.size()+change_videoList.size()))){
                    Intent intent2 = new Intent(getActivity(), ShowVideoActivity.class);
                    intent2.putExtra("video", (Serializable) change_videoList);
                    intent2.putExtra("position",(position-change_imageList.size()));
                    startActivity(intent2);
                }else {
                    initImageView(position,change_imageList);
                }
            }
        });


        return view;
    }

    private void initcontent(View view) {
        ReloadImageAndVideo reloadImageAndVideo = new ReloadImageAndVideo();

        imageList = reloadImageAndVideo.decodeImagePath(detectionResult.getREFJIM(),detectionResult.getLOGINNAME());
        videoList = reloadImageAndVideo.decodeVideoPath(detectionResult.getREFJVI(),detectionResult.getLOGINNAME());

        change_imageList = reloadImageAndVideo.decodeImagePath(detectionResult.getCHANGEDIMAGE(),detectionResult.getLOGINNAME());
        change_videoList = reloadImageAndVideo.decodeVideoPath(detectionResult.getCHANGEDVIDEO(),detectionResult.getLOGINNAME());

        for (int i = 0;i<(imageList.size()+videoList.size());i++){
            if (i<imageList.size()){
                mediaList.add(imageList.get(i));
            }else if ((i>=imageList.size())&&(i<(imageList.size()+videoList.size()))){
                mediaList.add(videoList.get((i-imageList.size())));
            }
        }
        Log.e(TAG,"  media大小："+mediaList.size());
        mediaAdapter = new MediaAdapter(mediaList,imageList.size(),videoList.size());
        gridView.setAdapter(mediaAdapter);

        for (int i = 0;i<(change_imageList.size()+change_videoList.size());i++){
            if (i<change_imageList.size()){
                change_mediaList.add(change_imageList.get(i));
            }else if ((i>=change_imageList.size())&&(i<(change_imageList.size()+change_videoList.size()))){
                change_mediaList.add(change_videoList.get((i-change_imageList.size())));
            }
        }
        Log.e(TAG,"  media大小："+change_mediaList.size());
        mediaAdapter_change = new MediaAdapter(change_mediaList,change_imageList.size(),change_videoList.size());
        gridView_change.setAdapter(mediaAdapter_change);


        if ((mediaList.size() == 0)&&(change_mediaList.size() == 0)){
            scrollView.setVisibility(View.GONE);
            no_resource.setVisibility(View.VISIBLE);
        }else {
            if (mediaList.size() == 0){
                media_lin.setVisibility(View.GONE);
            }else {
                media_lin.setVisibility(View.VISIBLE);
            }

            if (change_mediaList.size() == 0){
                media_change_lin.setVisibility(View.GONE);
            } else {
                media_change_lin.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initView(View view) {
        gridView = view.findViewById(R.id.check_details_media_gridview);
        gridView_change = view.findViewById(R.id.check_details_media_change_gridview);

        media_lin = view.findViewById(R.id.check_detail_unchange_lin);
        media_change_lin = view.findViewById(R.id.check_detail_change_lin);

        scrollView = view.findViewById(R.id.check_detail_scrollview);
        no_resource = view.findViewById(R.id.check_detail_media_textview_no_resource);

        //对测试的ListView的id进行连接
//        photoCheckListview = view.findViewById(R.id.listview_fragment_check_details_photo);
    }

    private void initImageView(int position,List<String> photos) {
        final WindowManager windowManager = getActivity().getWindowManager();
        final RelativeLayout relativeLayout = new RelativeLayout(getContext());
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        //FLAG_LAYOUT_IN_SCREEN
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        layoutParams.format = PixelFormat.RGBA_8888;//让背景透明，放大过程可以看到当前界面
        layoutParams.verticalMargin = 0;
        windowManager.addView(relativeLayout,layoutParams);

        final PhotoView photoview = new PhotoView(getContext());
        photoview.enable();
        photoview.setScaleType(ImageView.ScaleType.FIT_CENTER);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.addView(photoview,params);
        relativeLayout.setFocusableInTouchMode(true);

        Glide
                .with(relativeLayout.getContext())
                .load(photos.get(position))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(photoview);

        photoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeView(relativeLayout);
            }
        });

        relativeLayout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != windowManager && null != relativeLayout) {
                        windowManager.removeView(relativeLayout);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    class MediaAdapter extends BaseAdapter {

        private List<String> pathlists;
        private int photosize = 0;
        private int videosize = 0;

        public MediaAdapter(List<String> list,int photosize,int videosize) {
            pathlists = new ArrayList<>();
            this.pathlists = list;
            this.photosize = photosize;
            this.videosize = videosize;

        }

        @Override
        public int getCount() {
            return pathlists.size();
        }

        @Override
        public Object getItem(int position) {
            return pathlists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MediaViewHolder mediaViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.fujian_layout_redetection2, null);
                mediaViewHolder = new MediaViewHolder(convertView);
                convertView.setTag(mediaViewHolder);
            } else {
                mediaViewHolder = (MediaViewHolder) convertView.getTag();
            }

            if ((photosize <= position) && (position < (photosize + videosize))) {
                mediaViewHolder.imagePre.setVisibility(View.VISIBLE);
            } else {
                mediaViewHolder.imagePre.setVisibility(View.INVISIBLE);
            }
            Log.e(TAG, "getView: "+pathlists.get(position));

            Glide
                    .with(getContext())
                    .load(pathlists.get(position))
                    .centerCrop()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(mediaViewHolder.image);


            return convertView;
        }
    }

        class MediaViewHolder {
            private ImageView image;
            private ImageView imagePre;

            public MediaViewHolder(View view){
                image = view.findViewById(R.id.fujian_gridview_item_photo);
                imagePre = view.findViewById(R.id.fujian_gridview_item_photo_pre);
            }
        }

}
