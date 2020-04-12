package com.neu.test.layout;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.core.content.ContextCompat;

import com.neu.test.R;

import java.util.ArrayList;
import java.util.List;

public class RaeSeekBar extends AppCompatSeekBar {

  //  刻度说明文本，数组数量跟刻度数量一致，跟mTextSize的长度要一致

  private String[] mTickMarkTitles = new String[]{

    "A",
    "标准",
    "",
    "",
    "A"

  };

  // 刻度代表的字体大小

  private int[] mTextSize = new int[]{
    16,
    18,
    20,
    22,
    24
  };


Context context;
  // 刻度文本画笔

  private final Paint mTickMarkTitlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

  //  刻度文本字体大小

  private float mTickMarkTitleTextSize = 18;

  // 刻度文本跟刻度之间的间隔

  private float mOffsetY = 40;

  // 刻度线的高度

  private int mLineHeight = 10;



  // 保存位置大小信息

  private int mThumbWidth;

  private final Rect mRect = new Rect();

  private int mThumbHeight;
  // ...省略一些其他构造函数

  public RaeSeekBar(Context context, AttributeSet attrs) {

    super(context, attrs);
    this.context = context;
    init();

  }



  protected void init() {

    //  初始化刻度文本字体大小

    mTickMarkTitleTextSize = getSize(mTickMarkTitleTextSize);

    // 刻度文本跟刻度之间的间隔

    mOffsetY = getSize(mOffsetY);

    // 刻度线的高度

    mLineHeight = getSize(mLineHeight);

    // 刻度文字的对齐方式为居中对齐

    mTickMarkTitlePaint.setTextAlign(Paint.Align.CENTER);

    // 刻度文字的字体颜色

    mTickMarkTitlePaint.setColor(ContextCompat.getColor(getContext(), R.color.black));

    // 设置最大刻度值为字体大小数组的长度

    setMax(mTextSize.length-1);

    // 设置当前的刻度
 SharedPreferences sp = context.getSharedPreferences("Theme",Context.MODE_PRIVATE);
    List<Integer> themeList = new ArrayList<>();
    themeList.add(R.style.Theme_Small);
    themeList.add(R.style.Theme_Medium);
    themeList.add(R.style.Theme_Large);
    themeList.add(R.style.Theme_Large_Small);
    themeList.add(R.style.Theme_Large_Media);
 int theme = sp.getInt("SizeTheme",-1);
 if(theme ==  -1){
   setProgress(1);
   Log.e("sssssssssize","我第一次");
 }
 else{
   int bar = themeList.indexOf(theme);
   setProgress(bar);
   Log.e("sssssssssize","我第N次");
 }



  }


  @Override

  protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    mThumbWidth = getThumb().getIntrinsicWidth();

    mThumbHeight = getThumb().getIntrinsicHeight();
    // 原来的布局大小
    int wm = MeasureSpec.getMode(widthMeasureSpec);
    int hm = MeasureSpec.getMode(heightMeasureSpec);
    int w = getMeasuredWidth();
    int h = getMeasuredHeight();
    // 以最大的字体为基础，加上刻度字体大小
    h += getSize(mTextSize[mTextSize.length-1]);
    // 加上与刻度之间的间距大小
    h += mOffsetY;
    // 保存测量结果
    setMeasuredDimension(MeasureSpec.makeMeasureSpec(w, wm), MeasureSpec.makeMeasureSpec(h, hm));
  }

  @Override
  protected void onDraw(Canvas canvas) {

    super.onDraw(canvas);
    // 刻度长度
    int maxLength = getMax();
    int width = getWidth();
    int height = getHeight();
    int h2 = height / 2; // 居中
    // 画刻度背景
    mRect.left = getPaddingLeft();
    mRect.right = width - getPaddingRight();
    mRect.top = h2 - getSize(1); // 居中
    mRect.bottom = mRect.top + getSize(1.5f); // 1.5f为直线的高度
    // 画直线
    canvas.drawRect(mRect, mTickMarkTitlePaint);
    // 直线的长度
    int lineWidth = mRect.right - mRect.left;

    //  遍历刻度，画分割线和刻度文本

    for (int i = 0; i <= maxLength; i++) {

      // 刻度的起始间隔 = 左间距 + (线条的宽度 * 当前刻度位置 / 刻度长度)

      int thumbPos = getPaddingLeft() + (lineWidth * i / maxLength);

      // 画分割线

      mRect.top = h2 - mLineHeight / 2;

      mRect.bottom = h2 + mLineHeight / 2;

      mRect.left = thumbPos;

      mRect.right = thumbPos + getSize(1.5f); // 直线的宽度为1.5

      canvas.drawRect(mRect, mTickMarkTitlePaint);

      // 画刻度文本
      String title = mTickMarkTitles[i % mTickMarkTitles.length]; // 拿到刻度文本

      mTickMarkTitlePaint.getTextBounds(title, 0, title.length(), mRect); // 计算刻度文本的大小以及位置

      mTickMarkTitlePaint.setTextSize(getSize(mTextSize[i])); // 设置刻度文字大小
      // 画文本
      canvas.drawText(title, thumbPos, getSize(mTextSize[mTextSize.length - 1]), mTickMarkTitlePaint);

    }

  }

  protected int getSize(float size) {

    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources().getDisplayMetrics());

  }

  public int getRawTextSize(int progress) {

    return mTextSize[progress % mTextSize.length];

  }

  public int getTextSize(int progress) {

    return getSize(getRawTextSize(progress));

  }



  public void setTextSize(int size) {

    for (int i = 0; i < mTextSize.length; i++) {

      int textSize = getSize(mTextSize[i]);

      if (textSize == size) {

        setProgress(i);

        break;

      }

    }

  }

}

