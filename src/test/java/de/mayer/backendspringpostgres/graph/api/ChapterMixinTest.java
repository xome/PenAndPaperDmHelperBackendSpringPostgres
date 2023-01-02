package de.mayer.backendspringpostgres.graph.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;


@SpringBootTest
class ChapterMixinTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("""
            Chapter as json is just its name.
            """)
    void testMixin() throws IOException {

        var out = new ByteArrayOutputStream();

        objectMapper.writeValue(out, new Chapter("chapter", 1d));

        assertThat(out.toString(), is("\"chapter\""));

    }


}