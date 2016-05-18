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
package com.soo.ify.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.soo.ify.R;
import com.soo.ify.util.TextUtils;

public class IndexView extends View {
    
    public interface OnIndexSelectedListener {
        void onSelected(IndexView indexView, String text, int position);
    }
    
    private static final String[] chars = {
        "A", "B", "C","D", "E", "F", "G", "H", "I",
        "J", "K", "L","M", "N", "O", "P", "Q", "R",
        "S", "T", "U","V", "W", "X", "Y", "Z"
    };
    
    public static final int MOD_NONE = 0;
    public static final int MOD_ALWAYS = 1;
    public static final int MOD_ONTOUCH = 2;
    
    public static final int TYPE_ALL = 0;
    public static final int TYPE_JEDGE = 1;
    
    private TextView floatView;
    private int wOverly;
    private int hOverly;
    
    private int type = TYPE_ALL;
    private int ftMod = MOD_ONTOUCH;
    
    private RectF bgRectF = new RectF();
    private int bgColor = Color.GRAY;
    private int bgMod = MOD_ALWAYS;
    private Paint bgPaint = new Paint();
    
    private Rect textBounds = new Rect();
    private int textSize = 24;
    private int textColor = Color.WHITE;
    private Paint textPaint = new Paint();
    
    private boolean isTouched = false;
    private int spaceWidth = 10;
    
    private List<String> indexArray = new ArrayList<String>();
    private OnIndexSelectedListener onIndexSelectedListener;
    
    private int currentPosition = -1;
    
    public IndexView(Context context) {
        super(context);
        init(null);
    }
    
    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndexView);
        init(ta);
        ta.recycle();
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IndexView);
        init(ta);
        ta.recycle();
    }
    
    private void init(TypedArray typeArray) {
        
        if (typeArray != null) {
            ftMod = typeArray.getInt(R.styleable.IndexView_barFloatMod, MOD_ONTOUCH);
            bgColor = typeArray.getColor(R.styleable.IndexView_barBgColor, Color.GRAY);
            bgMod = typeArray.getInt(R.styleable.IndexView_barBgMod, MOD_ALWAYS);
            textSize = typeArray.getDimensionPixelSize(R.styleable.IndexView_barTextSize, 24);
            textColor = typeArray.getColor(R.styleable.IndexView_barTextColor, Color.WHITE);
            
            type = typeArray.getInt(R.styleable.IndexView_type, TYPE_ALL);
        }
        
        bgPaint.setColor(bgColor);
        bgPaint.setTypeface(Typeface.DEFAULT);
        bgPaint.setAntiAlias(true);
        
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setAntiAlias(true);
        
        initialFloatingView();
        if (type == TYPE_ALL) {
            indexArray = Arrays.asList(chars);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(meaureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    
    private int meaureWidth(int widthMeasureSpec) {
        int width = 0;
        int mod = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        switch (mod) {
            case MeasureSpec.EXACTLY:
                width = size;
                break;
            case MeasureSpec.AT_MOST:
                width = computeViewWidthByContent() + getPaddingLeft() + getPaddingRight();
                break;
            case MeasureSpec.UNSPECIFIED:
                int deSize = size + getPaddingLeft() + getPaddingRight();
                width = deSize;
                break;
        }
        return width;
    }
    
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int mod = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mod) {
            case MeasureSpec.EXACTLY:
                height = size;
                break;
            case MeasureSpec.AT_MOST:
                height = computeViewHeightByContent() + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.UNSPECIFIED:
                int deSize = size + getPaddingTop() + getPaddingBottom();
                height = deSize;
                break;
        }
        return height;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
//        draw background
        boolean drawBgAble = false;
        switch (bgMod) {
            case MOD_NONE:
                break;
            case MOD_ALWAYS:
                drawBgAble = true;
                break;
            case MOD_ONTOUCH:
                drawBgAble = isTouched;
                break;
        }
        if (drawBgAble) {
            bgRectF.set(0, 0, getWidth(), getHeight());
            float radius = Math.min(getWidth(), getHeight()) * 0.5f;
            
            canvas.drawRoundRect(bgRectF, radius, radius, bgPaint);
        }
        
//        draw text
        int count = indexArray.size();
        for (int i = 0; i < count; i++) {
            String text = indexArray.get(i);
            textPaint.getTextBounds(text, 0, text.length(), textBounds);
            float pX = getWidth() / 2.0f - textBounds.width() / 2.0f;
            float pY = (textSize + spaceWidth)* i + textSize / 2.0f + textBounds.height() / 2.0f + spaceWidth;
            
            canvas.drawText(text, pX, pY, textPaint);
        }
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isTouched = true;
                int index = computeTouchIndex(event.getY());
                onSelectedIndex(index);
                
                if (ftMod == MOD_ONTOUCH || ftMod == MOD_ALWAYS) {
                    if (floatView.getVisibility() != View.VISIBLE) {
                        floatView.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouched = false;
                
                if (ftMod == MOD_ONTOUCH  && floatView.getVisibility() == View.VISIBLE) {
                    floatView.setVisibility(View.GONE);
                }
                break;
        }
        
        return true;
    }
    
    private int computeTouchIndex(float y) {
        if (indexArray == null || indexArray.size() == 0) {
            return 0;
        }
        if (y <= bgRectF.top) {
            return 0;
        }
        if (y >= bgRectF.bottom) {
            return indexArray.size() - 1;
        }
        return (int) ((y - bgRectF.top) / (bgRectF.height() / indexArray.size()));
    }
    
    private int computeViewHeightByContent() {
        return (int) ((textSize + spaceWidth)* indexArray.size() + textSize / 2.0f + textBounds.height() / 2.0f);
    }
    
    private int computeViewWidthByContent() {
        float width = 0.0f;
        int count = indexArray.size();
        for (int i = 0; i < count; i++) {
            String text = indexArray.get(i);
            textPaint.getTextBounds(text, 0, text.length() - 1, textBounds);
            
            width = Math.max(width, (textBounds.width() + textSize));
        }
        return (int) width;
    }
    
    private void onSelectedIndex(int index) {
        floatView.setText(indexArray.get(index));
        if (currentPosition != index) {
            if (onIndexSelectedListener != null && currentPosition != index) {
                onIndexSelectedListener.onSelected(this, indexArray.get(index), index);
            }
            currentPosition = index;
        }
    }
    
    public void addTextCollection(Collection<String> texts) {
        if (type == TYPE_ALL || texts == null || texts.size() == 0) {
            return;
        }
        Iterator<String> iterator = texts.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            String pinyin = TextUtils.convertToPinyin2(value).toUpperCase(Locale.getDefault());
            
            if (pinyin.length() > 0) {
                String firstC = pinyin.substring(0, 1);
                if (!this.indexArray.contains(firstC)) {
                    this.indexArray.add(firstC);
                }
            }
        }
        
        Collections.sort(this.indexArray);
        invalidate();
    }
    
    public void setTextCollection(Collection<String> texts) {
        this.indexArray.clear();
        if (type == TYPE_ALL || texts == null || texts.size() == 0) {
            invalidate();
            return;
        }
        Iterator<String> iterator = texts.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next();
            String pinyin = TextUtils.convertToPinyin2(value).toUpperCase(Locale.getDefault());
            
            String key = "#";
            char firstChar = key.charAt(0);
            if (pinyin.length() > 0) {
                firstChar = pinyin.charAt(0);
                if (firstChar >= 65 && firstChar <= 90) {
                    key = pinyin.substring(0, 1);
                }
            }
            if (!this.indexArray.contains(key)) {
                this.indexArray.add(key);
            }
        }
        
        Collections.sort(this.indexArray);
        invalidate();
    }
    
    @Override
    public void setBackgroundColor(int color) {
        this.bgColor = color;
        this.bgPaint.setColor(color);
        invalidate();
    }
    
    public void setBackgroudColorMod(int mod) {
        this.bgMod = mod;
        invalidate();
    }
    
    public void setTextColor(int color) {
        this.textColor = color;
        this.textPaint.setColor(color);
        invalidate();
    }
    
    public void setTextSize(int size) {
        this.textSize = size;
        this.textPaint.setTextSize(size);
        invalidate();
    }
    
    public void setOnIndexSelectedListener(OnIndexSelectedListener onIndexSelectedListener) {
        this.onIndexSelectedListener = onIndexSelectedListener;
    }
    
    private void initialFloatingView() {
        final WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        floatView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_index_float,null) ;
        if (ftMod == MOD_ALWAYS) {
            floatView.setVisibility(View.VISIBLE);
        } else {
            floatView.setVisibility(View.GONE);
        }
        wOverly = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70,getResources().getDisplayMetrics()) ;
        hOverly = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70,getResources().getDisplayMetrics()) ;
        post(new Runnable() {
            
            @Override
            public void run() {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                        wOverly,
                        hOverly,
                        WindowManager.LayoutParams.TYPE_APPLICATION,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.TRANSLUCENT);
                wm.addView(floatView, layoutParams);
            }
        });
    }
    
    public void removeTextViewFromWindow() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (floatView != null) {
            wm.removeViewImmediate(floatView);
        }
    }
}
