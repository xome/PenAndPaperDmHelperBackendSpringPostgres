package de.mayer.backendspringpostgres.adventure.chapter.record.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
class RecordInAChapterTest {

    @Test
    @DisplayName("When adventure is null, then an exception is thrown")
    void adventureCannotBeNull(){
        var exc = assertThrows(RuntimeException.class, () -> {
            new RecordInAChapter(null,
                    "chapter",
                    0
            ){};
        });
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }
    @Test
    @DisplayName("When adventure is empty, then an exception is thrown")
    void adventureCannotBeEmpty(){
        var exc = assertThrows(RuntimeException.class, () -> {
            new RecordInAChapter("",
                    "chapter",
                    0
            ){};
        });
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }
    @Test
    @DisplayName("When chapter is null, then an exception is thrown")
    void chapterCannotBeNull(){
        var exc = assertThrows(RuntimeException.class, () -> {
            new RecordInAChapter("adventure",
                    null,
                    0
            ){};
        });
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

    @Test
    @DisplayName("When chapter is empty, then an exception is thrown")
    void chapterCannotBeEmpty(){
        var exc = assertThrows(RuntimeException.class, () -> {
            new RecordInAChapter("adventure",
                    "",
                    0
            ){};
        });
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }
    @Test
    @DisplayName("When index is null, then an exception is thrown")
    void indexCannotBeNull(){
        var exc = assertThrows(RuntimeException.class, () -> {
            new RecordInAChapter("adventure",
                    "chapter",
                    null
            ){};
        });
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }
    @Test
    @DisplayName("When index is negative, then an exception is thrown")
    void indexCannotBeNegative(){
        var exc = assertThrows(RuntimeException.class, () -> {
            new RecordInAChapter("adventure",
                    "chapter",
                    -1
            ){};
        });
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
    }

}