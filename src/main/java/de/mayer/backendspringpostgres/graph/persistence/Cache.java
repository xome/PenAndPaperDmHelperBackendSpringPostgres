package de.mayer.backendspringpostgres.graph.persistence;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class Cache {
    private HashMap<Class<?>, HashMap<String, Object>> cache;
    private HashMap<Class<?>, HashSet<String>> invalidatedKeys;

    public <T> void put(String key, T object) {
        if (cache == null) {
            cache = new HashMap<>();
        }

        if (!cache.containsKey(object.getClass())) {
            cache.put(object.getClass(), new HashMap<>());
        }

        cache.get(object.getClass()).put(key, object);
        if (invalidatedKeys != null
        && invalidatedKeys.containsKey(object.getClass())){
            invalidatedKeys.get(object.getClass()).remove(key);
        }
    }

    public <T> Optional<T> get(String key, Class<T> aClass) {
        if (invalidatedKeys != null
                && invalidatedKeys.containsKey(aClass)
                && invalidatedKeys.get(aClass).contains(key)) {
            return Optional.empty();
        }

        if (cache == null)
            return Optional.empty();

        if (!cache.containsKey(aClass)){
            return Optional.empty();
        }

        return Optional.ofNullable(aClass.cast(
                cache.get(aClass).getOrDefault(key, null)));
    }

    public <T> void invalidate(String key, Class<T> aClass) {
        if (invalidatedKeys == null) {
            invalidatedKeys = new HashMap<>();
        }
        invalidatedKeys.put(aClass, new HashSet<>(Collections.singleton(key)));
    }
}
