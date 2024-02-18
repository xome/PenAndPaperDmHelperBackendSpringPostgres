package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.mayer.penandpaperdmhelperjcore.adventure.model.Text;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Text.class)
@JsonSerialize(using = TextSerializer.class)
abstract class TextMixIn {
}
