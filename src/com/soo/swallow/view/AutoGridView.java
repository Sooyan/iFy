package com.soo.swallow.view;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class AutoGridView extends ViewGroup {

    private static final int DEFAULT_WIDTH = 480;
    private static final int DEFAULT_GAP = 5;

    public interface OnItemClickListener {
        void onItemClick(AutoGridView parent, View itemView, int position,
                Object obj);
    }

    public static abstract class Adapter {

        private AutoGridView layout;

        public abstract int getCount();

        public abstract Object getObject(int position);

        public abstract View onCreateView(AutoGridView parent, View view,
                int position);

        public void notifyDataChanged() {
            if (layout != null) {
                layout.refresh();
//                 layout.requestLayout();
            }
        }

        private void setNineGridlayout(AutoGridView layout) {
            this.layout = layout;
        }
    }

    private int gap = DEFAULT_GAP;
    private int columns;
    private int rows;

    private Adapter adapter;
    private OnItemClickListener onItemClickListener;

    private Recycler recycler = new Recycler();

    public AutoGridView(Context context) {
        super(context);
    }

    public AutoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (columns == 0) {
            super.onMeasure(widthMeasureSpec, 0);
            return;
        }
        int measureWidth = measureWidth(widthMeasureSpec);
        int singalHeight = (measureWidth - gap * (3 - 1)) / columns;
        setMeasuredDimension(measureWidth, singalHeight * rows);
    }

    private int measureWidth(int widthMeasureSpec) {
        int width = 0;
        int specMod = MeasureSpec.getMode(widthMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMod) {
        case MeasureSpec.AT_MOST:
            width = specWidth;
            break;
        case MeasureSpec.EXACTLY:
            width = specWidth;
            break;
        case MeasureSpec.UNSPECIFIED:
            width = DEFAULT_WIDTH;
            break;
        }
        return width;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isRefresh) {
            layoutChildren();
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.onItemClickListener = l;
    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;// 行
                    position[1] = j;// 列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        recycler.clear();
        this.adapter.setNineGridlayout(this);
        refresh();
//        layoutChildren();
    }
    
    public Adapter getAdapter() {
        return adapter;
    }

    public void refresh() {
        if (adapter == null) {
            return;
        }

        int oldCount = getChildCount();
        while (oldCount > 0) {
            View v = getChildAt(oldCount - 1);
            removeView(v);
            recycler.recycle(v);
            oldCount--;
        }
        // for (int i = 0; i < oldCount; i++) {
        // View v = getChildAt(i);
        // removeView(v);
        // recycler.recycle(v);
        // }
        oldCount = getChildCount();
        int newCount = adapter.getCount();
        generateChildrenLayout(newCount);
        
        isRefresh = true;
        
        for (int i = 0; i < newCount; i++) {
            View v = recycler.pop();
            View view = adapter.onCreateView(this, v, i);
            view.setOnClickListener(new InnerListener(this, i, adapter
                    .getObject(i)));
            addView(view);
//            addView(view, generateDefaultLayoutParams());
        }
        isRefresh = false;
//        layoutChildren();
    }
    
    boolean isRefresh = false;
    
    private void layoutChildren() {
        if (getChildCount() == 0) {
            return;
        }
        int singleWidth = 0;
        int singleHeight = 0;

        singleWidth = (getWidth() - gap * (columns - 1)) / columns;
        singleHeight = singleWidth;

        for (int i = 0; i < getChildCount(); i++) {
            View childrenView = getChildAt(i);
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }
    }

    /**
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            columns = 3;
            double dLength = length;
            double dColums = columns;
            rows = (int) Math.ceil(dLength / dColums);
        }
    }

    static class Recycler {

        private LinkedList<WeakReference<View>> pool = new LinkedList<WeakReference<View>>();

        Recycler() {

        }

        void recycle(View v) {
            if (v == null) {
                return;
            }
            ViewParent parent = v.getParent();
            if (parent != null || pool.contains(v)) {
                return;
            }
            pool.add(new WeakReference<View>(v));
        }

        View pop() {
            int count = pool.size();
            if (count > 0) {
                WeakReference<View> weakView = pool.remove(count - 1);
                View v = weakView.get();
                if (v != null) {
                    return v;
                }
            }
            return null;
        }

        void clear() {
            pool.clear();
        }
    }

    static class InnerListener implements OnClickListener {

        final AutoGridView parent;
        final int position;
        final Object obj;

        InnerListener(AutoGridView parent, int position, Object obj) {
            this.parent = parent;
            this.position = position;
            this.obj = obj;
        }

        @Override
        public void onClick(View v) {
            if (this.parent.onItemClickListener != null) {
                this.parent.onItemClickListener.onItemClick(parent, v,
                        position, obj);
            }
        }

    }
}
