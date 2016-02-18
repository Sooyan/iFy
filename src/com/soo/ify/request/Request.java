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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;

import com.soo.ify.util.ArgsUtils;

public class Request<Result> implements Runnable, Comparable<Request<Result>>{
    
    public enum Options {
        MEMORY,
        DISK,
        NET
    }
    
    public enum Method {
        GET("get"),
        POST("post");
        
        private String name;
        
        Method(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }
    
    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }
    
    public interface Callback<Result> {
        void onStart(Request<Result> request, String message);
        
        void onRequest(Request<Result> request, String message, int progress);
        
        void onFinish(Request<Result> request, Result result, String message, RequestException exception);
    }
    
    public interface Parser<Result> {
        
        Result onParse(byte[] data);
        
        boolean isClean(Result result);
    }
    
    private RequestContext requestContext;
    private Class<Result> resultClass;
    private Method method;
    private String url;
    private HttpEntity httpEntity;
    private List<BasicNameValuePair> headers;
    private List<Options> options;
    private boolean cacheAble;
    private boolean tuchCacheable = true;
    private Priority priority;
    private Callback<Result> callback;
    private Parser<Result> parser;
    private boolean async;
    
    private StringBuilder tracker;
    
    private boolean isCancled;
    
    public static <Result> Request<Result> newRequest(RequestContext requestContext, Class<Result> resultClass) {
        return new Request<Result>(requestContext, resultClass);
    }
    
    protected Request(RequestContext requestContext, Class<Result> resultClass) {
        ArgsUtils.notNull(requestContext, "RequestContext");
        ArgsUtils.notNull(resultClass, "Result`s class");
        this.requestContext = requestContext;
        this.resultClass = resultClass;
        this.options = new ArrayList<Options>();
        this.cacheAble = true;
        this.headers = new ArrayList<BasicNameValuePair>();
    }
    
    protected final void onStart(String message) {
        if (callback != null) {
            callback.onStart(this, message);
        }
    }
    
    protected final void onRequest(String message, int progress) {
        if (callback != null) {
            callback.onRequest(this, message, progress);
        }
    }
    
    protected final void onFinish(Result result, String message, RequestException exception) {
        if (callback != null) {
            callback.onFinish(this, result, message, exception);
        }
    }
    
    public synchronized final void cancle() {
        this.isCancled = true;
    }
    
    public RequestContext getRequestContext() {
        return requestContext;
    }
    
    public Class<Result> getResultClass() {
        return resultClass;
    }
    
    public Request<Result> setMethod(Method method) {
        this.method = method;
        return this;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public Request<Result> setUrl(String url) {
        this.url = url;
        return this;
    }
    
    public String getUrl() {
        return url;
    }
    
    public Request<Result> setEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
        return this;
    }
    
    public HttpEntity getHttpEntity() {
        return httpEntity;
    }
    
    public Request<Result> addHeader(String name, String value) {
        this.headers.add(new BasicNameValuePair(name, value));
        return this;
    }
    
    public List<BasicNameValuePair> getHeader() {
        return headers;
    }
    
    public Request<Result> addOptions(Options option) {
        options.remove(option);
        options.add(option);
        return this;
    }
    
    public List<Options> getOptions() {
        return options;
    }
    
    public Request<Result> setCacheable(boolean able) {
        this.cacheAble = able;
        return this;
    }
    
    public boolean getCacheable() {
        return cacheAble;
    }
    
    public Request<Result> setTuchCacheable(boolean able) {
        this.tuchCacheable = able;
        return this;
    }
    
    public boolean getTuchCacheable() {
        return tuchCacheable;
    }
    
    public Request<Result> setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    public Request<Result> setParser(Parser<Result> parser) {
        this.parser = parser;
        return this;
    }
    
    public Parser<Result> getParser() {
        return parser;
    }
    
    public Callback<Result> getCallback() {
        return callback;
    }
    
    public final Request<Result> request(Callback<Result> callback) {
        return this.request(true, callback);
    }
    
    public void appendTracker(String message) {
        synchronized (tracker) {
            if (message != null) {
                tracker.append("-->");
                tracker.append(message);
            }
        }
    }
    
    public synchronized String getTracker() {
        synchronized (tracker) {
            if (tracker != null) {
                return tracker.toString();
            }
        }
        return null;
    }
    
    public boolean isAsync() {
        return async;
    }
    
    public final Request<Result> request(boolean isAsync, Callback<Result> callback) {
        this.tracker = new StringBuilder(getUrl());
        if (isAsync) {
            AsyncCallback<Result> asyncCallback = new AsyncCallback<Result>(callback);
            this.async = isAsync;
            this.callback = asyncCallback;
            
            appendTracker("push into thread pool");
            requestContext.commitRequest(this);
        } else {
            this.async = isAsync;
            this.callback = callback;
            run();
        }
        return this;
    }
    
    @Override
    public synchronized final void run() {
        if (isCancled) {
            appendTracker("has bean cancled");
            onFinish(null, getTracker(), new RequestException(RequestException.CODE_CANCLE, "make a exception to cancle"));
            return;
        }
        appendTracker("start work");
        onStart(getTracker());
        try {
            Result result = requestContext.doRequest(this);
            if (isCancled) {
                onFinish(null, getTracker(), new RequestException(RequestException.CODE_CANCLE, "make a exception to cancle"));
                return;
            }
            onFinish(result, getTracker(), null);
        } catch (RequestException e) {
            e.printStackTrace();
            onFinish(null, getTracker(), e);
        }
    }
    
    @Override
    public final int compareTo(Request<Result> other) {
        Priority left = this.getPriority();
        Priority right = other.getPriority();

        return right.ordinal() - left.ordinal();
    }
}
