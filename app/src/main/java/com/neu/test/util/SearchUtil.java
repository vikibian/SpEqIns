package com.neu.test.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by Viki on 2020/3/3
 * system login name : lg
 * created time : 21:20
 * email : 710256138@qq.com
 */
public class SearchUtil {
    private static String TAG = "SearchUtil";
    public String[] deviceType = {"通用","锅炉","压力容器","电梯","起重机","厂车","大型游乐设施","压力管道","客车索道"};
    public String[] deviceTypeForDraft = {"通用","锅炉","压力容器","电梯","起重机","厂车","大型游乐设施","压力管道","客车索道"};
    public String[] deviceQualify = {"合格","不合格"};
    public String[] taskType = {"自查","整改","下派","临时"};
    public String[] taskTypeNew = {"日常","整改","政府专项","企业专项"};
    public String[] classofdev ={"10000","1000","2000","3000","4000","5000","6000","8000","9000"};
    public String[] classofdevForDraft ={"10000","1000","2000","3000","4000","5000","6000","8000","9000"};
    public Map<String,String> typeToDevclass = new HashMap<>();
    public Map<String,String> devclassToType = new HashMap<>();
    public Map<String,String> map_devclass = new HashMap<>();
    public String[] mTitles = new String[]{"文字描述", "图片视频"};
    public String[] choose = {"全部","合格","不合格","整改合格"};
    public List<String> chooseList ;
    public String hege = "0";
    public String nohege = "1";
    public String undecided = "2";
    public String recifyQualify = "3";
    public String hegeText = "合格";
    public String nohegeText = "不合格";
    public String undecidedText = "未定";
    public String recifyQualifyText = "整改合格";

    public String recify = "整改";

    public String changed = "1";
    public String unchanged = "0";

    public String haveDetail = "1";
    public String noHaveDetail = "0";

    public String getTypeToDevclass(String type){
        initTypeToDevclass();

        return typeToDevclass.get(type);
    }

    public String getDevclassToType(String devclass){
        initDevclassToType();
        return devclassToType.get(devclass);
    }

    private void initDevclassToType() {
        for (int i=0;i<deviceType.length;i++){
            devclassToType.put(classofdev[i],deviceType[i]);
        }
    }

    private void initTypeToDevclass() {
        for (int i=0;i<deviceType.length;i++){
            typeToDevclass.put(deviceType[i],classofdev[i]);
        }
    }

    public Map<String,String> getMapdevclass(){
        for (int i=0;i<classofdev.length;i++){
            map_devclass.put(classofdev[i],deviceType[i]);
        }
        return map_devclass;
    }

    public Map<String,String> getMapdevclassReverse(){
        Map<String,String> helpMapForResult = new HashMap<>();
        for (int i=0;i<classofdev.length;i++){
            helpMapForResult.put(deviceType[i],classofdev[i]);
        }
        return helpMapForResult;
    }

    public String getNumToQuality(String num){
        String quality = "";
        if (num.equals("0")){
            quality = "合格";
        } else if (num.equals("1")){
            quality = "不合格";
        }else if (num.equals("2")){
            quality = "未定";
        }
        return quality;
    }

    public String getQualityToNum(String quality){
        String num = " ";
        if (quality.equals("合格")){
            num = "0";
        } else if (quality.equals("不合格")){
            num = "1";
        }else if (quality.equals("未定")){
            num = "2";
        }else if (quality.equals("整改合格")){
            num = "3";
        }
        return num;
    }

    public Map<String,String> getHelpMapForResult(){
        Map<String,String> helpMapForResult = new HashMap<>();
        helpMapForResult.put(hege,hegeText);
        helpMapForResult.put(nohege,nohegeText);
        helpMapForResult.put(recifyQualify,recifyQualifyText);
        return helpMapForResult;
    }

    public Map<String,String> getHelpMapForResultReverse(){
        Map<String,String> helpMapForResult = new HashMap<>();
        helpMapForResult.put(hegeText,hege);
        helpMapForResult.put(nohegeText,nohege);
        helpMapForResult.put(recifyQualifyText,recifyQualify);
        return helpMapForResult;
    }

    public List<String> getChooseList(){
        chooseList = new ArrayList<>();
        for (int i=0;i<choose.length;i++){
            chooseList.add(choose[i]);
        }
        return chooseList;
    }
}
