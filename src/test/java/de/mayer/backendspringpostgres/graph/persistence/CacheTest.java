package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Graph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
class CacheTest {

    @Test
    @DisplayName("""
            Given the cache is empty,
            when a Graph is added,
            it can be retrieved
            """)
    void emptyAddOne(){
        var cache = new Cache();
        var chapter01 = new Chapter("Chapter 01", 2.0d);
        var graph = new Graph(Set.of(chapter01), Collections.emptySet());

        cache.put("Adventure", graph);
        var optional = cache.get("Adventure", Graph.class);

        assertThat(optional.isPresent(), is(true));
        assertThat(optional.get(), is(graph));

    }

    @Test
    @DisplayName("""
            Given a Graph is added,
            when the key is invalidated,
            the Cache returns an empty Optional
            """)
    void invalidatedKeyReturnsNull(){
        var cache = new Cache();
        var chapter = new Chapter("Chapter01", 1.0d);
        var graph = new Graph(Set.of(chapter), Collections.emptySet());
        var key = "Adventure";
        cache.put(key, graph);

        cache.invalidate(key);
        assertThat(cache.get(key, Graph.class).isEmpty(), is(true));

    }



}