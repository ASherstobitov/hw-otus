package ru.otus.cachehw;


import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> weakHashMap = new WeakHashMap<>();

    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        notify(key, value, "put");
        weakHashMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        var value = weakHashMap.remove(key);
        notify(key, value, "remove");
    }

    @Override
    public V get(K key) {

        var value = weakHashMap.get(key);
        notify(key, value, "get");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    @Override
    public List<V> getValues() {
        return this.weakHashMap.entrySet()
                .stream()
                .peek(e -> notify(e.getKey(), e.getValue(), "get"))
                .map(Map.Entry::getValue)
                .toList();
    }


    private void notify(K key, V value, String action) {
        listeners.forEach(e -> e.notify(key, value, action));
    }
}
