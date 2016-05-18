/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.soo.ify.R;

/**
 * @author Soo
 */
public class WaveView extends View implements Handler.Callback {
    
    public interface OnStateChangedListener {
        void onChanged(int beforeState, int nowState);
    }
    
    private static final int RATE_DRAWER = 20;
    private static final int RATE_PUSHER = 1000;
    private static final int WHAT_DRAWER = 1;
    private static final int WHAT_PUSHER = 2;
    
    public static final int STATE_NORMAL = 0xf01;
    public static final int STATE_PRESS = 0xf02;
    public static final int STATE_OUTSIDE = 0xf03;
    
    private static final int CIRCLE_NORMAL_DEF_COLOR  = Color.BLUE;
    private static final int CIRCLE_PRESS_DEF_COLOR  = Color.GREEN;
    private static final int CIRCLE_OUTSIDE_DEF_COLOR  = Color.RED;
    private static final int WAVE_DEF_COLOR  = Color.RED;
    private static final float WAVE_DEF_STROKE_WIDTH = 2.0f;
    private static final float RADIUS_DEF  = 80.0f;
    
    private static Handler handler;
    
    private int circleNormalColor;
    private int circlePressColor;
    private int circleOutSideColor;
    private int circleColor;
    private int waveColor;
    private float waveStrokeWidth;
    private float circleRadius;
    private Drawable drawable;
    private boolean isTouchIn = false;
    private int state = STATE_NORMAL;
    
    private Runner drawRunner;
    private Runner pushRunner;
    private Paint circlePaint;
    
    private List<Wave> aliveWaves = new ArrayList<WaveView.Wave>();
    private List<WeakReference<Wave>> deathWaves = new ArrayList<WeakReference<Wave>>();
    private OnStateChangedListener onStateChangedListener;
    
    public WaveView(Context context) {
        this(context,null);
    }
    
    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        init(ta);
        ta.recycle();
    }
    
    private void init(TypedArray ta) {
        handler = new Handler(this);
        drawRunner = new Runner(handler, WHAT_DRAWER, RATE_DRAWER);
        pushRunner = new Runner(handler, WHAT_PUSHER, RATE_PUSHER);
        
        if (ta != null) {
            circleNormalColor = ta.getColor(R.styleable.WaveView_circleColor_normal, CIRCLE_NORMAL_DEF_COLOR);
            circlePressColor = ta.getColor(R.styleable.WaveView_circleColor_press, CIRCLE_PRESS_DEF_COLOR);
            circleOutSideColor = ta.getColor(R.styleable.WaveView_circleColor_outside, CIRCLE_OUTSIDE_DEF_COLOR);
            waveColor = ta.getColor(R.styleable.WaveView_waveColor, WAVE_DEF_COLOR);
            waveStrokeWidth = ta.getDimension(R.styleable.WaveView_waveStrokeWidth, WAVE_DEF_STROKE_WIDTH);
            circleRadius = ta.getDimension(R.styleable.WaveView_circleRadius, RADIUS_DEF);
            drawable = ta.getDrawable(R.styleable.WaveView_circleDrawable);
        }
    }
    
    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        invalidate();
    }
    
    public void setCircleNormalColor(int color) {
        this.circleNormalColor = color;
        invalidate();
    }
    
    public void setCirclePressColor(int color) {
        this.circlePressColor = color;
        invalidate();
    }
    
    public void setCircleOutsideColor(int color) {
        this.circleOutSideColor = color;
        invalidate();
    }
    
    public void setWaveColor(int color) {
        this.waveColor = color;
        invalidate();
    }
    
    public void setRadius(float radius) {
        this.circleRadius = radius;
        invalidate();
    }
    
    public void setOnStateChangedListener(OnStateChangedListener l) {
        this.onStateChangedListener = l;
    }
    
    private void refreshPaintColor() {
        if (circlePaint == null) {
            circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Style.FILL);
        }
        
        switch (state) {
            case STATE_NORMAL:
                this.circleColor = circleNormalColor;
                break;
            case STATE_PRESS:
                this.circleColor = circlePressColor;
                break;
            case STATE_OUTSIDE:
                this.circleColor = circleOutSideColor;
                break;
        }
        
        circlePaint.setColor(circleColor);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                width = specSize;
                break;
            case MeasureSpec.AT_MOST:
                width = (int) circleRadius * 4;
                break;
            case MeasureSpec.UNSPECIFIED:
                width = (int) circleRadius * 4;
                break;
        }
        return width;
    }
    
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:
                height = specSize;
                break;
            case MeasureSpec.AT_MOST:
                height = (int) circleRadius * 2;
                break;
            case MeasureSpec.UNSPECIFIED:
                height = (int) circleRadius * 2;
                break;
        }
        return height;
    }
    
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_DRAWER:
                if (aliveWaves.size() > 0) {
                    invalidate();
                } else {
                    drawRunner.stop();
                }
                break;
            case WHAT_PUSHER:
                pushWave(getWave());
                break;
        }
        return true;
    }
    
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isInCircle(event.getX(), event.getY())) {
                    isTouchIn = true;
                    pushRunner.start();
                    
                    publishState(STATE_PRESS);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isTouchIn) {
                    if (!isInCircle(event.getX(), event.getY())) {
                        publishState(STATE_OUTSIDE);
                    } else {
                        publishState(STATE_PRESS);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isTouchIn = false;
                pushRunner.stop();
                
                publishState(STATE_NORMAL);
                break;
        }
        return true;
    }
    
    private void publishState(int newState) {
        if (this.state != newState) {
            int tempState = this.state;
            this.state = newState;
            invalidate();
            if (onStateChangedListener != null) {
                onStateChangedListener.onChanged(tempState, newState);
            }
        }
    }
    
    private boolean isInCircle(float x, float y) {
        float disX = Math.abs(x - getCircleX());
        float disY = Math.abs(y - getCircleY());
        double distance = Math.sqrt(disX * disX + disY * disY);
        return distance <= circleRadius;
    }
    
    public void pushWave() {
        pushWave(getWave());
    }
    
    public void pushWave(Wave wave) {
        if (wave != null) {
            aliveWaves.add(wave);
            drawRunner.start();
        }
    }
    
    public float getCircleX() {
        return getWidth() / 2;
    }
    
    public float getCircleY() {
        return getHeight() / 2;
    }
    
    public float getRadius() {
        return circleRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        refreshPaintColor();
        drawCenterCircle(canvas);
        drawCenterBitmap(canvas);
        drawWave(canvas);
    }
    
    private void drawCenterCircle(Canvas canvas) {
        final float cx = getCircleX();
        final float cy = getCircleY();
        canvas.drawCircle(cx, cy, getRadius(), circlePaint);
    }
    
    private void drawCenterBitmap(Canvas canvas) {
        if (drawable != null) {
            
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            
            drawable.setBounds(0, 0, width, height);
            
            canvas.save();
            
            float dx = getCircleX() - drawable.getBounds().left - width / 2;
            float dy = getCircleY() - drawable.getBounds().top - height / 2;
            
            canvas.translate(dx, dy);
            
            drawable.draw(canvas);
            canvas.restore();
        }
    }
    
    private void drawWave(Canvas canvas) {
        Iterator<Wave> iterator = aliveWaves.iterator();
        while (iterator.hasNext()) {
            Wave wave = iterator.next();
            if (wave.isAlive()) {
                wave.draw(canvas);
            } else {
                iterator.remove();
                recycleWave(wave);
            }
        }
    }
    
    private Wave getWave() {
        int enableSize = deathWaves.size();
        WeakReference<Wave> weakWave = null;
        Wave wave = null;
        if (enableSize > 0) {
            weakWave = deathWaves.get(enableSize - 1);
            if (weakWave != null) {
                wave = weakWave.get();
            }
        }
        if (wave != null) {
            deathWaves.remove(enableSize - 1);
        } else {
            wave = new Wave();
        }
        
        wave.cx = getCircleX();
        wave.cy = getCircleY();
        wave.radius = getRadius();
        wave.color = waveColor;
        wave.alpha = 255;
        wave.storkeWidth = waveStrokeWidth;
        
        return wave;
    }
    
    private void recycleWave(Wave wave) {
        if (wave != null) {
            boolean flag = false;
            int size = deathWaves.size();
            for (int i = 0; i < size; i++) {
                WeakReference<Wave> oldWeak = deathWaves.get(i);
                Wave oldWave = oldWeak.get();
                flag = wave == oldWave;
            }
            if (!flag) {
                deathWaves.add(0, new WeakReference<Wave>(wave));
            }
        }
    }
    
    static class Runner implements Runnable {
        
        private static Object LOCK = new Object();
        
        final Handler handler;
        final long rate;
        final int what;
        
        boolean flag;
        
        Runner(Handler handler, int what, long rate) {
            this.handler = handler;
            this.rate = rate;
            this.what = what;
        }
        
        void start() {
            synchronized (LOCK) {
                if (flag) {
                    return;
                }
                flag = true;
                new Thread(this).start();
            }
        }
        
        void stop() {
            synchronized (LOCK) {
                if (!flag) {
                    return;
                }
                flag = false;
            }
        }
        
        @Override
        public void run() {
            try {
                while (flag) {
                    handler.sendEmptyMessage(what);
                    Thread.sleep(rate);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    static class Wave {
        
        int color;
        int alpha;
        float radius;
        float cx;
        float cy;
        float storkeWidth;
        
        Paint paint;
        
        Wave() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Style.STROKE);
        }
        
        Wave(float cx, float cy, float radius, int color, float strokeWidth, int alpha) {
            this.cx = cx;
            this.cy = cy;
            this.radius = radius;
            this.color = color;
            this.alpha = alpha;
            this.storkeWidth = strokeWidth;
            
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Style.STROKE);
        }
        
        void draw(Canvas canvas) {
            if (isAlive()) {
                makeChange();
                canvas.drawCircle(cx, cy, radius, paint);
            }
        }
        
        boolean isAlive() {
//            TODO
            return alpha > 0;
        }
        
        void makeChange() {
            alpha = alpha - 3;
            radius = radius + 3;
            
            paint.setColor(color);
            paint.setAlpha(alpha);
            paint.setStrokeWidth(storkeWidth);
        }
    }
}