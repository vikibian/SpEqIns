package com.neu.test.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nanchen.compresshelper.CompressHelper;
import com.neu.test.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureActivity extends AppCompatActivity {

    private ImageButton btn_takepicture;
    private Button btn_preview;
    private Button btn_ensure;
    private SurfaceView sv_camara;
    private Camera camara;
    private String picString;
    private byte[] bitmapByte;
   // private  Bitmap myBitmap;
    private boolean isPreview = false; //是否为预览状态

    private boolean isTakingPicture;

    public static String checkdetailsPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        //为标志位赋值
        isTakingPicture = false;

        //拍照
        sv_camara = findViewById(R.id.sv_camara);
        btn_preview = findViewById(R.id.btn_preview);
        btn_ensure = findViewById(R.id.btn_ensure);
        btn_takepicture = findViewById(R.id.btn_takepicture);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏显示
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //高亮显示

        //打开摄像头并预览
        final SurfaceHolder surfaceHolder = sv_camara.getHolder();//获取SurfaceHolder
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置SurfaceView自己不维护缓冲

        btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isPreview) {
                    camara = Camera.open();//打开摄像头
                    btn_takepicture.setImageResource(R.mipmap.btn_video_record_p);
                    isPreview = true; //设置为预览状态
                    btn_preview.setText("调焦");
                }
                try {
                    camara.setPreviewDisplay(surfaceHolder); //设置用于显示预览的SurfaceView
                    Camera.Parameters parameters = camara.getParameters();//获取摄像头参数
                    parameters.setPictureFormat(PixelFormat.JPEG);//设置图片格式
                    parameters.set("jpeg-quality", 100); //设置图片质量
                    parameters.setRotation(90);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//连续对焦
                    camara.setParameters(parameters);//重新设置摄像头参数
                    camara.startPreview();//开始预览
                    camara.setDisplayOrientation(90);
                    //camara.autoFocus(null);//设置自动对焦

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isPreview) {
                    camara.autoFocus(null);//自动对焦
                }

            }
        });



        btn_takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // camara.autoFocus(null);//设置自动对焦
                btn_takepicture.setImageResource(R.mipmap.btn_video_record_n);
                //实现拍照功能
                if (camara != null) {
                    camara.takePicture(null, null, jpeg);//拍照

                    //若拍照 则将拍照标志位进行置位
                    isTakingPicture = true;
                } else {

                    Log.e("Error", "无摄像头");
                }


            }
        });



        btn_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对是否拍照进行判断  监测拍照的标志位
                if (isTakingPicture){
                    Intent intent1 = getIntent();
                    intent1.putExtra("imagePath",picString);
                    Log.d("图片地址： "," picture: "+picString);
                    setResult(RESULT_OK,intent1);
                    isTakingPicture = false;
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"对不起，您还未拍摄照片！",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }





    final  Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);//根据拍照获得的数据创建位图
            camera.stopPreview();//停止预览
            isPreview = false;//设置为非预览

            //设置路径
            String srtImgPath = Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/";
            String impName = System.currentTimeMillis()+"IMG"+".jpg";

            //获取sd中图片的位置
            File appDir = new File(srtImgPath);
            if(!appDir.exists()){ //如果目录不存在就创建
                appDir.mkdir();
            }
            File file = new File(appDir,impName);  //创建文件对象

            try {//保存拍到的图片
                FileOutputStream fos = new FileOutputStream(file);  //创建一个输出流对象
                bitmap.compress(Bitmap.CompressFormat.JPEG,50,fos);  //图片压缩
                fos.flush();//缓冲区全部写入输出流
                fos.close();

            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri uri = Uri.fromFile(file);

            String testphoto = System.currentTimeMillis()+"IMG";

            File newfile = new CompressHelper.Builder(getApplicationContext())
                    .setMaxWidth(720)  // 默认最大宽度为720
                    .setMaxHeight(960) // 默认最大高度为960
                    .setQuality(80)// 默认压缩质量为80
                    .setFileName(testphoto) // 设置你需要修改的文件名
                    .setCompressFormat(Bitmap.CompressFormat.PNG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(Environment.getExternalStorageDirectory().toString()+"/DCIM/Camera/")
                    .build()
                    .compressToFile(file);
            uri = Uri.fromFile(newfile);

            //设置照片的绝对路径
            srtImgPath = newfile.getAbsolutePath();

            picString = srtImgPath;


            checkdetailsPhoto = picString;


            //将图片插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(PictureActivity.this.getContentResolver(),newfile.getAbsolutePath(),testphoto+"IMG.png",null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //通知图库更新
            PictureActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
           // Toast.makeText(PictureActivity.this,"照片已存"+file,Toast.LENGTH_LONG).show();

           // resetCamera();//重新预览
        }
    };

    private  void resetCamera(){
        if (!isPreview){
            camara.startPreview();
            isPreview = true;
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        //停止预览并释放摄像头资源
        if(camara != null){
            camara.stopPreview();//停止预览
            camara.release();//释放资源
        }
    }



}
