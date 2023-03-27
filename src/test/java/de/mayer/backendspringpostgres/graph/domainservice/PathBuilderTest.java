package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Path;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PathBuilderTest {

    @Test
    @DisplayName("""
            Given no chapter was added,
            when build is called,
            then a new Path with no Chapters is returned.
            """)
    void noChapterBuild() {
        assertThat(new PathBuilder().build(), Is.is(new Path(new LinkedList<>(), 0)));
    }

    @Test
    @DisplayName("""
            Given initiateFrom gets a Path,
            when build is called,
            then the Path from initiate is returned.
            """)
    void buildFromInitiate() {
        var path = new Path(new LinkedList<>(List.of(new Chapter("c01", 1))),
                1);
        assertThat(new PathBuilder()
                .initiateFrom(path)
                .build(),
                is(path));
    }

    @Test
    @DisplayName("""
            Given a new PathBuilder,
            when a chapter is added,
            then build returns a path consisting only of the chapter
            """)
    void addChapterToEmptyPath(){
        var chapter = new Chapter("c01", 1);

        var pathExpected = new Path(new LinkedList<>(List.of(chapter)),
                1);

        assertThat(new PathBuilder()
                .addChapter(chapter)
                .build(),
                is(pathExpected));
    }


}