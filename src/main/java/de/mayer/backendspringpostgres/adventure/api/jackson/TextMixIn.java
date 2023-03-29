package de.mayer.backendspringpostgres.adventure.api.jackson;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.mayer.backendspringpostgres.adventure.model.Text;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Text.class)
@JsonSerialize(using = TextSerializer.class)
abstract class TextMixIn {
}
