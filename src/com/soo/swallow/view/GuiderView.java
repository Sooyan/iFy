package com.soo.swallow.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.soo.swallow.R;

public class GuiderView extends View {

    private int colorLeft, colorRight;
    private int textSize;
    private String text;
    
    private Bitmap bitmapLeft, bitmapRight;
    private Rect iconRect;
    
    private float mAlpha;

    private Rect mTextBound;
    private Paint mTextPaint;

    public GuiderView(Context context) {
        this(context, null);
    }

    public GuiderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuiderView(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.GuiderView);
        colorLeft = Color.argb(255, 51, 51, 51);
        colorRight = typeArray.getColor(R.styleable.GuiderView_color, 0xFF45C01A);
        text = typeArray.getString(R.styleable.GuiderView_text);
        textSize = (int) typeArray.getDimension(R.styleable.GuiderView_textSize, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                        getResources().getDisplayMetrics()));
        
        Drawable drawable = typeArray.getDrawable(R.styleable.GuiderView_icon);
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitDrawable = (BitmapDrawable) drawable;
            bitmapLeft = bitDrawable.getBitmap();
        }

        typeArray.recycle();

        mTextBound = new Rect();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(0Xff555555);
        if (text != null) {
            mTextPaint.getTextBounds(text, 0, text.length(), mTextBound);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom() - mTextBound.height());

        int iconLeft = getMeasuredWidth() / 2 - iconWidth / 2;
        int iconTop = getMeasuredHeight() / 2 - (mTextBound.height() + iconWidth) / 2;
        iconRect = new Rect(iconLeft, iconTop, iconLeft + iconWidth + 10, iconTop + iconWidth);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmapLeft, null, iconRect, null);
        int alpha = (int) Math.ceil(255 * mAlpha);

        // 内存去准备mBitmap , setAlpha , 纯色 ，xfermode ， 图标
        setupTargetBitmap(alpha);
        // 1、绘制原文本 ； 2、绘制变色的文本
        if (text != null) {
            drawSourceText(canvas, alpha);
            drawTargetText(canvas, alpha);
        }

        canvas.drawBitmap(bitmapRight, 0, 0, null);

    }

    /**
     * 绘制变色的文本
     * 
     * @param canvas
     * @param alpha
     */
    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(colorRight);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = iconRect.bottom + mTextBound.height();
        canvas.drawText(text, x, y, mTextPaint);

    }

    /**
     * 绘制原文本
     * 
     * @param canvas
     * @param alpha
     */
    private void drawSourceText(Canvas canvas, int alpha) {
        mTextPaint.setColor(colorLeft);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = iconRect.bottom + mTextBound.height();
        canvas.drawText(text, x, y, mTextPaint);

    }

    /**
     * 在内存中绘制可变色的Icon
     */
    private void setupTargetBitmap(int alpha) {
        bitmapRight = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapRight);
        Paint paint = new Paint();
        paint.setColor(colorRight);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setAlpha(alpha);
        canvas.drawRect(iconRect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAlpha(255);
        canvas.drawBitmap(bitmapLeft, null, iconRect, paint);
    }

    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_ALPHA = "status_alpha";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_ALPHA, mAlpha);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mAlpha = bundle.getFloat(STATUS_ALPHA);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setIconAlpha(float alpha) {
        this.mAlpha = alpha;
        invalidateView();
    }

    /**
     * 重绘
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

}
