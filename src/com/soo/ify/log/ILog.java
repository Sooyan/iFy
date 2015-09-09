package com.soo.ify.log;

import android.content.Context;

public interface ILog {
    
    void onCreate(Context context);
    
    int v(String tag, Object obj);
    
    int d(String tag, Object obj);
    
    int i(String tag, Object obj);
    
    int w(String tag, Object obj);
    
    int e(String tag, Object obj);
    
    int wtf(String tag, Object obj);
}
