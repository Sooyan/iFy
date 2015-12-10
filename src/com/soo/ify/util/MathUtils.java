package com.soo.ify.util;

public class MathUtils {

    /**获取在两个值之间的值
     * @param dest 目标值
     * @param left 
     * @param right
     * @return
     */
    public static int betweenValue(int dest, int left, int right) {
        left = Math.min(left, right);
        right = Math.max(left, right);
        if (dest > right) {
            dest = right;
        }
        if (dest < left) {
            dest = left;
        }
        return dest;
    }
    
    public static long betweenValue(long dest, long left, long right) {
        left = Math.min(left, right);
        right = Math.max(left, right);
        if (dest > right) {
            dest = right;
        }
        if (dest < left) {
            dest = left;
        }
        return dest;
    }
    
    public static float betweenValue(float dest, float left, float right) {
        left = Math.min(left, right);
        right = Math.max(left, right);
        if (dest > right) {
            dest = right;
        }
        if (dest < left) {
            dest = left;
        }
        return dest;
    }
}
