package de.mayer.backendspringpostgres.graph.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class Cache {
    private HashMap<Class<?>, HashMap<String, Object>> cache;
    private HashSet<String> invalidatedKeys;

    public <T> void put(String key, T object) {
        if (cache == null) {
            cache = new HashMap<>();
        }

        if (!cache.containsKey(object.getClass())) {
            cache.put(object.getClass(), new HashMap<>());
        }

        cache.get(object.getClass()).put(key, object);
    }

    public <T> Optional<T> get(String key, Class<T> aClass) {
        if (invalidatedKeys != null
                && invalidatedKeys.contains(key)) {
            return Optional.empty();
        }
        return Optional.of(aClass.cast(cache.get(aClass).get(key)));
    }

    public void invalidate(String key) {
        if (invalidatedKeys == null) {
            invalidatedKeys = new HashSet<>();
        }
        invalidatedKeys.add(key);
    }
}
