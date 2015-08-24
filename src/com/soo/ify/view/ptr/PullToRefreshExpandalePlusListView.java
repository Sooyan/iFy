package com.soo.ify.view.ptr;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import com.soo.ify.view.ExpandPlusListView;
import com.soo.ify.view.ptr.internal.EmptyViewMethodAccessor;

public class PullToRefreshExpandalePlusListView extends PullToRefreshAdapterViewBase<ExpandPlusListView> {
    public PullToRefreshExpandalePlusListView(Context context) {
        super(context);
        this.setOnScrollListener(mRefreshableView);
    }

    public PullToRefreshExpandalePlusListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(mRefreshableView);
    }

    public PullToRefreshExpandalePlusListView(Context context, Mode mode) {
        super(context, mode);
        this.setOnScrollListener(mRefreshableView);
    }

    public PullToRefreshExpandalePlusListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        this.setOnScrollListener(mRefreshableView);
    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected ExpandPlusListView createRefreshableView(Context context, AttributeSet attrs) {
        final ExpandPlusListView lv;
        if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
            lv = new InternalExpandablePlusListViewSDK9(context, attrs);
        } else {
            lv = new InternalExpandablePlusListView(context, attrs);
        }

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }

    class InternalExpandablePlusListView extends ExpandPlusListView implements EmptyViewMethodAccessor {

        public InternalExpandablePlusListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshExpandalePlusListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }
    }

    @TargetApi(9)
    final class InternalExpandablePlusListViewSDK9 extends InternalExpandablePlusListView {

        public InternalExpandablePlusListViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshExpandalePlusListView.this, deltaX, scrollX, deltaY, scrollY,
                    isTouchEvent);

            return returnValue;
        }
    }
}
