/*
 * Copyright (c) 2013-2014 Soo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soo.ify.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.soo.ify.R;

/**
 * ClassName: PathImage <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * @author Soo
 */
public class PathImage extends ImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private PathCreater pathCreater = new InnerRectPathCreater();
    private Paint paint = new Paint();
    private Paint filterPaint = new Paint();
    
    private int filterColor = Color.parseColor("#88000000");
    
    private boolean pressed;
    private boolean picked;
    
    private boolean pickEnable;
    private boolean pressEnable;
    private boolean filterEnable;
    
    private int stretchMod;
    
    public PathImage(Context context) {
        super(context);
        freshBitmapPaint();
        freshFilterPaint();
    }

    public PathImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        super.setScaleType(SCALE_TYPE);
        TypedArray typeArray = context.obtainStyledAttributes(attrs,
                R.styleable.PathImage);
        int pathType = typeArray.getInt(R.styleable.PathImage_shape, 0);
        switch (pathType) {
            case 0:
                break;
            case 1:
                pathCreater = new InnerOvalPathCreater();
                break;
            case 2:
                pathCreater = new InnerCirclePathCreater();
                break;
            case 3:
                pathCreater = new InnerTrianglePathCreater();
                break;
            case 4:
                pathCreater = new InnerRectPathCreater();
                break;
            case 5:
                pathCreater = new InnerRoundRectPathCreate();
                break;
        }
        
        filterColor = typeArray.getColor(R.styleable.PathImage_filterColor, Color.parseColor("#88ffffff"));
        picked = typeArray.getBoolean(R.styleable.PathImage_picked, false);
        
        pickEnable = typeArray.getBoolean(R.styleable.PathImage_pickEnable, false);
        pressEnable = typeArray.getBoolean(R.styleable.PathImage_pressEnable, false);
        filterEnable = typeArray.getBoolean(R.styleable.PathImage_filterEnable, false);
        
        stretchMod = typeArray.getInt(R.styleable.PathImage_stretchMod, 0);
        
        typeArray.recycle();
        freshBitmapPaint();
        freshFilterPaint();
    }

    public void setPathCreater(PathCreater pathCreater) {
        this.pathCreater = pathCreater;
        freshBitmapPaint();
        freshFilterPaint();
        invalidate();
    }

    public PathCreater getPathCreater() {
        return pathCreater;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        switch (stretchMod) {
        case 0:
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            break;
        case 1:
            setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
            break;
        case 2:
            setMeasuredDimension(heightMeasureSpec, heightMeasureSpec);
            break;
        default:
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        freshBitmapPaint();
        freshFilterPaint();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        if (pathCreater == null) {
            return;
        }
        Path path = pathCreater.create(0, 0, getWidth(), getHeight());

        canvas.drawPath(path, paint);
        
        if (filterEnable) {
            if (pickEnable && picked) {
                canvas.drawPath(path, filterPaint);
            } else {
                if (pressEnable && pressed) {
                    canvas.drawPath(path, filterPaint);
                }
            }
        }
        
    }
    
    private void freshBitmapPaint() {
        Bitmap bitmap = getDrawableBitmap();
        if (bitmap == null) {
            return;
        }

        if (paint == null) {
            paint = new Paint();
        }
        paint.setAntiAlias(true);
        Shader shader = getBitmapShader(bitmap);
        paint.setShader(shader);
    }
    
    private void freshFilterPaint() {
        Drawable drawable = new ColorDrawable(filterColor);
        
        Bitmap bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
        
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        
        if (filterPaint == null) {
            filterPaint = new Paint();
        }
        filterPaint.setAntiAlias(true);
        Shader shader = getBitmapShader(bitmap);
        filterPaint.setShader(shader);
    }
    
    private Shader getBitmapShader(Bitmap bitmap) {
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        float scale;
        float dx = 0;
        float dy = 0;

        Matrix shaderMatrix = new Matrix();

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        if (bitmapWidth * getHeight() > getWidth() * bitmapHeight) {

            scale = getHeight() / (float) bitmapHeight;
            dx = (getWidth() - bitmapWidth * scale) * 0.5f;
        } else {
            scale = getWidth() / (float) bitmapWidth;
            dy = (getHeight() - bitmapHeight * scale) * 0.5f;
        }
        
        shaderMatrix.setScale(scale, scale);
        shaderMatrix.postTranslate((int) (dx + 0.5), (int) (dy + 0.5));
        shader.setLocalMatrix(shaderMatrix);
        return shader;
    }
    
    private Bitmap getDrawableBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            drawable = new ColorDrawable(Color.WHITE);
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        
        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
                        COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);

            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!filterEnable || !pressEnable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            this.pressed = true;
            invalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            this.pressed = false;
            invalidate();
            break;
        }
        return super.onTouchEvent(event);
    }
    
    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        freshBitmapPaint();
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        freshBitmapPaint();
        invalidate();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        freshBitmapPaint();
        invalidate();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(SCALE_TYPE);
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }
    
    public void setPicked(boolean picked) {
        this.picked = picked;
        invalidate();
    }
    
    public boolean getPicked() {
        return picked;
    }
    
    public void setPickEnable(boolean enable) {
        this.pickEnable = enable;
    }
    
    public boolean getPickEnable() {
        return pickEnable;
    }
    
    public void setPressEnable(boolean enable) {
        this.pressEnable = enable;
    }
    
    public boolean getPressEnable() {
        return pressEnable;
    }
    
    public void setFilterEnable(boolean enable) {
        this.filterEnable = enable;
        invalidate();
    }

    public interface PathCreater {
        Path create(int startX, int startY, int stopX, int stopY);
    }
    
    public static class InnerRoundRectPathCreate implements PathCreater {

        @Override
        public Path create(int startX, int startY, int stopX, int stopY) {
            Path path = new Path();
            RectF rectF = new RectF(startY, startY, stopX, stopY);
            path.addRoundRect(rectF, 10, 10, Direction.CW);
            return path;
        }
        
    }

    public static class InnerRectPathCreater implements PathCreater {

        @Override
        public Path create(int startX, int startY, int stopX, int stopY) {
            Path path = new Path();
            path.addRect(startY, startY, stopX, stopY, Direction.CW);
            return path;
        }

    }

    public static class InnerCirclePathCreater implements PathCreater {

        @Override
        public Path create(int startX, int startY, int stopX, int stopY) {
            Path path = new Path();
            int desX = Math.abs(stopX - startX);
            int desY = Math.abs(stopY - startY);
            int temp = Math.min(desX, desY);
            path.addCircle(desX / 2, desY / 2, temp / 2, Direction.CW);
            return path;
        }
    }

    public static class InnerOvalPathCreater implements PathCreater {

        @Override
        public Path create(int startX, int startY, int stopX, int stopY) {
            Path path = new Path();
            RectF rectF = new RectF(startY, startY, stopX, stopY);
            path.addOval(rectF, Direction.CW);
            return path;
        }

    }

    public static class InnerTrianglePathCreater implements PathCreater {

        @Override
        public Path create(int startX, int startY, int stopX, int stopY) {
            Path path = new Path();
            int desX = Math.abs(stopX - startX);
            int desY = Math.abs(stopY - startY);

            Point op = new Point();
            int height;
            Point[] points;
            if (desX > desY) {
                height = desY;
                op.x = (stopX - startX) / 2;
                op.y = startY + height * 2 / 3;
            } else {
                height = (int) (Math.sqrt(3) * (desX / 2));
                op.x = (stopX - startX) / 2;
                op.y = (stopY - startY) / 2;
            }
            points = getTrianglePoints(op, height);
            if (points != null) {
                path.moveTo(points[0].x, points[0].y);
                if (points[1].y < startY) {
                    points[1].y = startY;
                }
                path.lineTo(points[1].x, points[1].y);
                path.lineTo(points[2].x, points[2].y);
                path.close();
            }
            return path;
        }

        private Point[] getTrianglePoints(Point oPoint, int height) {
            Point[] points = new Point[3];
            double temp = height / 3;
            points[0] = new Point();
            points[0].x = (int) (Math.sqrt(3) * temp) * (-1);
            points[0].y = (int) (temp);

            points[1] = new Point();
            points[1].x = 0;
            points[1].y = ((int) temp) * (-2);

            points[2] = new Point();
            points[2].x = (int) (Math.sqrt(3) * temp);
            points[2].y = (int) (temp);

            for (Point point : points) {
                point.x += oPoint.x;
                point.y += oPoint.y;
            }

            return points;
        }

    }
    
    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }
}
