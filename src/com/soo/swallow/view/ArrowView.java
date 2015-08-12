package com.soo.swallow.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.soo.swallow.R;

public class ArrowView extends TextView {
    
    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    
    private Paint paint = new Paint();
    
    private int direction = RIGHT;
    private float strokeWidth = 2.0f;
    private int tagSpacing = 5;
    
    private int lineColor = 0;
    
    public ArrowView(Context context) {
        super(context);
        init(null);
    }
    
    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowView);
        init(typeArray);
        typeArray.recycle();
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ArrowView);
        init(typeArray);
        typeArray.recycle();
    }
    
    private void init(TypedArray typeArray) {
        direction = typeArray.getInt(R.styleable.ArrowView_direction, RIGHT);
        strokeWidth = typeArray.getDimension(R.styleable.ArrowView_strokeWidth, 2.0f);
        tagSpacing = typeArray.getDimensionPixelSize(R.styleable.ArrowView_tagSpacing, 5);
        lineColor = typeArray.getColor(R.styleable.ArrowView_lineColor, 0);
        
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        switch (direction) {
            case LEFT:
                width += (height / 2) + tagSpacing;
                break;
            case TOP:
                height += (width / 2) + tagSpacing;
                break;
            case RIGHT:
                width += (height / 2) + tagSpacing;
                break;
            case BOTTOM:
                height += (width / 2) + tagSpacing;
                break;
        }
        setMeasuredDimension(width, height);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        
        paint.setColor(lineColor == 0 ? getCurrentTextColor() : lineColor);
        
        int width = getWidth();
        int height = getHeight();
        
        int dx = 0,dy = 0;
        switch (direction) {
        case LEFT:
            canvas.drawLine(0, height / 2, height / 2, 0, paint);
            canvas.drawLine(strokeWidth / 2, height / 2 - strokeWidth / 2, height / 2, height, paint);
            dx += height / 2 + tagSpacing;
            break;
        case TOP:
            canvas.drawLine((width + strokeWidth) / 2, 0, 0, width / 2, paint);
            canvas.drawLine((width - strokeWidth) / 2, 0, width, width / 2, paint);
            dy += width / 2 + tagSpacing;
            break;
        case RIGHT:
            canvas.drawLine(width, height / 2, width - height / 2, 0, paint);
            canvas.drawLine(width - strokeWidth / 2, (height - strokeWidth) / 2, width - height / 2, height, paint);
            
            break;
        case BOTTOM:
            canvas.drawLine(width / 2 + strokeWidth, height, 0, height - width / 2, paint);
            canvas.drawLine(width / 2, height, width, height - width / 2, paint);
            break;
        }
        canvas.save();
        canvas.translate(dx, dy);
        super.onDraw(canvas);
    }
    
    public void setStrokeWidth(float width) {
        this.strokeWidth = width;
        invalidate();
    }
    
    public void setDirection(int direction) {
        this.direction = direction;
        invalidate();
    }
    
    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
    }
}
