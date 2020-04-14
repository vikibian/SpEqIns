package com.neu.test.util;


public class ClickUtil {
  private static final int MIN_CLICK_DELAY_TIME = 500;
  private static long lastClickTime;

  public static boolean isFastClick(){
    boolean flag = false;
    long currenTime = System.currentTimeMillis();
    if((currenTime - lastClickTime) <= MIN_CLICK_DELAY_TIME){
      flag = true;
    }
    lastClickTime = currenTime;
    return flag;
  }
}
