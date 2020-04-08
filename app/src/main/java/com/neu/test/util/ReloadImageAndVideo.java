package com.neu.test.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Viki on 2020/3/18
 * system login name : lg
 * created time : 21:45
 * email : 710256138@qq.com
 */
public class ReloadImageAndVideo {
    private static final String TAG = "ReloadImageAndVideo";


    public List<String> getPathlist(String ImagePath, String VideoPath, String loginname){

        List<String> pathlistOfPhoto = new ArrayList<>();

        if(!(ImagePath.equals(""))){
            String[] imgSplit = ImagePath.split(",");
            Log.e("lenth",imgSplit.length+"");
            for (int i=0;i<imgSplit.length;i++){
                String imgPath = Environment.getExternalStorageDirectory() +"/DCIM/"+loginname+"/Photo/"+imgSplit[i];
                File file = new File(imgPath);
                //本地有，从本地读取
                if(file.exists()){
                    pathlistOfPhoto.add(imgPath);
                }//若本地没有，从服务器获取
                else {
                    String path = BaseUrl.pathOfPhotoAndVideo+loginname+"/"+imgSplit[i];
                    pathlistOfPhoto.add(path);
                }
            }
        }
        if(!(VideoPath.equals(""))){
            String[] vdoSplit = VideoPath.split(",");
            for (int i=0;i<vdoSplit.length;i++){
                String vdoPath = Environment.getExternalStorageDirectory() +"/DCIM/"+loginname+"/Video/"+vdoSplit[i];
                File file = new File(vdoPath);
                //本地有，从本地读取
                if(file.exists()){
                    pathlistOfPhoto.add(vdoPath);
                }//若本地没有，从服务器获取
                else {
                    String path = BaseUrl.pathOfPhotoAndVideo+loginname+"/"+vdoSplit[i];
                    pathlistOfPhoto.add(path);
                }
            }
        }

        return pathlistOfPhoto;
    }

    public List<String> decodeImagePath(String imagePath,String name) {
        List<String> paths = new ArrayList<>();
        if ((!imagePath.equals(""))&&(!imagePath.equals(null))){
            String[] splitpath = imagePath.split(",");
            for (int i=0;i<splitpath.length;i++){

                String imgPath = Environment.getExternalStorageDirectory() +"/DCIM/"+name+"/Photo/"+splitpath[i];
                File file = new File(imgPath);
                //本地有，从本地读取
                if(file.exists()){
                    paths.add(imgPath);
                }//若本地没有，从服务器获取
                else {
                    String path = BaseUrl.pathOfPhotoAndVideo+name+"/"+splitpath[i];
                    paths.add(path);
                }
            }
        }else {

        }
        return paths;
    }

    public List<String> decodeVideoPath(String videoPath,String name) {

        List<String> paths = new ArrayList<>();
        if ((!videoPath.equals(""))&&(!videoPath.equals(null))){
            String[] splitpath = videoPath.split(",");
            for (int i=0;i<splitpath.length;i++){
                String vdoPath = Environment.getExternalStorageDirectory() +"/DCIM/"+name+"/Video/"+splitpath[i];
                File file = new File(vdoPath);
                //本地有，从本地读取
                if(file.exists()){
                    paths.add(vdoPath);
                }//若本地没有，从服务器获取
                else {
                    String path = BaseUrl.pathOfPhotoAndVideo+name+"/"+splitpath[i];
                    paths.add(path);
                }
            }
        }else {

        }
        return paths;
    }

    public int getVideoNumber(String videoPath) {

        int number = 0;
        if ((!videoPath.equals(""))&&(!videoPath.equals(null))){
            String[] splitpath = videoPath.split(",");
            number = splitpath.length;
        }else {

        }
        return number;
    }

    public int getPhotoNumber(String imagePath) {
        int number = 0;
        if ((!imagePath.equals(""))&&(!imagePath.equals(null))){
            String[] splitpath = imagePath.split(",");
            number = splitpath.length;
        }else {

        }
        return number;
    }
}
