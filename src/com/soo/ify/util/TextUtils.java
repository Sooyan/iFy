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
package com.soo.ify.util;

import java.util.ArrayList;

import com.soo.ify.common.util.HanziToPinyin;
import com.soo.ify.common.util.HanziToPinyin.Token;

public class TextUtils {
    
    /** 
     * Use fixS to fix s , if s is empty(s == null || s.lengtt() == 0);
     */
    public static CharSequence fixIfEmpty(CharSequence s, CharSequence fixS) {
        if (s == null || s.length() == 0) {
            s = fixS;
        }
        return s;
    }
    
    /** 
     * Use fixS to fix s , if s is null;
     */
    public static CharSequence fixIfNull(CharSequence s, CharSequence fixS) {
        if (s == null) {
            s = fixS;
        }
        return s;
    }

    /**
     * Returns true if the parameter is null or of zero length
     */
    public static boolean isEmpty(final CharSequence s) {
        if (s == null) {
            return true;
        }
        return s.length() == 0;
    }

    /**
     * Returns true if the parameter is null or contains only whitespace
     */
    public static boolean isBlank(final CharSequence s) {
        if (s == null) {
            return true;
        }
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the parameter contains whitespace
     */
    public static boolean containsBlanks(final CharSequence s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            if (Character.isWhitespace(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static String convertToPinyin(String string) {
        if (isEmpty(string)) {
            return "";
        }
        ArrayList<Token> tokens = HanziToPinyin.getInstance().get(string);
        StringBuilder sb = new StringBuilder();
        if (tokens != null && tokens.size() > 0) {
            for (Token token : tokens) {
                if (Token.PINYIN == token.type) {
                    sb.append(token.target);
                } else {
                    sb.append(token.source);
                }
            }
        }
        return sb.toString();
    }
}
