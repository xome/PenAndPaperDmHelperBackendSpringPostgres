package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.*;
import de.mayer.backendspringpostgres.graph.persistence.InMemoryCache;
import de.mayer.backendspringpostgres.graph.persistence.impl.InMemoryChapterLinkRepository;
import de.mayer.backendspringpostgres.graph.persistence.impl.InMemoryChapterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

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
        var c01 = new Chapter("c01", 0);
        var c02 = new Chapter("c02", 1);
        var chapters = new HashSet<>(Arrays.asList(c01, c02));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c01)));

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphService(new InMemoryChapterRepository(), new InMemoryChapterLinkRepository(),
                        new InMemoryCache())
                        .generatePaths("adventure", new Graph(chapters, links)));

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
        var c01 = new Chapter("c01", 0);
        var c02 = new Chapter("c02", 1);
        var c03 = new Chapter("c03", 1);

        var chapters = new HashSet<>(Arrays.asList(c01, c02, c03));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c03),
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c03),
                new ChapterLink(c03, c02)));

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphService(new InMemoryChapterRepository(), new InMemoryChapterLinkRepository(), new InMemoryCache())
                        .generatePaths("adventure", new Graph(chapters, links)));

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
        var c01 = new Chapter("c01", 1);
        var c02 = new Chapter("c02", 1);
        var c03 = new Chapter("c03", 1);
        var c04 = new Chapter("c04", 1);
        var chapters = new HashSet<>(Arrays.asList(c01, c02, c03, c04));
        var links = new HashSet<>(Arrays.asList(
                new ChapterLink(c01, c03),
                new ChapterLink(c01, c02),
                new ChapterLink(c02, c03),
                new ChapterLink(c03, c02),
                new ChapterLink(c01, c04)));

        var exc = assertThrows(InvalidGraphException.class,
                () -> new GraphService(new InMemoryChapterRepository(), new InMemoryChapterLinkRepository(), new InMemoryCache())
                        .generatePaths("adventure", new Graph(chapters, links)));

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
        var c01 = new Chapter("c01", 1);
        var c02 = new Chapter("c02", 1);
        var c03 = new Chapter("c03", 1);
        var c04 = new Chapter("c04", 1);
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

        var paths = new GraphService(new InMemoryChapterRepository(), new InMemoryChapterLinkRepository(), new InMemoryCache())
                .generatePaths("adventure", new Graph(chapters, links));

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
        var c01 = new Chapter("c01", 1);
        var c02 = new Chapter("c02", 1);
        var c03 = new Chapter("c03", 1);
        var c04 = new Chapter("c04", 1);
        var chapters = Set.of(c01, c02, c03, c04);

        var path01 = new PathBuilder(c01).build();
        var path02 = new PathBuilder(c02).build();
        var path03 = new PathBuilder(c03).build();
        var path04 = new PathBuilder(c04).build();

        var paths = new GraphService(new InMemoryChapterRepository(), new InMemoryChapterLinkRepository(), new InMemoryCache())
                .generatePaths("adventure", new Graph(chapters, Collections.emptySet()));

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
        var c01 = new Chapter("c01", 1);
        var c02 = new Chapter("c02", 1);
        var c03 = new Chapter("c03", 1);
        var c04 = new Chapter("c04", 1);
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

        var paths = new GraphService(new InMemoryChapterRepository(), new InMemoryChapterLinkRepository(), new InMemoryCache())
                .generatePaths("adventure", new Graph(chapters, links));

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
        var inMemoryRepository = new InMemoryChapterRepository();
        String adventure = "Adventure which does not exist";
        inMemoryRepository.deleteByAdventure(adventure);

        GraphService graphService = new GraphService(inMemoryRepository, new InMemoryChapterLinkRepository(), new InMemoryCache());
        assertThrows(NoChaptersForAdventureException.class, () -> graphService.createGraph(adventure));

    }

    @Test
    @DisplayName("""
            Given there is a circle Path,
            when a Graph is requested,
            then an exception is thrown
            """)
    void invalidGraph() {
        var inMemoryChapterRepository = new InMemoryChapterRepository();
        var adventure = "Adventure";
        var chapter1 = new Chapter("c01", 1);
        var chapter2 = new Chapter("c02", 1);

        inMemoryChapterRepository.save(adventure, chapter1);
        inMemoryChapterRepository.save(adventure, chapter2);

        var inMemoryChapterLinkRepository = new InMemoryChapterLinkRepository();
        inMemoryChapterLinkRepository.save(adventure,
                new ChapterLink(chapter1, chapter2));
        inMemoryChapterLinkRepository.save(adventure,
                new ChapterLink(chapter2, chapter1));

        assertThrows(InvalidGraphException.class,
                () -> new GraphService(inMemoryChapterRepository, inMemoryChapterLinkRepository, new InMemoryCache())
                        .createGraph(adventure));

    }

    @Test
    @DisplayName("""
            Given only valid Paths,
            when a Graph for the adventure is requested,
            the Graph is returned
            """)
    void validGraph() throws InvalidGraphException, NoChaptersForAdventureException {
        var inMemoryChapterRepository = new InMemoryChapterRepository();
        var adventure = "Adventure";
        var chapter1 = new Chapter("c01", 1);
        var chapter2 = new Chapter("c02", 1);
        var link = new ChapterLink(chapter1, chapter2);

        inMemoryChapterRepository.save(adventure, chapter1);
        inMemoryChapterRepository.save(adventure, chapter2);

        var inMemoryChapterLinkRepository = new InMemoryChapterLinkRepository();
        inMemoryChapterLinkRepository.save(adventure,
                link);

        var graphExpected = new Graph(Set.of(chapter1, chapter2), Set.of(link));
        assertThat(new GraphService(inMemoryChapterRepository, inMemoryChapterLinkRepository, new InMemoryCache()).createGraph(adventure),
                is(graphExpected));

    }

    @Test
    @DisplayName("""
            Given a Graph for the adventure is cached,
            when a Graph for the adventure shall be created,
            then the cached instance is returned
            """)
    void serviceUsesCache() throws InvalidGraphException, NoChaptersForAdventureException {
        var adventure = "Adventure";
        var chapter01 = new Chapter("Chapter01", 1);
        var chapter02 = new Chapter("Chapter02", 2);
        var chapterLink = new ChapterLink(chapter01, chapter02);

        InMemoryChapterRepository chapterRepo = new InMemoryChapterRepository();
        chapterRepo.save(adventure, chapter01);
        chapterRepo.save(adventure, chapter02);
        InMemoryChapterLinkRepository chapterLinkRepo = new InMemoryChapterLinkRepository();
        chapterLinkRepo.save(adventure, chapterLink);

        var graphCached = new Graph(Set.of(chapter01), Collections.emptySet());
        var cache = new InMemoryCache();
        cache.put(adventure, graphCached);

        var service = new GraphService(chapterRepo, chapterLinkRepo, cache);
        assertThat("Service returns cached Graph", service.createGraph(adventure), is(graphCached));

    }

    @Test
    @DisplayName("""
            Given a valid Graph,
            when it is created,
            then it is saved into the inMemoryCache
            """)
    void validGraphIsCached() throws InvalidGraphException, NoChaptersForAdventureException {
        var adventure = "Adventure";
        var chapter01 = new Chapter("Chapter01", 1);

        InMemoryChapterRepository chapterRepo = new InMemoryChapterRepository();
        chapterRepo.save(adventure, chapter01);

        var cache = new InMemoryCache();
        var service = new GraphService(chapterRepo, new InMemoryChapterLinkRepository(), cache);
        var graph = service.createGraph(adventure);

        var optional = cache.get(adventure, Graph.class);
        assertThat("Graph is cached", optional.isPresent(), is(true));
        assertThat("Cached object equals expected Graph", optional.get(), is(graph));

    }


    @Test
    @DisplayName("""
            Given there are multiple shortest Paths in a Graph,
            When the shortest Path is requested,
            Then all shortest Paths are returned
            """)
    void multipleShortestPaths() throws InvalidGraphException, NoChaptersForAdventureException {
        var adventure = "Adventure";
        var cache = new InMemoryCache();
        var chapterRepo = new InMemoryChapterRepository();
        var linkRepo = new InMemoryChapterLinkRepository();

        var chapter1 = new Chapter("1", 1);
        var chapter2 = new Chapter("2", 2);
        var chapter3 = new Chapter("3", 3);
        var chapter4 = new Chapter("4", 1);
        var chapter5 = new Chapter("5", 100);
        chapterRepo.save(adventure, chapter1);
        chapterRepo.save(adventure, chapter2);
        chapterRepo.save(adventure, chapter3);
        chapterRepo.save(adventure, chapter4);
        chapterRepo.save(adventure, chapter5);

        //  Path 1 with 4 minutes
        var link1To2 = new ChapterLink(chapter1, chapter2);
        var link2To4 = new ChapterLink(chapter2, chapter4);
        var path1 = new PathBuilder(chapter1)
                .addChapter(chapter2)
                .addChapter(chapter4)
                .build();

        // Path 2 with 4 minutes
        var link1To3 = new ChapterLink(chapter1, chapter3);
        linkRepo.save(adventure, link1To2);
        linkRepo.save(adventure, link2To4);
        linkRepo.save(adventure, link1To3);
        var path2 = new PathBuilder(chapter1)
                .addChapter(chapter3)
                .build();

        // Path 3 with 101 minutes
        var link1To5 = new ChapterLink(chapter1, chapter5);
        linkRepo.save(adventure, link1To5);
        var path3 = new PathBuilder(chapter1)
                .addChapter(chapter5)
                .build();


        var shortestPaths = new GraphService(chapterRepo, linkRepo, cache)
                .getShortestPaths(adventure);


        assertThat(shortestPaths, containsInAnyOrder(path1, path2));
        assertThat(shortestPaths, not(contains(path3)));
        assertThat(shortestPaths, hasSize(2));

    }

    @Test
    @DisplayName("""
            Given the Paths for the adventure are cached,,
            When all Paths are requested,
            Then the cached Paths are returned
            """)
    void cacheIsUsed() throws InvalidGraphException {

        var adventure = "Adventure";
        var cache = new InMemoryCache();
        var chapterRepo = new InMemoryChapterRepository();
        var linkRepo = new InMemoryChapterLinkRepository();

        var chapter = new Chapter("1", 1);
        var chapter2 = new Chapter("2", 2);
        var expectedPaths = new HashSet<>(Set.of(new PathBuilder(chapter).addChapter(chapter2).build()));
        cache.put("AllPaths - %s".formatted(adventure), expectedPaths);

        var graph = new Graph(Set.of(chapter), Collections.emptySet());

        assertThat(new GraphService(chapterRepo, linkRepo, cache).generatePaths(adventure, graph),
                is(expectedPaths));

    }

    @Test
    @DisplayName("""
            Given an empty Cache,
            When Paths are generated,
            Then the Cache is populated
            """)
    void pathUpdatesCache() throws InvalidGraphException {
        var adventure = "Adventure";
        var cache = new InMemoryCache();
        var chapterRepo = new InMemoryChapterRepository();
        var linkRepo = new InMemoryChapterLinkRepository();

        var chapter = new Chapter("1", 1);

        var graph = new Graph(Set.of(chapter), Collections.emptySet());
        var expectedPath = new HashSet<>(Set.of(new PathBuilder(chapter).build()));

        new GraphService(chapterRepo, linkRepo, cache).generatePaths(adventure, graph);

        var cacheKey = "AllPaths - %s".formatted(adventure);
        var cachedObject = cache.get(cacheKey, HashSet.class);

        assertThat(cachedObject.isPresent(), is(true));
        assertThat(cachedObject.get(), is(expectedPath));
    }

    @Test
    @DisplayName("""
            Given there are two Chapters
                And they are linked,
            When the next Paths for the starting point are requested,
            Then a Path consisting of the second Chapter is returned
            """)
    void nextPathsTwoChapters() throws InvalidGraphException, NoChaptersForAdventureException {

        var adventure = "Adventure";
        var chapter1 = new Chapter("1", 1);
        var chapter2 = new Chapter("2", 2);
        var link = new ChapterLink(chapter1, chapter2);

        var cache = new InMemoryCache();
        var chapterRepo = new InMemoryChapterRepository();
        var linkRepo = new InMemoryChapterLinkRepository();

        chapterRepo.save(adventure, chapter1);
        chapterRepo.save(adventure, chapter2);
        linkRepo.save(adventure, link);

        var expectedPath = List.of(new PathBuilder(chapter2).build());

        var service = new GraphService(chapterRepo, linkRepo, cache);

        assertThat(service.getNextPaths(adventure, chapter1.name()), is(expectedPath));
    }

    @Test
    @DisplayName("""
            Given a ending Chapter is given as a starting point,
            When the next Paths are requested,
            Then an empty List of Paths is returned
            """)
    void nextPathsEndingChapterAsStartingPoint() throws InvalidGraphException, NoChaptersForAdventureException {
        var adventure = "Adventure";
        var chapter1 = new Chapter("1", 1);
        var chapter2 = new Chapter("2", 2);
        var link = new ChapterLink(chapter1, chapter2);

        var cache = new InMemoryCache();
        var chapterRepo = new InMemoryChapterRepository();
        var linkRepo = new InMemoryChapterLinkRepository();

        chapterRepo.save(adventure, chapter1);
        chapterRepo.save(adventure, chapter2);
        linkRepo.save(adventure, link);

        var service = new GraphService(chapterRepo, linkRepo, cache);

        assertThat(service.getNextPaths(adventure, chapter2.name()), is(empty()));

    }
}