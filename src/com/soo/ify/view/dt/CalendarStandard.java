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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.soo.ify.R;

/**
 * @author Soo
 */
public class CalendarStandard {
    
    private final static Locale DEFAULT_LOCALE = Locale.CHINA;
    
    private Context context;
    private Locale locale = DEFAULT_LOCALE;
    private Calendar minCalendar;
    private Calendar maxCalendar;
    private Calendar curCalendar;

    public CalendarStandard(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        String minDateStr = typedArray.getString(R.styleable.CalendarView_minDate);
        String maxDateStr = typedArray.getString(R.styleable.CalendarView_maxDate);
        try {
            DateFormat SDFORMATER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", locale);
            minCalendar = newCalendar(SDFORMATER.parse(minDateStr));
            maxCalendar = newCalendar(SDFORMATER.parse(maxDateStr));
            mustBefore(minCalendar, maxCalendar);
        } catch (Exception e) {
            minCalendar = newCalendar();
            minCalendar.set(Calendar.YEAR, 2016);
            minCalendar.set(Calendar.MONTH, 0);
            minCalendar.set(Calendar.DAY_OF_MONTH, 1);
            minCalendar.set(Calendar.HOUR_OF_DAY, 0);
            minCalendar.set(Calendar.MINUTE, 0);
            minCalendar.set(Calendar.SECOND, 0);
            
            maxCalendar = newCalendar();
            maxCalendar.set(Calendar.YEAR, 2050);
            maxCalendar.set(Calendar.MONTH, 11);
            maxCalendar.set(Calendar.DAY_OF_MONTH, 1);
            maxCalendar.set(Calendar.HOUR_OF_DAY, 0);
            maxCalendar.set(Calendar.MINUTE, 0);
            maxCalendar.set(Calendar.SECOND, 0);
        }
        curCalendar = newCalendar();
        
        typedArray.recycle();
    }
    
    public int getMonthTotalCount() {
        int minYear = minCalendar.get(Calendar.YEAR);
        int maxYear = maxCalendar.get(Calendar.YEAR);
        int minMonth = minCalendar.get(Calendar.MONTH);
        int maxMonth = maxCalendar.get(Calendar.MONTH);
        int disYear = maxYear - minYear;
        if (disYear > 0) {
            return disYear * 12 + maxMonth + 1;
        } else {
            return minMonth + 1;
        }
    }
    
    public Calendar getMonthCalendarByPosition(int position) {
        Calendar tmpCalendar = newCalendar(minCalendar);
        tmpCalendar.add(Calendar.MONTH, position);
        if (tmpCalendar.after(maxCalendar)) {
            tmpCalendar = newCalendar(maxCalendar);
        }
        return tmpCalendar;
    }
    
    public void setMinCalendar(Calendar calendar) {
        mustBefore(calendar, maxCalendar);
        this.minCalendar = newCalendar(calendar);
    }
    
    public void setMaxCalendar(Calendar calendar) {
        mustBefore(minCalendar, calendar);
        this.maxCalendar = newCalendar(calendar);
    }
    
    public void setCurrentCalendar(Calendar calendar) {
        clamp(minCalendar, calendar, maxCalendar);
        this.curCalendar = newCalendar(calendar);
    }
    
    public Calendar getMinCalendar() {
        return minCalendar;
    }
    
    public Calendar getMaxCalendar() {
        return maxCalendar;
    }
    
    public Calendar getCurrentCalendar() {
        return curCalendar;
    }
    
    public Context getContext() {
        return context;
    }
    
    public Calendar newCalendar() {
        Calendar calendar = Calendar.getInstance(locale);
        return calendar;
    }
    
    public Calendar newCalendar(Date date) {
        Calendar calendar = newCalendar();
        calendar.setTime(date);
        return calendar;
    }
    
    public Calendar newCalendar(long millis) {
        Calendar calendar = newCalendar();
        calendar.setTimeInMillis(millis);
        return calendar;
    }
    
    public Calendar newCalendar(Calendar calendar) {
        Calendar t = newCalendar();
        t.setTimeInMillis(calendar.getTimeInMillis());
        return t;
    }
    
    public static void mustBefore(Calendar minCalendar, Calendar maxCalendar) {
        mustBefore(minCalendar.getTimeInMillis(), maxCalendar.getTimeInMillis());
    }
    
    public static void mustBefore(Date minDate, Date maxDate) {
        mustBefore(minDate.getTime(), maxDate.getTime());
    }
    
    public static void mustBefore(long minTime, long maxTime) {
        if (minTime >= maxTime) {
            throw new IllegalArgumentException("The min time must before than the max time");
        }
    }
    
    public static Calendar clamp(Calendar minCalendar, Calendar curCalendar, Calendar maxCalendar) {
        if (minCalendar.after(curCalendar)) {
            return minCalendar;
        }
        if (maxCalendar.before(curCalendar)) {
            return maxCalendar;
        }
        return curCalendar;
    }
}
