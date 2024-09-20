package de.mayer.backendspringpostgres.adventure.api.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.mayer.penandpaperdmhelperjcore.adventure.model.RecordInAChapter;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(RecordInAChapter.class)
@JsonDeserialize(using = RecordInAChapterDeserializer.class)
abstract class RecordInAChapterMixIn {
}
