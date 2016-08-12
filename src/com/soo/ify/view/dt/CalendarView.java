/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.dt;

import com.soo.ify.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Soo
 */
public class CalendarView extends FrameLayout {
    
    public enum Mode {
        List(1),
        Pager(2);
        
        int value;
        
        Mode(int value) {
            this.value = value;
        }
        
        public int value() {
            return value;
        }
        
        public static Mode valueOf(int value) {
            switch (value) {
                case 1: return List;
                case 2: return Pager;
            }
            return null;
        }
    }
    
    private int mode;
    private CalendarViewDelegate delegate;
    
    public CalendarView(Context context) {
        this(context, null);
    }
    
    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public CalendarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mode = typedArray.getInt(R.styleable.CalendarView_mode, Mode.List.value());
        switch (Mode.valueOf(mode)) {
            case List:
                delegate = new ListCalendarDelegate(context, this, attrs);
                break;
            case Pager:
                delegate = new PagerCalendarDelegate(context, this, attrs);
                break;
        }
        typedArray.recycle();
    }

    private interface CalendarViewDelegate {
        
    }
    
    abstract static class AbsCalendarViewDelegate implements CalendarViewDelegate {
        
        protected Context context;
        protected CalendarView proxy;
        
        AbsCalendarViewDelegate(Context context, CalendarView proxy, AttributeSet attrs) {
            this.context = context;
            this.proxy = proxy;
        }
    }
}
