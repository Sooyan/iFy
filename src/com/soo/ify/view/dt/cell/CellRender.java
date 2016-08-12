/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view.dt.cell;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author Soo
 */
public class CellRender {
    
    public Paint getCommonPaint() {
        return null;
    }

    public Paint getPaint(Cell cell) {
        Paint p = new Paint();
        p.setTextSize(30);
        p.setColor(Color.WHITE);
        return p;
    }
}
