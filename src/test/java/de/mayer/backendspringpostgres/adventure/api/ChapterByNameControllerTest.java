package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.*;
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
class ChapterByNameControllerTest {

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

    private static final String PATH = "chapter/{adventureName}/{chapterName}";

    private RequestSpecification getGivenForPathWithParams(String adventure, String chapter) {
        var given = given()
                .port(port)
                .pathParam("adventureName", "Testadventure")
                .pathParam("chapterName", "Testchapter");
        return given;
    }

    @DisplayName("""
            Given there are is no Chapter by the Name Testchapter in the Adventure Testadventure,
            When it is requested,
            Then status code NOT_FOUND is returned
            """)
    @Test
    void getChapterNotFound() {

        var when = getGivenForPathWithParams("Testadventure", "Testchapter")
                .when().get(PATH);

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

        var when = getGivenForPathWithParams(adventure.getName(), chapter.getName())
                .when().get(PATH);

        var bodyReturned = when.then()
                .statusCode(is(HttpStatus.OK.value()))
                .extract().asString();

        assertThat(bodyReturned, is(expectedBody));

    }

    @DisplayName("""
            Given there are is a Chapter by the Name Testchapter in the Adventure Testadventure
                And it has Records,
            When it is requested,
            Then the Chapter is returned with its records
            """)
    @Test
    void getChapterWithRecords() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "Testchapter",
                "This is a subheader",
                30L));
        var chapter2 = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "Testchapter 2",
                "This is a subheader",
                30L));

        var map = saveExampleRecordsAndGetLinkedHashMapOfJsonRepresentation(chapter, chapter2);
        var expectedBody = jsonMapper.writer().writeValueAsString(map);

        var when = getGivenForPathWithParams(adventure.getName(), chapter.getName())
                .when().get(PATH);

        var bodyReturned = when.then()
                .statusCode(is(HttpStatus.OK.value()))
                .extract().asString();

        assertThat(bodyReturned, is(expectedBody));

    }

    private Map<String, Object> saveExampleRecordsAndGetLinkedHashMapOfJsonRepresentation(ChapterJpa chapter, ChapterJpa chapter2) {
        var recordText = recordJpaRepository.save(new RecordJpa(chapter.getId(), 0, RecordType.Text));
        var text = textJpaRepository.save(new TextJpa(recordText, "Testtext"));
        var recordEnvLight = recordJpaRepository.save(new RecordJpa(chapter.getId(), 1, RecordType.EnvironmentLightning));
        var envLight = environmentLightningJpaRepository.save(new EnvironmentLightningJpa(recordEnvLight,
                255, 255, 255, 0.4));
        var recordPic = recordJpaRepository.save(new RecordJpa(chapter.getId(), 2, RecordType.Picture));
        var pic = pictureJpaRepository.save(new PictureJpa(recordPic,
                "thisisvalidbase64===============",
                "png", true));
        var recordLink = recordJpaRepository.save(new RecordJpa(chapter.getId(), 3, RecordType.ChapterLink));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(recordLink, chapter2.getId()));
        var recordMusic = recordJpaRepository.save(new RecordJpa(chapter.getId(), 4, RecordType.Music));
        var music = backgroundMusicRepository.save(new BackgroundMusicJpa(recordMusic,
                "Testmusic",
                "thisisvalidbase64==============="));

        var listRecords = new LinkedList<>();

        listRecords.add(text.getText());

        Map<String, Object> mapEnvLight = new LinkedHashMap<>();
        mapEnvLight.put("rgb", new int[]{envLight.getRgb1(), envLight.getRgb2(), envLight.getRgb3()});
        mapEnvLight.put("brightness", envLight.getBrightness());
        listRecords.add(mapEnvLight);

        var mapPic = new LinkedHashMap<String, Object>();
        mapPic.put("base64", pic.getBase64());
        mapPic.put("fileFormat", pic.getFileFormat());
        mapPic.put("isShareableWithGroup", pic.getShareableWithGroup());
        listRecords.add(mapPic);

        var mapLink = new LinkedHashMap<String, Object>();
        mapLink.put("chapterNameTo", chapter2.getName());
        listRecords.add(mapLink);

        var mapMusic = new LinkedHashMap<String, Object>();
        mapMusic.put("name", music.getName());
        mapMusic.put("data", music.getBase64());
        listRecords.add(mapMusic);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", chapter.getName());
        map.put("subheader", chapter.getSubheader());
        if (chapter.getApproximateDurationInMinutes() == null){
            map.put("approximateDurationInMinutes", chapter.getApproximateDurationInMinutes());
        } else {
            map.put("approximateDurationInMinutes", chapter.getApproximateDurationInMinutes().intValue());
        }
        map.put("records", listRecords);
        return map;
    }


    @DisplayName("""
            Given there is no Chapter by the Name "Testchapter" in the adventure "Testadventure"
            When the Chapter is patched,
            Then http status code 404 is returned.
            """)
    @Test
    void patchNonExistentChapter() throws JsonProcessingException {

        var simpleChapterForPatch = new LinkedHashMap<>();
        simpleChapterForPatch.put("name", "new name for a chapter");
        simpleChapterForPatch.put("subheader", null);
        simpleChapterForPatch.put("approximateDurationInMinutes", null);
        simpleChapterForPatch.put("records", null);

        var given = getGivenForPathWithParams("Testadventure", "Testchapter");
        given.body(jsonMapper.writeValueAsString(simpleChapterForPatch))
                .contentType(ContentType.JSON);

        given.when().patch(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @DisplayName("""
            Given there is a Chapter by the name "Testchapter",
            When it is patched with a new name,
            Then the Chapter is saved with the new name
            """)
    @Test
    void patchName() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        null,
                        null));

        String newName = "new name for a chapter";

        var simpleChapterForPatch = new LinkedHashMap<>();
        simpleChapterForPatch.put("name", newName);
        simpleChapterForPatch.put("subheader", null);
        simpleChapterForPatch.put("approximateDurationInMinutes", null);
        simpleChapterForPatch.put("records", null);

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName());
        given.body(jsonMapper.writeValueAsString(simpleChapterForPatch))
                .contentType(ContentType.JSON);

        given.when().patch(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var patchedChapter = chapterJpaRepository.findByAdventureAndName(adventure.getId(), newName);
        assertThat(patchedChapter.isPresent(), is(true));
        assertThat(patchedChapter.get().getId(), is(chapter.getId()));

    }

    @DisplayName("""
            Given there is a Chapter with the subheader Testsubheader,
            When it is patched with a new Subheader,
            Then the Chapter is saved with the new Subheader
            """)
    @Test
    void patchSubheader() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        "Testsubheader",
                        null));

        String newSubheader = "new subheader";

        var simpleChapterForPatch = new LinkedHashMap<>();
        simpleChapterForPatch.put("name", null);
        simpleChapterForPatch.put("subheader", newSubheader);
        simpleChapterForPatch.put("approximateDurationInMinutes", null);
        simpleChapterForPatch.put("records", null);

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName());
        given.body(jsonMapper.writeValueAsString(simpleChapterForPatch))
                .contentType(ContentType.JSON);

        var when = given
                .when().patch(PATH);

        when
                .then().statusCode(is(HttpStatus.OK.value()));

        var patchedChapter = chapterJpaRepository.findByAdventureAndName(adventure.getId(), chapter.getName());
        assertThat(patchedChapter.isPresent(), is(true));
        assertThat(patchedChapter.get().getId(), is(chapter.getId()));
        assertThat(patchedChapter.get().getSubheader(), is(newSubheader));
    }

    @DisplayName("""
            Given there is a Chapter with a duration of 30 minutes,
            When it is patched with a new duration of 50 minutes,
            Then the Chapter is saved with the new duration
            """)
    @Test
    void patchDuration() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        null,
                        30L));

        Long newDuration = 50L;

        var simpleChapterForPatch = new LinkedHashMap<>();
        simpleChapterForPatch.put("name", null);
        simpleChapterForPatch.put("subheader", null);
        simpleChapterForPatch.put("approximateDurationInMinutes", newDuration);
        simpleChapterForPatch.put("records", null);

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName());
        given.body(jsonMapper.writeValueAsString(simpleChapterForPatch))
                .contentType(ContentType.JSON);

        var when = given
                .when().patch(PATH);

        when
                .then().statusCode(is(HttpStatus.OK.value()));

        var patchedChapter = chapterJpaRepository.findByAdventureAndName(adventure.getId(), chapter.getName());
        assertThat(patchedChapter.isPresent(), is(true));
        assertThat(patchedChapter.get().getId(), is(chapter.getId()));
        assertThat(patchedChapter.get().getApproximateDurationInMinutes(), is(newDuration));
    }

    @DisplayName("""
            Given there is a Chapter with multiple records,
            When it is patched with records,
            Then the records are replaced
            """)
    @Test
    void patchRecords() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        null,
                        null));
        var chapter2 = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "2nd Chapter",
                null,
                null));
        var chapter3 = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "3rd Chapter",
                null,
                null));
        var chapter4 = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "4th Chapter",
                null,
                null));

        saveExampleRecordsAndGetLinkedHashMapOfJsonRepresentation(chapter, chapter2);
        List<Long> recordIdsBeforePatch = recordJpaRepository.findByChapterIdOrderByIndex(chapter.getId())
                .stream()
                .mapToLong(RecordJpa::getId)
                .boxed()
                .toList();

        var newRecords = saveExampleRecordsAndGetLinkedHashMapOfJsonRepresentation(chapter3, chapter4);

        var simpleChapterForPatch = new LinkedHashMap<>();
        simpleChapterForPatch.put("name", null);
        simpleChapterForPatch.put("subheader", null);
        simpleChapterForPatch.put("approximateDurationInMinutes", null);
        simpleChapterForPatch.put("records", newRecords.get("records"));

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName());
        given.body(jsonMapper.writeValueAsString(simpleChapterForPatch))
                .contentType(ContentType.JSON);

        var when = given
                .when().patch(PATH);

        when
                .then().statusCode(is(HttpStatus.OK.value()));

        var recordsAfterPatch = recordJpaRepository.findByChapterIdOrderByIndex(chapter.getId())
                .stream()
                .mapToLong(RecordJpa::getId)
                .boxed()
                .toList();

        var patchedChapter = chapterJpaRepository.findByAdventureAndName(adventure.getId(), chapter.getName());
        assertThat(patchedChapter.isPresent(), is(true));
        assertThat(patchedChapter.get().getId(), is(chapter.getId()));
        assertThat(recordsAfterPatch, hasSize(5)); // one of every kind
        for (Long idAfterPatch : recordsAfterPatch) {
            assertThat(recordIdsBeforePatch, not(hasItem(idAfterPatch)));
        }
    }


}