package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.*;
import de.mayer.backendspringpostgres.graph.persistence.ChapterLinkJpa;
import de.mayer.backendspringpostgres.graph.persistence.InMemoryChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.persistence.InMemoryChapterDomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphServiceTest {


    @Test
    @DisplayName("""
            Given there are two chapters
                and the links are forming a circle,
            when Paths are generated,
            then an exception is thrown
            """)
    void noStartpointsAndEndings() {
        var c01 = new Chapter("c01", 0.0d);
        var c02 = new Chapter("c02", 1.0d);
        var chapters = new HashSet<>(Arrays.asList(c01, c02));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c01)));

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphService(new InMemoryChapterDomainRepository(), new InMemoryChapterLinkDomainRepository())
                        .generatePaths(new Graph(chapters, links)));

        assertThat(exc.getMessage(),
                is("Graph is invalid. There are only Paths with circles."));

    }

    @Test
    @DisplayName("""
            Given there are three nodes and one Path is a circle,
            when Paths are generated,
            then an exception is thrown
            """)
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
                () -> new GraphService(new InMemoryChapterDomainRepository(), new InMemoryChapterLinkDomainRepository())
                        .generatePaths(new Graph(chapters, links)));

        assertThat(exc.getMessage(),
                is("Graph is invalid. There are only Paths with circles."));
    }

    @Test
    @DisplayName("""
            Given there are four nodes
               and one Path is no circle
               and one Path is a circle,
            when Paths are generated,
            then an exception is thrown
            """)
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

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphService(new InMemoryChapterDomainRepository(), new InMemoryChapterLinkDomainRepository())
                        .generatePaths(new Graph(chapters, links)));

        assertThat(exc.getMessage(),
                is("There are problematic Graphs."));


        var possiblePath1 = new PathBuilder(c01).addChapter(c02).addChapter(c03).addChapter(c02).build();
        var possiblePath2 = new PathBuilder(c01).addChapter(c03).addChapter(c02).addChapter(c03).build();

        assertThat(exc.getProblematicPaths(), hasItems(anyOf(is(possiblePath1), is(possiblePath2))));
        assertThat(exc.getProblematicPaths(), hasSize(1));


    }

    @Test
    @DisplayName("""
            Given there are multiple starting points
                and a single ending point
                and no circles,
            when Paths are generated,
            then all possible Paths are returned
            """)
    void multipleStartingPointsJustOneEndingNoCircles() throws InvalidGraphException {
        var c01 = new Chapter("c01", 1.0d);
        var c02 = new Chapter("c02", 1.0d);
        var c03 = new Chapter("c03", 1.0d);
        var c04 = new Chapter("c04", 1.0d);
        var chapters = Set.of(c01, c02, c03, c04);

        var link01to02 = new ChapterLink(c01, c02);
        var link02to04 = new ChapterLink(c02, c04);
        var link03to04 = new ChapterLink(c03, c04);
        var links = Set.of(link01to02, link02to04, link03to04);

        var pathLong = new PathBuilder(c01)
                .addChapter(c02)
                .addChapter(c04)
                .build();
        var pathShort = new PathBuilder(c03)
                .addChapter(c04)
                .build();

        var paths = new GraphService(new InMemoryChapterDomainRepository(), new InMemoryChapterLinkDomainRepository())
                .generatePaths(new Graph(chapters, links));

        assertThat(paths, containsInAnyOrder(pathLong, pathShort));
        assertThat(paths, hasSize(2));

    }

    @Test
    @DisplayName("""
            Given multiple chapters which are starting and ending points,
            when Paths are generated,
            all Paths are single-chapter-paths
            """)
    void singleChaptersAdventures() throws InvalidGraphException {
        var c01 = new Chapter("c01", 1.0d);
        var c02 = new Chapter("c02", 1.0d);
        var c03 = new Chapter("c03", 1.0d);
        var c04 = new Chapter("c04", 1.0d);
        var chapters = Set.of(c01, c02, c03, c04);

        var path01 = new PathBuilder(c01).build();
        var path02 = new PathBuilder(c02).build();
        var path03 = new PathBuilder(c03).build();
        var path04 = new PathBuilder(c04).build();

        var paths = new GraphService(new InMemoryChapterDomainRepository(), new InMemoryChapterLinkDomainRepository())
                .generatePaths(new Graph(chapters, Collections.emptySet()));

        assertThat(paths, containsInAnyOrder(path01, path02, path03, path04));
        assertThat(paths, hasSize(4));

    }

    @Test
    @DisplayName("""
            Given there are multiple ending points
                and a single starting point
                and no circles,
            when Paths are generated,
            then all possible Paths are returned
            """)
    void multipleEndingPointsJustOneStartNoCircles() throws InvalidGraphException {
        var c01 = new Chapter("c01", 1.0d);
        var c02 = new Chapter("c02", 1.0d);
        var c03 = new Chapter("c03", 1.0d);
        var c04 = new Chapter("c04", 1.0d);
        var chapters = Set.of(c01, c02, c03, c04);

        var link01to02 = new ChapterLink(c01, c02);
        var link01to03 = new ChapterLink(c01, c03);
        var link01to04 = new ChapterLink(c01, c04);
        var links = Set.of(link01to02, link01to03, link01to04);

        var pathTo02 = new PathBuilder(c01)
                .addChapter(c02)
                .build();

        var pathTo03 = new PathBuilder(c01)
                .addChapter(c03)
                .build();

        var pathTo04 = new PathBuilder(c01)
                .addChapter(c04)
                .build();

        var paths = new GraphService(new InMemoryChapterDomainRepository(), new InMemoryChapterLinkDomainRepository())
                .generatePaths(new Graph(chapters, links));

        assertThat(paths, containsInAnyOrder(pathTo02, pathTo03, pathTo04));
        assertThat(paths, hasSize(3));

    }

    @Test
    @DisplayName("""
            Given there are no chapters for the adventure,
            when a Graph is requested,
            then an exception is thrown
            """)
    void noChaptersForAdventure() {
        var inMemoryRepository = new InMemoryChapterDomainRepository();
        String adventure = "Adventure which does not exist";
        inMemoryRepository.deleteByAdventure(adventure);

        GraphService graphService = new GraphService(inMemoryRepository, new InMemoryChapterLinkDomainRepository());
        assertThrows(NoChaptersForAdventureException.class, () -> graphService.createGraph(adventure));

    }

    @Test
    @DisplayName("""
            Given there is a circle Path,
            when a Graph is requested,
            then an exception is thrown
            """)
    void invalidGraph() {
        var inMemoryChapterRepository = new InMemoryChapterDomainRepository();
        var adventure = "Adventure";
        var chapter1 = new Chapter("c01", 1.0d);
        var chapter2 = new Chapter("c02", 1.0d);

        inMemoryChapterRepository.save(adventure, chapter1);
        inMemoryChapterRepository.save(adventure, chapter2);

        var inMemoryChapterLinkRepository = new InMemoryChapterLinkDomainRepository();
        inMemoryChapterLinkRepository.save(adventure,
                new ChapterLink(chapter1, chapter2));
        inMemoryChapterLinkRepository.save(adventure,
                new ChapterLink(chapter2, chapter1));

        assertThrows(InvalidGraphException.class,
                () -> new GraphService(inMemoryChapterRepository, inMemoryChapterLinkRepository)
                        .createGraph(adventure));

    }

    @Test
    @DisplayName("""
            Given only valid Paths,
            when a Graph for the adventure is requested,
            the Graph is returned
            """)
    void validGraph() throws InvalidGraphException, NoChaptersForAdventureException {
        var inMemoryChapterRepository = new InMemoryChapterDomainRepository();
        var adventure = "Adventure";
        var chapter1 = new Chapter("c01", 1.0d);
        var chapter2 = new Chapter("c02", 1.0d);
        var link = new ChapterLink(chapter1, chapter2);

        inMemoryChapterRepository.save(adventure, chapter1);
        inMemoryChapterRepository.save(adventure, chapter2);

        var inMemoryChapterLinkRepository = new InMemoryChapterLinkDomainRepository();
        inMemoryChapterLinkRepository.save(adventure,
                link);

        var graphExpected = new Graph(Set.of(chapter1, chapter2), Set.of(link));
        assertThat(new GraphService(inMemoryChapterRepository, inMemoryChapterLinkRepository).createGraph(adventure),
                is(graphExpected));



    }


}