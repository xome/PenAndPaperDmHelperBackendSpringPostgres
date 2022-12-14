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
    @DisplayName("When base64 is null, then an exception is thrown")
    void base64CannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture(null, "png", false));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("When base64 is empty, then an exception is thrown")
    void base64CannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("", "png", false));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("When fileFormat is null, then an exception is thrown")
    void fileFormatCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("base64",
                        null,
                        false));

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), Matchers.containsStringIgnoringCase("File format"));

    }

    @Test
    @DisplayName("When fileFormat is empty, then an exception is thrown")
    void fileFormatCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("base64",
                        "",
                        false));

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), Matchers.containsStringIgnoringCase("File format"));

    }

    @Test
    @DisplayName("When isShareableWithGroup is null, then an exception is thrown")
    void isShareableWithGroupCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("base64",
                        "png",
                        null));

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), Matchers.containsStringIgnoringCase("isShareableWithGroup"));

    }


}