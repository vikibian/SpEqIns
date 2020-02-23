package com.neu.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.neu.test.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class VideoActivity extends AppCompatActivity {
    private static String TAG = "VideoActivity";

    private boolean mStartedFlag = false; //录像中标志
    private MediaRecorder mRecorder = null ;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera= null;
    private MediaPlayer mMediaPlayer = null;
    private String path ;//最终视频路径
    private String imgName;
    private int timer = 0 ;//计时器
    private int maxSec = 30;//最长时间
    private String imgPath ;//图片地址？
    private  Uri uri;

    private LinearLayout mLlRecordOp;
    private LinearLayout mLlRecordBtn;
    private Button mBtnPlay;
    private Button mBtnRecord;
    private Button mBtnCancle;
    private Button mBtnSubmit;
    private TextView mTvRecordTip;

    public static String checkDetailsVideoPath;

    private String abDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();

        Log.e("ERROR","video crate");
        SurfaceView surfaceView = findViewById(R.id.mSurfaceview);
        mSurfaceHolder = surfaceView.getHolder();
        mMediaPlayer = new MediaPlayer();
        mSurfaceHolder.setFixedSize(1920,1680);
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                mSurfaceHolder = holder;
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                mCamera.setDisplayOrientation(90);
                try {
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    Camera.Parameters parameters = mCamera.getParameters();
                    parameters .setPictureFormat(PixelFormat.JPEG);
                    parameters.set("jpeg-quality", 100); //设置图片质量
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//连续对焦
                    mCamera.setParameters(parameters);
                } catch (IOException e) {
                    e.printStackTrace();
                    //失败
                    finish();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                mSurfaceHolder = holder;
                mCamera.startPreview();
                mCamera.cancelAutoFocus();
                mCamera.unlock();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //handler.removeCallbacks(runnable);
            }
        });


        mBtnRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP){
                    stopRecord();
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    startRecord();
                    //takePicture();
                }
                return true;
            }
        });

        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRecord();
            }
        });

        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED,intent);
                finish();
            }
        });

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlay();
                Intent intent = getIntent();
                intent.putExtra("path", path);
                intent.putExtra("imagePath", imgPath);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlay();
    }
    void initView(){
        mLlRecordOp = findViewById(R.id.mLlRecordOp);
        mLlRecordBtn = findViewById(R.id.mLlRecordBtn);
        mBtnPlay = findViewById(R.id.mBtnPlay);
        mBtnRecord = findViewById(R.id.mBtnRecord);
        mBtnCancle = findViewById(R.id.mBtnCancle);
        mBtnSubmit = findViewById(R.id.mBtnSubmit);
        mTvRecordTip = findViewById(R.id.mTvRecordTip);
    }


    //开始录制
    private void startRecord() {
        if (!mStartedFlag) {
            mStartedFlag = true;
            mLlRecordOp.setVisibility(View.INVISIBLE);
            mBtnPlay.setVisibility(View.INVISIBLE);
            mLlRecordBtn.setVisibility(View.INVISIBLE);
            //开始计时
            //handler.postDelayed(runnable, 1000);
            mRecorder = new MediaRecorder();//实例化MediaRecorder对象
            mRecorder.setCamera(mCamera);//使用摄像头
            mRecorder.reset();
            // 这两项需要放在setOutputFormat之前
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//摄像头互殴图像
            // Set output file format
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//输出格式
            // 这两项需要放在setOutputFormat之后
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//视频编码
            mRecorder.setVideoSize(1920,1080);
            mRecorder.setVideoFrameRate(20);//每秒20帧
            mRecorder.setVideoEncodingBitRate(1920*1080);
            mRecorder.setOrientationHint(90);//播放角度
            //设置记录会话的最大持续时间（毫秒）
            mRecorder.setMaxDuration(30 * 1000);
            path = Environment.getExternalStorageDirectory()+"/DCIM/Video/";
            abDate = getDate();
            String impName = abDate+"VIDEO"+".mp4";
            if (path != null) {
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir,impName);  //创建文件对象
                path = file.getAbsolutePath();
                Log.e(TAG,"path ab : "+ path);

                checkDetailsVideoPath = path;

                Log.e("视频路径",path);
                mRecorder.setOutputFile(path);
                try {
                    mRecorder.prepare();//准备录像
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mRecorder.start();//开始录制
            }
        }
    }

    //结束录制
    private void stopRecord() {
        if (mStartedFlag) {
            mStartedFlag = false;
            mLlRecordOp.setVisibility(View.VISIBLE);
            mBtnPlay.setVisibility(View.VISIBLE);
            mLlRecordBtn.setVisibility(View.VISIBLE);

            //handler.removeCallbacks(runnable);
            try {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
                mCamera.lock();
                mCamera.stopPreview();
                mCamera.release();
                mCamera=null;
                mBtnPlay.setVisibility(View.VISIBLE);
                Bitmap newBitmap = getVideoThumb(path);
                String impName = abDate+"IMG"+".jpg";
                imgPath = bitmap2File(newBitmap,impName);
                mLlRecordOp.setVisibility(View.VISIBLE);
            }
            catch (java.lang.RuntimeException e){
               // Log.e("拍摄时间过短", e.getMessage());
                mRecorder.reset();
                mRecorder.release();
                mRecorder = null;
                mCamera.takePicture(null,null,new Camera.PictureCallback(){
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {

                        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);//根据拍照获得的数据创建位图
                        camera.stopPreview();//停止预览
                        String srtImgPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
                        String impName = abDate+"IMG"+".jpg";
                        //获取sd中图片的位置
                        File appDir = new File(srtImgPath);
                        if(!appDir.exists()){ //如果目录不存在就创建
                            appDir.mkdir();
                        }
                        File file = new File(appDir,impName);  //创建文件对象

                        //设置照片的绝对路径
                        imgPath = srtImgPath + impName;
                        Uri uri = Uri.fromFile(file);
                        try {//保存拍到的图片
                            FileOutputStream fos = new FileOutputStream(file);  //创建一个输出流对象
                            bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);  //图片压缩
                            fos.flush();//缓冲区全部写入输出流
                            fos.close();;
                        }
                        catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //将图片插入到系统图库
                        try {
                            MediaStore.Images.Media.insertImage(VideoActivity.this.getContentResolver(),file.getAbsolutePath(),impName,null);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        //通知图库更新
                        VideoActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
                        Toast.makeText(VideoActivity.this,"短时照片已存"+file,Toast.LENGTH_LONG).show();

                        mCamera.lock();
                        mCamera.stopPreview();
                        mCamera.release();
                        mCamera = null;
                       // mBtnPlay.setVisibility(View.GONE);
                        //mLlRecordOp.setVisibility(View.VISIBLE);
                    }
                });

            }

        }
    }

    //播放录像
    private void playRecord() {

        mBtnPlay.setVisibility(View.INVISIBLE);
        mMediaPlayer.reset();
        Uri uri = Uri.parse(path);
        mMediaPlayer = MediaPlayer.create(VideoActivity.this, uri);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(mSurfaceHolder);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBtnPlay.setVisibility(View.VISIBLE);
            }

        });//播放解释后再次显示播放按钮
        try{
            mMediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    //停止播放录像
    private void stopPlay() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 获取系统时间
     * @return
     */
     String getDate(){
         Calendar ca = Calendar.getInstance();
         int year = ca.get(Calendar.YEAR);          // 获取年份
        int month = ca.get(Calendar.MONTH);       // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
        int minute = ca.get(Calendar.MINUTE) ;      // 分
        int hour = ca.get(Calendar.HOUR);           // 小时
        int second = ca.get(Calendar.SECOND);    // 秒
        return "" + year + (month + 1) + day + hour + minute + second;
    }




    private void takePicture(){
        mCamera.takePicture(null, null, jpeg);//拍照

    }
    final  Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);//根据拍照获得的数据创建位图
            camera.stopPreview();//停止预览
            //设置路径
            String srtImgPath = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/";
            String impName = abDate+"IMG"+".jpg";

            //获取sd中图片的位置
            File appDir = new File(srtImgPath);
            if(!appDir.exists()){ //如果目录不存在就创建
                appDir.mkdir();
            }
            File file = new File(appDir,impName);  //创建文件对象

            //设置照片的绝对路径
            srtImgPath = srtImgPath + impName;

            Uri uri = Uri.fromFile(file);

            try {//保存拍到的图片
                FileOutputStream fos = new FileOutputStream(file);  //创建一个输出流对象
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);  //图片压缩
                fos.flush();//缓冲区全部写入输出流
                fos.close();
                ;
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //将图片插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(VideoActivity.this.getContentResolver(),file.getAbsolutePath(),impName,null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //通知图库更新
            VideoActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            mCamera.stopPreview();//停止预览
            mCamera.release();//释放资源
        }
    };

    /* @param path 视频文件的路径 * @return Bitmap 返回获取的Bitmap */
    private   Bitmap getVideoThumb(String path) {
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(path);
        return  media.getFrameAtTime();
    }

    /**  * Bitmap保存成File * * @param bitmap input bitmap * @param name output file's name * @return String output file's path */
    private   String bitmap2File(Bitmap bitmap, String name) {

        //设置路径
        String srtImgPath = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/";
        String impName = name;
        //获取sd中图片的位置
        File appDir = new File(srtImgPath);
        if(!appDir.exists()){ //如果目录不存在就创建
            appDir.mkdir();
        }
        File file = new File(appDir,impName);  //创建文件对象
        //设置照片的绝对路径
        srtImgPath = srtImgPath + impName;
        Uri uri = Uri.fromFile(file);
        try {//保存拍到的图片
            FileOutputStream fos = new FileOutputStream(file);  //创建一个输出流对象
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);  //图片压缩
            fos.flush();//缓冲区全部写入输出流
            fos.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将图片插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(VideoActivity.this.getContentResolver(),file.getAbsolutePath(),impName,null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //通知图库更新
        VideoActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri));
        Toast.makeText(VideoActivity.this,"截图照片已存"+file,Toast.LENGTH_LONG).show();
        return  srtImgPath;
    }

}
