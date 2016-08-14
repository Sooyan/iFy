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
package com.soo.ify.view.dt.cell;

import java.util.Calendar;
import java.util.Locale;

import com.soo.ify.view.dt.CalendarStandard;
import com.soo.ify.view.dt.util.CalendarContext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Soo
 */
public abstract class CellView extends View {
    
    private static final Locale DEFAULT_LOCALE = Locale.CHINA;
    
    protected CalendarStandard standard;
    protected Calendar currentCalendar;
    protected Locale currentLocale = DEFAULT_LOCALE;
    
    public CellView(Context context) {
        this(context, null);
    }
    
    public CellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.standard = new CalendarStandard(context, attrs);
        init();
    }
    
    private void init() {
        
    }
    
    public void setCalendarContext(CalendarContext calendarContext) {
        
    }
    
    public void setCalendarStandard(CalendarStandard standard) {
        this.standard = standard;
        onDateChanged();
        invalidate();
    }
    
    public void setCurrentDate(Calendar calendar) {
        if (this.currentCalendar != null && this.currentCalendar.equals(calendar)) {
            return;
        }
        this.currentCalendar = newCalendar(calendar);
        onDateChanged();
        invalidate();
    }
    
    protected abstract void onDateChanged();
    
    protected Calendar newCalendar() {
        if (standard != null) {
            return standard.newCalendar();
        }
        return Calendar.getInstance(currentLocale);
    }
    
    protected Calendar newCalendar(Calendar calendar) {
        Calendar c = newCalendar();
        c.setTimeInMillis(calendar.getTimeInMillis());
        return c;
    }
}
