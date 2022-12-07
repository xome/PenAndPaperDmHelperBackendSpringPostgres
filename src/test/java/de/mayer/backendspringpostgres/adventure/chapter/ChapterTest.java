package de.mayer.backendspringpostgres.adventure.chapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class ChapterTest {

    @Test
    @DisplayName("When name is null, then an exception is thrown")
    void nameCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Chapter("Adventure",
                        null,
                        null,
                        null,
                        null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("When name is empty, then an exception is thrown")
    void nameCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class, () ->
                new Chapter("Adventure",
                        "",
                        null,
                        null,
                        null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("When adventure is null, then an exception is thrown")
    void adventureCannotBeNull(){
        var exc = assertThrows(RuntimeException.class, () ->
                new Chapter(null,
                        "Chapter 1",
                        null,
                        null,
                        null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }
    @Test
    @DisplayName("When adventure is empty, then an exception is thrown")
    void adventureCannotBeEmpty(){
        var exc = assertThrows(RuntimeException.class, () ->
                new Chapter("",
                        "Chapter 1",
                        null,
                        null,
                        null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }



}