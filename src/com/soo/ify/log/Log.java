package com.soo.ify.log;

import com.soo.ify.util.TextUtils;

public class Log {
    
    private static LogConfiger configer;
    private static ILog iLog;
    
    public static void init(LogConfiger logConfiger) {
        if (configer != null && iLog != null) {
            return;
        }
        Class<? extends ILog> classL = logConfiger.classL;
        try {
            iLog = classL.newInstance();
            iLog.onCreate(logConfiger.context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        configer = logConfiger;
    }
    
    private Log(){}
    
    public static int v(Object obj) {
        return v(null, obj);
    }
    
    public static int v(String tag, Object obj) {
        selfChecking();
        if (!configer.allowV) {
            return 0;
        }
        tag = TextUtils.isEmpty(tag) ? configer.tag : tag;
        return iLog.v(tag, obj);
    }
    
    public static int d(Object obj) {
        return d(configer.tag, obj);
    }
    
    public static int d(String tag, Object obj) {
        selfChecking();
        if (!configer.allowD) {
            return 0;
        }
        tag = TextUtils.isEmpty(tag) ? configer.tag : tag;
        return iLog.d(tag, obj);
    }
    
    public static int i(Object obj) {
        return i(configer.tag, obj);
    }
    
    public static int i(String tag, Object obj) {
        selfChecking();
        if (!configer.allowI) {
            return 0;
        }
        tag = TextUtils.isEmpty(tag) ? configer.tag : tag;
        return iLog.i(tag, obj);
    }
    
    public static int w(Object obj) {
        return w(configer.tag, obj);
    }
    
    public static int w(String tag, Object obj) {
        selfChecking();
        if (!configer.allowW) {
            return 0;
        }
        tag = TextUtils.isEmpty(tag) ? configer.tag : tag;
        return iLog.w(tag, obj);
    }
    
    public static int e(Object obj) {
        return e(configer.tag, obj);
    }
    
    public static int e(String tag, Object obj) {
        selfChecking();
        if (!configer.allowE) {
            return 0;
        }
        tag = TextUtils.isEmpty(tag) ? configer.tag : tag;
        return iLog.e(tag, obj);
    }
    
    public static int wtf(Object obj) {
        return wtf(configer.tag, obj);
    }
    
    public static int wtf(String tag, Object obj) {
        selfChecking();
        if (!configer.allowWtf) {
            return 0;
        }
        tag = TextUtils.isEmpty(tag) ? configer.tag : tag;
        return iLog.wtf(tag, obj);
    }
    
    private static boolean selfChecking() {
        if (configer == null) {
            throw new RuntimeException("No LogConfiger be found");
        }
        if (iLog == null) {
            throw new RuntimeException("No ILog be found");
        }
        return true;
    }
}
