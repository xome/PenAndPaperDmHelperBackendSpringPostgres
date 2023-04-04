package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.MatcherAssert.*;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecordInAChapterDeserializerTest {

    @Autowired
    ObjectMapper mapper;

    @DisplayName("""
            Given a Text-Json-Node,
            When deserialize is called,
            Then a Text Object is deserialized
            """)
    @Test
    void textDeserialize() throws JsonProcessingException {
        String json = "\"Hello!\"";
        var recordInAChapter = mapper.readValue(json, RecordInAChapter.class);
        assertThat(recordInAChapter, is(instanceOf(Text.class)));
        assertThat(((Text) recordInAChapter).text(), is("Hello!"));
    }

    @DisplayName("""
            Given a ChapterLink-Json-Node,
            When deserialize is called,
            Then a ChapterLink Object is deserialized
            """)
    @Test
    void chapterLinkDeserialize() throws JsonProcessingException {
        var asMap = new LinkedHashMap<String, Object>();
        asMap.put("chapterNameTo", "Testchapter");
        String json = mapper.writeValueAsString(asMap);

        var recordInAChapter = mapper.readValue(json, RecordInAChapter.class);
        assertThat(recordInAChapter, is(instanceOf(ChapterLink.class)));
        assertThat(((ChapterLink) recordInAChapter).chapterNameTo(), is("Testchapter"));

    }

    @DisplayName("""
            Given a Picture-Json-Node,
            When deserialize is called,
            Then a Picture Object is deserialized
            """)
    @Test
    void pictureDeserialize() throws JsonProcessingException {
        var asMap = new LinkedHashMap<String, Object>();
        asMap.put("base64", "base64string");
        asMap.put("fileFormat", "png");
        asMap.put("isShareableWithGroup", true);
        String json = mapper.writeValueAsString(asMap);

        var recordInAChapter = mapper.readValue(json, RecordInAChapter.class);
        assertThat(recordInAChapter, is(instanceOf(Picture.class)));
        assertThat(((Picture) recordInAChapter).base64(), is("base64string"));
        assertThat(((Picture) recordInAChapter).fileFormat(), is("png"));
        assertThat(((Picture) recordInAChapter).isShareableWithGroup(), is(true));

    }

    @DisplayName("""
            Given a EnvironmentLightning-Json-Node,
            When deserialize is called,
            Then a EnvironmentLightning Object is deserialized
            """)
    @Test
    void envLightDeserialize() throws JsonProcessingException {
        var asMap = new LinkedHashMap<String, Object>();
        var rgb = new int[]{255, 254, 253};
        asMap.put("rgb", rgb);
        asMap.put("brightness", 0.5d);
        String json = mapper.writeValueAsString(asMap);

        var recordInAChapter = mapper.readValue(json, RecordInAChapter.class);
        assertThat(recordInAChapter, is(instanceOf(EnvironmentLightning.class)));
        assertThat(((EnvironmentLightning) recordInAChapter).rgb(), is(rgb));
        assertThat(((EnvironmentLightning) recordInAChapter).brightness(), is(0.5d));

    }
    @DisplayName("""
            Given a BackgroundMusic-Json-Node,
            When deserialize is called,
            Then a BackgroundMusic Object is deserialized
            """)
    @Test
    void musicDeserialize() throws JsonProcessingException {
        var asMap = new LinkedHashMap<String, Object>();
        asMap.put("name", "background");
        asMap.put("data", "base64string");
        String json = mapper.writeValueAsString(asMap);

        var recordInAChapter = mapper.readValue(json, RecordInAChapter.class);
        assertThat(recordInAChapter, is(instanceOf(BackgroundMusic.class)));
        assertThat(((BackgroundMusic) recordInAChapter).name(), is("background"));
        assertThat(((BackgroundMusic) recordInAChapter).base64(), is("base64string"));

    }
}