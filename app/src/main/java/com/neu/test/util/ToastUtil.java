package com.neu.test.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.neu.test.R;

public class ToastUtil {


  public static void showNumber(Context context,String content){
    View view = LayoutInflater.from(context).inflate( R.layout.toast_num_layout,null);
    TextView textView = view.findViewById(R.id.tv_number);
    Typeface typeface = Typeface.createFromAsset(context.getAssets(),"日本青柳衡山毛笔字体.ttf");
    textView.setTypeface(typeface);
    textView.setText(content);
    Toast toast = new Toast(context);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER,0,0);
    toast.setView(view);
    toast.show();
  }

  public static void showString(Context context,String content){
    View view = LayoutInflater.from(context).inflate( R.layout.toast_num_layout,null);
    TextView textView = view.findViewById(R.id.tv_number);
    Typeface typeface = Typeface.createFromAsset(context.getAssets(),"日本青柳衡山毛笔字体.ttf");
    textView.setTypeface(typeface);
    textView.setText(content);
    Toast toast = new Toast(context);
    toast.setDuration(Toast.LENGTH_LONG);
    toast.setGravity(Gravity.CENTER,0,0);
    toast.setView(view);
    toast.show();
  }



}
