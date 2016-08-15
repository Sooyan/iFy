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
package com.soo.ify.view.dt.support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author Soo
 */
public class CalendarContext_6x7 extends CalendarContext {
    
    private static final int RAW_COUNT = 6;
    private static final int COLUMN_COUNT = 7;
    private static final int TOTAL_COUNT = RAW_COUNT * COLUMN_COUNT;
    
    private int monthTotalCount;

    public CalendarContext_6x7(Context context, AttributeSet attrs) {
        super(context, attrs);
        invalidate();
    }

    @Override
    protected void invalidate() {
        int minYear = minCalendar.get(Calendar.YEAR);
        int maxYear = maxCalendar.get(Calendar.YEAR);
        int minMonth = minCalendar.get(Calendar.MONTH);
        int maxMonth = maxCalendar.get(Calendar.MONTH);
        int disYear = maxYear - minYear;
        if (disYear > 0) {
            monthTotalCount = disYear * 12 + maxMonth + 1;
        } else {
            monthTotalCount = minMonth + 1;
        }
    }
    
    public int getMonthTotalCount() {
        return monthTotalCount;
    }
    
    public Calendar getCalendar(int position) {
        Calendar tempCalendar = newCalendar(minCalendar);
        tempCalendar.add(Calendar.MONTH, position);
        return newCalendar(clamp(minCalendar, tempCalendar, maxCalendar));
    }
    
    public List<Cell> getCells(int position) {
        Calendar calendar = getCalendar(position);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeekInCurrent = calendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        int dis = firstDayOfWeekInCurrent - firstDayOfWeek;
        
        calendar.add(Calendar.DAY_OF_MONTH, (-1) * (dis + 1));
        
        List<Cell> cells = new ArrayList<Cell>();
        
        for (int i = 0; i < TOTAL_COUNT; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            
            Cell cell = new Cell(newCalendar(calendar));
            
            cells.add(cell);
        }
        
        return cells;
    }

}
