package com.soo.ify.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.soo.ify.R;

public class ExtViewPager extends ViewPager {
    
    private boolean scrollAble = true;
    
    public ExtViewPager(Context context) {
        super(context);
    }

    public ExtViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ExtViewPager);
        scrollAble = ta.getBoolean(R.styleable.ExtViewPager_scrollAble, true);
        ta.recycle();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return scrollAble ? super.onInterceptTouchEvent(arg0) : scrollAble;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return scrollAble ? super.onTouchEvent(arg0) : scrollAble;
    }
    
    public void setScrollAble(boolean able) {
        this.scrollAble = able;
    }
}
