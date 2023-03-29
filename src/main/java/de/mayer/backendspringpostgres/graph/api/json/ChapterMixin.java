package de.mayer.backendspringpostgres.graph.api.json;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(Chapter.class)
@JsonSerialize(using = ChapterSerializer.class)
abstract class ChapterMixin {
}
