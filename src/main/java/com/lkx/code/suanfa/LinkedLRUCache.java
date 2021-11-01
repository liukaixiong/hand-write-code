package com.lkx.code.suanfa;

import org.checkerframework.checker.units.qual.K;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedLRUCache<K, V> extends LinkedHashMap<K, V> {

    private int cacheLimit;

    public LinkedLRUCache(int cacheLimit) {
        super(cacheLimit,0.75f,true);
        this.cacheLimit = cacheLimit;
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> map) {
        return size() > cacheLimit;
    }

    public static void main(String[] args) {
        LinkedLRUCache lruCache = new LinkedLRUCache(5);
        lruCache.put("001", "用户1信息");
        lruCache.put("002", "用户2信息");
        lruCache.put("003", "用户3信息");
        lruCache.put("004", "用户4信息");
        lruCache.put("005", "用户5信息");
        Object s = lruCache.get("002");
        lruCache.put("004", "用户2信息更新");
        for (int i = 10; i < 100; i++) {
            lruCache.put("00" + i, "用户" + i + "信息更新");
        }
    }

}
