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

import java.util.Calendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * @author Soo
 */
public class Cell {
    
    private Calendar calendar;
    
    private CellRender cellRender;
    private Rect rect;
    private Size fontSize;
    
    private boolean changed;
    
    public Cell() {
        this.fontSize = new Size();
        this.changed = true;
    }
    
    public void setCalendar(Calendar calendar) {
        if (this.calendar != null && this.calendar.equals(calendar)) {
            return;
        }
        this.changed = true;
        this.calendar = calendar;
    }
    
    public void setBounds(Rect rect) {
        if (this.rect != null && this.rect.equals(rect)) {
            return;
        }
        this.changed = true;
        this.rect = rect;
    }
    
    public void setCellRender(CellRender cellRender) {
        if (this.cellRender != null && this.cellRender.equals(cellRender)) {
            return;
        }
        this.changed = true;
        this.cellRender = cellRender;
    }
    
    public void draw(Canvas canvas) {
        if (!changed) {
            return;
        }
        if (cellRender == null) {
            throw new RuntimeException("No CellRender be found");
        }
        if (rect == null) {
            rect = new Rect();
        }
        Paint paint = cellRender.getPaint(this);
        if (paint == null) {
            paint = new Paint();
        }
        makeDraw(canvas, rect, paint);
        this.changed = false;
    }
    
    private void makeDraw(Canvas canvas, Rect rect, Paint paint) {
        String text = getDrawText();
        Log.d("--->", text);
        
        computeTextSize(paint, text);
        float x = (rect.width() * 1.0f - fontSize.width) / 2.0f + rect.left;
        float y = (rect.height() * 1.0f - fontSize.height) / 2.0f + rect.top;
        canvas.drawText(text, 0, text.length(), x, y, paint);
    }
    
    private void computeTextSize(Paint paint, String content) {
        fontSize.width = paint.measureText(content);
        fontSize.height = (float) Math.ceil(paint.getFontMetrics().descent - paint.getFontMetrics().ascent);
    }
    
    private String getDrawText() {
        if (calendar == null) {
            return "D";
        }
        return calendar.get(Calendar.DAY_OF_MONTH) + "";
    }
    
    public static class Size {
        public float width;
        public float height;
        
        public Size() {
        }
        
        public Size(Size size) {
            this.width = size.width;
            this.height = size.height;
        }
        
        public Size(float width, float height) {
            this.width = width;
            this.height = height;
        }
    }
}
