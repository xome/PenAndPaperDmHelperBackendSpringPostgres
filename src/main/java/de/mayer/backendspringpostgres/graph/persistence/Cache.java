package de.mayer.backendspringpostgres.graph.persistence;

import java.util.HashMap;

public class Cache {
    private HashMap<Class<?>, HashMap<String, Object>> cache;

    public void put(String key, Object object) {
        if (cache == null) {
            cache = new HashMap<>();
        }

        if (!cache.containsKey(object.getClass())) {
            cache.put(object.getClass(), new HashMap<>());
        }

        cache.get(object.getClass()).put(key, object);
    }

    public Object get(String key, Class<?> aClass) {
        return cache.get(aClass).get(key);
    }

    public void invalidate(String key) {
        cache
                .entrySet()
                .parallelStream()
                .filter((entry) -> entry
                        .getValue()
                        .keySet()
                        .stream()
                        .anyMatch(cacheKey -> cacheKey.equals(key)))
                .forEach(entry ->
                        cache
                                .get(entry.getKey())
                                .put(key, null));
    }
}
