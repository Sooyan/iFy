/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.dt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.soo.ify.view.dt.CalendarView.AbsCalendarViewDelegate;
import com.soo.ify.view.dt.cell.CellBuilder;
import com.soo.ify.view.dt.cell.MonthCellBuilder;

/**
 * @author Soo
 */
public class ListCalendarDelegate extends AbsCalendarViewDelegate {
    
    private CellBuilder cellBuilder;
    private ListView listView;
    private InnerAdapter adapter;

    ListCalendarDelegate(Context context, CalendarView proxy, AttributeSet attrs) {
        super(context, proxy, attrs);
        
        cellBuilder = new MonthCellBuilder(context);
        
        listView = new ListView(context);
        proxy.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        adapter = new InnerAdapter(cellBuilder);
        listView.setAdapter(adapter);
    }

    static class InnerAdapter extends BaseAdapter {
        
        CellBuilder cellBuilder;

        InnerAdapter(CellBuilder cellBuilder) {
            this.cellBuilder = cellBuilder;
        }

        @Override
        public int getCount() {
            return cellBuilder.getCount();
        }

        @Override
        public Object getItem(int position) {
            return cellBuilder.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
