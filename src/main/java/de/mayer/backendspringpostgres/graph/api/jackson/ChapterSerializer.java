package de.mayer.backendspringpostgres.graph.api.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.mayer.backendspringpostgres.graph.model.Chapter;

import java.io.IOException;

public class ChapterSerializer extends JsonSerializer<Chapter> {

    @Override
    public void serialize(Chapter chapter, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(chapter.name());
    }
}
