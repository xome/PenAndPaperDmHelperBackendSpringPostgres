package de.mayer.backendspringpostgres.graph.domainservice;

import java.util.Optional;

public interface Cache {

    void put(String key, Object object);
    <T> Optional<T> get(String key, Class<T> aClass);
    void invalidate(String key, Class<?> aClass);

    void invalidateAll();
}
