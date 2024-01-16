package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.MyPostgresContainer;
import de.mayer.backendspringpostgres.adventure.persistence.dto.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.AdventureJpaRepository;
import io.restassured.http.ContentType;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdventureByNameControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    AdventureJpaRepository adventureJpaRepository;

    @Autowired
    ObjectMapper jsonMapper;

    @ServiceConnection
    static MyPostgresContainer sqlContainer = MyPostgresContainer.getInstance();

    @AfterEach
    void cleanup() {
        adventureJpaRepository.deleteAll();
    }

    @DisplayName("""
            Given there is no Adventure named Testadventure,
            When an Adventure with the name Testadventure is created,
            Then it is persisted
            """)
    @Test
    void createAdventure() {

        String adventureName = "Testadventure";
        var expectedAdventure = new AdventureJpa(adventureName);

        given().port(port).pathParam("adventureName", adventureName)
                .when().put("/adventure/{adventureName}")
                .then().statusCode(is(HttpStatus.OK.value()));

        var optionalAdventureSaved = adventureJpaRepository.findByName(adventureName);
        assertThat(optionalAdventureSaved.isPresent(), is(true));
        assertThat(optionalAdventureSaved.get().getName(), is(expectedAdventure.getName()));
    }

    @DisplayName("""
            Given there already is an Adventure named Testadventure,
            When an Adventure with the name Testadventure is created,
            Then status BAD_REQUEST is returned
            """)
    @Test
    void adventureToCreateAlreadyExists() {
        var adventureName = "Testadventure";

        adventureJpaRepository.save(new AdventureJpa(adventureName));

        given().port(port).pathParam("adventureName", adventureName)
                .when().put("adventure/{adventureName}")
                .then().statusCode(is(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("""
            Given there is no Adventure name Testadventure,
            When an Adventure by that name is patched,
            Then status BAD_REQUEST is returned
            """)
    @Test
    void patchNonExistentAdventure() {

        var adventureName = "Testadventure";

        given().port(port).pathParam("adventureName", adventureName).body("\"Adventure01\"")
                .when().patch("adventure/{adventureName}")
                .then().statusCode(is(HttpStatus.BAD_REQUEST.value()));


    }

    @DisplayName("""
            Given there is an Adventure by the name Testadventure,
            When the Adventure is patched with the new Name Adventure01,
            Then the Adventure is updated
            """)
    @Test
    void patchAdventureUpdatesAdventure() throws JsonProcessingException {

        var oldAdventureName = "Testadventure";
        var newAdventureName = "Adventure01";

        adventureJpaRepository.save(new AdventureJpa(oldAdventureName));

        var given = given()
                .port(port)
                .pathParam("adventureName", oldAdventureName)
                .body(jsonMapper.writeValueAsString(newAdventureName))
                .contentType(ContentType.JSON);

        given.when().patch("adventure/{adventureName}")
                .then().statusCode(is(HttpStatus.OK.value()));

        assertThat(adventureJpaRepository.findByName(oldAdventureName).isPresent(), is(false));
        assertThat(adventureJpaRepository.findByName(newAdventureName).isPresent(), is(true));


    }

    @DisplayName("""
            Given there is no Adventure by the name Testadventure,
            When the Adventure is to be deleted,
            Then HTTP.NOT_FOUND is returned
            """)
    @Test
    void deleteNotFound() {

        var adventure = new AdventureJpa("Testadventure");

        var given = given()
                .port(port)
                .pathParam("adventureName", adventure.getName());

        given.when().delete("/adventure/{adventureName}")
                .then().statusCode(is(HttpStatus.NOT_FOUND.value()));

    }

    @DisplayName("""
            Given there is an Adventure by the name Testadventure,
            When the Adventure is to be deleted,
            Then HTTP.OK is returned
                And the Adventure is deleted
            """)
    @Test
    void delete() {

        var adventure = new AdventureJpa("Testadventure");

        adventure = adventureJpaRepository.save(adventure);

        var given = given()
                .port(port)
                .pathParam("adventureName", adventure.getName());

        given.when().delete("/adventure/{adventureName}")
                .then().statusCode(is(HttpStatus.OK.value()));

        assertThat(adventureJpaRepository.findById(adventure.getId()).isEmpty(), is(true));
        assertThat(adventureJpaRepository.findByName(adventure.getName()).isEmpty(), is(true));

    }


}