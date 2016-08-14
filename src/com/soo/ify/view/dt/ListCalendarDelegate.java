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

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import com.soo.ify.view.dt.CalendarView.AbsCalendarViewDelegate;
import com.soo.ify.view.dt.cell.MonthView;
import com.soo.ify.view.dt.util.CalendarContext;

/**
 * @author Soo
 */
public class ListCalendarDelegate extends AbsCalendarViewDelegate {
    
    private CalendarComputer calendarComputer;
    private ListView listView;
    private InnerAdapter adapter;

    ListCalendarDelegate(Context context, CalendarView proxy, AttributeSet attrs) {
        super(context, proxy, attrs);
        
        calendarComputer = new CalendarComputer(calendarContext);
        listView = new ListView(context);
        proxy.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        adapter = new InnerAdapter(context, calendarComputer);
        listView.setAdapter(adapter);
    }

    static class InnerAdapter extends BaseAdapter {
        
        Context context;
        CalendarComputer calendarComputer;
        
        InnerAdapter(Context context, CalendarComputer calendarComputer) {
            this.context = context;
            this.calendarComputer = calendarComputer;
        }

        @Override
        public int getCount() {
            return calendarComputer.getMonthTotalCount();
        }

        @Override
        public Object getItem(int position) {
            return calendarComputer.getMonthCalendarByPosition(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MonthView monthView = null;
            if (convertView == null) {
                monthView = new MonthView(context);
                monthView.setCalendarStandard(standard);
            } else {
                monthView = (MonthView) convertView;
            }
            
            Calendar calendar = (Calendar) getItem(position);
            
            monthView.setCurrentDate(calendar);
            
            return monthView;
        }
    }
}
