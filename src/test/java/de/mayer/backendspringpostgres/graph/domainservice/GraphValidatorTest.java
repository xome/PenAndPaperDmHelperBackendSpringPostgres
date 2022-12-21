package de.mayer.backendspringpostgres.graph.service;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.model.InvalidGraphException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.matchesRegex;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class GraphValidatorTest {


    @Test
    @DisplayName("Given there are two chapters and the links are forming a circle, " +
            "when a Graph is created, " +
            "then an exception is thrown")
    void noStartpointsAndEndings() {
        var c01 = new Chapter("c01", 0.0d);
        var c02 = new Chapter("c02", 1.0d);
        var chapters = new HashSet<>(Arrays.asList(c01, c02));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c01)));

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphValidator().validateGraph(new Graph(chapters, links)));

        assertThat(exc.getMessage(),
                is("Graph is invalid. There are only Paths with circles."));

    }

    @Test
    @DisplayName("Given there are three nodes and one Path is a circle," +
            "when a Graph is created," +
            "then an exception is thrown")
    void oneCirclePath() {
        var c01 = new Chapter("c01", 0.0d);
        var c02 = new Chapter("c02", 1.0d);
        var c03 = new Chapter("c03", 1.0d);
        
        var chapters = new HashSet<>(Arrays.asList(c01, c02, c03));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c03),
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c03),
                new ChapterLink(c03, c02)));

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphValidator().validateGraph(new Graph(chapters, links)));

        assertThat(exc.getMessage(),
                is("Graph is invalid. There are only Paths with circles."));
    }

    @Test
    @DisplayName("Given there are three nodes and one Path is a circle," +
            "when a Graph is created," +
            "then an exception is thrown")
    void oneCirclePathAndOneNormalPath() {
        var c01 = new Chapter("c01", 1.0d);
        var c02 = new Chapter("c02", 1.0d);
        var c03 = new Chapter("c03", 1.0d);
        var c04 = new Chapter("c04", 1.0d);
        var chapters = new HashSet<>(Arrays.asList(c01, c02, c03, c04));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c03),
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c03),
                new ChapterLink(c03, c02),
                new ChapterLink(c01, c04)));

        var exc = assertThrows(RuntimeException.class,
                () -> new GraphValidator().validateGraph(new Graph(chapters, links)));

        assertThat(exc.getCause(), is(instanceOf(InvalidGraphException.class)));
        assertThat(exc.getCause().getMessage(),
                matchesRegex("Graph is invalid\\. Circle detected on Path ((c01 -> c02 -> c03 -> c02 \\(4([,.])00 Minutes\\))|(c01 -> c03 -> c02 -> c03 \\(4[,.]00 Minutes\\)))\\."));
    }


}