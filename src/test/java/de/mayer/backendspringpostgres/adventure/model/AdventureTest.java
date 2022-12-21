package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.adventure.model.Adventure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

class AdventureTest {

    @Test
    @DisplayName("""
            Given name is null,
            when an Adventure is created,
            then exception is thrown
            """)
    void nameCannotBeNullable() {
        Throwable exception = assertThrows(RuntimeException.class, () -> new Adventure(null, null));
        assertThat(exception.getCause(), is(instanceOf(IllegalAccessException.class)));
    }


    @Test
    @DisplayName("""
            Given name is empty,
            when an Adventure is created,
            then exception is thrown
            """)
    void nameCannotBeEmpty() {
        Throwable exc = assertThrows(RuntimeException.class, () -> new Adventure("", null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }


}