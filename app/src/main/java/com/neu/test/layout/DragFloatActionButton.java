package com.neu.test.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.core.view.ViewConfigurationCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * created by Viki on 2020/4/17
 * system login name : lg
 * created time : 22:00
 * email : 710256138@qq.com
 */
public class DragFloatActionButton extends FloatingActionButton {
    private static final String TAG = "DragFloatActionButton";

    private int parentWidth;
    private int parentHeight;
    private ViewGroup parent;
    private int lastX;
    private int lastY;

    private boolean isDrag;
    private int slop;

    public DragFloatActionButton(Context context) {
        super(context);
        init(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        slop= ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
        Log.e(TAG, "init:"+slop);
    }

    private void init(Context context) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                if(getParent()!=null){
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(parentHeight<=0||parentWidth<=0){
                    //如果不存在父类的宽高则无法拖动，默认直接返回false
                    isDrag=false;
                    break;
                }

                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些手机无法触发点击事件的问题
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                Log.e("distance---->",distance+"");
                //此处稍微增加一些移动的偏移量，防止手指抖动，误判为移动无法触发点击时间
                if(distance==0||distance<=slop){
                    isDrag=false;
                    break;
                }
                isDrag = true;
                float x = getX() + dx;
                float y = getY() + dy;

                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x ;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y ;
                setX(x);
                setY(y);
                lastX=rawX;
                lastY=rawY;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                }
                Log.e("up---->",isDrag+"");
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);

    }

    public boolean isDrag() {
        return isDrag;
    }


}
