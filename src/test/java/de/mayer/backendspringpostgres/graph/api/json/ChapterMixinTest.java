package de.mayer.backendspringpostgres.graph.api.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.MyPostgresContainer;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChapterMixinTest {

    // if not defined here, another Spring Instance would be started
    @ServiceConnection
    static MyPostgresContainer sqlContainer = MyPostgresContainer.getInstance();

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("""
            Chapter as json is just its name.
            """)
    void testMixin() throws IOException {

        var out = new ByteArrayOutputStream();

        objectMapper.writeValue(out, new Chapter("chapter", 1));

        assertThat(out.toString(), is("\"chapter\""));

    }


}