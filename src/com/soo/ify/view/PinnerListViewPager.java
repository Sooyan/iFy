package com.soo.ify.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.soo.ify.R;

public class PinnerListViewPager extends FrameLayout {
    
    private FrameLayout headerLayout;
    private ViewPager viewPager;
    
    private PinnerHeaderPagerAdapter adapter;
    
    private int headerHeight;
    
    public PinnerListViewPager(Context context) {
        super(context);
        init();
    }
    
    public PinnerListViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PinnerListViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_pinner_header_viewpager, this);
        viewPager = (ViewPager) findViewById(R.id.view_pinner_header_viewpager);
        headerLayout = (FrameLayout) findViewById(R.id.view_pinner_header_headerlayout);
    }
    
    public void setAdapter(PinnerHeaderPagerAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("PinnerHeaderPagerAdapter is null");
        }
        this.adapter = adapter;
        
        View headerView = adapter.onCreateHeaderView(headerLayout);
        if (headerLayout.getChildCount() == 0) {
            headerLayout.addView(headerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(headerLayout, widthMeasureSpec, heightMeasureSpec);
        
        headerHeight = headerLayout.getMeasuredHeight();
        
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        int maxHeight = 0;
        LayoutParams params;
        
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            int childHeight = child.getMeasuredHeight();
//            params = (LayoutParams) child.getLayoutParams();
////            get max height value
//            int height = params.topMargin + params.bottomMargin + childHeight + divWH;
//            maxHeight = Math.max(height, maxHeight);
//        }
        
        setMeasuredDimension(widthMeasureSpec, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : maxHeight);
        
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    static class InnerLinerLayout extends LinearLayout {
        
        public InnerLinerLayout(Context context) {
            super(context);
        }
        
        public InnerLinerLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public InnerLinerLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
    }
    
    public static abstract class PinnerHeaderPagerAdapter extends PagerAdapter {
        
        private Context context;
        
        public PinnerHeaderPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            InnerLinerLayout layout = new InnerLinerLayout(context);
            View view = onCreatePageView(position);
            layout.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            container.addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            return view;
        }
        
        public abstract View onCreatePageView(int position);
        
        public abstract View onCreateHeaderView(ViewGroup viewGroup);
    }
    
}
