package de.mayer.backendspringpostgres.adventure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdventureTest {

    @Test
    @DisplayName("""
            Given name is null,
            when an Adventure is created,
            then exception is thrown
            """)
    void nameCannotBeNullable() {
        Throwable exception = assertThrows(RuntimeException.class, () -> new Adventure(null, null));
        assertThat(exception, is(instanceOf(IllegalModelAccessException.class)));
    }


    @Test
    @DisplayName("""
            Given name is empty,
            when an Adventure is created,
            then exception is thrown
            """)
    void nameCannotBeEmpty() {
        Throwable exc = assertThrows(RuntimeException.class, () -> new Adventure("", null));
        assertThat(exc, is(instanceOf(IllegalModelAccessException.class)));
    }


}