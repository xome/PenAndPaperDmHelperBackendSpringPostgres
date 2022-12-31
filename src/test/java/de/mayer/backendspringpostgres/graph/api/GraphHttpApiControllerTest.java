package de.mayer.backendspringpostgres.graph.api;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Set;

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
    void oneChapterAdventure() {

        var chapter = new Chapter("Chapter 01", 30d);

        chapterDomainRepository
                .save("Adventure",
                        chapter);
        var graphExpected = new Graph(Set.of(chapter), Collections.emptySet());

        var graphReturned =
                given()
                        .port(port)
                        .pathParam("adventureName", "Adventure")
                .when()
                        .get("/graph/{adventureName}")
                .then()
                        .statusCode(is(HttpStatus.OK.value()))
                        .extract()
                        .as(Graph.class);

        assertThat(graphReturned, is(graphExpected));

    }


}