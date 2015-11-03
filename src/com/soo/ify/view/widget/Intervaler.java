package com.soo.ify.view.widget;

import android.os.SystemClock;


public class Intervaler {
    
    private static final long TIME_BASE = 250;
    
    private int num;
    private long[] hits;
    private long intTime;
    
    public Intervaler() {
        this(1);
    }
    
    public Intervaler(int num) {
        this.num = num;
        this.hits = new long[num];
        this.intTime = num * TIME_BASE;
    }
    
    public boolean trace() {
        if (num <= 1) {
            return true;
        } else {
            System.arraycopy(hits, 1, hits, 0, hits.length - 1);
            hits[hits.length - 1] = SystemClock.uptimeMillis();
            return hits[0] >= SystemClock.uptimeMillis() - intTime;
        }
    }
}
