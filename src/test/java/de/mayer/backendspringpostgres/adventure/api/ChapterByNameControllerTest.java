package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.MyPostgresContainer;
import de.mayer.backendspringpostgres.adventure.persistence.*;
import de.mayer.backendspringpostgres.adventure.persistence.dto.*;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.*;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

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

    @ServiceConnection
    static MyPostgresContainer sqlContainer = MyPostgresContainer.getInstance();

    @AfterEach
    void cleanup() {
        chapterJpaRepository.deleteAll();
        adventureJpaRepository.deleteAll();
    }
    private static final String PATH = "chapter/{adventureName}/{chapterName}";

    private static RequestSpecification getGivenForPathWithParams(String adventure, String chapter, int port) {
        return given()
                .port(port)
                .pathParam("adventureName", adventure)
                .pathParam("chapterName", chapter);
    }

    @DisplayName("""
            Given there are is no Chapter by the Name Testchapter in the Adventure Testadventure,
            When it is requested,
            Then status code NOT_FOUND is returned
            """)
    @Test
    void getChapterNotFound() {

        var when = getGivenForPathWithParams("Testadventure", "Testchapter", port)
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

        var when = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port)
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

        var when = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port)
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

        var given = getGivenForPathWithParams("Testadventure", "Testchapter", port);
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

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port);
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

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port);
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

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port);
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

        var chapterWithNewRecordsForPatch = new LinkedHashMap<>();
        chapterWithNewRecordsForPatch.put("name", null);
        chapterWithNewRecordsForPatch.put("subheader", null);
        chapterWithNewRecordsForPatch.put("approximateDurationInMinutes", null);
        chapterWithNewRecordsForPatch.put("records", newRecords.get("records"));

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port);
        given.body(jsonMapper.writeValueAsString(chapterWithNewRecordsForPatch))
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

    @DisplayName("""
            Given there is an Adventure with a Chapter by the Name "New Chapter"
            When a another Chapter in the Adventure is patched to be renamned to "New Chapter",
            Then http status BAD_REQUEST is returned
            """)
    @Test
    void patchChapterNewChapterNameAlreadyExists() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        null,
                        null));
        var chapterWithConflictingName = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "New Chapter",
                        null,
                        null)
        );

        var chapterWithNewNameForPatch = new LinkedHashMap<String, Object>();
        chapterWithNewNameForPatch.put("name", chapterWithConflictingName.getName());
        chapterWithNewNameForPatch.put("subheader", null);
        chapterWithNewNameForPatch.put("approximateDurationInMinutes", null);
        chapterWithNewNameForPatch.put("records", null);

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port);
        given.body(jsonMapper.writeValueAsString(chapterWithNewNameForPatch))
                .contentType(ContentType.JSON);

        var when = given
                .when().patch(PATH);

        when
                .then().statusCode(is(HttpStatus.BAD_REQUEST.value()));

    }

    @DisplayName("""
            Given there is an Adventure with a Chapter by the Name "Chapter"
            When the same Chapter is patched to its own name,
            Then http status OK is returned
            """)
    @Test
    void patchChapterWithItsOwnName() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(
                new ChapterJpa(adventure.getId(),
                        "Testchapter",
                        null,
                        null));

        var chapterWithNewNameForPatch = new LinkedHashMap<String, Object>();
        chapterWithNewNameForPatch.put("name", chapter.getName());
        chapterWithNewNameForPatch.put("subheader", null);
        chapterWithNewNameForPatch.put("approximateDurationInMinutes", null);
        chapterWithNewNameForPatch.put("records", null);

        var given = getGivenForPathWithParams(adventure.getName(), chapter.getName(), port);
        given.body(jsonMapper.writeValueAsString(chapterWithNewNameForPatch))
                .contentType(ContentType.JSON);

        var when = given
                .when().patch(PATH);

        when
                .then().statusCode(is(HttpStatus.OK.value()));

    }

    @DisplayName("""
            Given there is no Chapter by the name "Testchapter",
            When it shall be deleted,
            Then http status NOT_FOUND is returned
            """)
    @Test
    void deleteNonExistentChapter() {
        getGivenForPathWithParams("Testadventure", "Testchapter", port)
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("""
            Given there is a Chapter with records by the name "Testchapter",
                And it is referenced to by a ChapterLink in another Chapter,
            When it shall be deleted,
            Then all Records, the Chapter and the referencing ChapterLink are deleted
            """)
    @Test
    void deleteChapterWithRecords() {
        var adventureJpa = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapterJpa = chapterJpaRepository.save(new ChapterJpa(adventureJpa.getId(), "Testchapter", null, null));
        var chapter2Jpa = chapterJpaRepository.save(new ChapterJpa(adventureJpa.getId(), "Chapter2", null, null));
        saveExampleRecordsAndGetLinkedHashMapOfJsonRepresentation(chapterJpa, chapter2Jpa);
        saveExampleRecordsAndGetLinkedHashMapOfJsonRepresentation(chapter2Jpa, chapterJpa); // leads to a ChapterLink-Record in Chapter2 referencing Testchapter

        getGivenForPathWithParams("Testadventure", "Testchapter", port)
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var recordsInDeltedChapter = recordJpaRepository.findByChapterIdOrderByIndex(chapterJpa.getId());
        assertThat(recordsInDeltedChapter, is(empty()));

        var recordsInChapter2 = recordJpaRepository.findByChapterIdOrderByIndex(chapter2Jpa.getId());
        var hasNoMatchForChapterLink = recordsInChapter2
                .stream()
                .noneMatch(record -> record.getType() == RecordType.ChapterLink);
        assertThat(hasNoMatchForChapterLink, is(true));


    }


}
