package com.soo.ify.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * ClassName: PagerGuiderTabStrip <br/>
 * Function: 实现viewpager的Tab导航控件. <br/>
 */
public class PagerGuiderTabStrip extends LinearLayout {

    private ViewPager viewPager;

    private PageChangeListener pageChangeListener = new PageChangeListener();

    private int currentItemPosition = 0;

    public PagerGuiderTabStrip(Context context) {
        super(context);
    }

    public PagerGuiderTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PagerGuiderTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        this.currentItemPosition = viewPager.getCurrentItem();
        PagerAdapter adapter = viewPager.getAdapter();

        if (adapter == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        if (!(adapter instanceof PagerTabAdapter)) {
            throw new IllegalStateException(
                    "The PagerAdpater of ViewPager is not impl of PagerTabAdapter.");
        }

        viewPager.setOnPageChangeListener(pageChangeListener);

        notifyDataSetChanged();

        ((PagerTabAdapter) viewPager.getAdapter()).onSelectedChanged(
                this.getChildAt(0), 0, true);
        ;
    }

    private void notifyDataSetChanged() {
        removeAllViews();
        int count = viewPager.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            addGuiderTab(i);
        }
    }

    private void addGuiderTab(final int position) {
        PagerTabAdapter tabAdapter = (PagerTabAdapter) viewPager.getAdapter();
        View tab = tabAdapter.getPagerTab(position);
        LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT,
                1.0f);
        if (tab != null) {
            tab.setClickable(true);
            tab.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(position);
                }
            });

            this.addView(tab, params);
        }
    }

    private class PageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == currentItemPosition) {
                View tabView = getChildAt(arg0);
                ((PagerTabAdapter) viewPager.getAdapter()).onSelectedChanged(
                        tabView, arg0, true);
            } else {
                View preTabView = getChildAt(currentItemPosition);
                PagerTabAdapter adapter = (PagerTabAdapter) viewPager
                        .getAdapter();
                adapter.onSelectedChanged(preTabView, currentItemPosition,
                        false);

                currentItemPosition = arg0;
                View tabView = getChildAt(currentItemPosition);
                adapter.onSelectedChanged(tabView, currentItemPosition, true);
            }
        }
    }

    public interface PagerTabAdapter {

        View getPagerTab(int position);

        void onSelectedChanged(View tabView, int position, boolean flag);
    }

}
