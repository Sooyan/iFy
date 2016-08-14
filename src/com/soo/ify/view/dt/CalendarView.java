/*
 * Copyright (c) 2013-2014 Soo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soo.ify.view.dt;

import com.soo.ify.R;
import com.soo.ify.view.dt.util.CalendarContext;

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
    
    private CalendarContext calendarContext;
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
        calendarContext = new CalendarContext(context, attrs);
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
    
    public CalendarContext getCalendarContext() {
        return calendarContext;
    }

    private interface CalendarViewDelegate {
        
    }
    
    abstract static class AbsCalendarViewDelegate implements CalendarViewDelegate {
        
        protected Context context;
        protected CalendarView proxy;
        protected CalendarContext calendarContext;
        
        AbsCalendarViewDelegate(Context context, CalendarView proxy, AttributeSet attrs) {
            this.context = context;
            this.proxy = proxy;
            this.calendarContext = proxy.getCalendarContext();
        }
    }
}
