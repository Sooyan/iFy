/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.dt.util;

import java.util.Calendar;

/**节日接口类
 * @author Soo
 */
public interface Festival {

    /**获取阳历的节日
     * @param calendar
     * @return
     */
    String getSolarFestival(Calendar calendar);
    
    /**获取阴历的节日
     * @param calendar
     * @return
     */
    String getLunarFestival(Calendar calendar);
}
