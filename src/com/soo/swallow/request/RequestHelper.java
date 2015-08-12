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

import java.io.IOException;

import org.apache.http.client.HttpClient;

import com.soo.swallow.cache.DiskCache;
import com.soo.swallow.cache.MemoryCache;
import com.soo.swallow.http.SooHttpClient;


/**
 * @author Soo
 */
public class RequestHelper {
    
    public static RequestWorker createRequestWorker(MemoryCache<String, byte[]> memoryCache,
            DiskCache<String, byte[]> diskCache, HttpClient httpClient) throws RequestException{
        return new RequestWorker(memoryCache, diskCache, httpClient);
    }
    
    public static HttpClient createHttpClient() throws RequestException {
        return new SooHttpClient();
    }
    
    public static MemoryCache<String, byte[]> createMemoryCache(int maxCacheSize, final Keyer keyer) throws RequestException {
        MemoryCache<String, byte[]> memoryCache = new MemoryCache<String, byte[]>(maxCacheSize) {
            
            final Keyer defaultKey = keyer;
            
            @Override
            public int sizeOf(byte[] value) {
                return value.length;
            }

            @Override
            protected Object getCacheKey(String key) {
                return this.defaultKey.getCacheKey(key);
            }
        };
        return memoryCache;
    }
    
    public static DiskCache<String, byte[]> createDiskCache(String cacheDir, int version, int maxCacheSize,
            long maxCycleLife, final Keyer keyer) throws RequestException {
        try {
            DiskCache<String, byte[]> diskCache = new DiskCache<String, byte[]>(cacheDir, version, 1,
                    maxCacheSize, maxCycleLife) {
                
                final Keyer defaultKey = keyer;

                        @Override
                        protected String getFileName(String key) {
                            return this.defaultKey.getCacheFileName(key);
                        }

                        @Override
                        protected byte[] toValue(byte[] data) {
                            return data;
                        }

                        @Override
                        protected byte[] toByteArray(byte[] value) {
                            return value;
                        }};
              return diskCache;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RequestException("Create disk cache exception:" + e.getMessage());
        }
    }
}
