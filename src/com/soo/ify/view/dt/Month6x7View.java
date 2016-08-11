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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Soo
 */
public class Month6x7View extends View {
    
    private static final int RAW_COUNT = 6;
    private static final int COL_COUNT = 7;
    private static final int DEFAULT_CELL_WIDTH = 100;
    private static final int DEFAULT_CELL_HEIGHT = 100;
    
    private int rawCount = RAW_COUNT;
    private int colCount = COL_COUNT;
    private int totalCount = rawCount * colCount;
    
    private int cellWidth = DEFAULT_CELL_WIDTH;
    private int cellHeight = DEFAULT_CELL_HEIGHT;
    
    private CellBuilder cellBuilder;
    private CellRender cellRender;
    
    private List<Cell> cells;
    
    public Month6x7View(Context context) {
        super(context);
    }
    
    public Month6x7View(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentDate(Date date) {
        cellBuilder.setCurrentDate(date);
        invalidateCell();
    }
    
    public Date getCurrentDate() {
        return cellBuilder.getCurrentDate();
    }
    
    public void invalidateCell() {
        cells = cellBuilder.getCells();
        if (cells == null) {
            return;
        }
        for (Cell cell : cells) {
            cell.setCellRender(cellRender);
        }
        invalidate();
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    
    private int measureWidth(int widthMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        
        if (mode == MeasureSpec.EXACTLY) {
            cellWidth = width / colCount;
            return widthMeasureSpec;
        } else {
            width = cellWidth * colCount;
        }
        
        return MeasureSpec.makeMeasureSpec(width, mode);
    }
    
    private int measureHeight(int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        
        if (mode == MeasureSpec.EXACTLY) {
            cellWidth = height / rawCount;
            return heightMeasureSpec;
        } else {
            height = cellHeight * rawCount;
        }
        
        return MeasureSpec.makeMeasureSpec(height, mode);
    }
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutCells();
    }
    
    private void layoutCells() {
        if (cells == null) {
            return;
        }
        int count = cells.size();
        for (int i = 0; i < count; i++) {
            int raw = i / colCount;
            int col = i % (rawCount + 1);
            
            Cell cell = cells.get(i);
            cell.setBounds(new Rect(col * cellWidth, raw * cellHeight, col * cellWidth + cellWidth, raw * cellHeight + cellHeight));
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (cells == null) {
            return;
        }
        for (Cell cell : cells) {
            cell.draw(canvas);
        }
    }
}
