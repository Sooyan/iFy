package com.soo.ify.view;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.soo.ify.R;

public class RecyclerPagerSlidingTab extends RecyclerView implements OnPageChangeListener {
    
    private static final int DEFAULT_TAB_WH = 5;
    private static final int DEFAULT_TAB_COLOR = Color.YELLOW;
    private static final int DEFAULT_DIV_WH = 10;
    private static final int DEFAULT_DIV_COLOR = Color.BLUE;
    
    public static abstract class Adapter<VH extends ViewHolder> extends RecyclerView.Adapter<VH> implements OnClickListener {

        private Map<View, Integer> recorder = new HashMap<View, Integer>();
        private ViewPager viewPager;
        
        public Adapter(ViewPager viewPager) {
            this.viewPager = viewPager;
        }
        
        public ViewPager getViewPager() {
            return viewPager;
        }
        
        @Override
        public final int getItemCount() {
            return viewPager.getAdapter().getCount();
        }
        
        @Override
        public void onBindViewHolder(VH vh, int position) {
            onBindViewHolder(vh, position, false);
            recorder.put(vh.itemView, position);
            vh.itemView.setOnClickListener(this);
        }
        
        public abstract void onBindViewHolder(VH vh, int position, boolean flag);
        
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(recorder.get(v), false);
        }
    }
    
    private Adapter<?> adapter;
    private OnPageChangeListener onPageChangeListener;
    
    private int orientation = LinearLayoutManager.HORIZONTAL;
    private int tabWH = 5;
    private int divWH = 10;
    private int tabColor = Color.YELLOW;
    private int divColor = Color.BLUE;
    
    private Paint tabPaint = new Paint();
    private Paint divPaint = new Paint();
    
    public RecyclerPagerSlidingTab(Context context) {
        super(context);
    }
    
    public RecyclerPagerSlidingTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerPagerSlidingTab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RecyclerPagerSlidingTab);
        init(ta);
        ta.recycle();
    }
    
    private void init(TypedArray ta) {
        
        orientation = ta.getInt(R.styleable.RecyclerPagerSlidingTab_orientation, LinearLayoutManager.HORIZONTAL);
        tabWH = ta.getDimensionPixelSize(R.styleable.RecyclerPagerSlidingTab_tabWH, DEFAULT_TAB_WH);
        tabColor = ta.getColor(R.styleable.RecyclerPagerSlidingTab_tabColor, DEFAULT_TAB_COLOR);
        divWH = ta.getDimensionPixelSize(R.styleable.RecyclerPagerSlidingTab_divWH, DEFAULT_DIV_WH);
        divColor = ta.getColor(R.styleable.RecyclerPagerSlidingTab_divColor, DEFAULT_DIV_COLOR);
        
        tabWH = divWH = Math.min(tabWH, divWH);
        
        switch (orientation) {
        case 1:
            orientation = LinearLayoutManager.HORIZONTAL;
            break;
        case 2:
            orientation = LinearLayoutManager.HORIZONTAL;
            break;
            default:
                orientation = LinearLayoutManager.HORIZONTAL;
                break;
        }
        
        
        this.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
        initialPaint();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAdapter(android.support.v7.widget.RecyclerView.Adapter adapter) {
        if (!(adapter instanceof Adapter)) {
            throw new IllegalArgumentException("The adapter must extends RecyclerPagerSlidingTab#Adapter");
        }
        super.setAdapter(adapter);
        this.adapter = (Adapter<?>) adapter;
        this.adapter.getViewPager().setOnPageChangeListener(this);
    }
    
    private void initialPaint() {
        tabPaint.setColor(tabColor);
        tabPaint.setStrokeWidth(1.0f);
        tabPaint.setStyle(Style.FILL);
        
        
        divPaint.setColor(divColor);
        divPaint.setStrokeWidth(1.0f);
        divPaint.setStyle(Style.FILL);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            onMeasureHorizontal(widthMeasureSpec, heightMeasureSpec);
        } else {
            onMeasureVertical(widthMeasureSpec, heightMeasureSpec);
        }
    }
    
    private void onMeasureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        int maxHeight = 0;
        LayoutParams params;
        
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childHeight = child.getMeasuredHeight();
            params = (LayoutParams) child.getLayoutParams();
//            get max height value
            int height = params.topMargin + params.bottomMargin + childHeight + divWH;
            maxHeight = Math.max(height, maxHeight);
        }
        
        setMeasuredDimension(widthMeasureSpec, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : maxHeight);
    }
    
    private void onMeasureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int sizewidth = MeasureSpec.getSize(widthMeasureSpec);
        
        int maxWidth = 0;
        LayoutParams params;
        
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            params = (LayoutParams) child.getLayoutParams();
//            get max width value
            int width = params.leftMargin + params.rightMargin + childWidth + divWH;
            maxWidth = Math.max(width, maxWidth);
        }
        
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizewidth : maxWidth, heightMeasureSpec);
    }
    
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        final int height = getHeight();


        View currentTab = getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < getAdapter().getItemCount() - 1) {

            View nextTab = getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);
        }
        
        canvas.drawRect(0, height - divWH, getWidth(), height, divPaint);
        canvas.drawRect(lineLeft, height - tabWH, lineRight, height, tabPaint);
        
    }
    
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPosition = position;
        currentPositionOffset = positionOffset;
//          if (positionOffset > 0) {
//              View left = getChildAt(currentPosition);
//              View right = getChildAt(currentPosition + 1);
//              left.setAlpha(1 - positionOffset);
//              right.setAlpha(positionOffset);
//          }
        invalidate();
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(position);
        }
    }
    
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
