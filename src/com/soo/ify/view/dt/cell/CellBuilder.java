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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;

/**
 * @author Soo
 */
public abstract class CellBuilder {
    private static final Locale DEFAULT_LOCALE = Locale.getDefault();
    private static final int FIRST_DAY_OF_WEEK = Calendar.SUNDAY;
    
    protected Context context;
    protected Calendar minCalendar;
    protected Calendar maxCalendar;
    protected Calendar curCalendar;
    
    private List<Observer> observers = new ArrayList<CellBuilder.Observer>();
    
    public CellBuilder(Context context) {
        this.context = context;
        try {
            DateFormat SDFORMATER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", DEFAULT_LOCALE);
            this.minCalendar = newCalendar(SDFORMATER.parse("1970-01-01 00:00:00"));
            this.maxCalendar = newCalendar(SDFORMATER.parse("2050-01-01 00:00:00"));
            this.curCalendar = newCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    
    public CellBuilder(Context context, long minMillis, long maxMillis) {
        this(context, newCalendar(minMillis), newCalendar(maxMillis));
    }
    
    public CellBuilder(Context context, Date minDate, Date maxDate) {
        this(context, newCalendar(minDate), newCalendar(maxDate));
    }
    
    public CellBuilder(Context context, Calendar minCalendar, Calendar maxCalendar) {
        if (minCalendar.after(maxCalendar)) {
            throw new IllegalArgumentException("The min time must before than the max time");
        }
        this.context = context;
        this.minCalendar = newCalendar(minCalendar.getTime());
        this.maxCalendar = newCalendar(maxCalendar.getTime());
        this.curCalendar = newCalendar();
    }
    
    public Date getCurrentDate() {
        return curCalendar.getTime();
    }
    
    public void setCurrentDate(Date date) {
        this.curCalendar.setTime(date);
        notifyDataChanged();
    }
    
    public void notifyDataChanged() {
        for (Observer observer : observers) {
            observer.onDateChanged(this);
        }
    }
    
    public void registObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void unRegistObserver(Observer observer) {
        observers.remove(observer);
    }
    
    public abstract int getCount();
    
    public abstract List<Cell> getItem(int position);
    
    public abstract int getRawCount();
    
    public abstract int getColumnCount();
    
    public abstract List<Cell> preCells();
    
    public abstract List<Cell> currentCells();
    
    public abstract List<Cell> nextCells();
    
    public interface Observer {
        void onDateChanged(CellBuilder cellBuilder);
    }
    
    protected static Calendar newCalendar() {
        Calendar calendar = Calendar.getInstance(DEFAULT_LOCALE);
        calendar.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        return calendar;
    }
    
    protected static Calendar newCalendar(Date date) {
        Calendar calendar = newCalendar();
        calendar.setTime(date);
        return calendar;
    }
    
    protected static Calendar newCalendar(long millis) {
        Calendar calendar = newCalendar();
        calendar.setTimeInMillis(millis);
        return calendar;
    }
}
