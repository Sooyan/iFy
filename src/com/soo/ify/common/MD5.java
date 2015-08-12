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
package com.soo.ify.common;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ClassName: MD5 <br/>
 * Function: md5加密. <br/>
 */
public class MD5 {

    private static final String CHARSET = "utf-8";
    private static final String ALGORITHM = "MD5";
    private static final int RADIX = 18;

    public static String deep(String... strings) {
        if (strings == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String str : strings) {
            builder.append(str);
        }
        return deep(builder.toString());
    }

    public static String deep(String str) {

        if (str != null) {
            try {
                byte[] orCode = str.getBytes(CHARSET);
                byte[] md5Code = getMD5(orCode);
                BigInteger bi = new BigInteger(md5Code).abs();
                return bi.toString(RADIX);
            } catch (UnsupportedEncodingException e) {
            }
        }
        return String.valueOf(System.currentTimeMillis());
    }

    private static byte[] getMD5(byte[] data) {
        byte[] code = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            digest.update(data);
            code = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return code;
    }
}
