package com.neu.test.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Scroller;

import com.neu.test.R;

public class SlideLayout extends FrameLayout {

    private View contentView;

    private View menuView;


    private RadioButton rb_detection_1;
    private RadioButton rb_detection_2;


    //private int viewHeight; //高是相同的
    private int menuViewHeight;
    private int contentViewHeight;
    private int halfContentWidth;


    private int contentWidth;

    private int menuWidth;


    //滑动器

    private Scroller scroller;



    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }


    /**

     * 布局文件加载完成时被调用

     */

    @Override

    protected void onFinishInflate() {

        super.onFinishInflate();

        contentView = findViewById(R.id.tv_detection_item);

        menuView = findViewById(R.id.tv_detection_menu);
        Log.e("SideLayout"," "+menuView.getTextAlignment());

        rb_detection_1 = findViewById(R.id.rb_detection_1);
        rb_detection_2 = findViewById(R.id.rb_detection_2);

    }



    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        menuViewHeight = getMeasuredHeight();
        contentViewHeight =(int) menuViewHeight/3;

        contentWidth = contentView.getMeasuredWidth();
        halfContentWidth = (int) contentWidth/2;
        menuWidth = menuView.getMeasuredWidth();

    }



    @Override

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0,0,contentWidth,2*contentViewHeight);
        rb_detection_1.layout(0,2*contentViewHeight,halfContentWidth,menuViewHeight);
        rb_detection_2.layout(halfContentWidth,2*contentViewHeight,contentWidth,menuViewHeight);
        menuView.layout(contentWidth, 0, contentWidth+menuWidth, menuViewHeight);

    }





    private float startX;

    private float startY;



    private float downX;

    private float downY;



    @Override

    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);

        switch (event.getAction())

        {

            case MotionEvent.ACTION_DOWN:

                downX = startX = event.getX();

                downY = startY = event.getY();

                break;

            case MotionEvent.ACTION_MOVE:

                float endX = event.getX();

                float endY = event.getY();



                //计算偏移量

                float distanceX = endX - startX;



                int toScrollX = (int) (getScrollX()-distanceX);

                //屏蔽非法值

                if (toScrollX < 0 )

                {

                    toScrollX = 0;

                }

                if (toScrollX > menuWidth)

                {

                    toScrollX = menuWidth;

                }

                System.out.println("toScroll-->"+toScrollX+"-->"+getScrollX());

                scrollTo(toScrollX,getScrollY());



                startX = event.getX();



                float dx = Math.abs(event.getX()-downX);

                float dy = Math.abs(event.getY()-downY);

                if (dx > dy && dx > 6)

                {

                    //事件反拦截，使父ListView的事件传递到自身SlideLayout

                    getParent().requestDisallowInterceptTouchEvent(true);

                }



                break;

            case MotionEvent.ACTION_UP:



                if (getScrollX() > menuWidth/2)

                {

                    //打开menu

                    openMenu();

                }else {

                    closeMenu();

                }



                break;

        }

        return true;

    }



    @Override

    public boolean onInterceptTouchEvent(MotionEvent event) {

        switch (event.getAction())

        {

            case MotionEvent.ACTION_DOWN:

                downX = startX = event.getX();

                downY = startY = event.getY();

                if (onStateChangeListener != null)

                {

                    onStateChangeListener.onMove(this);

                }

                break;

            case MotionEvent.ACTION_MOVE:



                float dx = Math.abs(event.getX()-downX);

                float dy = Math.abs(event.getY()-downY);

                if (dx > dy && dx > 6)

                {

                    //拦截事件

                    return true;

                }



                break;

            case MotionEvent.ACTION_UP:

                break;

        }

        return super.onInterceptTouchEvent(event);

    }



    /**

     * 打开menu菜单

     */

    public void openMenu() {

        int dx = menuWidth-getScrollX();

        scroller.startScroll(getScrollX(), getScrollY(),dx, getScrollY());

        invalidate();

        if (onStateChangeListener != null)

        {

            onStateChangeListener.onOpen(this);

        }

    }



    /**

     * 关闭菜单

     */

    public void closeMenu() {

        //0表示menu移动到的目标距离

        int dx = 0-getScrollX();

        scroller.startScroll(getScrollX(), getScrollY(),dx, getScrollY());

        invalidate();

        if (onStateChangeListener != null)

        {

            onStateChangeListener.onClose(this);

        }

    }



    @Override

    public void computeScroll() {

        super.computeScroll();

        if (scroller.computeScrollOffset())

        {

            scrollTo(scroller.getCurrX(), scroller.getCurrY());

            invalidate();

        }

    }



    public interface OnStateChangeListener

    {

        void onOpen(SlideLayout slideLayout);

        void onMove(SlideLayout slideLayout);

        void onClose(SlideLayout slideLayout);

    }



    public OnStateChangeListener onStateChangeListener;



    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {

        this.onStateChangeListener = onStateChangeListener;

    }







}
