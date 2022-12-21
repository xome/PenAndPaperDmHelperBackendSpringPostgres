package de.mayer.backendspringpostgres.adventure.model;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;

class PictureTest {

    @Test
    @DisplayName("""
            Given base64 is null,
            when a Picture is created,
            then an exception is thrown
            """)
    void base64CannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture(null, "png", false));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("""
            Given base64 is empty,
            when a Picture is created,
            then an exception is thrown
            """)
    void base64CannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("", "png", false));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("""
            Given fileFormat is null,
            when a Picture is created,
            then an exception is thrown
            """)
    void fileFormatCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("base64",
                        null,
                        false));

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), Matchers.containsStringIgnoringCase("File format"));

    }

    @Test
    @DisplayName("""
            Given fileFormat is empty,
            when a Picture is created,
            then an exception is thrown
            """)
    void fileFormatCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("base64",
                        "",
                        false));

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), Matchers.containsStringIgnoringCase("File format"));

    }

    @Test
    @DisplayName("""
            Given isShareableWithGroup is null,
            when a Picture is created,
            then an exception is thrown
            """)
    void isShareableWithGroupCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("base64",
                        "png",
                        null));

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), Matchers.containsStringIgnoringCase("isShareableWithGroup"));

    }


}