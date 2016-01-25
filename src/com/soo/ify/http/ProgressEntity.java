package com.soo.ify.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

public class ProgressEntity implements HttpEntity {
    
    public interface ProgressListener {
        void onProgress(long contentLength, long index);
    }
    
    private HttpEntity httpEntity;
    private ProgressListener listener;
    
    public ProgressEntity(HttpEntity httpEntity, ProgressListener listener) {
        this.httpEntity = httpEntity;
        this.listener = listener;
    }

    @Override
    public void consumeContent() throws IOException {
        httpEntity.consumeContent();
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        ProgressInputStream inputStream = new ProgressInputStream(httpEntity.getContent(), listener, getContentLength());
        return inputStream;
    }

    @Override
    public Header getContentEncoding() {
        return httpEntity.getContentEncoding();
    }

    @Override
    public long getContentLength() {
        return httpEntity.getContentLength();
    }

    @Override
    public Header getContentType() {
        return httpEntity.getContentType();
    }

    @Override
    public boolean isChunked() {
        return httpEntity.isChunked();
    }

    @Override
    public boolean isRepeatable() {
        return httpEntity.isRepeatable();
    }

    @Override
    public boolean isStreaming() {
        return httpEntity.isStreaming();
    }

    @Override
    public void writeTo(OutputStream arg0) throws IOException {
        ProgressOutputStream pos = new ProgressOutputStream(arg0, listener, getContentLength());
        httpEntity.writeTo(pos);
    }
    
    static class ProgressInputStream extends InputStream {
        
        InputStream inputStream;
        ProgressListener listener;
        long contentLength;
        
        int index;
        
        public ProgressInputStream(InputStream inputStream, ProgressListener listener, long contentLength) {
            this.inputStream = inputStream;
            this.listener = listener;
            this.contentLength = contentLength;
        }

        @Override
        public int read() throws IOException {
            index++;
            if (this.listener != null) {
                listener.onProgress(contentLength, index);
            }
            return this.inputStream.read();
        }
        
    }
    
    static class ProgressOutputStream extends OutputStream {
        
        OutputStream ouputStream;
        ProgressListener listener;
        long contentLength;
        
        long index;
        
        ProgressOutputStream(OutputStream outputStream, ProgressListener listener, long contentLength) {
            this.ouputStream = outputStream;
            this.listener = listener;
            this.contentLength = contentLength;
        }

        @Override
        public void write(int oneByte) throws IOException {
            index++;
            int progress = (int) ((index / (float)contentLength) * 100);
            if (listener != null) {
                listener.onProgress(contentLength, progress);
            }
            this.ouputStream.write(oneByte);
        }
    }
}
