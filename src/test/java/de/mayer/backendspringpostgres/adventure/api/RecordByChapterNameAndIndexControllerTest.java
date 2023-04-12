package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.RecordType;
import de.mayer.backendspringpostgres.adventure.persistence.dto.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.ChapterJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.TextJpa;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.*;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecordByChapterNameAndIndexControllerTest {
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

    private static final String PATH = "record/{adventureName}/{chapterName}/{index}";

    private RequestSpecification givenForPathWithParams(String adventure, String chapter, int index) {
        return given()
                .port(port)
                .pathParam("adventureName", adventure)
                .pathParam("chapterName", chapter)
                .pathParam("index", index);
    }

    @DisplayName("""
            Given there is no Chapter by the given name,
            When a Record is requested by index,
            Then http status NOT_FOUND is returned
            """)
    @Test
    void getRecordByIndexForNonExistentChapter() {
        givenForPathWithParams("Testadventure", "Testchapter", 0)
                .when().get(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("""
            Given there is a Record specified by the parameters,
            When it is requested,
            Then it is returned
            """)
    @Test
    void getRecordByIndex() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Testchapter", null, null));
        var record = recordJpaRepository.save(new RecordJpa(chapter.getId(), 0, RecordType.Text));
        textJpaRepository.save(new TextJpa(record, "Hello world!"));

        givenForPathWithParams(adventure.getName(), chapter.getName(), record.getIndex())
                .when().get(PATH)
                .then().statusCode(is(HttpStatus.OK.value()))
                .body(is(jsonMapper.writer().writeValueAsString("Hello world!")));


    }

}