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
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.soo.ify.R;

/**
 * @author Soo
 */
public class MonthView extends GridView{
    
    private CalendarContext calendarContext;
    private InnerAdapter adapter;
    
    public MonthView(Context context) {
        this(context, null);
    }
    
    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setNumColumns(7);
        setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        
        this.adapter = new InnerAdapter(context);
        this.setAdapter(adapter);
    }
    
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof InnerAdapter) {
            super.setAdapter(adapter);
            return;
        }
        throw new IllegalArgumentException("Can`t support invoking for the method");
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    
    public void setCells(List<Cell> cells) {
        adapter.setCells(cells);
    }
    
    static class InnerAdapter extends BaseAdapter {
        
        Context context;
        List<Cell> cells;
        
        InnerAdapter(Context context) {
            this.context = context;
            this.cells = new ArrayList<Cell>();
        }
        
        void setCells(List<Cell> cells) {
            this.cells.clear();
            this.cells.addAll(cells);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return cells.size();
        }

        @Override
        public Object getItem(int position) {
            return cells.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.dt_monthview, null);
                
                holder = new ViewHolder();
                holder.solarTxv = (TextView) convertView.findViewById(R.id.dt_monthview_solar_txv);
                holder.lunarTxv = (TextView) convertView.findViewById(R.id.dt_monthview_lunar_txv);
                
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            Cell cell = (Cell) getItem(position);
            holder.solarTxv.setText(cell.getCalendar().get(Calendar.DAY_OF_MONTH) + "");
            if (cell.isMarked()) {
                holder.solarTxv.setTextColor(Color.WHITE);
            } else {
                holder.solarTxv.setTextColor(Color.BLACK);
            }
            
            return convertView;
        }
        
        static class ViewHolder {
            TextView solarTxv;
            TextView lunarTxv;
        }
    }
}
