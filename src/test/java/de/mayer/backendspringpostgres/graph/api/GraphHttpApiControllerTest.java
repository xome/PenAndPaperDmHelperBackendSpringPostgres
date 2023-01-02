package de.mayer.backendspringpostgres.graph.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.persistence.InMemoryChapterLinkDomainRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class GraphHttpApiControllerTest {

    @LocalServerPort
    private int port;

    @AfterEach
    void cleanup() {
        chapterDomainRepository.deleteAll();
    }

    @Autowired
    ChapterDomainRepository chapterDomainRepository;

    @Autowired
    ChapterLinkDomainRepository chapterLinkDomainRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @Test
    @DisplayName("""
            Given there is no adventure by the name of the parameters value,
            When a Graph is requested,
            Then Status NOT_FOUND is returned.
            """)
    void noAdventureByThatName() {

        chapterDomainRepository.deleteAll();

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
            when get graph is called,
            the simple Graph is returned
            """)
    void oneGraphNoLinks() throws JsonProcessingException {

        chapterDomainRepository.deleteAll();
        chapterLinkDomainRepository.deleteAll();

        var chapter = new Chapter("Chapter 01", 30d);

        chapterDomainRepository
                .save("Adventure",
                        chapter);
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
            and a link,
            when get graph is called,
            the Graph is returned according to spec
            """)
    void twoChaptersOneLink() throws JsonProcessingException {

        var chapter = new Chapter("Chapter 01", 30d);
        var chapter2 = new Chapter("Chapter 02", 30d);

        chapterDomainRepository
                .save("Adventure",
                        chapter);

        chapterDomainRepository
                .save("Adventure",
                        chapter2);

        chapterLinkDomainRepository
                .save("Adventure",
                        new ChapterLink(chapter, chapter2));

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


}