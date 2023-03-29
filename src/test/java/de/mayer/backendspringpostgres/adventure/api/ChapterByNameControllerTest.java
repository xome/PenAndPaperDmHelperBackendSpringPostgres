package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

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
    BackgroundMusicRepository backgroundMusicRepository;


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

        var recordText = recordJpaRepository.save(new RecordJpa(chapter.getId(), 0, RecordType.Text));
        var recordEnvLight = recordJpaRepository.save(new RecordJpa(chapter.getId(), 1, RecordType.EnvironmentLightning));
        var recordPic = recordJpaRepository.save(new RecordJpa(chapter.getId(), 2, RecordType.Picture));
        var recordLink = recordJpaRepository.save(new RecordJpa(chapter.getId(), 3, RecordType.ChapterLink));
        var recordMusic = recordJpaRepository.save(new RecordJpa(chapter.getId(), 4, RecordType.Music));

        var text = textJpaRepository.save(new TextJpa(recordText.getId(), "Testtext"));
        var envLight = environmentLightningJpaRepository.save(new EnvironmentLightningJpa(recordEnvLight.getId(),
                255, 255, 255, 0.4));
        var pic = pictureJpaRepository.save(new PictureJpa(recordPic.getId(),
                "thisisvalidbase64===============",
                "png", true));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(recordLink.getId(), chapter2.getId()));
        var music = backgroundMusicRepository.save(new BackgroundMusicJpa(recordMusic.getId(),
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
        map.put("approximateDurationInMinutes", chapter.getApproximateDurationInMinutes().intValue());
        map.put("records", listRecords);
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
