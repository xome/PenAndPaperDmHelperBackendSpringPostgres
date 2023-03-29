package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.mayer.backendspringpostgres.adventure.model.Text;

import java.io.IOException;

public class TextSerializer extends JsonSerializer<Text> {
    @Override
    public void serialize(Text text, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(text.text());
    }
}
