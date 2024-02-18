package de.mayer.backendspringpostgres.adventure.api.json;

import de.mayer.penandpaperdmhelperjcore.adventure.model.Chapter;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Chapter.class)
abstract class ChapterMixIn {
    String name;
}
