package de.mayer.backendspringpostgres.adventure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChapterLinkRecordTest {

    @Test
    @DisplayName("Given chapterNameTo is null, when a record is created, then an exception is raised")
    void chapterNameToCannotBeNull() {
        var exc = assertThrows(RuntimeException.class,
                () -> new ChapterLinkRecord(null)
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("chapterNameTo cannot be null or empty"));
    }

    @Test
    @DisplayName("Given chapterNameTo is empty, when a record is created, then an exception is raised")
    void chapterNameToCannotBeEmpty() {
        var exc = assertThrows(RuntimeException.class,
                () -> new ChapterLinkRecord("")
        );

        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("chapterNameTo cannot be null or empty"));
    }

}