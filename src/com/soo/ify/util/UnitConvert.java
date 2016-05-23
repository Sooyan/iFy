/**
 *        http://www.june.com
 * Copyright © 2015 June.Co.Ltd. All Rights Reserved.
 */
package com.soo.ify.util;

/**单位换算
 * @author Soo
 */
public class UnitConvert {

    public static class FileUnit {
        
        public static final int SCALE = 1024;
        
        public static final int SCALE_KB = SCALE;
        public static final int SCALE_MB = SCALE * SCALE_KB;
        public static final int SCALE_GB = SCALE * SCALE_MB;

        public static float toKb(int size) {
            return (size * 1.0f) / SCALE_KB;
        }
        
        public static float toMb(int size) {
            return (size * 1.0f) / SCALE_MB;
        }
        
        public static float toGb(int size) {
            return (size * 1.0f) / SCALE_GB;
        }
        
        public static int size(float size, int scale) {
            return (int) (size * scale);
        }
        
        public static int parse(String str, int scale) {
            float size = Float.valueOf(str);
            return (int) (size * scale);
        }
    }
}
