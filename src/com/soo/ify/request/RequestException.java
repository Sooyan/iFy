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
package com.soo.ify.request;

/**
 * @author Soo
 *
 */
public class RequestException extends Exception {

    private static final long serialVersionUID = -3881559141290947491L;
    
    public static final int CODE_UNKNOWE = 0xf000;
    public static final int CODE_CANCLE = 0xf001;
    public static final int CODE_NONETWORK = 0xf002;
    public static final int CODE_NETWORERROR = 0xf003;
    public static final int CODE_SERVERERROR = 0xf004;
    public static final int CODE_PARSEDATA = 0xf005;
    
    private int code;
    
    public RequestException(String detailMessage) {
        this(CODE_UNKNOWE, detailMessage);
    }

    public RequestException(int code, String detailMessage) {
        super(detailMessage);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
    
}
