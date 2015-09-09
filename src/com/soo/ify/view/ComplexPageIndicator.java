package com.soo.ify.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.soo.ify.R;

public class ComplexPageIndicator extends View implements PageIndicator {
    
    public static final int MOD_BELOW = 1;
    public static final int MOD_ABOVE = 2;
    
    private float indicatorWidth;
    private float indicatorHeigth;
    private int indicatorColor;
    private Paint indicatorPaint;
    
    private float textSize;
    private int textColor;
    private CharSequence text;
    private Paint textPaint;
    
    private float ballRadius;
    private int ballColor;
    private Paint ballPaint;
    
    private float ballarmSize;
    private float ballarmHeight;
    private int ballarmColor;
    private Paint ballarmPaint;
    
    private int coodinateColor;
    private float coodinateSize;
    private Paint coodinatePaint;
    
    
    private int currentPosition;
    private int currentScrollState;
    private float currentOffset;
    private boolean smoothAble = true;
    private int mod = MOD_ABOVE;
    
    private ViewPager viewPager;
    private OnPageChangeListener onPageChangeListener;
    
    private int touchSlop;
    
    public ComplexPageIndicator(Context context) {
        this(context, null);
    }
    
    public ComplexPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComplexPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ComplexPageIndicator, defStyleAttr, 0);
        indicatorWidth = ta.getDimension(R.styleable.ComplexPageIndicator_indicator_Width, 5.0f);
        indicatorHeigth = ta.getDimension(R.styleable.ComplexPageIndicator_indicator_Height, 10.0f);
        indicatorColor = ta.getColor(R.styleable.ComplexPageIndicator_indicator_Color, Color.RED);
        
        ballRadius = ta.getDimension(R.styleable.ComplexPageIndicator_ball_radius, 20.0f);
        ballColor = ta.getColor(R.styleable.ComplexPageIndicator_ball_Color, Color.RED);
        
        ballarmSize = ta.getDimension(R.styleable.ComplexPageIndicator_ballarm_width, 5.0f);
        ballarmHeight = ta.getDimension(R.styleable.ComplexPageIndicator_ballarm_height, 30.0f);
        ballarmColor = ta.getColor(R.styleable.ComplexPageIndicator_ballarm_color, Color.RED);
        
        coodinateSize = ta.getDimension(R.styleable.ComplexPageIndicator_coodinate_Size, 10.0f);
        coodinateColor = ta.getColor(R.styleable.ComplexPageIndicator_coodinate_Color, Color.RED);
        
        textSize = ta.getDimension(R.styleable.ComplexPageIndicator_text_size, 20.0f);
        textColor = ta.getColor(R.styleable.ComplexPageIndicator_text_color, Color.RED);
        
        mod = ta.getInt(R.styleable.ComplexPageIndicator_mod, MOD_ABOVE);
        smoothAble = ta.getBoolean(R.styleable.ComplexPageIndicator_smooth, true);
        
        ta.recycle();
        init();
    }
    
    private void init() {
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        
        indicatorPaint = new Paint();
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setStyle(Style.FILL);
        indicatorPaint.setStrokeWidth(2.0f);
        indicatorPaint.setAntiAlias(true);
        
        ballPaint = new Paint();
        ballPaint.setColor(ballColor);
        ballPaint.setAntiAlias(true);
        ballPaint.setStrokeWidth(2.0f);
        ballPaint.setStyle(Style.FILL);
        
        ballarmPaint = new Paint();
        ballarmPaint.setColor(ballarmColor);
        ballarmPaint.setAntiAlias(true);
        ballarmPaint.setStyle(Style.FILL);
        ballarmPaint.setStrokeWidth(ballarmSize);

        coodinatePaint = new Paint();
        coodinatePaint.setColor(coodinateColor);
        coodinatePaint.setStyle(Style.FILL);
        coodinatePaint.setStrokeWidth(coodinateSize);
        coodinatePaint.setAntiAlias(true);
        
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setAntiAlias(true);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }
    
    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY || viewPager == null) {
            width = specSize;
        } else {
            int count = viewPager.getAdapter().getCount();
            width = (int) (getPaddingLeft() + getPaddingRight() + count * indicatorWidth);
            if (!TextUtils.isEmpty(text)) {
                int textWidth = computeTextWidth();
                width = Math.max(width, textWidth);
            }
            if (specMode == MeasureSpec.AT_MOST) {
                width = Math.min(specSize, width);
            }
        }
        return width;
    }
    
    private int measureHeight(int heightMeasureSpec) {
        int height = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY || viewPager == null) {
            height = specSize;
        } else {
            int textHeight = 0;
            if (!TextUtils.isEmpty(text)) {
                textHeight = computeTextHeight();
            }
            height = (int) (getPaddingTop() + getPaddingBottom() + Math.max(indicatorHeigth, ballarmHeight) + textHeight + ballRadius * 2);
            if (specMode == MeasureSpec.AT_MOST) {
                height = Math.min(specSize, height);
            }
        }
        return height;
    }
    
    private int computeTextWidth() {
        return (int) textPaint.measureText(text.toString());
    }
    
    private int computeTextHeight() {
        Rect bounds = new Rect();
        textPaint.getTextBounds(text.toString(), 0, text.length(), bounds);
        return bounds.height();
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (viewPager == null || viewPager.getAdapter() == null) {
            return;
        }
        int count = viewPager.getAdapter().getCount();
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight();
        float disLength;
        
        if (count == 0) {
//            empty
        } else if (count == 1) {
            disLength = 0;
            
            float left;
            float top;
            float right;
            float bottom;
            
            float ballarmStartX;
            float ballarmStartY;
            float ballarmStopX;
            float ballarmStopY;
            
            float cX;
            float cY;
            
            float x = width / 2;
            
            if (mod == MOD_ABOVE) {
                left = (width - indicatorWidth) / 2;
                top = height - indicatorHeigth;
                right = left + indicatorWidth;
                bottom = height;
                
                ballarmStartX = x;
                ballarmStartY = height - ballarmHeight;
                ballarmStopX = x;
                ballarmStopY = height;
                
                cX = x;
                cY = ballarmStartY - ballRadius;
            } else {
                left = (width - indicatorWidth) / 2;
                top = 0;
                right = left + indicatorWidth;
                bottom = indicatorHeigth;
                
                ballarmStartX = x;
                ballarmStartY = 0;
                ballarmStopX = x;
                ballarmStopY = ballarmHeight;
                
                cX = x;
                cY = ballarmHeight + ballRadius;
            }
            canvas.drawRect(left, top, right, bottom, indicatorPaint);
            canvas.drawLine(ballarmStartX, ballarmStartY, ballarmStopX, ballarmStopY, ballarmPaint);
            canvas.drawCircle(cX, cY, ballRadius, ballPaint);
            
            if (!TextUtils.isEmpty(text) && (currentScrollState == ViewPager.SCROLL_STATE_IDLE || currentScrollState == ViewPager.SCROLL_STATE_SETTLING)) {
                int textWidth = computeTextWidth();
                float tX;
                float tY;
                if (mod == MOD_ABOVE) {
                    tX = cX;
                    tY = cY - ballRadius;
                    if (cX < textWidth / 2) {
                        tX = 0;
                    } else {
                        tX = cX - textWidth / 2;
                    }
                } else {
                    tX = cX;
                    tY = cY + ballRadius;
                    if (cX < textWidth / 2) {
                        tX = 0;
                    } else {
                        tX = cX - textWidth / 2;
                    }
                }
                if (tX > width - textWidth) {
                    tX = width - textWidth;
                    
                }
                canvas.drawText(text, 0, text.length(), tX, tY, textPaint);
            }
            
        } else {
            float startX = 0.0f;
            float startY = 0.0f;
            float stopX = 0.0f;
            float stopY = 0.0f;
            if (mod == MOD_ABOVE) {
                startX = 0.0f;
                startY = getHeight();
                stopX = getWidth();
                stopY = getHeight();
            } else {
                startX = 0.0f;
                startY = 0.0f;
                stopX = getWidth();
                stopY = 0.0f;
            }
            canvas.drawLine(startX, startY, stopX, stopY, coodinatePaint);
            
            disLength = (width - indicatorWidth * count) / (count - 1);
            for (int i = 0; i < count; i++) {
                float left = 0.0f;
                float top = 0.0f;
                float right = 0.0f;
                float bottom = 0.0f;
                if (mod == MOD_ABOVE) {
                    left = (disLength + indicatorWidth) * i;
                    top = height - indicatorHeigth;
                    right = left + indicatorWidth;
                    bottom = height;
                } else {
                    left = (disLength + indicatorWidth) * i;
                    top = 0;
                    right = left + indicatorWidth;
                    bottom = indicatorHeigth;
                }
                
                canvas.drawRect(left, top, right, bottom, indicatorPaint);
            }
            
            if (isDraging) {
                
            } else {
                lastX = currentPosition * (indicatorWidth + disLength) + indicatorWidth / 2;
                
                if (smoothAble) {
                    lastX += disLength * currentOffset;
                }
            }
            float ballarmStartX;
            float ballarmStartY;
            float ballarmStopX;
            float ballarmStopY;
            
            float cX;
            float cY;
            
            if (mod == MOD_ABOVE) {
                ballarmStartX = lastX;
                ballarmStartY = height - ballarmHeight;
                ballarmStopX = lastX;
                ballarmStopY = height;
                
                cX = lastX;
                cY = ballarmStartY - ballRadius;
            } else {
                ballarmStartX = lastX;
                ballarmStartY = 0;
                ballarmStopX = lastX;
                ballarmStopY = ballarmHeight;
                
                cX = lastX;
                cY = ballarmHeight + ballRadius;
            }
            
            canvas.drawLine(ballarmStartX, ballarmStartY, ballarmStopX, ballarmStopY, ballarmPaint);
            canvas.drawCircle(cX, cY, ballRadius, ballPaint);
            if (!TextUtils.isEmpty(text) && (currentScrollState == ViewPager.SCROLL_STATE_IDLE || currentScrollState == ViewPager.SCROLL_STATE_SETTLING)) {
                int textWidth = computeTextWidth();
                float tX;
                float tY;
                if (mod == MOD_ABOVE) {
                    tX = cX;
                    tY = cY - ballRadius;
                    if (cX < textWidth / 2) {
                        tX = 0;
                    } else {
                        tX = cX - textWidth / 2;
                    }
                } else {
                    tX = cX;
                    tY = cY + ballRadius;
                    if (cX < textWidth / 2) {
                        tX = 0;
                    } else {
                        tX = cX - textWidth / 2;
                    }
                }
                if (tX > width - textWidth) {
                    tX = width - textWidth;
                }
                canvas.drawText(text, 0, text.length(), tX, tY, textPaint);
                
                
            }
        }
    }
    
    private float lastX;
    private boolean isDraging;
    private float tempX;
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tempX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float delX = x - tempX;
                if (!isDraging && Math.abs(delX) > touchSlop) {
                    isDraging = true;
                }
                if (isDraging) {
                    tempX = x;
                    lastX += delX;
                    if (lastX < 0) {
                        lastX = getWidth();
                    }
                    if (lastX > getWidth()) {
                        lastX = 0;
                    }
                    int position = computeDragFreezPosition();
                    text = viewPager.getAdapter().getPageTitle(position);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDraging) {
                    isDraging = false;
                    int position = computeDragFreezPosition();
                    setCurrentItem(position);
                }
                break;
        }
        return true;
    }
    
    private int computeDragFreezPosition() {
        int position = 0;
        
        int count = viewPager.getAdapter().getCount();
        float disLength = (getWidth() - indicatorWidth * count) / (count - 1);
        
        position = (int) (((lastX - indicatorWidth / 2)) / (indicatorWidth + disLength));
        
        return position;
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {
        currentScrollState = state;

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPosition = position;
        currentOffset = positionOffset;
        invalidate();

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        PagerAdapter adapter = null;
        if (viewPager != null && (adapter = viewPager.getAdapter()) != null) {
            text = adapter.getPageTitle(position);
        }
        invalidate();

        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        setViewPager(view, 0);
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        if (this.viewPager == view) {
            return;
        }
        if (this.viewPager != null) {
            this.viewPager.setOnPageChangeListener(null);
        }
        if (view.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        this.viewPager = view;
        this.viewPager.setOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (viewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        viewPager.setCurrentItem(item, smoothScroll);
        this.currentPosition = item;
        if(viewPager.getAdapter() != null) {
            text = viewPager.getAdapter().getPageTitle(item);
        }
        invalidate();
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.onPageChangeListener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }
    
}
