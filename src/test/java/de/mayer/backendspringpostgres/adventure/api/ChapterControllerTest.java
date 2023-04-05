package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.RecordType;
import de.mayer.backendspringpostgres.adventure.persistence.dto.*;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
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
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChapterControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    ChapterJpaRepository chapterJpaRepository;

    @Autowired
    AdventureJpaRepository adventureJpaRepository;

    @Autowired
    RecordJpaRepository recordJpaRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    TextJpaRepository textJpaRepository;

    @Autowired
    EnvironmentLightningJpaRepository environmentLightningJpaRepository;

    @Autowired
    PictureJpaRepository pictureJpaRepository;
    @Autowired
    ChapterLinkJpaRepository chapterLinkJpaRepository;
    @Autowired
    BackgroundMusicJpaRepository backgroundMusicRepository;


    @AfterEach
    void cleanup() {
        chapterJpaRepository.deleteAll();
        adventureJpaRepository.deleteAll();
    }

    private static final String PATH = "chapter/{adventureName}";

    private static RequestSpecification getGivenForPathWithParams(String adventure, int port) {
        return given()
                .port(port)
                .pathParam("adventureName", adventure);
    }

    @DisplayName("""
            Given an existing Chapter,
            When it is patched with a new subheader,
            Then the subheader is saved
            """)
    @Test
    void patchChapter(){
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        "Subheader",
                        null));

        assertThat(true, is(false));




    }


}
