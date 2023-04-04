package de.mayer.backendspringpostgres.adventure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class TextTest {

    @Test
    @DisplayName("""
            Given text is null,
            when a Text is created,
            then an exception is thrown
            """)
    void textCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () -> new Text(null));
        assertThat(exc, is(instanceOf(IllegalModelAccessException.class)));
    }

    @Test
    @DisplayName("""
            Given text is empty,
            when a Text is created,
            then an exception is thrown
            """)
    void textCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () -> new Text(""));
        assertThat(exc, is(instanceOf(IllegalModelAccessException.class)));
    }

}