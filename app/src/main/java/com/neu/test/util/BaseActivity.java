package com.neu.test.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.neu.test.R;
import com.neu.test.activity.SplashActivity;

public class BaseActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null){
      Intent intent = new Intent(this, SplashActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
    }
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
}
