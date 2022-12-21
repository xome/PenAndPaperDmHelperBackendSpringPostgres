package de.mayer.backendspringpostgres.graph.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphTest {

    @Test
    @DisplayName("""
            Given chapters is null,
            when a Graph is created,
            then an exception is thrown
            """)
    void chaptersCannotBeNull() {

        var c01 = new Chapter("c01", 0.0d);
        var c02 = new Chapter("c02", 1.0d);

        var exc = assertThrows(RuntimeException.class,
                () ->
                        new Graph(null,
                                Collections.singleton(new ChapterLink(c01, c02)
                                )
                        )
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("chapters cannot be null"));

    }

    @Test
    @DisplayName("""
            Given chapters is empty,
            when a Graph is created,
            then an exception is thrown
            """)
    void chaptersCannotBeEmpty() {
        var c01 = new Chapter("c01", 0.0d);
        var c02 = new Chapter("c02", 1.0d);
        var exc = assertThrows(RuntimeException.class,
                () ->
                        new Graph(Collections.emptySet(),
                                Collections.singleton(new ChapterLink(c01, c02)
                                )
                        )
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("chapters cannot be null or empty"));

    }

    @Test
    @DisplayName("""
            Given chapterLinks is null,
            when a Graph is created,
            then an exception is thrown
            """)
    void chapterLinksCannotBeNull() {
        var c01 = new Chapter("c01", 0.0d);
        var exc = assertThrows(RuntimeException.class,
                () ->
                        new Graph(Collections.singleton(c01),
                                null
                        )
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("chapterLinks cannot be null"));

    }


}