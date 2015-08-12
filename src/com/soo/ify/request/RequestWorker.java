package com.soo.ify.request;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.soo.ify.cache.DiskCache;
import com.soo.ify.cache.MemoryCache;
import com.soo.ify.http.ProgressEntity;
import com.soo.ify.http.ProgressEntity.ProgressListener;
import com.soo.ify.request.Request.Method;
import com.soo.ify.request.Request.Options;
import com.soo.ify.request.Request.Parser;
import com.soo.ify.util.ArgsUtils;
import com.soo.ify.util.DevUtils;

public class RequestWorker {
    
    private MemoryCache<String, byte[]> memoryCache;
    private DiskCache<String, byte[]> diskCache;
    private HttpClient httpClient;
    
    public RequestWorker(MemoryCache<String, byte[]> memoryCache, DiskCache<String, byte[]> diskCache,
            HttpClient httpClient) {
        this.memoryCache = memoryCache;
        this.diskCache = diskCache;
        this.httpClient = httpClient;
    }
    
    public <Result> Result doRequest(Request<Result> request) throws RequestException {
        ArgsUtils.notNull(request, "Request");
        List<Options> options = request.getOptions();
        Parser<Result> parser = request.getParser();
        if (options == null || options.size() == 0) {
            request.appendTracker("no options has been found");
            throw new RequestException(RequestException.CODE_UNKNOWE, "No request options");
        }
        if (parser == null) {
            request.appendTracker("no parser has been found");
            throw new RequestException(RequestException.CODE_UNKNOWE, "No request parser");
        }
        byte[] data = null;
        for (Options option : options) {
            switch (option) {
            case MEMORY:
                if (request.getTuchCacheable()) {
                    data = getDataFromMemoryCache(request);
                }
                break;
            case DISK:
                if (request.getTuchCacheable()) {
                    data = getDataFromDiskCache(request);
                }
                break;
            case NET:
                data = getDataFromNetwork(request);
                break;
            }
            if (data != null) {
                break;
            }
        }
        if (data == null) {
            return null;
        }
        request.appendTracker("parse data");
        Result result = parser.onParse(data);
        if (result == null) {
            request.appendTracker("parse data to null");
            throw new RequestException(RequestException.CODE_PARSEDATA, "Parse data to null");
        }
        boolean isCleanResult = parser.isClean(result);
        if (isCleanResult && request.getCacheable()) {
            request.appendTracker("put data into cache-memory-disk");
            memoryCache.putValue(request.getUrl(), data);
            diskCache.putValue(request.getUrl(), data);
            memoryCache.flush();
            diskCache.flush();
        }
        request.appendTracker("return data");
        return result;
    }
    
    protected <Result> byte[] getDataFromMemoryCache(Request<Result> request) throws RequestException {
        ArgsUtils.notNull(request, "Request");
        request.appendTracker("working in cache-memory");
        if (memoryCache == null) {
            request.appendTracker("no cache-memeory has been found");
            throw new RequestException(RequestException.CODE_UNKNOWE, "No memory cache");
        }
        byte[] data = memoryCache.getValue(request.getUrl());
        if(data == null) {
            request.appendTracker("no data has been found in cache-memory");
        } else {
            request.appendTracker("hit data in cache-memory");
        }
        return data;
    }
    
    protected <Result> byte[] getDataFromDiskCache(Request<Result> request) throws RequestException {
        ArgsUtils.notNull(request, "Request");
        request.appendTracker("working in cache-disk");
        if (diskCache == null) {
            request.appendTracker("no cache-disk has been found");
            throw new RequestException(RequestException.CODE_UNKNOWE, "No disk cache");
        }
        
        byte[] data = diskCache.getValue(request.getUrl());
        if(data == null) {
            request.appendTracker("no data has been found in cache-disk");
        } else {
            request.appendTracker("hit data in cache-disk");
        }
        
        return data;
    }
    
    protected <Result> byte[] getDataFromNetwork(Request<Result> request) throws RequestException {
        ArgsUtils.notNull(request, "Request");
        request.appendTracker("working in network");
        if (!DevUtils.NetWork.networkAvaliable(request.getRequestContext().getContext())) {
            request.appendTracker("no network");
            throw new RequestException(RequestException.CODE_NONETWORK, "no network");
        }
        if (httpClient == null) {
            request.appendTracker("no httpclient has been found");
            throw new RequestException(RequestException.CODE_UNKNOWE, "No http client");
        }
        HttpUriRequest httpUriRequest = getHttpUriRequest(request);
        if (httpUriRequest == null) {
            request.appendTracker("no httpUriRequest has been found");
            throw new RequestException(RequestException.CODE_UNKNOWE, "Can`t create HttpUriRequest by request");
        }
        try {
            HttpResponse httpResponse = httpClient.execute(httpUriRequest);
            StatusLine statusLine = httpResponse.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                request.appendTracker("no data has been found in network:status code=" + statusCode);
                throw new RequestException(RequestException.CODE_SERVERERROR, "HttpResponse cod=" + statusCode);
            }
            HttpEntity entity = httpResponse.getEntity();
            ProgressEntity progressEntity = new ProgressEntity(entity, new InnerListener(request));
            byte[] data = EntityUtils.toByteArray(progressEntity);
            request.appendTracker("hit data in network");
            return data;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            request.appendTracker("ClientProtocolException in network");
            throw new RequestException(RequestException.CODE_NETWORERROR, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            request.appendTracker("IOException in network");
            throw new RequestException(RequestException.CODE_NETWORERROR, e.getMessage());
        }
    }
    
    private static HttpUriRequest getHttpUriRequest(Request<?> request) {
        HttpUriRequest httpUriRequest = null;
        if (request != null) {
            String url = request.getUrl();
            Method method = request.getMethod();
            if (url != null) {
                switch (method) {
                case GET:
                    httpUriRequest = new HttpGet(url);
                    break;
                case POST:
                    HttpPost httpPost = new HttpPost(url);
                    HttpEntity entity = request.getHttpEntity();
                    if (entity != null) {
                        ProgressEntity progressEntity = new ProgressEntity(entity, new InnerListener(request));
                        httpPost.setEntity(progressEntity);
                    }
                    httpUriRequest = httpPost;
                    break;
                }
            }
        }
        for (BasicNameValuePair bnvp : request.getHeader()) {
            httpUriRequest.addHeader(bnvp.getName(), bnvp.getValue());
        }
        return httpUriRequest;
    }
    
    static class InnerListener implements ProgressListener {
        
        final Request<?> request;
        
        InnerListener(Request<?> request) {
            this.request = request;
        }

        @Override
        public void onProgress(long contentLength, long index) {
//            this.request.onRequest(request.getTracker(), index);
//            TODO
        }
        
    }
}
