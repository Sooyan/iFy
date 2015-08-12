/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.swallow.view.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Soo
 *
 */
public class ClickerHelper implements OnClickListener {
    
    public interface ClickInterface {
//        Empty
    }
    
    private static ClickerHelper BUNDLE = new ClickerHelper();
    
    private static Map<View, Clicker> maps = new HashMap<View, ClickerHelper.Clicker>();
    
    public static Clicker newClicker(ClickInterface clickInterface, View view) {
        maps.remove(view);
        Clicker clicker = new Clicker(clickInterface);
        view.setOnClickListener(BUNDLE);
        maps.put(view, clicker);
        
        return clicker;
    }
    
    public static void removeClicker(View view) {
        if (view != null) {
            maps.remove(view);
        }
    }
    
    public static void removeClickerWithHost(ClickInterface host) {
        Iterator<View> iterator = maps.keySet().iterator();
        while (iterator.hasNext()) {
            View view = iterator.next();
            Clicker clicker = maps.get(view);
            if (clicker.host == host) {
                maps.remove(view);
            }
        }
    }
    
    public static void clear() {
        maps.clear();
    }

    private ClickerHelper() {
    }
    
    @Override
    public void onClick(View v) {
        Clicker clicker = maps.get(v);
        if (clicker == null || clicker.method == null
                || clicker.host == null) {
            return;
        }
        try {
            clicker.method.setAccessible(true);
            if (clicker.params != null) {
                clicker.method.invoke(clicker.host, clicker.params);
            } else {
                clicker.method.invoke(clicker.host);
            }
            clicker.method.setAccessible(false);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public static class Clicker {
        
        private ClickInterface host;
        private Method method;
        private Object[] params;
        
        protected Clicker(ClickInterface host) {
            this.host = host;
        }
        
        public void onClick(String methodName, Object...params) {
            this.params = params;
            if (params == null) {
                return;
            }
            
            int count = params.length;
            Class<?>[] classes = new Class<?>[count];
            for (int i = 0; i < count; i++) {
                classes[i] = params[i].getClass();
            }
            try {
                method = host.getClass().getDeclaredMethod(methodName, classes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        
    }
}
