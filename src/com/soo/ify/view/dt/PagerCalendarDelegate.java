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

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;

import com.soo.ify.R;
import com.soo.ify.view.dt.CalendarView.AbsCalendarViewDelegate;
import com.soo.ify.view.dt.support.CalendarContext_6x7;
import com.soo.ify.view.dt.support.Cell;
import com.soo.ify.view.dt.support.MonthView;

/**
 * @author Soo
 */
public class PagerCalendarDelegate extends AbsCalendarViewDelegate {
    
    private ViewPager viewPager;
    private PagerAdapter adapter;
    private CalendarContext_6x7 calendarContext;

    PagerCalendarDelegate(Context context, CalendarView proxy, AttributeSet attrs) {
        super(context, proxy, attrs);
        viewPager = new ViewPager(context, attrs);
        proxy.addView(viewPager, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        
        calendarContext = new CalendarContext_6x7(context, attrs);
        
        adapter = new InnerAdapter(context, calendarContext);
        viewPager.setAdapter(adapter);
    }
    
    static class InnerAdapter extends PagerAdapter implements OnItemClickListener {
        
        Context context;
        CalendarContext_6x7 calendarContext;
        
        SparseArray<View> viewPool = new SparseArray<View>();
        
        InnerAdapter(Context context, CalendarContext_6x7 calendarContext) {
            this.context = context;
            this.calendarContext = calendarContext;
        }

        @Override
        public int getCount() {
            return calendarContext.getMonthTotalCount();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == viewPool.get((Integer) arg1);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewPool.get(position));
            viewPool.remove(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewPool.get(position);
            if (view == null) {
                view = View.inflate(context, R.layout.dt_monthview_pager, null);
                
                viewPool.put(position, view);
            } 
            
            
            MonthView monthView = (MonthView) view.findViewById(R.id.dt_monthview_pager_item);
            
            List<Cell> cells = calendarContext.getCells(position);
            monthView.setCells(cells);
            monthView.setOnItemClickListener(this);
            
            container.addView(view);
            
            return position;
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
