package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.adventure.model.Chapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class ChapterTest {

    @Test
    @DisplayName("""
            Given name is null,
            when a Chapter is created,
            then an exception is thrown
            """)
    void nameCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Chapter(null,
                        null,
                        null,
                        null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("""
            Given name is empty,
            when a Chapter is created,
            then an exception is thrown
            """)
    void nameCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Chapter("",
                        null,
                        null,
                        null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }



}