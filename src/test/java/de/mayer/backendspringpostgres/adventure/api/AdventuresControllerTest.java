package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.AdventureJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdventuresControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    AdventureJpaRepository adventureJpaRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @AfterEach
    void cleanup() {
        adventureJpaRepository.deleteAll();
    }

    @DisplayName("""
            Given there are no Adventures,
            When all Adventurenames are requested,
            Then a empty list is returned
            """)
    @Test
    void getNoAdventures() throws JsonProcessingException {

        var expectedBody = jsonMapper.writer().writeValueAsString(Collections.emptyList());

        var given = given()
                .port(port);

        var when = given.when().get("adventures");

        var retrievedBody = when.then()
                .statusCode(is(HttpStatus.OK.value()))
                .extract().body().asString();

        assertThat(retrievedBody, is(expectedBody));
    }

    @DisplayName("""
            Given there are Adventures,
            When all Adventurenames are requested,
            Then all adventures are returned in alphabetical order
            """)
    @Test
    void readAll() throws JsonProcessingException {
        var adventure01 = new AdventureJpa("AA");
        var adventure02 = new AdventureJpa("AB");
        var adventure03 = new AdventureJpa("BA");
        var adventure04 = new AdventureJpa("BB");
        var adventure05 = new AdventureJpa("BC");

        adventureJpaRepository.saveAll(List.of(adventure05, adventure02, adventure03, adventure01, adventure04));

        var expectedBody = jsonMapper.writer().writeValueAsString(List.of(adventure01.getName(),
                adventure02.getName(),
                adventure03.getName(),
                adventure04.getName(),
                adventure05.getName()));

        var given = given()
                .port(port);

        var when = given.when().get("adventures");

        var retrievedBody = when.then()
                .statusCode(is(HttpStatus.OK.value()))
                .extract().body().asString();

        assertThat(retrievedBody, is(expectedBody));

    }

}
