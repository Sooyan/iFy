package com.soo.ify.util;

import java.util.WeakHashMap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * @author Soo
 */
public class ResLoader {
    
    private static WeakHashMap<Integer, Object> pool = new WeakHashMap<Integer, Object>();
    
    public static void flush() {
        pool.clear();
    }
    
    public static Animation loadAnimation(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = AnimationUtils.loadAnimation(context, id);
            pool.put(id, obj);
        }
        return (Animation) obj;
    }
    
    public static boolean loadBoolean(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getBoolean(id);
            pool.put(id, obj);
        }
        return (Boolean) obj;
    }
    
    public static int loadColor(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getColor(id);
            pool.put(id, obj);
        }
        return (Integer) obj;
    }
    
    public static ColorStateList loadColorStateList(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getColorStateList(id);
            pool.put(id, obj);
        }
        return (ColorStateList) obj;
    }
    
    public static float loadDimension(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getDimension(id);
            pool.put(id, obj);
        }
        return (Float) obj;
    }
    
    public static int loadDimensionPixelOffset(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getDimensionPixelOffset(id);
            pool.put(id, obj);
        }
        return (Integer) obj;
    }
    
    public static int loadDimensionPixelSize(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getDimensionPixelSize(id);
            pool.put(id, obj);
        }
        return (Integer) obj;
    }
    
    public static Drawable loadDrawable(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getDrawable(id);
            pool.put(id, obj);
        }
        return (Drawable) obj;
    }
    
    public static int loadInteger(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getInteger(id);
            pool.put(id, obj);
        }
        return (Integer) obj;
    }
    
    public static int[] loadIntArray(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getIntArray(id);
            pool.put(id, obj);
        }
        return (int[]) obj;
    }
    
    public static Movie loadMovie(Context context, int id) {
        return context.getResources().getMovie(id);
    }
    
    public static String loadString(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getString(id);
            pool.put(id, obj);
        }
        return (String) obj;
    }
    
    public static String loadString(Context context, int id, String defaultValue) {
        Object obj = pool.get(id);
        if (obj != null) {
            return (String) obj;
        }
        String value = null;
        try{
            value = context.getResources().getString(id);
        } catch (NotFoundException exception) {
            value = defaultValue;
        }
        pool.put(id, value);
        return value;
    }
    
    public static String loadFormats(Context context, int id, Object...formats) {
        return context.getResources().getString(id, formats);
    }
    
    public static String[] loadStringArray(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getStringArray(id);
            pool.put(id, obj);
        }
        return (String[]) obj;
    }
    
    public static CharSequence loadText(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getText(id);
            pool.put(id, obj);
        }
        return (CharSequence) obj;
    }
    
    public static CharSequence[] loadTextArray(Context context, int id) {
        Object obj = pool.get(id);
        if (obj == null) {
            obj = context.getResources().getTextArray(id);
            pool.put(id, obj);
        }
        return (CharSequence[]) obj;
    }
    
    public static XmlResourceParser loadXml(Context context, int id) {
        return context.getResources().getXml(id);
    }
}
