/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.dt;

import java.util.Calendar;

import com.soo.ify.view.dt.util.CalendarContext;

/**日期算法封装类
 * @author Soo
 */
public class CalendarComputer {
    
    private CalendarContext calendarContext;
    
    public CalendarComputer(CalendarContext calendarContext) {
        this.calendarContext = calendarContext;
    }

    /**获取月份的总数量
     * @return
     */
    public int getMonthTotalCount() {
        int minYear = calendarContext.getMinCalendar().get(Calendar.YEAR);
        int maxYear = calendarContext.getMaxCalendar().get(Calendar.YEAR);
        int minMonth = calendarContext.getMinCalendar().get(Calendar.MONTH);
        int maxMonth = calendarContext.getMaxCalendar().get(Calendar.MONTH);
        int disYear = maxYear - minYear;
        if (disYear > 0) {
            return disYear * 12 + maxMonth + 1;
        } else {
            return minMonth + 1;
        }
    }
    
    /**获取指定位置的月份Calendar
     * @param position
     * @return
     */
    public Calendar getMonthCalendarByPosition(int position) {
        Calendar maxCalendar = calendarContext.getMaxCalendar();
        Calendar minCalendar = calendarContext.getMinCalendar();
        Calendar tmpCalendar = calendarContext.newCalendar(minCalendar);
        tmpCalendar.add(Calendar.MONTH, position);
        return CalendarContext.clamp(minCalendar, tmpCalendar, maxCalendar);
    }
}
