package de.mayer.backendspringpostgres.adventure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChapterLinkTest {

    @Test
    @DisplayName("""
            Given chapterNameTo is null,
            when a ChapterLink is created,
            then an exception is raised
            """)
    void chapterNameToCannotBeNull() {
        var exc = assertThrows(RuntimeException.class,
                () -> new ChapterLink(null)
        );

        assertThat(exc, is(instanceOf(IllegalModelAccessException.class)));
        assertThat(exc.getMessage(), containsStringIgnoringCase("chapterNameTo cannot be null or empty"));
    }

    @Test
    @DisplayName("""
            Given chapterNameTo is empty,
            when a ChapterLink is created,
            then an exception is raised
            """)
    void chapterNameToCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class,
                () -> new ChapterLink("")
        );

        assertThat(exc, is(instanceOf(IllegalModelAccessException.class)));
        assertThat(exc.getMessage(), containsStringIgnoringCase("chapterNameTo cannot be null or empty"));
    }

}