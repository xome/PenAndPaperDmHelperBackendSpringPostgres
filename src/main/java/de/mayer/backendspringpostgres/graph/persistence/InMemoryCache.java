package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.Cache;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
public class InMemoryCache implements Cache {
    private ConcurrentHashMap<Class<?>, HashMap<String, Object>> cache;
    private ConcurrentHashMap<Class<?>, HashSet<String>> invalidatedKeys;

    public void put(String key, Object object) {
        if (cache == null) {
            cache = new ConcurrentHashMap<>();
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

    public void invalidate(String key, Class<?> aClass) {
        if (invalidatedKeys == null) {
            invalidatedKeys = new ConcurrentHashMap<>();
        }
        invalidatedKeys.put(aClass, new HashSet<>(Collections.singleton(key)));
    }

    @Override
    public void invalidateAll() {
        if (cache != null) {
            cache.clear();
        }
        if (invalidatedKeys != null) {
            invalidatedKeys.clear();
        }
    }
}
