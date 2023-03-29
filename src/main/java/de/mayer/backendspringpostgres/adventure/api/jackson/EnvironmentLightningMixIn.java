package de.mayer.backendspringpostgres.adventure.api.jackson;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.mayer.backendspringpostgres.adventure.model.EnvironmentLightning;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(EnvironmentLightning.class)
@JsonPropertyOrder({"rgb", "brightness"})
abstract class EnvironmentLightningMixIn {
}
