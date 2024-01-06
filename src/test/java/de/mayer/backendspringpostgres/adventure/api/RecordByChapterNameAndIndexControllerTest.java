package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.MyPostgresContainer;
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
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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

    @ServiceConnection
    static MyPostgresContainer sqlContainer = MyPostgresContainer.getInstance();

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

    @DisplayName("""
            Given there is no Chapter defined by the parameters,
            When a new Record is put,
            Then http status NOT_FOUND is returned
            """)
    @Test
    void putRecordByIndexForNonExistentChapter() throws JsonProcessingException {
        givenForPathWithParams("Testadventure", "Testchapter", 0)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString("Hello World!"))
                .when().put(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("""
            Given there is a Chapter defined by the parameters
                And there is no record with a smaller or equal index,
            When a new Record is put,
            Then it is appended at the end of the records
            """)
    @Test
    void appendRecordAtEnd() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Testchapter", null, null));
        for (int i = 0; i < 5; i++) {
            var record = recordJpaRepository.save(new RecordJpa(chapter.getId(), i, RecordType.Text));
            textJpaRepository.save(new TextJpa(record, "Record %d".formatted(i)));
        }

        givenForPathWithParams(adventure.getName(), chapter.getName(), 10)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString("Hello World!"))
                .when().put(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var newRecord = recordJpaRepository.findByChapterIdAndIndex(chapter.getId(), 5);
        assertThat(newRecord.isPresent(), is(true));

    }

    @DisplayName("""
            Given there is a Chapter defined by the parameters
                And there are records with a smaller or equal indexes,
            When a new Record is put,
            Then it inserted in the middle of the records
            """)
    @Test
    void insertRecordInTheMiddle() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Testchapter", null, null));
        for (int i = 0; i < 5; i++) {
            var record = recordJpaRepository.save(new RecordJpa(chapter.getId(), i, RecordType.Text));
            textJpaRepository.save(new TextJpa(record, "Record %d".formatted(i)));
        }

        givenForPathWithParams(adventure.getName(), chapter.getName(), 2)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString("Hello World!"))
                .when().put(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var recordsAfterPut = recordJpaRepository.findByChapterIdOrderByIndex(chapter.getId());
        assertThat(recordsAfterPut, hasSize(6));

        var newTextJpa = textJpaRepository.findByRecordJpa(recordsAfterPut.get(2));
        assertThat(newTextJpa.isPresent(), is(true));
        assertThat(newTextJpa.get().getText(), is("Hello World!"));

    }

    @DisplayName("""
            Given there is a Chapter defined by the parameters
                And there exists no chapter defined in the new Chapter Link,
            When the new Chapter Link is to be put,
            Then http status BAD_REQUEST is returned
            """)
    @Test
    void putInvalidChapterLink() throws JsonProcessingException {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Testchapter", null, null));

        givenForPathWithParams(adventure.getName(), chapter.getName(), 2)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString(Collections.singletonMap("chapterNameTo", "hubba")))
                .when().put(PATH)
                .then().statusCode(is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    @DisplayName("""
            Given there exists a Record in a chapter,
            When it is patched,
            Then the updated Record is saved
            """)
    void patchText() throws Exception {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Testchapter", null, null));

        var record = recordJpaRepository.save(new RecordJpa(chapter.getId(), 0, RecordType.Text));
        textJpaRepository.save(new TextJpa(record, "Hello world!"));

        givenForPathWithParams(adventure.getName(), chapter.getName(), record.getIndex())
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString("Hallo Welt!"))
                .when().patch(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        var textAfterPatch = textJpaRepository.findByRecordJpa(record);

        assertThat(textAfterPatch.isPresent(), is(true));
        assertThat(textAfterPatch.get().getText(), is("Hallo Welt!"));
    }

    @Test
    @DisplayName("""
            Given there exists no Record with the given Index,
            When it shall be patched,
            Then http status NOT_FOUND is returned
            """)
    void recordToPatchNotFound() throws Exception {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Testchapter", null, null));
        givenForPathWithParams(adventure.getName(), chapter.getName(), 0)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString("Hallo Welt!"))
                .when().patch(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    @DisplayName("""
            Given there exists no Chapter with the given Name,
            When a Record shall be patched,
            Then http status NOT_FOUND is returned
            """)
    void chapterForRecordToPatchNotFound() throws Exception {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        givenForPathWithParams(adventure.getName(), "Testchapter", 0)
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString("Hallo Welt!"))
                .when().patch(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    @DisplayName("""
            Given there exists no Chapter To with the given Name,
            When a Chapter Link shall be patched,
            Then http status BAD_REQUEST is returned
            """)
    void chapterToDoesNotExist() throws Exception {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(),
                "Testchapter",
                null,
                null));
        var record = recordJpaRepository.save(new RecordJpa(chapter.getId(), 0, RecordType.ChapterLink));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(record, chapter.getId()));

        givenForPathWithParams(adventure.getName(), chapter.getName(), record.getIndex())
                .contentType(ContentType.JSON)
                .body(jsonMapper.writeValueAsString(Collections.singletonMap("chapterNameTo", "hubba")))
                .when().patch(PATH)
                .then().statusCode(is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    @DisplayName("""
            Given there exists no Adventure with the given Name,
            When a Record shall be deleted,
            Then http status NOT_FOUND is returned
            """)
    void adventureWithRecordToBeDeletedDoesNotExist() {
        givenForPathWithParams("Dummy", "Dummy", 0)
                .contentType(ContentType.JSON)
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    @DisplayName("""
            Given there exists no Chapter with the given Name,
            When a Record shall be deleted,
            Then http status NOT_FOUND is returned
            """)
    void chapterWithRecordToBeDeletedDoesNotExist() {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));

        givenForPathWithParams(adventure.getName(), "Dummy", 0)
                .contentType(ContentType.JSON)
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @Test
    @DisplayName("""
            Given there exists no Record with the given Index,
            When a Record shall be deleted,
            Then http status OK is returned
            """)
    void recordToBeDeletedDoesNotExist() {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Chapter", null, 0L));

        givenForPathWithParams(adventure.getName(), chapter.getName(), 0)
                .contentType(ContentType.JSON)
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

    }

    @Test
    @DisplayName("""
            Given there exists a Record with the given Index,
            When the Record shall be deleted,
            Then http status OK is returned
            """)
    void recordToBeDeletedDoesExist() {
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));
        var chapter = chapterJpaRepository.save(new ChapterJpa(adventure.getId(), "Chapter", null, 0L));
        var record = recordJpaRepository.save(new RecordJpa(chapter.getId(), 0, RecordType.Text));
        textJpaRepository.save(new TextJpa(record, "Test"));

        givenForPathWithParams(adventure.getName(), chapter.getName(), 0)
                .contentType(ContentType.JSON)
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.OK.value()));

        assertThat(recordJpaRepository.findByChapterIdAndIndex(chapter.getId(), record.getIndex()),
                is(Optional.empty()));
        assertThat(textJpaRepository.findByRecordJpa(record),
                is(Optional.empty()));

    }


}