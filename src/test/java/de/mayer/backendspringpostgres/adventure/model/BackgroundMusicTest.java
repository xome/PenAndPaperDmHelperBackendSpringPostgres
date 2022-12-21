package de.mayer.backendspringpostgres.adventure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;

class BackgroundMusicTest {

    @Test
    @DisplayName("""
            Given name is null,
            when a BackgroundMusic record is created,
            then an exception is raised
            """)
    void nameCannotBeNull() {
        var exc = assertThrows(RuntimeException.class,
                () -> new BackgroundMusic(null, new byte[1])
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("name cannot be null"));
    }

    @Test
    @DisplayName("""
            Given name is empty,
            when a BackgroundMusic record is created,
            then an exception is raised
            """)
    void nameCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class,
                () -> new BackgroundMusic("", new byte[1])
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("name cannot be null or empty"));
    }

    @Test
    @DisplayName("""
            Given data is null,
            when a BackgroundMusic record is created,
            then an exception is raised
            """)
    void dataCannotBeNull() {
        var exc = assertThrows(RuntimeException.class,
                () -> new BackgroundMusic("Music", null)
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("data cannot be null"));
    }

    @Test
    @DisplayName("""
            Given data is a byte array of size 0,
            when a BackgroundMusic record is created,
            then an exception is raised
            """)
    void dataCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class,
                () -> new BackgroundMusic("Music", new byte[0])
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("data cannot be null or empty"));
    }


}