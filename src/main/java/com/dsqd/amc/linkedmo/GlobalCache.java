package com.dsqd.amc.linkedmo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class GlobalCache {
    private static final Logger logger = LoggerFactory.getLogger(GlobalCache.class);

    private static GlobalCache instance;
    private ConcurrentHashMap<String, Object> cache;

    // private constructor to prevent instantiation
    private GlobalCache() {
        cache = new ConcurrentHashMap<>();
    }

    // public method to get the singleton instance
    public static GlobalCache getInstance() {
        if (instance == null) {
            synchronized (GlobalCache.class) {
                if (instance == null) {
                    instance = new GlobalCache();
                }
            }
        }
        return instance;
    }

    // method to put a value into the cache
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    // method to get a value from the cache
    public Object get(String key) {
        return cache.get(key);
    }

    // method to get a value from the cache as a String
    public String getAsString(String key) {
        Object value = cache.get(key);
        return value != null ? value.toString() : null;
    }

    // method to get a value from the cache as an Integer
    public Integer getAsInteger(String key) {
        Object value = cache.get(key);
        return value instanceof Integer ? (Integer) value : null;
    }

    // method to remove a value from the cache
    public void remove(String key) {
        cache.remove(key);
    }

    // method to clear the entire cache
    public void clear() {
        cache.clear();
    }

    // method to initialize the cache with properties
    public void initWithProperties(Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            cache.put(key, properties.getProperty(key));
        }
    }

    // method to log the current contents of the cache
    public void logCacheContents() {
    	AtomicInteger index = new AtomicInteger(1);
        if (cache.isEmpty()) {
            logger.info("GlobalCache is empty.");
        } else {
            cache.forEach((key, value) -> logger.info("[{}]INITIALIZE - {}:{}", index.getAndIncrement(), key, value));
        }
    }
}


