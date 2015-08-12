/*
 * Copyright (c) 2015. Soo (sootracker@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soo.swallow.util;

import java.util.Locale;

import com.alibaba.fastjson.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * @author Soo
 *
 */
public class DevUtils {
    
    public static class NetWork {
        /**获取当前网络连接类型
         * @param context
         * @return @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
         */
        public static int getNetworkType(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            if (info != null && info.isConnected()) {
                int type = info.getType();
                if (type == ConnectivityManager.TYPE_MOBILE) {
                    String extraInfo = info.getExtraInfo();
                    if (!TextUtils.isEmpty(extraInfo)) {
                        if (extraInfo.toLowerCase(Locale.CHINA).equals("cmnet")) {
                            return 3;
                        } else {
                            return 2;
                        }
                    }
                } else if (type == ConnectivityManager.TYPE_WIFI) {
                    return 1;
                }
            }
            return 0;
        }
        
        /**判断网络是否可用
         * @param context
         * @return 返回设备当前网络是否处于连接状态
         */
        public static boolean networkAvaliable(Context context) {
            NetworkInfo info = getNetworkInfo(context);
            return info != null && info.isConnected();
        }
        
        /**
         * 获取设备网络信息
         * @param context 上下文
         * @return 返回设备网络信息
         */
        public static NetworkInfo getNetworkInfo(Context context) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return manager.getActiveNetworkInfo();
        }
    }
    
    /**
     * 获取设备信息
     * @param context
     * @return 设备信息json字符串
     */
    public static String getDevInfos(Context context) {
        JSONObject json = new JSONObject();
        TelephonyManager m = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        json.put("versioncode", 1);
        json.put("os", "android");
        json.put("imei", m.getDeviceId());
        json.put("line1number", m.getLine1Number());
        json.put("networkoperator", m.getNetworkOperator());
        json.put("simoperator", m.getSimOperator());
        json.put("subscriberid", m.getSubscriberId());
        json.put("brand", Build.BRAND);
        json.put("product", Build.PRODUCT);
        json.put("serial", Build.SERIAL);
        json.put("sdk", Build.VERSION.SDK_INT);
        
        return json.toString();
    }
    
    public static boolean isAvaliableSD() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    
    public static ScreenTools getScreenTools(Context context) {
        if (ScreenTools.screenTools == null) {
            ScreenTools.screenTools = new ScreenTools(context.getApplicationContext());
        }
        return ScreenTools.screenTools;
    }

    public static class ScreenTools {
        
        private static ScreenTools screenTools;

        private Context context;

        private ScreenTools(Context context) {
            this.context = context.getApplicationContext();
        }

        public int dip2px(float f) {
            return (int) (0.5D + (double) (f * getDensity(context)));
        }

        public int dip2px(int i) {
            return (int) (0.5D + (double) (getDensity(context) * (float) i));
        }

        public int get480Height(int i) {
            return (i * getScreenWidth()) / 480;
        }

        public float getDensity(Context context) {
            return context.getResources().getDisplayMetrics().density;
        }

        public int getScal() {
            return (100 * getScreenWidth()) / 480;
        }

        public int getScreenDensityDpi() {
            return context.getResources().getDisplayMetrics().densityDpi;
        }

        public int getScreenHeight() {
            return context.getResources().getDisplayMetrics().heightPixels;
        }

        public int getScreenWidth() {
            return context.getResources().getDisplayMetrics().widthPixels;
        }


        public float getXdpi() {
            return context.getResources().getDisplayMetrics().xdpi;
        }

        public float getYdpi() {
            return context.getResources().getDisplayMetrics().ydpi;
        }

        public int px2dip(float f) {
            float f1 = getDensity(context);
            return (int) (((double) f - 0.5D) / (double) f1);
        }

        public int px2dip(int i) {
            float f = getDensity(context);
            return (int) (((double) i - 0.5D) / (double) f);
        }

    }
}
