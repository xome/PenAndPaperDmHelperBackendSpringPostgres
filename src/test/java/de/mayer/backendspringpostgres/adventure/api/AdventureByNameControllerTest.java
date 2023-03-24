package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.persistence.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.AdventureJpaRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

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


}