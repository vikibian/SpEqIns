package com.neu.test.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.neu.test.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * created by Viki on 2020/3/1
 * system login name : lg
 * created time : 16:12
 * email : 710256138@qq.com
 */
public class SuggestionActivitySaveDataUtil {
    private Context context;
    private String taskid;
    private List<String> photolist = new ArrayList<>();

    public SuggestionActivitySaveDataUtil(Context context){
        this.context = context;

    }

    public void save(String taskid, int position, String status, String suggestion, String editSugg, List<String> photolist){
        String name = context.getResources().getString(R.string.sharepreference);
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = getDate()+"$"+taskid+"$"+String.valueOf(position);
        String value = status+"#"+suggestion+"#"+editSugg+"#";
        for (int i=0;i<photolist.size();i++){
            if (i != photolist.size()-1){
                value = value+photolist.get(i)+"&";
            }else {
                value = value+photolist.get(i);
            }
        }
        editor.putString(key,value);
        editor.apply();
//        Log.e("SuggestionActivity","  "+load(taskid,position));
//        load(taskid,position);
    }

    public String load(String taskid,int position){
        String name = context.getResources().getString(R.string.sharepreference);
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        String key = getDate()+"$"+taskid+"$"+String.valueOf(position);
        String result = sharedPreferences.getString(key,"not");

//        String[] resultSet = result.split(String.valueOf('#'));
//
//        for (int j=0;j<resultSet.length;j++){
//            Log.e("SuggestionActivity"," resultSet"+resultSet[j]);
//        }
//        String[] pathSet = resultSet[resultSet.length-1].split(String.valueOf('&'));
//        for (int j=0;j<pathSet.length;j++){
//            Log.e("SuggestionActivity"," pathSet"+pathSet[j]);
//            Log.e("SuggestionActivity"," pathSet"+pathSet[j].equals(" "));
//        }
        return result;
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
