package com.neu.test.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;

/**
 * created by Viki on 2020/4/7
 * system login name : lg
 * created time : 21:11
 * email : 710256138@qq.com
 */
public class CacheUtil {


    //计算缓存大小 代码
    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     */
    public long getFileSizes(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
            Log.e("获取文件大小",flist[i].getAbsolutePath()+i);
            if (flist[i].isDirectory()){
                size = size + getFileSizes(flist[i]);
                Log.e("获取文件大小",size+""+i);
            }
            else{
                size =size + getFileSize(flist[i]);
                Log.e("获取文件大小",size+""+i+"-");
            }
        }
        return size;
    }

    /**
     * 获取指定文件大小
     * @param
     * @return
     * @throws Exception
     */
    public long getFileSize(File file) throws Exception
    {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
            Log.e("获取文件大小",size+"-");
        }
        else{
            file.createNewFile();
            Log.e("获取文件大小","文件不存在!");
            return 0;
        }
        return size;
    }

    public   String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }


}
