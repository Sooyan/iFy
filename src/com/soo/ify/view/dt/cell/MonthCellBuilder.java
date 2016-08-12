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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

/**月的方式
 * 
 * cell构造器
 * @author Soo
 */
public class MonthCellBuilder extends CellBuilder {
    
    private static final int RAW_COUNT = 6;
    private static final int COL_COUNT = 7;
    private static final int TOTAL_COUNT = RAW_COUNT * COL_COUNT;
    
    public MonthCellBuilder(Context context) {
        super(context);
    }
    
    public MonthCellBuilder(Context context, long minMillis, long maxMillis) {
        super(context, minMillis, maxMillis);
    }
    
    public MonthCellBuilder(Context context, Date minDate, Date maxDate) {
        super(context, minDate, maxDate);
    }

    public MonthCellBuilder(Context context, Calendar minCalendar, Calendar maxCalendar) {
        super(context, minCalendar, maxCalendar);
    }
    
    @Override
    public int getRawCount() {
        return RAW_COUNT;
    }

    @Override
    public int getColumnCount() {
        return COL_COUNT;
    }

    @Override
    public List<Cell> preCells() {
        return null;
    }

    @Override
    public List<Cell> currentCells() {
        Calendar tmpCalendar = newCalendar(curCalendar.getTimeInMillis());
        tmpCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeekInCurrent = tmpCalendar.get(Calendar.DAY_OF_WEEK);
        int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
        int dis = firstDayOfWeekInCurrent - firstDayOfWeek;
        
        List<Cell> cells = new ArrayList<Cell>();
        for (int i = 0; i < TOTAL_COUNT; i++) {
            if (i < dis) {
                tmpCalendar.add(Calendar.DAY_OF_MONTH, i - dis);
            } else {
                tmpCalendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            
            Cell cell = new Cell();
            cell.setCalendar(newCalendar(tmpCalendar.getTimeInMillis()));
            cells.add(cell);
        }
        
        return cells;
    }

    @Override
    public List<Cell> nextCells() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.soo.ify.view.dt.cell.CellBuilder#getCount()
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    /* (non-Javadoc)
     * @see com.soo.ify.view.dt.cell.CellBuilder#getItem(int)
     */
    @Override
    public List<Cell> getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
}
