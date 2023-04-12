package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.LinkedHashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.*;

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

    private RequestSpecification givenForPathWithParams(String adventure) {
        return given()
                .port(port)
                .pathParam("adventureName", adventure)
                .contentType(ContentType.JSON);
    }

    @DisplayName("""
            Given an existing Chapter,
            When it is patched with a new subheader,
            Then the subheader is saved
            """)
    @Test
    void patchChapter() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        "Subheader",
                        null));

        var newChapterForRequest = new LinkedHashMap<String, Object>();
        newChapterForRequest.put("name", chapter.getName());
        newChapterForRequest.put("subheader", "new Subheader");
        newChapterForRequest.put("approximateDurationInMinutes", null);
        newChapterForRequest.put("records", null);

        givenForPathWithParams(adventure.getName())
                .body(jsonMapper.writeValueAsString(List.of(newChapterForRequest)))
                .when().patch(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var chapterAfterPatch = chapterJpaRepository.findById(chapter.getId());

        assertThat(chapterAfterPatch.isPresent(), is(true));
        assertThat(chapterAfterPatch.get().getSubheader(), is("new Subheader"));

    }

    @DisplayName("""
            Given there exists no adventure with the name "Testadventure",
            When Chapters are put,
            Then HttpStatus NOT_FOUND is returned
            """)
    @Test
    void putChapterForNonExistentAdventure() throws JsonProcessingException {
        var newChapter = new LinkedHashMap<String, Object>();
        newChapter.put("name", "Chapter");
        newChapter.put("subheader", null);
        newChapter.put("approximateDurationInMinutes", 0);
        newChapter.put("records", null);

        givenForPathWithParams("Testadventure")
                .body(jsonMapper.writeValueAsString(List.of(newChapter)))
                .when().put(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));
    }
    
    @DisplayName("""
            Given there exists an adventure with the name "Testadventure",
            When valid Chapters are put,
            Then HttpStatus OK is returned
            """)
    @Test
    void putChapterForExistentAdventure() throws JsonProcessingException {

        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));

        var newChapter = new LinkedHashMap<String, Object>();
        newChapter.put("name", "Chapter");
        newChapter.put("subheader", null);
        newChapter.put("approximateDurationInMinutes", null);
        newChapter.put("records", List.of("Textrecord"));

        givenForPathWithParams(adventure.getName())
                .body(jsonMapper.writeValueAsString(List.of(newChapter)))
                .when().put(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventure.getId(), "Chapter");
        assertThat(chapterJpa.isPresent(), is(true));

        var records = recordJpaRepository.findByChapterIdOrderByIndex(chapterJpa.get().getId());
        assertThat(records, hasSize(1));

        var text = textJpaRepository.findByRecordJpa(records.get(0));
        assertThat(text.isPresent(), is(true));
        assertThat(text.get().getText(), is("Textrecord"));


    }


}
