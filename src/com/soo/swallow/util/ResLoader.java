package com.soo.swallow.util;

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
    
    public static Animation loadAnimation(Context context, int id) {
        return AnimationUtils.loadAnimation(context, id);
    }
    
    public static boolean loadBoolean(Context context, int id) {
        return context.getResources().getBoolean(id);
    }
    
    public static int loadColor(Context context, int id) {
        return context.getResources().getColor(id);
    }
    
    public static ColorStateList loadColorStateList(Context context, int id) {
        return context.getResources().getColorStateList(id);
    }
    
    public static float loadDimension(Context context, int id) {
        return context.getResources().getDimension(id);
    }
    
    public static int loadDimensionPixelOffset(Context context, int id) {
        return context.getResources().getDimensionPixelOffset(id);
    }
    
    public static int loadDimensionPixelSize(Context context, int id) {
        return context.getResources().getDimensionPixelSize(id);
    }
    
    public static Drawable loadDrawable(Context context, int id) {
        return context.getResources().getDrawable(id);
    }
    
    public static int loadInteger(Context context, int id) {
        return context.getResources().getInteger(id);
    }
    
    public static int[] loadIntArray(Context context, int id) {
        return context.getResources().getIntArray(id);
    }
    
    public static Movie loadMovie(Context context, int id) {
        return context.getResources().getMovie(id);
    }
    
    public static String loadString(Context context, int id) {
        return context.getResources().getString(id);
    }
    
    public static String loadString(Context context, int id, String defaultValue) {
        String value = null;
        try{
            value = context.getResources().getString(id);
        } catch (NotFoundException exception) {
            value = defaultValue;
        }
        return value;
    }
    
    public static String loadFormats(Context context, int id, Object...formats) {
        return context.getResources().getString(id, formats);
    }
    
    public static String[] loadStringArray(Context context, int id) {
        return context.getResources().getStringArray(id);
    }
    
    public static CharSequence loadText(Context context, int id) {
        return context.getResources().getText(id);
    }
    
    public static CharSequence[] loadTextArray(Context context, int id) {
        return context.getResources().getTextArray(id);
    }
    
    public static XmlResourceParser loadXml(Context context, int id) {
        return context.getResources().getXml(id);
    }
}
