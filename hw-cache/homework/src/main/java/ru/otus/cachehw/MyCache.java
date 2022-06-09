package ru.otus.cachehw;


import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private Map<K, V> weakHashMap = new WeakHashMap<>();

    private HwListener<K, V> listener;

    @Override
    public void put(K key, V value) {
        if (listener != null) {
            listener.notify(key, value, "put");
        }
        weakHashMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        weakHashMap.remove(key);
    }

    @Override
    public V get(K key) {
        return weakHashMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.listener = listener;
    }
}
