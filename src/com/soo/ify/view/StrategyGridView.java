/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Soo
 */
public class StrategyGridView extends ViewGroup {
    
    private Strategy strategy; 
    private int rowCount;
    private int columnCount;
    private int unitGridSize;
    private int spaceSize;
    
    public StrategyGridView(Context context) {
        super(context);
    }
    
    public StrategyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StrategyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMod == MeasureSpec.EXACTLY && heightMod != MeasureSpec.EXACTLY) {
            unitGridSize = width / (rowCount > 0 ? rowCount : 1);
        } else if (widthMod != MeasureSpec.EXACTLY && heightMod == MeasureSpec.EXACTLY) {
            unitGridSize = height / (columnCount > 0 ? columnCount : 1);
        } else if (widthMod == MeasureSpec.EXACTLY && heightMod == MeasureSpec.EXACTLY) {
            int tempWidth = width / (rowCount > 0 ? rowCount : 1);
            int tempHeight = height / (columnCount > 0 ? columnCount : 1);
            unitGridSize = Math.min(tempWidth, tempHeight);
        }
        
        
        
        int length = Math.max(width, height);
        
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private int measureUnitSize(int measureSpec, int count) {
        int mod = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mod == MeasureSpec.EXACTLY) {
            return size / (count > 0 ? count : 1);
        }
        return unitGridSize;
    }
    
    
    
    public void setStrategy(Strategy strategy) {
        
    }
    
    
    
    /**策略接口
     * @author Soo
     */
    public static interface Strategy {
        
        /**获取行数
         * @return
         */
        int getRowCount();
        
        /**获取列数
         * @return
         */
        int getColumnCount();
        
        /**获取单位格子的大小
         * @return
         */
        int getUnitGridSize();
        
        /**获取策略的总数量
         * @return
         */
        int getCount();
        
        /**获取指定位置，在行上的扩展比重，无扩展返回1，已被占用返回0
         * @param position
         * @return
         */
        int getRowSpan(int position);
        
        /**获取指定位置，在列上的扩展比重，无扩展返回1，已被占用返回0
         * @param position
         * @return
         */
        int getColumnSpan(int position);
        
        /**获取指定位置上的视图,
         * @param parent
         * @param convertView
         * @param position
         * @return
         */
        View getView(StrategyGridView parent, View convertView, int position);
    }
}
