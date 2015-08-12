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
package com.soo.swallow.cache;

/**
 * @author Soo
 *
 */
public abstract class MemoryCache<K, V> implements Cache<K, V> {

    private static final int DEFAULT_MAX_CACHE_SIZE = 1024 * 1024 * 5;

    private LruCache<Object, V> lruCache;

    private int maxSize;

    public MemoryCache() {
        this(DEFAULT_MAX_CACHE_SIZE);
    }

    public MemoryCache(int maxSize) {
        this.maxSize = maxSize;
        lruCache = new LruCache<Object, V>(maxSize) {

            @Override
            protected int sizeOf(Object objKey, V value) {
                return MemoryCache.this.sizeOf(value);
            }
        };
    }

    protected abstract Object getCacheKey(K key);

    @Override
    public V getValue(K key) {
        Object obj = getCacheKey(key);
        if (obj == null) {
            return null;
        }
        return lruCache.get(obj);
    }

    @Override
    public V putValue(K key, V value) {
        Object obj = getCacheKey(key);
        if (obj == null) {
            return null;
        }
        return lruCache.put(obj, value);
    }

    @Override
    public V remove(K key) {
        Object obj = getCacheKey(key);
        if (obj == null) {
            return null;
        }
        return lruCache.remove(obj);
    }

    @Override
    public void clear() {
        lruCache.evictAll();
    }

    @Override
    public long size() {
        return lruCache.size();
    }

    public void setMaxSize(int maxSize) {
        lruCache.setMaxSize(maxSize);
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public void flush() {
//        empty
    }

    @Override
    public void close() {
//      empty
    }
    
    
}
