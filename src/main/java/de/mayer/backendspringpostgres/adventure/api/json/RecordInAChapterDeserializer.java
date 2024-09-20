package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.mayer.penandpaperdmhelperjcore.adventure.model.*;

import java.io.IOException;

public class RecordInAChapterDeserializer extends StdDeserializer<RecordInAChapter> {

    protected RecordInAChapterDeserializer() {
        super(RecordInAChapter.class);
    }

    @Override
    public RecordInAChapter deserialize(JsonParser jsonParser,
                                        DeserializationContext deserializationContext)
            throws IOException, JacksonException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        if (jsonNode.has("chapterNameTo")) {
            return new ChapterLink(jsonNode.get("chapterNameTo").asText());
        }

        if (jsonNode.has("base64")
                && jsonNode.has("fileFormat")
                && jsonNode.has("isShareableWithGroup")) {
            return new Picture(jsonNode.get("base64").asText(),
                    jsonNode.get("fileFormat").asText(),
                    jsonNode.get("isShareableWithGroup").asBoolean());
        }

        if (jsonNode.has("rgb")
                && jsonNode.has("brightness")) {
            var arrNode = (ArrayNode) jsonNode.get("rgb");
            var rgb = new int[]{arrNode.get(0).asInt(), arrNode.get(1).asInt(), arrNode.get(2).asInt()};
            return new EnvironmentLightning(jsonNode.get("brightness").asDouble(), rgb);
        }

        if (jsonNode.has("name") &&
                jsonNode.has("data")) {
            return new BackgroundMusic(jsonNode.get("name").asText(), jsonNode.get("data").asText());
        }

        // Only Text is just a value
        if (jsonNode.isValueNode()) {
            return new Text(jsonNode.asText());
        }

        return null;
    }
}
