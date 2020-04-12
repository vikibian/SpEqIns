package com.neu.test.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.neu.test.R;

public class BaseActivity extends AppCompatActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
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
