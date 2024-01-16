package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.MyPostgresContainer;
import de.mayer.backendspringpostgres.adventure.persistence.dto.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.*;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecordByChapterControllerTest {

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

    private static final String PATH = "record/{adventureName}/{chapterName}";

    private RequestSpecification givenForPathWithParams(String adventure, String chapter) {
        return given()
                .port(port)
                .pathParam("adventureName", adventure)
                .pathParam("chapterName", chapter);
    }

    @DisplayName("""
            Given there is no Chapter by the Name Testchapter,
            When Records are requested,
            Then http status NOT_FOUND is returned
            """)
    @Test
    void getRecordsForNonExistentChapter(){
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));

        givenForPathWithParams(adventure.getName(), "Testchapter")
                .when().get(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));
    }

    @DisplayName("""
            Given there is no Chapter by the Name Testchapter,
            When Records shall be deleted,
            Then http status NOT_FOUND is returned
            """)
    @Test
    void deleteRecordsForNonExistentChapter(){
        var adventure = adventureJpaRepository.save(new AdventureJpa("Testadventure"));

        givenForPathWithParams(adventure.getName(), "Testchapter")
                .when().delete(PATH)
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));
    }
}