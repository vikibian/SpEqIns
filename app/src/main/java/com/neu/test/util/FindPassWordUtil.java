package com.neu.test.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by Viki on 2020/4/19
 * system login name : lg
 * created time : 9:37
 * email : 710256138@qq.com
 */
public class FindPassWordUtil {

    public String getRandomCode() {
        String strRand = "";
        for (int i = 0; i < 4; i++) {
            strRand += String.valueOf((int) (Math.random() * 10));
        }
        Log.i("aaa", "randomCode: " + strRand);
        return strRand;
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public  boolean checkPassword(String password){
        //正则表达式判断数字或密码
        Pattern Password_Pattern = Pattern.compile("^[A-Za-z0-9]+$");
        Matcher matcher = Password_Pattern.matcher(password);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /***
     * 验证邮箱
     */
    public  boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
