package com.soo.ify.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.soo.ify.R;
import com.soo.ify.util.ResLoader;

public class TagImageView extends ImageView {
    
    private Drawable ltDrawable;
    private Drawable rtDrawable;
    private Drawable lbDrawable;
    private Drawable rbDrawable;
    
    private Cell[] cells = new Cell[4];
    private Rect rect = new Rect();
    
    public TagImageView(Context context) {
        super(context);
        init();
    }
    
    public TagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init() {
        ltDrawable = ResLoader.loadDrawable(getContext(), R.drawable.icon_picked);
        rtDrawable = ResLoader.loadDrawable(getContext(), R.drawable.icon_picked);
        rbDrawable = ResLoader.loadDrawable(getContext(), R.drawable.icon_picked);
        lbDrawable = ResLoader.loadDrawable(getContext(), R.drawable.ic_launcher);
        
        cells[0] = new Cell(ltDrawable, Cell.POSITION_LT);
        cells[1] = new Cell(rtDrawable, Cell.POSITION_RT);
        cells[2] = new Cell(rbDrawable, Cell.POSITION_RB);
        cells[3] = new Cell(lbDrawable, Cell.POSITION_LB);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        prepareRect();
        
        for (Cell cell : cells) {
            cell.draw(canvas, rect);
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        for (Cell cell : cells) {
            if (cell.onTouchEvent(event, rect)) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.d("--->", "click");
                }
                return true;
            } 
        }
        return super.onTouchEvent(event);
    }
    
    private void prepareRect() {
        rect.left = 0;
        rect.top = 0;
        rect.right = getWidth();
        rect.bottom = getHeight();
    }
    
    static class Cell {
        
        static final int POSITION_LT = 1;
        static final int POSITION_LB = 2;
        static final int POSITION_RT = 3;
        static final int POSITION_RB = 4;
        
        private static final long TEMP_TIME = 500;
        final Drawable drawable;
        final int position;
        long timeTag;
        float dx, dy;
        
        Cell(Drawable drawable, int position) {
            this.drawable = drawable;
            this.position = position;
        }
        
        
        void draw(Canvas canvas, Rect rect) {
            if (drawable != null) {
                
                int width = drawable.getIntrinsicWidth();
                int height = drawable.getIntrinsicWidth();
                
                drawable.setBounds(0, 0, width, height);
                
                canvas.save();
                computeDxy(rect);
                canvas.translate(dx, dy);
                
                drawable.draw(canvas);
                canvas.restore();
            }
        }
        
        void computeDxy(Rect rect) {
            switch (position) {
                case POSITION_LT:
                    break;
                case POSITION_LB:
                    dy = rect.bottom - drawable.getBounds().height();
                    break;
                case POSITION_RT:
                    dx = rect.right - drawable.getBounds().width();
                    break;
                case POSITION_RB:
                    dx = rect.right - drawable.getBounds().width();
                    dy = rect.bottom - drawable.getBounds().height();
                    break;
                default:
                    break;
            }
        }
        
        boolean onTouchEvent(MotionEvent event, Rect rect) {
            float x = event.getX();
            float y = event.getY();
            
            Rect dRect = drawable.getBounds();
            Rect newRect = new Rect();
            
            switch (position) {
                case POSITION_LT:
                    newRect.left = 0;
                    newRect.top = 0;
                    newRect.right = dRect.width();
                    newRect.bottom = dRect.height();
                    break;
                case POSITION_LB:
                    newRect.left = 0;
                    newRect.top = rect.height() - dRect.height();
                    newRect.right = dRect.width();
                    newRect.bottom = rect.height();
                    break;
                case POSITION_RT:
                    newRect.left = rect.width() - dRect.width();
                    newRect.top = 0;
                    newRect.right = rect.width();
                    newRect.bottom = dRect.height();
                    break;
                case POSITION_RB:
                    newRect.left = rect.width() - dRect.width();
                    newRect.top = rect.height() - dRect.height();
                    newRect.right = rect.width();
                    newRect.bottom = rect.height();
                    break;
                default:
                    break;
            }
            
            if (newRect.contains((int)x, (int)y)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        timeTag = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        return System.currentTimeMillis() - timeTag <= TEMP_TIME;
                }
            }
            
            return false;
        }
    }
}
