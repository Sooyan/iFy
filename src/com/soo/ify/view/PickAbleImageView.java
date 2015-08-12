package com.soo.ify.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.soo.ify.R;

public class PickAbleImageView extends ImageView {
    
    private Drawable pickedDrawable = null;
    private Drawable deleteDrawable = null;
    private boolean picked = false;
    private boolean deleteAble = false;
    
    public PickAbleImageView(Context context) {
        super(context);
        init();
    }
    
    public PickAbleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PickAbleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init(){
        setPickedResource(R.drawable.icon_picked);
//        setDeleteResource(R.drawable.icon_delete);
    }
    
    public void setPicked(boolean picked){
        this.picked = picked;
        invalidate();
    }
    
    public boolean getPicked(){
        return picked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(pickedDrawable != null && picked){
            setDrawableBounds();
            pickedDrawable.draw(canvas);
        }
        if(deleteDrawable != null && deleteAble){
            setDrawableBounds();
            deleteDrawable.draw(canvas);
        }
    }
    
    private void setDrawableBounds(){
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(pickedDrawable != null){
            pickedDrawable.setBounds(width - 20, height - 20, width, height);
        }
        if(deleteDrawable != null){
            deleteDrawable.setBounds(width - 20, 0, width, 20);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
    
    public void setDeleteAble(boolean flag){
        this.deleteAble = flag;
    }

    public void setPickedResource(int resID){
        Drawable drawable = getContext().getResources().getDrawable(resID);
        setPickedDrawable(drawable);
    }
    
    public void setPickedBitmap(Bitmap bitmap){
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        setPickedDrawable(drawable);
    }
    
    public void setPickedDrawable(Drawable drawable){
        this.pickedDrawable = drawable;
    }
    
    public void setDeleteResource(int resID){
        Drawable drawable = getContext().getResources().getDrawable(resID);
        setDeleteDrawable(drawable);
    }
    
    public void setDeleteBitmap(Bitmap bitmap){
        BitmapDrawable drawable = new BitmapDrawable(bitmap);
        setDeleteDrawable(drawable);
    }
    
    public void setDeleteDrawable(Drawable drawable){
        this.deleteDrawable = drawable;
    }
}
