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
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.soo.ify.R;
import com.soo.ify.view.dt.CalendarView.AbsCalendarViewDelegate;
import com.soo.ify.view.dt.support.CalendarContext_6x7;
import com.soo.ify.view.dt.support.Cell;
import com.soo.ify.view.dt.support.MonthView;

/**
 * @author Soo
 */
public class ListCalendarDelegate extends AbsCalendarViewDelegate {
    
    private ListView listView;
    private CalendarContext_6x7 calendarContext;
    private InnerAdapter adapter;

    ListCalendarDelegate(Context context, CalendarView proxy, AttributeSet attrs) {
        super(context, proxy, attrs);
        
        listView = new ListView(context);
        proxy.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        calendarContext = new CalendarContext_6x7(context, attrs);
        adapter = new InnerAdapter(context, calendarContext);
        listView.setAdapter(adapter);
        
    }

    static class InnerAdapter extends BaseAdapter implements OnItemClickListener {
        
        Context context;
        CalendarContext_6x7 calendarContext;
        
        InnerAdapter(Context context, CalendarContext_6x7 calendarContext) {
            this.context = context;
            this.calendarContext = calendarContext;
        }

        @Override
        public int getCount() {
            return calendarContext.getMonthTotalCount();
        }

        @Override
        public Object getItem(int position) {
            return calendarContext.getCells(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.dt_monthview_list, null);
                
                holder = new ViewHolder();
                holder.monthView = (MonthView) convertView.findViewById(R.id.dt_monthview_list_item);
                holder.titleTxv = (TextView) convertView.findViewById(R.id.dt_monthview_list_title);
                
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            Calendar calendar = calendarContext.getCalendar(position);
            holder.titleTxv.setText(calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
            
            @SuppressWarnings("unchecked")
            List<Cell> cells = (List<Cell>) getItem(position);
            holder.monthView.setCells(cells);
            holder.monthView.setOnItemClickListener(this);
            
            return convertView;
        }
        
        static class ViewHolder {
            TextView titleTxv;
            MonthView monthView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BaseAdapter adapter = (BaseAdapter) parent.getAdapter();
            Cell cell = (Cell) adapter.getItem(position);
            cell.toggle();
            adapter.notifyDataSetChanged();
        }
    }
}
