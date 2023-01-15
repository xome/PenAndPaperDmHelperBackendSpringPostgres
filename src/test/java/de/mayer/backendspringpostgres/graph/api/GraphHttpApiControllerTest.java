package de.mayer.backendspringpostgres.graph.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.graph.domainservice.Cache;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.persistence.ChapterJpa;
import de.mayer.backendspringpostgres.graph.persistence.ChapterJpaRepository;
import de.mayer.backendspringpostgres.graph.persistence.ChapterLinkJpa;
import de.mayer.backendspringpostgres.graph.persistence.ChapterLinkJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GraphHttpApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    ChapterLinkJpaRepository chapterLinkJpaRepository;

    @Autowired
    ChapterJpaRepository chapterJpaRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    Cache cache;

    @Autowired
    ConcurrentMapCacheManager jpaCache;

    @AfterEach
    void cleanup() {
        cache.invalidateAll();
        chapterLinkJpaRepository.deleteAll();
        chapterJpaRepository.deleteAll();
        jpaCache.getCacheNames().forEach(
                cacheName -> {
                    var cache = jpaCache.getCache(cacheName);
                    if (cache != null)
                        cache.invalidate();
                }
        );

    }

    @Test
    @DisplayName("""
            Given there is no adventure by the name of the parameters value,
            When a Graph is requested,
            Then Status NOT_FOUND is returned.
            """)
    void noAdventureByThatName() {
        cache.invalidate("test adventure", Graph.class);

        given()
                .port(port)
                .pathParam("adventureName", "test adventure")
        .when()
                .get("/graph/{adventureName}")
        .then()
                .statusCode(is(HttpStatus.NOT_FOUND.value()))
                .body(is(emptyString()));
    }

    @Test
    @DisplayName("""
            Given there is an adventure with one chapter,
            When a Graph is requested,
            Then the simple Graph is returned
            """)
    void oneGraphNoLinks() throws JsonProcessingException {
        cache.invalidate("Adventure", Graph.class);
        var chapter = new Chapter("Chapter 01", 30d);

        chapterJpaRepository
                .save(new ChapterJpa("Adventure",
                        chapter.name(),
                        chapter.approximateDurationInMinutes()));
        var map = new HashMap<String, Object>();
        map.put("chapters", Set.of(chapter.name()));
        map.put("chapterLinks", Collections.emptySet());

        var graphExpected = jsonMapper
                .writer()
                .writeValueAsString(map);

        var graphReturned =
                given()
                        .port(port)
                        .pathParam("adventureName", "Adventure")
                .when()
                        .get("/graph/{adventureName}")
                .then()
                        .statusCode(is(HttpStatus.OK.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(graphReturned, is(graphExpected));

    }

    @Test
    @DisplayName("""
            Given there is an adventure with two chapters
                and one link,
            When a Graph is requested,
            Then the Graph is returned according to spec
            """)
    void twoChaptersOneLink() throws JsonProcessingException {
        cache.invalidate("Adventure", Graph.class);
        var chapter = new Chapter("Chapter 01", 30d);
        var chapter2 = new Chapter("Chapter 02", 30d);

        chapterJpaRepository
                .save(new ChapterJpa("Adventure",
                        chapter.name(),
                        chapter.approximateDurationInMinutes()));

        chapterJpaRepository
                .save(new ChapterJpa("Adventure",
                        chapter2.name(),
                        chapter2.approximateDurationInMinutes()));

        var link = new ChapterLink(chapter, chapter2);
        chapterLinkJpaRepository
                .save(new ChapterLinkJpa("Adventure",
                        link.from().name(),
                        0,
                        link.to().name()));

        var linkMap = new LinkedHashMap<String, String>();
        linkMap.put("chapterNameFrom", chapter.name());
        linkMap.put("chapterNameTo", chapter2.name());

        var chapters = new LinkedList<>();
        chapters.add(chapter.name());
        chapters.add(chapter2.name());

        var map = new HashMap<String, Object>();
        map.put("chapters", chapters);
        map.put("chapterLinks", Set.of(linkMap));

        var graphExpectedOne = jsonMapper
                .writer()
                .writeValueAsString(map);

        chapters.clear();
        chapters.add(chapter2.name());
        chapters.add(chapter.name());

        map.clear();
        map.put("chapters", chapters);
        map.put("chapterLinks", Set.of(linkMap));
        var graphExpectedTwo = jsonMapper
                .writer()
                .writeValueAsString(map);

        var graphReturned =
                given()
                        .port(port)
                        .pathParam("adventureName", "Adventure")
                .when()
                        .get("/graph/{adventureName}")
                .then()
                        .statusCode(is(HttpStatus.OK.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(graphReturned, is(anyOf(is(graphExpectedOne), is(graphExpectedTwo))));

    }

    @Test
    @DisplayName("""
            Given the adventure has a cycle,
            When a Graph is requested,
            Then HTTP-Status EXPECTATION_FAILED and the cyclic paths are returned
            """)
    void cycleAdventure() throws JsonProcessingException {

        var adventure = "Adventure";
        cache.invalidate(adventure, Graph.class);
        var chapter01 = new Chapter("Chapter 01", 1.0d);
        var chapter02 = new Chapter("Chapter 02", 1.0d);
        var chapterLink01 = new ChapterLink(chapter01, chapter02);
        var chapterLink02 = new ChapterLink(chapter02, chapter01);

        chapterJpaRepository.save(new ChapterJpa(adventure, chapter01.name(), chapter01.approximateDurationInMinutes()));
        chapterJpaRepository.save(new ChapterJpa(adventure, chapter02.name(), chapter02.approximateDurationInMinutes()));
        chapterLinkJpaRepository
                .save(new ChapterLinkJpa(adventure,
                        chapterLink01.from().name(),
                        0,
                        chapterLink01.to().name()));
        chapterLinkJpaRepository
                .save(new ChapterLinkJpa(adventure,
                        chapterLink02.from().name(),
                        0,
                        chapterLink02.to().name()));

        var expectedBody = new LinkedHashMap<String, Object>();
        expectedBody.put("message", "Graph is invalid. There are only Paths with circles.");
        expectedBody.put("problematicPaths", Collections.emptySet());
        String expectedBodyAsJson = jsonMapper.writeValueAsString(expectedBody);

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure)
                        .when()
                        .get("/graph/{adventureName}")
                        .then()
                        .statusCode(is(HttpStatus.EXPECTATION_FAILED.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(returnedBody, is(expectedBodyAsJson));

    }

    @Test
    @DisplayName("""
            Given there is only one Path,
            When the shortest Path is requested,
            Then the only Path is returned
            """)
    void shortestPathWithOnePath() throws JsonProcessingException {
        var adventure = "Adventure";
        cache.invalidate(adventure, Graph.class);

        var chapter01 = new Chapter("1", 1d);
        var chapter02 = new Chapter("2", 1d);

        var link = new ChapterLink(chapter01, chapter02);

        chapterJpaRepository.save(new ChapterJpa(adventure, chapter01.name(), chapter01.approximateDurationInMinutes()));
        chapterJpaRepository.save(new ChapterJpa(adventure, chapter02.name(), chapter02.approximateDurationInMinutes()));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(adventure, link.from().name(), 0, link.to().name()));

        var expectedBody = new LinkedHashMap<String, Object>();
        expectedBody.put("chapters", List.of(link.from().name(), link.to().name()));
        expectedBody.put("approximateDurationInMinutes", 2d);
        var expectedBodyAsJson = jsonMapper.writeValueAsString(List.of(expectedBody));

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure)
                        .when()
                        .get("/paths/shortest/{adventureName}")
                        .then()
                        .statusCode(is(HttpStatus.OK.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(returnedBody, is(expectedBodyAsJson));
    }

    @Test
    @DisplayName("""
            Given the Graph is invalid,
            When the shortest Path is requested,
            Then Code EXPECTATION_FAILED is returned
            """)
    void shortestPathOfInvalidGraph() throws JsonProcessingException {
        var adventure = "Adventure";
        cache.invalidate(adventure, Graph.class);

        String expectedBodyAsJson = prepareCyclicGraphAndGetJsonString(adventure);

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure)
                        .when()
                        .get("/paths/shortest/{adventureName}")
                        .then()
                        .statusCode(is(HttpStatus.EXPECTATION_FAILED.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(returnedBody, is(expectedBodyAsJson));
    }

    private String prepareCyclicGraphAndGetJsonString(String adventure) throws JsonProcessingException {
        var chapter01 = new Chapter("1", 1d);
        var chapter02 = new Chapter("2", 1d);

        var link = new ChapterLink(chapter01, chapter02);
        var link2 = new ChapterLink(chapter02, chapter01);

        chapterJpaRepository.save(new ChapterJpa(adventure, chapter01.name(), chapter01.approximateDurationInMinutes()));
        chapterJpaRepository.save(new ChapterJpa(adventure, chapter02.name(), chapter02.approximateDurationInMinutes()));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(adventure, link.from().name(), 0, link.to().name()));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(adventure, link2.from().name(), 1, link.to().name()));

        var expectedBody = new LinkedHashMap<String, Object>();
        expectedBody.put("message", "Graph is invalid. There are only Paths with circles.");
        expectedBody.put("problematicPaths", Collections.emptySet());
        return jsonMapper.writeValueAsString(expectedBody);
    }

    @Test
    @DisplayName("""
            Given there are no Chapters for the adventure,
            When the shortest Paths are requested,
            Then status NOT_FOUND is returned
            """)
    void shortestPathForAdventureWithoutChapters() {
        var adventure = "Adventure";
        cache.invalidate(adventure, Graph.class);

        var returnedBody = given()
                .port(port)
                .pathParam("adventureName", adventure)
                .when()
                .get("/paths/shortest/{adventureName}")
                .then()
                .statusCode(is(HttpStatus.NOT_FOUND.value()))
                .extract()
                .body()
                .asString();

        assertThat(returnedBody, is(emptyString()));
    }

    @Test
    @DisplayName("""
            Given there is an Adventure with one Chapter,
            When the longest Path is requested,
            Then a Path with the chapter is returned
            """)
    void longestPathOneChapter() throws JsonProcessingException {
        var adventure = "Adventure";
        var chapter = new Chapter("Chapter", 1d);

        chapterJpaRepository.save(new ChapterJpa(adventure, chapter.name(), chapter.approximateDurationInMinutes()));

        var pathInBody = new HashMap<String, Object>();
        pathInBody.put("chapters", List.of(chapter.name()));
        pathInBody.put("approximateDurationInMinutes", chapter.approximateDurationInMinutes());
        var expectedBodyAsJson = jsonMapper.writeValueAsString(List.of(pathInBody));

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure)
                        .when()
                        .get("/paths/longest/{adventureName}")
                        .then()
                        .statusCode(is(HttpStatus.OK.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(returnedBody, is(expectedBodyAsJson));
    }

    @Test
    @DisplayName("""
            Given there is no Chapter for the Adventure,
            When the longest Path is requested,
            Then status code NOT_FOUND is returned
            """)
    void longestPathNoChapters() {
        var adventure = "Adventure";
        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure)
                        .when()
                        .get("/paths/longest/{adventureName}")
                        .then()
                        .statusCode(is(HttpStatus.NOT_FOUND.value()))
                        .extract()
                        .body()
                        .asString();

        assertThat(returnedBody, is(emptyString()));
    }

    @Test
    @DisplayName("""
            Given the Graph is cyclic,
            When the longest Path is requested,
            Then status code EXPECTATION_FAILED is returned
            """)
    void longestPathCyclicGraph() throws JsonProcessingException {

        var adventure = "Adventure";
        var expectedBody = prepareCyclicGraphAndGetJsonString(adventure);

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure)
                        .when()
                        .get("/paths/longest/{adventureName}")
                        .then()
                        .statusCode(is(HttpStatus.EXPECTATION_FAILED.value()))
                        .extract()
                        .body()
                        .asString();


        assertThat(returnedBody, is(expectedBody));
    }

}