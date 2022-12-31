package de.mayer.backendspringpostgres.graph.api;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkRepository;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
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

}