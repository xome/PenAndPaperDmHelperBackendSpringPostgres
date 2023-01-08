package de.mayer.backendspringpostgres.graph.api.jackson;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.mayer.backendspringpostgres.graph.model.InvalidGraphException;
import de.mayer.backendspringpostgres.graph.model.Path;
import org.springframework.boot.jackson.JsonMixin;

import java.util.List;
import java.util.Set;

@JsonMixin(InvalidGraphException.class)
@JsonPropertyOrder({"message", "problematicPaths"})
abstract class InvalidGraphExceptionMixin {

    @JsonIgnore
    abstract Throwable getCause();
    @JsonIgnore
    abstract List<Object> getStackTrace();
    @JsonIgnore
    abstract Throwable[] getSuppressed();
    @JsonIgnore
    abstract String getLocalizedMessage();
    @JsonProperty
    String message;
    @JsonProperty
    Set<Path> problematicPaths;


}
