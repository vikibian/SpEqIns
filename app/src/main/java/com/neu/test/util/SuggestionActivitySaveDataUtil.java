package com.neu.test.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.neu.test.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * created by Viki on 2020/3/1
 * system login name : lg
 * created time : 16:12
 * email : 710256138@qq.com
 */
public class SuggestionActivitySaveDataUtil {
    private static final String TAG = "SuggestionActivitySaveD";
    private Context context;
    private String taskid;
    private List<String> photolist = new ArrayList<>();

    public SuggestionActivitySaveDataUtil(Context context){
        this.context = context;

    }

    public void save(String name,String phone){
        /**
         * SharedPreferences将用户的数据存储到该包下的shared_prefs/config.xml文件中，
         * 并且设置该文件的读取方式为私有，即只有该软件自身可以访问该文件
         */
        SharedPreferences sPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor editor=sPreferences.edit();
        //当然sharepreference会对一些特殊的字符进行转义，使得读取的时候更加准确
        editor.putString("phonenumber", phone);

//        PhotoActivity.phonenumber = phone;
        editor.commit();

    }

    public String load(String name){
        //已登录软件便获取手机号
        //显示用户此前录入的数据
        String phonenumber ="";
        SharedPreferences sPreferences = context.getSharedPreferences(name, MODE_PRIVATE);
        Log.e(TAG," "+String.valueOf(sPreferences.equals(null)));
        if (!sPreferences.equals(null)){
            String num = sPreferences.getString("phonenumber", "");
            Log.e(TAG," "+num.equals(""));
            Log.e(TAG," "+num.equals(null));
//            if (!num.equals("")){
//                 phonenumber = num;
//            }
            phonenumber = num;
        }

        return phonenumber;
    }


    /**
     * 获取系统时间
     * @return
     */
    public String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);          // 获取年份
        int month = ca.get(Calendar.MONTH);       // 获取月份
        int day = ca.get(Calendar.DATE);            // 获取日
//        int minute = ca.get(Calendar.MINUTE) ;      // 分
//        int hour = ca.get(Calendar.HOUR);           // 小时
//        int second = ca.get(Calendar.SECOND);    // 秒
        return "" + year + (month + 1) + day ;
    }

}
