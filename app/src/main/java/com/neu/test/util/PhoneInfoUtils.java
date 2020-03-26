package com.neu.test.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.neu.test.activity.SuggestionActivity;

/**
 * created by Viki on 2020/3/21
 * system login name : lg
 * created time : 14:40
 * email : 710256138@qq.com
 */
public class PhoneInfoUtils implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static String TAG = "PhoneInfoUtils";
    public final static int REQUEST_READ_PHONE_STATE = 1;
    private TelephonyManager telephonyManager;
    //移动运营商编号
    private String NetworkOperator;
    private Context context;
    private String number = "N/A";

    public PhoneInfoUtils(Context context, Activity activity) {
        this.context = context;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

//        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
//                && (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(activity, new String[]{
//                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_NUMBERS
//            }, REQUEST_READ_PHONE_STATE);
//        }

    }


    //获取电话号码
    public String getNativePhoneNumber() {


        if ((ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                &&(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)) {

            number = telephonyManager.getLine1Number();
        }


        return number;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_PHONE_STATE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//
//                    if (ContextCompat.checkSelfPermission(context,Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
//                            && ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
//                            && ContextCompat.checkSelfPermission(context,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                        number = telephonyManager.getLine1Number();
//                        return;
//                    }
//
//                }
//                break;
//
//            default:
//                break;
//        }
    }
}
