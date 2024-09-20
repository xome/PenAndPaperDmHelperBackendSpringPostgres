package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.mayer.penandpaperdmhelperjcore.adventure.model.BackgroundMusic;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(BackgroundMusic.class)
abstract class BackgroundMusicMixIn {
    @JsonProperty("data")
    String base64;
}
