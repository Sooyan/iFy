package com.soo.ify.request;

import com.soo.ify.common.MD5;

public class DefaultKeyer implements Keyer {

    @Override
    public Object getCacheKey(String args) {
        return MD5.deep(args);
    }

    @Override
    public String getCacheFileName(String args) {
        return MD5.deep(args);
    }

}
