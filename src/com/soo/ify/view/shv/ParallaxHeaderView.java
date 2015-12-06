package com.soo.ify.view.shv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class ParallaxHeaderView<T extends View> extends ViewGroup {
    
    public enum ParallaxMod {
        None,
        LayerUp,
        LayerDown;
    }
    
    public ParallaxHeaderView(Context context) {
        super(context);
    }
    
    public ParallaxHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setHeaderView(View headerView) {
        
    }
    
    public void setParallaxMod() {
        
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
