package de.mayer.backendspringpostgres.graph.api.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.mayer.penandpaperdmhelperjcore.graph.model.Chapter;
import de.mayer.penandpaperdmhelperjcore.graph.model.ChapterLink;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(ChapterLink.class)
abstract class ChapterLinkMixin {

    @JsonProperty("chapterNameFrom")
    Chapter from;

    @JsonProperty("chapterNameTo")
    Chapter to;

}
