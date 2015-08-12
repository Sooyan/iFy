package com.soo.swallow.view;

import com.soo.swallow.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class ExpandPlusListView extends ExpandableListView implements
        OnScrollListener, OnGroupClickListener {
    
    public static abstract class ExpandPlusListAdapter extends BaseExpandableListAdapter {
        
        public abstract View getGroupHeaderView(ViewGroup parent);
        
        public abstract void configureGroupHeaderView(View header, int groupPosition, int childPosition, int alpha);
    }
    
    public static final int PINNED_HEADER_GONE = 0;
    public static final int PINNED_HEADER_VISIBLE = 1;
    public static final int PINNED_HEADER_PUSHED_UP = 2;
    
    private static final int MAX_ALPHA = 255;

    private ExpandPlusListAdapter mAdapter;

    private View mHeaderView;

    private boolean mHeaderViewVisible;
    
    private boolean headerTagViewClickAble = true;
    private boolean groupCloseAble = true;
    private boolean groupExpandAble = true;

    private int mHeaderViewWidth;
    private int mHeaderViewHeight;
    
    private OnGroupClickListener onGroupClickListener;
    private OnChildClickListener onChildClickListener;
    
    private SparseBooleanArray isExpander = new SparseBooleanArray();
    
    public ExpandPlusListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandPlusListView);
        init(typeArray);
        typeArray.recycle();
    }

    public ExpandPlusListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandPlusListView);
        init(typeArray);
        typeArray.recycle();
    }

    public ExpandPlusListView(Context context) {
        super(context);
        init(null);
    }
    
    private void init(TypedArray typeArray) {
        if (typeArray != null) {
            headerTagViewClickAble = typeArray.getBoolean(R.styleable.ExpandPlusListView_headTagViewClickAble, true);
            groupCloseAble = typeArray.getBoolean(R.styleable.ExpandPlusListView_headTagViewClickAble, true);
            groupExpandAble = typeArray.getBoolean(R.styleable.ExpandPlusListView_headTagViewClickAble, true);
        }
        registerListener();
    }
    
    private void registerListener() {
        super.setOnScrollListener(this);
        super.setOnGroupClickListener(this);
    }

    private void headerViewClick() {
        long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());
        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

        if (isExpander.get(groupPosition)) {
            if (groupCloseAble) {
                this.collapseGroup(groupPosition);
                isExpander.put(groupPosition, false);
            }
        } else {
            if (groupExpandAble) {
                this.expandGroup(groupPosition);
                isExpander.put(groupPosition, true);
            }
        }

        this.setSelectedGroup(groupPosition);
    }
    
    @Override
    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        this.onGroupClickListener = onGroupClickListener;
    }
    
    @Override
    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        super.setOnChildClickListener(onChildClickListener);
        this.onChildClickListener = onChildClickListener;
    }
    
    public OnGroupClickListener getOnGroupClickListener() {
        return onGroupClickListener;
    }
    
    public OnChildClickListener getOnChildClickListener() {
        return onChildClickListener;
    }

    private float mDownX;
    private float mDownY;
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mHeaderViewVisible) {
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                if (mDownX <= mHeaderViewWidth && mDownY <= mHeaderViewHeight) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                float x = ev.getX();
                float y = ev.getY();
                float offsetX = Math.abs(x - mDownX);
                float offsetY = Math.abs(y - mDownY);
                if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
                        && offsetX <= mHeaderViewWidth
                        && offsetY <= mHeaderViewHeight) {
                    if (mHeaderView != null && headerTagViewClickAble) {
                        headerViewClick();
                    }

                    return true;
                }
                break;
            default:
                break;
            }
        }

        return super.dispatchTouchEvent(ev);
    }
    
    @Deprecated
    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        if (!(adapter instanceof ExpandPlusListAdapter)) {
            throw new IllegalArgumentException("adapter must instanceof ExpandPlusListAdapter");
        }
        this.setAdapter((ExpandPlusListAdapter) adapter);
    }
    
    public void setAdapter(ExpandPlusListAdapter adapter) {
        mAdapter = adapter;
        setHeaderView();
        super.setAdapter(adapter);
    }
    
    private void setHeaderView() {
        mHeaderView = mAdapter.getGroupHeaderView(this);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(lp);

        if (mHeaderView != null) {
            setFadingEdgeLength(0);
        }

        requestLayout();
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (isExpander.get(groupPosition)) {
            if (groupCloseAble) {
                parent.collapseGroup(groupPosition);
                isExpander.put(groupPosition, false);
            }
        } else {
            if (groupExpandAble) {
                parent.expandGroup(groupPosition);
                parent.setSelectedGroup(groupPosition);
                isExpander.put(groupPosition, true);
            }
        }
        
        if (onGroupClickListener != null) {
            onGroupClickListener.onGroupClick(parent, v, groupPosition, id);
        }
        
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    private int mOldState = -1;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        int state = getHeaderState(groupPos, childPos);
        if (mHeaderView != null && mAdapter != null && state != mOldState) {
            mOldState = state;
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
        }

        configureHeaderView(groupPos, childPos);
    }

    public void configureHeaderView(int groupPosition, int childPosition) {
        if (mHeaderView == null || mAdapter == null
                || ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
            return;
        }

        int state = getHeaderState(groupPosition, childPosition);
        switch (state) {
        case PINNED_HEADER_GONE: {
            mHeaderViewVisible = false;
            break;
        }

        case PINNED_HEADER_VISIBLE: {
            mAdapter.configureGroupHeaderView(mHeaderView, groupPosition,
                    childPosition, MAX_ALPHA);

            if (mHeaderView.getTop() != 0) {
                mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
            }

            mHeaderViewVisible = true;

            break;
        }

        case PINNED_HEADER_PUSHED_UP: {
            View firstView = getChildAt(0);
            int bottom = firstView.getBottom();

            // intitemHeight = firstView.getHeight();
            int headerHeight = mHeaderView.getHeight();

            int y;

            int alpha;

            if (bottom < headerHeight) {
                y = (bottom - headerHeight);
                alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
            } else {
                y = 0;
                alpha = MAX_ALPHA;
            }

            mAdapter.configureGroupHeaderView(mHeaderView, groupPosition,
                    childPosition, alpha);

            if (mHeaderView.getTop() != y) {
                mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight
                        + y);
            }

            mHeaderViewVisible = true;
            break;
        }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
    
    private int getHeaderState(int groupPosition, int childPosition) {
        
        if (groupPosition == -1) {
            return PINNED_HEADER_GONE;
        }
        
        final int childCount = mAdapter.getChildrenCount(groupPosition);
        if(childPosition == childCount - 1){
            return PINNED_HEADER_PUSHED_UP; 
        }
        else if(childPosition == -1 && !isGroupExpanded(groupPosition)){ 
            return PINNED_HEADER_GONE; 
        }
        else{
            return PINNED_HEADER_VISIBLE;
        }
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final long flatPos = getExpandableListPosition(firstVisibleItem);
        int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
        int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

        configureHeaderView(groupPosition, childPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
}
