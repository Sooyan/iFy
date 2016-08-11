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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @author Soo
 */
public class Cell {
    
    private CellRender cellRender;
    private String text;
    private Rect rect;
    private Size fontSize;
    
    private boolean changed;
    
    public Cell() {
        this.fontSize = new Size();
        this.changed = true;
    }
    
    public void setText(String text) {
        if (text.equals(text)) {
            return;
        }
        this.changed = true;
        this.text = text;
    }
    
    public void setBounds(Rect rect) {
        if (rect.equals(rect)) {
            return;
        }
        this.changed = true;
        this.rect = rect;
    }
    
    public void setCellRender(CellRender cellRender) {
        if (cellRender.equals(cellRender)) {
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
        computeTextSize(paint, text);
        float x = (rect.width() * 1.0f - fontSize.width) / 2.0f + rect.left;
        float y = (rect.height() * 1.0f - fontSize.height) / 2.0f + rect.height();
        canvas.drawText(text, 0, text.length(), x, y, paint);
    }
    
    private void computeTextSize(Paint paint, String content) {
        fontSize.width = paint.measureText(content);
        fontSize.height = (float) Math.ceil(paint.getFontMetrics().descent - paint.getFontMetrics().ascent);
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
