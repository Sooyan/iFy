/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.dt.util;

import java.util.Calendar;

/**负责显示日历信息
 * @author Soo
 */
public class CalendarDisplayer {
    
    private CalendarContext calendarContext;
    
    public CalendarDisplayer(CalendarContext calendarContext) {
        this.calendarContext = calendarContext;
    }
    
    /**获取阳历显示的年:2016
     * @param calendar
     * @return
     */
    public String getSolarYear(Calendar calendar) {
        calendar = calendarContext.newCalendar(calendar);
        return calendar.get(Calendar.YEAR) + "";
    }
    
    /**获取阴历显示的年:二零一六
     * @param calendar
     * @return
     */
    public String getLunarYear(Calendar calendar) {
        return null;
    }
}
