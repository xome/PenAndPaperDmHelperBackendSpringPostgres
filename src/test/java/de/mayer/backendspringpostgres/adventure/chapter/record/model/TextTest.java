package de.mayer.backendspringpostgres.adventure.chapter.record.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    @DisplayName("When text is null, then an exception is thrown")
    void textCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () -> new Text("Adventure", "chapter", 0, null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("When text is empty, then an exception is thrown")
    void textCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () -> new Text("Adventure", "Chapter", 0, ""));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

}