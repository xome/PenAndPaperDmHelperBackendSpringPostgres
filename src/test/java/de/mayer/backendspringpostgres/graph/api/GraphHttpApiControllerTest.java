package de.mayer.backendspringpostgres.graph.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.MyPostgresContainer;
import de.mayer.backendspringpostgres.graph.domainservice.Cache;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.persistence.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
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

    @ServiceConnection
    static MyPostgresContainer sqlContainer = MyPostgresContainer.getInstance();

    @LocalServerPort
    private int port;

    @Autowired
    GraphChapterLinkJpaRepository chapterLinkJpaRepository;

    @Autowired
    GraphChapterJpaRepository chapterJpaRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    Cache cache;

    @Autowired
    ConcurrentMapCacheManager jpaCache;

    @Autowired
    GraphAdventureJpaRepository adventureJpaRepository;
    @Autowired
    private GraphRecordJpaRepository recordJpaRepository;

    @AfterEach
    void cleanup() {
        cache.invalidateAll();
        chapterLinkJpaRepository.deleteAll();
        chapterJpaRepository.deleteAll();
        adventureJpaRepository.deleteAll();
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
        var adventure = new AdventureJpa("Adventure");
        var chapter = new Chapter("Chapter 01", 30);

        adventure = adventureJpaRepository.save(adventure);

        chapterJpaRepository
                .save(new ChapterJpa(adventure.getId(),
                        chapter.name(),
                        chapter.approximateDurationInMinutes().longValue()));
        var map = new HashMap<String, Object>();
        map.put("chapters", Set.of(chapter.name()));
        map.put("chapterLinks", Collections.emptySet());

        var graphExpected = jsonMapper
                .writer()
                .writeValueAsString(map);

        var graphReturned =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure.getName())
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
        var chapter = new Chapter("Chapter 01", 30);
        var chapter2 = new Chapter("Chapter 02", 30);

        var adventure = new AdventureJpa("Adventure");
        adventure = adventureJpaRepository.save(adventure);

        var chapter1Jpa = chapterJpaRepository
                .save(new ChapterJpa(adventure.getId(),
                        chapter.name(),
                        chapter.approximateDurationInMinutes().longValue()));

        var chapter2Jpa = chapterJpaRepository
                .save(new ChapterJpa(adventure.getId(),
                        chapter2.name(),
                        chapter2.approximateDurationInMinutes().longValue()));

        var record = recordJpaRepository.save(new RecordJpa(chapter1Jpa.getId(), 0));
        chapterLinkJpaRepository
                .save(new ChapterLinkJpa(record, chapter2Jpa.getId()));

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

        var adventure = adventureJpaRepository.save(new AdventureJpa("Adventure"));
        cache.invalidate(adventure.getName(), Graph.class);
        var chapter01 = new Chapter("Chapter 01", 1);
        var chapter02 = new Chapter("Chapter 02", 1);

        var chapter01Jpa = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                chapter01.name(),
                chapter01.approximateDurationInMinutes().longValue()));

        var chapter02Jpa = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                chapter02.name(),
                chapter02.approximateDurationInMinutes().longValue()));

        var record01 = recordJpaRepository.save(new RecordJpa(chapter01Jpa.getId(), 0));
        var record02 = recordJpaRepository.save(new RecordJpa(chapter02Jpa.getId(), 0));

        chapterLinkJpaRepository
                .save(new ChapterLinkJpa(record01,
                        chapter02Jpa.getId()));
        chapterLinkJpaRepository
                .save(new ChapterLinkJpa(record02,
                        chapter02Jpa.getId()));

        var expectedBody = new LinkedHashMap<String, Object>();
        expectedBody.put("message", "Graph is invalid. There are only Paths with circles.");
        expectedBody.put("problematicPaths", Collections.emptySet());
        String expectedBodyAsJson = jsonMapper.writeValueAsString(expectedBody);

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure.getName())
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
        var adventure = adventureJpaRepository.save(new AdventureJpa("Adventure"));
        cache.invalidate(adventure.getName(), Graph.class);

        var chapter01 = new Chapter("1", 1);
        var chapter02 = new Chapter("2", 1);

        var link = new ChapterLink(chapter01, chapter02);

        var chapter01Jpa = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), chapter01.name(), chapter01.approximateDurationInMinutes().longValue()));
        var chapter02Jpa = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), chapter02.name(), chapter02.approximateDurationInMinutes().longValue()));

        var record = recordJpaRepository.save(new RecordJpa(chapter01Jpa.getId(), 0));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(record, chapter02Jpa.getId()));

        var expectedBody = new LinkedHashMap<String, Object>();
        expectedBody.put("chapters", List.of(link.from().name(), link.to().name()));
        expectedBody.put("approximateDurationInMinutes", 2);
        var expectedBodyAsJson = jsonMapper.writeValueAsString(List.of(expectedBody));

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure.getName())
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
        var adventureJpa = adventureJpaRepository.save(new AdventureJpa(adventure));

        var chapter01 = new Chapter("1", 1);
        var chapter02 = new Chapter("2", 1);

        var chapter01Jpa = chapterJpaRepository.save(new ChapterJpa(adventureJpa.getId(),
                chapter01.name(),
                chapter01.approximateDurationInMinutes().longValue()));

        var chapter02Jpa = chapterJpaRepository.save(new ChapterJpa(adventureJpa.getId(),
                chapter02.name(),
                chapter02.approximateDurationInMinutes().longValue()));

        var record = recordJpaRepository.save(new RecordJpa(chapter01Jpa.getId(), 0));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(record, chapter02Jpa.getId()));
        var record02 = recordJpaRepository.save(new RecordJpa(chapter02Jpa.getId(), 0));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(record02, chapter01Jpa.getId()));

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
        var adventure = adventureJpaRepository.save(new AdventureJpa("Adventure"));
        var chapter = new Chapter("Chapter", 1);

        chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                chapter.name(),
                chapter.approximateDurationInMinutes().longValue()));

        var pathInBody = new HashMap<String, Object>();
        pathInBody.put("chapters", List.of(chapter.name()));
        pathInBody.put("approximateDurationInMinutes", chapter.approximateDurationInMinutes());
        var expectedBodyAsJson = jsonMapper.writeValueAsString(List.of(pathInBody));

        var returnedBody =
                given()
                        .port(port)
                        .pathParam("adventureName", adventure.getName())
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


    @Test
    @DisplayName("""
            Given there is one Chapter in a Graph,
            When the next Paths are requested,
            Then a empty list of Paths is returned
            """)
    void nextPathsOneChapter() {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Adventure"));
        var expectedBody = "[]";

        var chapter = new Chapter("1", 1);
        chapterJpaRepository.save(new ChapterJpa(adventure.getId(), chapter.name(), chapter.approximateDurationInMinutes().longValue()));

        var returnedBody =
                given().port(port).pathParam("adventureName", adventure.getName()).pathParam("startingPoint", chapter.name())
                        .when().get("/paths/next/{adventureName}/{startingPoint}")
                        .then().statusCode(HttpStatus.OK.value()).extract().body().asString();

        assertThat(returnedBody, is(expectedBody));
    }
}