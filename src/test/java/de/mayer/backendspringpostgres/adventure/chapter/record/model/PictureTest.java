package de.mayer.backendspringpostgres.adventure.chapter.record.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
class PictureTest {

    @Test
    @DisplayName("When base64 is null, then an exception is thrown")
    void base64CannotBeNull(){
        var exc = assertThrows(RuntimeException.class, () ->
                new Picture("adventure", "chapter",
                        0, null, "png", false));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }


}