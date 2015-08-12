package com.soo.swallow.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.soo.swallow.R;

/**
 * @author Soo
 *记住在要在xml引用该view，并且不要设置drawstart或者drawend属性
 */
public class AutoDrawableTextView extends TextView {
    
    // 需要从xml中读取的各个方向图片的宽和高
    private int leftHeight = -1;
    private int leftWidth = -1;
    private int rightHeight = -1;
    private int rightWidth = -1;
    private int topHeight = -1;
    private int topWidth = -1;
    private int bottomHeight = -1;
    private int bottomWidth = -1;
    
    public AutoDrawableTextView(Context context) {
        super(context);
    }
    
    public AutoDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AutoDrawableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    /**
     * 初始化读取参数
     * */
    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoDrawableTextView, defStyle, 0);
        
        leftWidth = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_left_width, 0);
        leftHeight = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_left_height, 0);
        topWidth = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_top_width, 0);
        topHeight = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_top_height, 0);
        rightWidth = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_right_width, 0);
        rightHeight = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_right_height, 0);
        bottomWidth = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_bottom_width, 0);
        bottomHeight = a.getDimensionPixelSize(R.styleable.AutoDrawableTextView_bottom_height, 0);
        Log.d("--->", "lw:" + leftWidth + " lh:" + leftHeight + " tw:" + topWidth + " th:" + topHeight + " bw:" + bottomWidth + "  bh:" + bottomHeight);
        
        
        // 获取各个方向的图片，按照：左-上-右-下 的顺序存于数组中
        Drawable[] drawables = getCompoundDrawables();
        int dir = 0;
        // 0-left; 1-top; 2-right; 3-bottom;
        for (Drawable drawable : drawables) {
            // 设定图片大小
            setImageSize(drawable, dir++);
        }
        // 将图片放回到TextView中
        setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        
        a.recycle();
    }

    /**
     * 设定图片的大小
     * */
    private void setImageSize(Drawable d, int dir) {
        if (d == null) {
            return;
        }

        int height = 0;
        int width = 0;
        // 根据方向给宽和高赋值
        switch (dir) {
        case 0:
            // left
            height = leftHeight;
            width = leftWidth;
            break;
        case 1:
            // top
            height = topHeight;
            width = topWidth;
            break;
        case 2:
            // right
            height = rightHeight;
            width = rightWidth;
            break;
        case 3:
            // bottom
            height = bottomHeight;
            width = bottomWidth;
            break;
        }
        // 如果有某个方向的宽或者高没有设定值，则不去设定图片大小
        if (width != 0 && height != 0) {
            d.setBounds(0, 0, width, height);
        }
    }
}
