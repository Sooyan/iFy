package com.soo.ify.log;

import java.util.Collection;

import android.content.Context;

public class SimpleLog implements ILog {
    
    @Override
    public void onCreate(Context context) {
//        empty
    }

    @Override
    public int v(String tag, Object obj) {
        if (obj == null) {
            return android.util.Log.v(tag, "null");
        } else if (obj instanceof Throwable) {
            return android.util.Log.v(tag, "Thrwable:", (Throwable)obj);
        } else {
            return android.util.Log.v(tag, o2s(obj));
        }
    }

    @Override
    public int d(String tag, Object obj) {
        if (obj == null) {
            return android.util.Log.d(tag, "null");
        } else if (obj instanceof Throwable) {
            return android.util.Log.d(tag, "Thrwable:", (Throwable)obj);
        } else {
            return android.util.Log.d(tag, o2s(obj));
        }
    }

    @Override
    public int i(String tag, Object obj) {
        if (obj == null) {
            return android.util.Log.i(tag, "null");
        } else if (obj instanceof Throwable) {
            return android.util.Log.i(tag, "Thrwable:", (Throwable)obj);
        } else {
            return android.util.Log.i(tag, o2s(obj));
        }
    }

    @Override
    public int w(String tag, Object obj) {
        if (obj == null) {
            return android.util.Log.w(tag, "null");
        } else if (obj instanceof Throwable) {
            return android.util.Log.w(tag, "Thrwable:", (Throwable)obj);
        } else {
            return android.util.Log.w(tag, o2s(obj));
        }
    }

    @Override
    public int e(String tag, Object obj) {
        if (obj == null) {
            return android.util.Log.e(tag, "null");
        } else if (obj instanceof Throwable) {
            return android.util.Log.e(tag, "Thrwable:", (Throwable)obj);
        } else {
            return android.util.Log.e(tag, o2s(obj));
        }
    }

    @Override
    public int wtf(String tag, Object obj) {
        if (obj == null) {
            return android.util.Log.wtf(tag, "null");
        } else if (obj instanceof Throwable) {
            return android.util.Log.wtf(tag, "Thrwable:", (Throwable)obj);
        } else {
            return android.util.Log.wtf(tag, o2s(obj));
        }
    }

    private static String o2s(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Collection) {
            Collection<?> collection = (Collection<?>) obj;
            return getClassName(obj) + ":" + collection.size();
        }
        if (obj instanceof Object[]) {
            return getClassName(obj) + ":" + ((Object[]) obj).length;
        }
        if (obj instanceof boolean[]) {
            return "boolean[]:" + ((boolean[]) obj).length;
        }
        if (obj instanceof byte[]) {
            return "byte[]:" + ((byte[]) obj).length;
        }
        if (obj instanceof char[]) {
            return "char[]:" + ((char[]) obj).length;
        }
        if (obj instanceof short[]) {
            return "short[]:" + ((short[]) obj).length;
        }
        if (obj instanceof int[]) {
            return "int[]:" + ((int[]) obj).length;
        }
        if (obj instanceof long[]) {
            return "long[]:" + ((long[]) obj).length;
        }
        if (obj instanceof float[]) {
            return "float[]:" + ((float[]) obj).length;
        }
        if (obj instanceof double[]) {
            return "double[]:" + ((double[]) obj).length;
        }
//        More to judge
        return obj.toString();
    }
    
    private static String getClassName(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getSimpleName();
    }
}
