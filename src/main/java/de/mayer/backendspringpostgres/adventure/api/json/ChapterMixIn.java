package de.mayer.backendspringpostgres.adventure.api.json;

import de.mayer.backendspringpostgres.adventure.model.Chapter;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Chapter.class)
abstract class ChapterMixIn {
    String name;
}
