package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.AdventureJpaRepository;
import de.mayer.backendspringpostgres.adventure.persistence.ChapterJpa;
import de.mayer.backendspringpostgres.adventure.persistence.ChapterJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChapterByNameControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    ChapterJpaRepository chapterJpaRepository;

    @Autowired
    AdventureJpaRepository adventureJpaRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @AfterEach
    void cleanup() {
        chapterJpaRepository.deleteAll();
        adventureJpaRepository.deleteAll();
    }

    @DisplayName("""
            Given there are is no Chapter by the Name Testchapter in the Adventure Testadventure,
            When it is requested,
            Then status code NOT_FOUND is returned
            """)
    @Test
    void getChapterNotFound() {

        var given = given()
                .port(port)
                .pathParam("adventureName", "Testadventure")
                .pathParam("chapterName", "Testchapter");

        var when = given.when().get("chapter/{adventureName}/{chapterName}");

        when.then()
                .statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @DisplayName("""
            Given there are is a Chapter by the Name Testchapter in the Adventure Testadventure,
            When it is requested,
            Then the Chapter is returned
            """)
    @Test
    void getChapter() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "Testchapter",
                "This is a subheader",
                30L));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", chapter.getName());
        map.put("subheader", chapter.getSubheader());
        map.put("approximateDurationInMinutes", chapter.getApproximateDurationInMinutes().intValue());
        map.put("records", Collections.emptyList());
        var expectedBody = jsonMapper.writer().writeValueAsString(map);

        var given = given()
                .port(port)
                .pathParam("adventureName", "Testadventure")
                .pathParam("chapterName", "Testchapter");

        var when = given.when().get("chapter/{adventureName}/{chapterName}");

        var bodyReturned = when.then()
                .statusCode(is(HttpStatus.OK.value()))
                .extract().asString();

        assertThat(bodyReturned, is(expectedBody));

    }

}
