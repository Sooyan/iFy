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
package com.soo.swallow.request;

public class Error {
    
    public enum Label {
        
        NODATA(8, "no data has been found"),
        NOOPTIONS(7, "no options in request"),
        NOMETHOD(6, "no http request method"),
        PROTOCOL(5, "http protocol flow error"),
        IO(4, "http io flow error"),
        RESPONSECODE(3, "the response code is't 200"),
        NOPARSE(2, "no parse error"),
        PARSE(1, "parse byte data flow error"),
        UNKNOWN(0, "unknown");
        
        private int code;
        private String label;
        
        Label(int code, String label) {
            this.code = code;
            this.label = label;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    public static Error getError(Label label) {
        return getError(label, null);
    }
    
    public static Error getError(Label label, String message) {
        return new Error(label.getCode(), label.getLabel(), message);
    }
    
    public static Label getLabel(int errorCode) {
        Label[] labels = Label.values();
        for (Label label : labels) {
            if (label.code == errorCode) {
                return label;
            }
        }
        return Label.UNKNOWN;
    }
    
    private int code;
    private String label;
    private String message;
    
    private Error(int code, String label, String message) {
        this.code = code;
        this.label = label;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getMessage() {
        return label + ":" + message;
    }
    
}
