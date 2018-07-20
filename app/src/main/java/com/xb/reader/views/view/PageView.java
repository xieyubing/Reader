package com.xb.reader.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by asus on 2017/8/7.
 */

public class PageView extends View {

    private Bitmap bit;
    private int touchSlop;  //滑动距离
    private OnScrollListener mScrollListener;
    private OnClickCallback mOnClickCallback;
    private int clickX;  //单击的X
    private int currentX;  //当前的X
    private boolean moved;
    private int stemUiVisibility;

    public PageView(Context context) {
        super(context);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnClickCallback != null && Math.abs(clickX - currentX) < touchSlop){
                    int width = getWidth();
                    if(clickX > width * 2/3){
                        mOnClickCallback.onRightClick();
                    }else if(clickX < width / 3){
                        mOnClickCallback.onLeftClick();
                    }else{
                        mOnClickCallback.onMiddleClick();
                    }
                }
            }
        });
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){  // 按下
            clickX = (int)event.getX();
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){  //移动
            if(Math.abs(event.getX()-clickX) > touchSlop){
                moved = true;
            }
        }else if(event.getAction() == MotionEvent.ACTION_UP){  //抬起
            currentX = (int) event.getX();
            if(moved && mScrollListener != null){
                if(clickX > currentX){
                    mScrollListener.onLeftScroll();
                }else{
                    mScrollListener.onRightScroll();
                }
            }
            moved = false;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //canvas.drawColor(Color.BLUE);
        canvas.drawBitmap(bit,0,0,null);
        canvas.restore();
    }

    public void setBitmap(Bitmap bit) {
        this.bit = bit;
    }

    public void setOnClickCallback(OnClickCallback listener){
        mOnClickCallback = listener;
    }

    public void setOnScrollListener(OnScrollListener listener){
        mScrollListener = listener;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }


    public interface OnScrollListener {  //滑动事件
        void onLeftScroll();
        void onRightScroll();
    }
    public interface OnClickCallback {  //点击事件
        void onLeftClick();
        void onMiddleClick();
        void onRightClick();
    }
}
