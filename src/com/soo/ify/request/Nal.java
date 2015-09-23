package com.soo.ify.request;

import java.util.Map.Entry;
import java.util.Set;

import android.content.ContentValues;

/**Net access locator
 * @author Soo
 */
public final class Nal {
    
    private final String host;
    private final String port;
    private final String nameSpace;
    private final String api;
    private ContentValues contentValues = new ContentValues();
    
    public Nal(String host, String port, String nameSpace, String api) {
        this.host = host;
        this.port = port;
        this.nameSpace = nameSpace;
        this.api = api;
    }
    
    public Nal append(String key, Byte value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal append(String key, Short value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal append(String key, Integer value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal append(String key, Long value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal append(String key, Float value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal append(String key, Double value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal append(String key, String value) {
        contentValues.put(key, value);
        return this;
    }
    
    public Nal flush() {
        contentValues.clear();
        return this;
    }

    public String value() {
        if (host == null) {
            throw new IllegalArgumentException("host must not be null");
        }
        StringBuilder result = new StringBuilder();
        if (host.endsWith("/")) {
            result.append(host.substring(0, host.length() - 1));
        } else {
            result.append(host);
        }
        if (port != null) {
            try {
                int portValue = Integer.valueOf(port);
                if (portValue <= 0) {
                    throw new IllegalArgumentException("invalid port=" + port);
                }
                result.append(":");
                result.append(port);
            } catch (Exception e) {
                throw new IllegalArgumentException("invalid port=" + port);
            }
        }
        if (nameSpace != null) {
            if (!nameSpace.startsWith("/")) {
                result.append("/");
            }
            result.append(nameSpace);
        }
        if (api != null) {
            if (!api.startsWith("/")) {
                result.append("/");
            }
            result.append(api);
        }
        result.append(cv2s());
        return result.toString();
    }
    
    private String cv2s() {
        String str = "";
        Set<Entry<String, Object>> entries = contentValues.valueSet();
        boolean flag = false;
        for (Entry<String, Object> entry : entries) {
            if (!flag) {
                str += "?";
                flag = true;
            } else {
                str += "&";
            }
            str += entry.getKey() + "=" + entry.getValue().toString();
        }
        return str;
    }
    
    
    @Override
    public String toString() {
        return value();
    }
}
