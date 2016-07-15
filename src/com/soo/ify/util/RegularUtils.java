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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtils {
    
    /**验证字符串有limit位小数的正实数
     * @param str
     * @return
     */
    public static boolean isLimitedDot(String str, int limit) {
        Pattern p = Pattern.compile("^[0-9]+(.[0-9]{" + limit + "})?$");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    
    /**验证字符串只包含中文
     * @param text
     * @return
     */
    public static boolean isChinese(String str) {
        Pattern p = Pattern.compile("^[\u4e00-\u9fa5]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    
    /**验证字符串是否为手机号
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {   
        Pattern p = null;  
        Matcher m = null;  
        boolean b = false;   
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); 
        m = p.matcher(str);  
        b = m.matches();   
        return b;  
    }
    
    public static boolean isOkPwd(String str) {
        Pattern p = Pattern.compile("^[0-9a-zA-Z]+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }
    
    public static String filterSpecial(String str) {
          String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
          Pattern p = Pattern.compile(regEx);
          Matcher m = p.matcher(str);
          return m.replaceAll("").trim();
    }
    
    /**判断是否包含特殊字符
     * @param string
     * @return
     */
    public static boolean conSpeCharacters(String string) {
        if (string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]*[\0]", "").length()==0) {
            return true;
        }
            return false;
     }
}
