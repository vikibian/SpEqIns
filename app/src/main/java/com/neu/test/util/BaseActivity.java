package com.neu.test.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.neu.test.R;
import com.neu.test.activity.ChangeFontActivity;
import com.neu.test.activity.FragmentManagerActivity;
import com.neu.test.activity.SplashActivity;

public class BaseActivity extends AppCompatActivity {

  public static String myLongitude ="";
  public static String myLatitude = "";
  GPSUtil gpsUtil;
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    gpsUtil = new GPSUtil(this);
    if (savedInstanceState != null){
      Intent intent = new Intent(this, SplashActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    }
    gpsUtil.startLocate();
    SharedPreferences sp = getSharedPreferences("Theme", Context.MODE_PRIVATE);
    int size = sp.getInt("SizeTheme",-1);
    if(size == -1){
      this.setTheme(R.style.Theme_Medium);
      Log.e("theme:","R.style.Theme_Medium"+ R.style.Theme_Medium);
    }else{
      this.setTheme(size);
      Log.e("theme:","R.style.Theme_?"+size);
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if((!gpsUtil.getLongitude().equals(""))&&(!gpsUtil.getLatitude().equals(""))){
      if((!myLongitude.equals(gpsUtil.getLongitude()))||(!myLatitude.equals(gpsUtil.getLatitude()))){
        myLongitude = gpsUtil.getLongitude();
        myLatitude = gpsUtil.getLatitude();
      }
    }
    if(gpsUtil.isOpenGPS()){
      gpsUtil.startLocate();
    }else{

      MessageDialog.build(this)
        .setStyle(DialogSettings.STYLE.STYLE_IOS)
        .setTitle("提示")
        .setMessage("请打开位置信息")
        .setCancelable(false)
        .setOkButton("设置", new OnDialogButtonClickListener() {
          @Override
          public boolean onClick(BaseDialog baseDialog, View v) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return false;
          }
        })
        .show();
    }
  }
}
