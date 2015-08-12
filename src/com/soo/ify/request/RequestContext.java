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

import org.apache.http.client.HttpClient;

import android.content.Context;
import android.os.Looper;

import com.soo.ify.cache.DiskCache;
import com.soo.ify.cache.MemoryCache;
import com.soo.ify.util.ArgsUtils;
import com.soo.ify.util.DevUtils;
import com.soo.ify.util.TextUtils;

/**
 * the context of request
 * @author Soo
 *
 */
public final class RequestContext {
    
    private Builder builder;
    private RequestWorker requestWorker;
    private RequestExecutor requestExecutor;
    
    private RequestContext(Builder builder) throws RequestException {
        this.builder = builder;
        initRequestWroker();
        initRequestExecutor();
    }
    
    private void initRequestWroker() throws RequestException {
        MemoryCache<String, byte[]> memoryCache = RequestHelper.createMemoryCache(builder.maxMemoryCacheSize, builder.keyer);
        DiskCache<String, byte[]> diskCache = RequestHelper.createDiskCache(builder.diskCacheDir, builder.version,
                builder.maxDiskCacheSize, builder.maxDiskCacheCycleLife, builder.keyer);
        HttpClient httpClient = RequestHelper.createHttpClient();
        this.requestWorker = RequestHelper.createRequestWorker(memoryCache, diskCache, httpClient);
    }
    
    private void initRequestExecutor() throws RequestException {
        this.requestExecutor = new RequestExecutor(builder.poolCoreSize, 256);
    }
    
    protected void commitRequest(Request<?> request) {
        requestExecutor.execute(request);
    }
    
    protected <Result> Result doRequest(Request<Result> request) throws RequestException {
        return requestWorker.doRequest(request);
    }
    
    public void stop() {
//        TODO
    }
    
    public RequestWorker getRequestWorker() {
        return requestWorker;
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }
    
    public Context getContext() {
        return builder.context;
    }
    
    public Looper getLooper() {
        return builder.context.getMainLooper();
    }
    
    public String getName() {
        return builder.name;
    }
    
    public static class Builder implements Cloneable{
        
        private static final String DEFAULT_NAME = "Swallow";
        private static final int DEFAULT_MEMORY_COUNT = 20;
        private static final int DEFAULT_MEMORY_SIZE = 1024 * 1024 * 10;//(10m)
        private static final int DEFAULT_DISK_COUNT = 500;
        private static final int DEFAULT_DISK_SIZE = 1024 * 1024 * 50;//(50m)
        private static final int DEFAULT_DISK_LIFE = 1000 * 60 * 60 * 24 * 7;//(7 days)
        private static final int DEFAULT_CORE_SIZE = 5;
        
        private int version;
        private Keyer keyer;
        
        private Context context;
        private String name;
        private int maxMemoryCacheSize;
        private int maxMemoryCacheCount;
        private int maxDiskCacheSize;
        private int maxDiskCacheCount;
        private long maxDiskCacheCycleLife;
        private String diskCacheDir;
        private int poolCoreSize;
        
        public Builder(Context context) {
            this.context = context;
        }
        
        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }
        
        public Builder setName(String name) {
            this.name = name;
            return this;
        }
        
        public Builder setMaxMemoryCacheSize(int size) {
            this.maxMemoryCacheSize = size;
            return this;
        }
        
        public Builder setMaxMemoryCacheCount(int count) {
            this.maxMemoryCacheCount = count;
            return this;
        }
        
        public Builder setDiskCacheDir(String diskCacheDir) {
            this.diskCacheDir = diskCacheDir;
            return this;
        }
        
        public Builder setMaxDiskCacheSize(int size) {
            this.maxDiskCacheSize = size;
            return this;
        }
        
        public Builder setMaxDiskCacheCount(int count) {
            this.maxDiskCacheCount = count;
            return this;
        }
        
        public Builder setMaxDiskCacheCycleLife(long cycleLife) {
            this.maxDiskCacheCycleLife = cycleLife;
            return this;
        }
        
        public Builder setCorePoolSize(int size) {
            this.poolCoreSize = size;
            return this;
        }
        
        public Builder setKeyer(Keyer keyer) {
            this.keyer = keyer;
            return this;
        }
        
        public RequestContext build() throws RequestException {
            try {
                Builder newBuilder = (Builder) this.clone();
                checkAddFixParams(newBuilder);
                return new RequestContext(newBuilder);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                throw new RequestException(e.getMessage());
            }
        }
        
        private static void checkAddFixParams(Builder builder) {
            ArgsUtils.notNull(builder.context, "Context");
            builder.keyer = ArgsUtils.returnIfNull(builder.keyer, new DefaultKeyer());
            
            TextUtils.fixIfEmpty(builder.name, DEFAULT_NAME);
            
            if (DevUtils.isAvaliableSD()) {
                builder.diskCacheDir = ArgsUtils.returnIfNull(builder.diskCacheDir, 
                        builder.context.getExternalCacheDir().getAbsolutePath() + "/" + builder.name);
            } else {
                builder.diskCacheDir = ArgsUtils.returnIfNull(builder.diskCacheDir, 
                        builder.context.getCacheDir().getAbsolutePath() + "/" + builder.name);
            }
            
            builder.maxMemoryCacheCount = Math.max(DEFAULT_MEMORY_COUNT, builder.maxMemoryCacheCount);
            builder.maxMemoryCacheSize = Math.max(DEFAULT_MEMORY_SIZE, builder.maxMemoryCacheSize);
            builder.maxDiskCacheCount = Math.max(DEFAULT_DISK_COUNT, builder.maxDiskCacheCount);
            builder.maxDiskCacheSize = Math.max(DEFAULT_DISK_SIZE, builder.maxDiskCacheSize);
            builder.maxDiskCacheCycleLife = Math.max(DEFAULT_DISK_LIFE, builder.maxDiskCacheCycleLife);
            builder.poolCoreSize = Math.max(builder.poolCoreSize, DEFAULT_CORE_SIZE);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            Builder instance = null;
            instance = (Builder) super.clone();
            return instance;
        }
    }

}
