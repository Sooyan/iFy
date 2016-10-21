/*
 * Copyright (c) 2013-2014 Soo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soo.ify.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;

import android.text.TextUtils;

import com.soo.ify.util.ArgsUtils;

/**
 * @author Soo
 */
public class SimpleMultipartEntity2 implements HttpEntity {
    
    private final SimpleMultipartEntity2.Builder builder;
    
    private SimpleMultipartEntity2(SimpleMultipartEntity2.Builder builder) {
        this.builder = builder;
    }
    
    @Override
    public void consumeContent() throws IOException {
        if (isStreaming()) {
            throw new UnsupportedOperationException(
            "Streaming entity does not implement #consumeContent()");
        }
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        throw new UnsupportedOperationException(
                "Streaming entity does not implement #getContent()");
    }

    @Override
    public Header getContentEncoding() {
        return null;
    }

    @Override
    public long getContentLength() {
        return builder.getLength();
    }

    @Override
    public Header getContentType() {
        return new BasicHeader("Content-Type", "multipart/form-data; boundary=" + builder.boundary);
    }

    @Override
    public boolean isChunked() {
        return false;
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    @Override
    public void writeTo(OutputStream arg0) throws IOException {
        builder.doWrite(arg0);
    }
    
    public static class Builder {
        final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        
        Map<String, String> kvParts;
        Map<String, File> fileParts;
        
        String boundary;
        
        public Builder() {
            this.kvParts = new LinkedHashMap<String, String>();
            this.fileParts = new LinkedHashMap<String, File>();
            
            final StringBuffer buf = new StringBuffer();
            final Random rand = new Random();
            for (int i = 0; i < 30; i++) {
                buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
            }
            this.boundary = buf.toString();
        }
        
        public Builder add(String key, String value) {
            kvParts.put(key, value);
            return this;
        }
        
        public Builder add(File file) {
            return add(file.getName(), file);
        }
        
        public Builder add(String key, File file) {
            ArgsUtils.notNull(file, "File");
            if (TextUtils.isEmpty(key)) {
                key = file.getName();
            }
            fileParts.put(key, file);
            return this;
        }
        
        protected long getLength() {
            int length = 0;
            for (Entry<String, String> entry : kvParts.entrySet()) {
                String text = getWrapperText(boundary, entry.getKey(), entry.getValue());
                length += text.getBytes().length;
            }
            for (Entry<String, File> entry : fileParts.entrySet()) {
                String text = getWrapperText(boundary, entry.getKey(), entry.getValue());
                length += text.getBytes().length + "\r\n".getBytes().length + entry.getValue().length();
            }
            length += getEndLine().getBytes().length;
            return length;
        }
        
        private String getEndLine() {
            return getEndLine(boundary);
        }
        
        protected void doWrite(OutputStream os) throws IOException {
            FileInputStream fin = null;
            try {
                for (Entry<String, String> entry : kvParts.entrySet()) {
                    String kvText = getWrapperText(boundary, entry.getKey(), entry.getValue());
                    os.write(kvText.getBytes());
                }
                
                for (Entry<String, File> entry : fileParts.entrySet()) {
                    String fileText = getWrapperText(boundary, entry.getKey(), entry.getValue());
                    os.write(fileText.getBytes());
                    
                    fin = new FileInputStream(entry.getValue());
                    
                    final byte[] buffer = new byte[4096];
                    int len = 0;
                    while((len = fin.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                    os.write("\r\n".getBytes());
                    fin.close();
                }
                os.write(getEndLine().getBytes());
                os.flush();
            } finally {
                if (fin != null) {
                    fin.close();
                }
            }
        }
        
        public SimpleMultipartEntity2 build() {
            return new SimpleMultipartEntity2(this);
        }
        
        private static String getWrapperText(String boundary, String key, String value) {
            StringBuilder builder = new StringBuilder();
            builder.append("--");
            builder.append(boundary);
            builder.append("\r\n");
            builder.append("Content-Disposition: form-data; name=\""+ key + "\"\r\n\r\n"); 
            builder.append(value);
            builder.append("\r\n");
            return builder.toString();
        }
        
        private static String getWrapperText(String boundary, String key, File file) {
            StringBuilder builder = new StringBuilder();
            builder.append("--");
            builder.append(boundary);
            builder.append("\r\n");
            builder.append("Content-Disposition: form-data;name=\""+ key +"\";filename=\""+ file.getName() + "\"\r\n");
            builder.append("Content-Type: application/octet-stream" +"\r\n");
            builder.append("Content-Transfer-Encoding: binary" + "\r\n\r\n");
            return builder.toString();
        }
        
        private static String getEndLine(String boundary) {
            return "--" + boundary + "--\r\n";
        }
    }
    
}
