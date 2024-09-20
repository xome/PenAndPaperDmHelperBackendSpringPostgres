package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure;

import java.io.IOException;

public class AdventureSerializer extends JsonSerializer<Adventure> {
    @Override
    public void serialize(Adventure adventure, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(adventure.name());
    }
}
