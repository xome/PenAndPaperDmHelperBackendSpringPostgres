package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.persistence.InMemoryCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
class InMemoryCacheTest {

    @Test
    @DisplayName("""
            Given the inMemoryCache is empty,
            when a Graph is added,
            it can be retrieved
            """)
    void emptyAddOne(){
        var cache = new InMemoryCache();
        var chapter01 = new Chapter("Chapter 01", 2.0d);
        var graph = new Graph(Set.of(chapter01), Collections.emptySet());

        cache.put("Adventure", graph);

        var optional = cache.get("Adventure", Graph.class);

        assertThat("Object can be retrieved", optional.isPresent(), is(true));
        assertThat("Cached object equals expected Graph", optional.get(), is(graph));

    }

    @Test
    @DisplayName("""
            Given a Graph is added,
            when the key is invalidated,
            the Cache returns an empty Optional
            """)
    void invalidatedKeyReturnsNull(){
        var cache = new InMemoryCache();
        var chapter = new Chapter("Chapter01", 1.0d);
        var graph = new Graph(Set.of(chapter), Collections.emptySet());
        var key = "Adventure";
        cache.put(key, graph);

        cache.invalidate(key, Graph.class);
        assertThat("Object cannot be retrieved", cache.get(key, Graph.class).isEmpty(), is(true));

    }

    @Test
    @DisplayName("""
            Given a key is invalidated,
            when it is put again,
            it can be retrieved
            """)
    void validateKey(){
        var cache = new InMemoryCache();
        var chapter = new Chapter("Chapter01", 1.0d);
        var graph = new Graph(Set.of(chapter), Collections.emptySet());
        var key = "Adventure";
        cache.invalidate(key, Graph.class);

        cache.put(key, graph);
        var optional = cache.get("Adventure", Graph.class);

        assertThat("Cached object can be retrieved", optional.isPresent(), is(true));
        assertThat("Cached object equals expected Graph", optional.get(), is(graph));
    }

    @Test
    @DisplayName("""
            Given no key is neither put nor invalidated,
            when the key is requested with get,
            an empty Optional is returned
            """)
    void cacheNotInitialised(){
        assertThat("No object can be retrieved from an empty Cache.",
                new InMemoryCache().get("any key", Graph.class).isEmpty(), is(true));
    }

    @Test
    @DisplayName("""
            Given no key was ever put for the requested Class,
            when the key is requested with get,
            an empty Optional is returned
            """)
    void classCacheNotInitialised(){
        var cache = new InMemoryCache();
        cache.put("a key", new Chapter("Chapter 01", 2.0d));

        assertThat("There is no object retrieved for the given Key and Class",
                cache.get("a key", Graph.class).isEmpty(), is(true));
    }

    @Test
    @DisplayName("""
            Given the requested key was never put,
            when the key is requested with get,
            an empty Optional is returned
            """)
    void unknownKey(){
        var cache = new InMemoryCache();
        cache.put("a key", new Chapter("Chapter 01", 1d));

        assertThat("No object can be retrieved for the given key",
                cache.get("another key", Chapter.class).isEmpty(), is(true));
    }



}