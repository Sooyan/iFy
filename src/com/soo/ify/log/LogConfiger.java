package com.soo.ify.log;

import android.content.Context;
import android.util.SparseBooleanArray;

import com.soo.ify.util.TextUtils;


public class LogConfiger {
    
    Context context;
    Class<? extends ILog> classL;
    String tag;
    
    boolean allowV;
    boolean allowD;
    boolean allowI;
    boolean allowW;
    boolean allowE;
    boolean allowWtf;
    
    private LogConfiger() {}
    
    public static class Builder {
        
        public static final String TAG = "--->";
        
        private static final int KEY_V = 0xf01;
        private static final int KEY_D = 0xf02;
        private static final int KEY_I = 0xf03;
        private static final int KEY_W = 0xf04;
        private static final int KEY_E = 0xf05;
        private static final int KEY_WTF = 0xf06;
        
        private Context context;
        private Class<? extends ILog> classL;
        private String tag;
        private SparseBooleanArray booleanArray = new SparseBooleanArray();
        
        public Builder(Context context) {
            this.context = context;
        }
        
        public Builder setILog(Class<? extends ILog> classL) {
            this.classL = classL;
            return this;
        }
        
        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }
        
        public Builder setLogAble(boolean v, boolean d, boolean i, boolean w, boolean e, boolean wtf) {
            booleanArray.put(KEY_V, v);
            booleanArray.put(KEY_D, d);
            booleanArray.put(KEY_I, i);
            booleanArray.put(KEY_W, w);
            booleanArray.put(KEY_E, e);
            booleanArray.put(KEY_WTF, wtf);
            return this;
        }
        
        public LogConfiger build() {
            LogConfiger configer = new LogConfiger();
            configer.context = context.getApplicationContext();
            configer.classL = classL == null ? SimpleLog.class : classL;
            configer.tag = TextUtils.isEmpty(tag) ? TAG : tag;
            configer.allowV = booleanArray.get(KEY_V, true);
            configer.allowD = booleanArray.get(KEY_D, true);
            configer.allowI = booleanArray.get(KEY_I, true);
            configer.allowW = booleanArray.get(KEY_W, true);
            configer.allowE = booleanArray.get(KEY_E, true);
            configer.allowWtf = booleanArray.get(KEY_WTF, true);
            return configer;
        }
    }
}
