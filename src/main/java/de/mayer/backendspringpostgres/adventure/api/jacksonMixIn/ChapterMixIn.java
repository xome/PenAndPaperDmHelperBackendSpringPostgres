package de.mayer.backendspringpostgres.adventure.api.jacksonMixIn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.mayer.backendspringpostgres.adventure.model.RecordInAChapter;

import java.util.List;

abstract class ChapterMixIn {

    ChapterMixIn(String name,
                 String subheader,
                 String approximateDurationInMinutes,
                 List<RecordInAChapter> records) {
    }


    @JsonIgnore
    abstract String adventure();

    abstract String getName();

    abstract String getSubheader();

    abstract Double getApproximateDurationInMinutes();

    abstract List<RecordInAChapter> getRecords();

}
