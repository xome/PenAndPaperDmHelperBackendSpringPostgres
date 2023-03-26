package de.mayer.backendspringpostgres.adventure.api.jackson;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.mayer.backendspringpostgres.adventure.model.Adventure;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Adventure.class)
@JsonSerialize(using = AdventureSerializer.class)
public abstract class AdventureMixIn {
}
