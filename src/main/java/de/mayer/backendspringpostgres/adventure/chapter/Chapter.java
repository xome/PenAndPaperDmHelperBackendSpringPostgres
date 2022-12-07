package de.mayer.backendspringpostgres.adventure.chapter;

import de.mayer.backendspringpostgres.adventure.chapter.record.model.RecordInAChapter;

import java.util.List;

public record Chapter(String adventure, String name, String subheader, Double approximateDurationInMinutes,
                      List<RecordInAChapter> recordInAChapters) {

    public Chapter {
        if (name == null || name.isEmpty())
            throw new RuntimeException(new IllegalAccessException("Name cannot be null or empty"));
        if (adventure == null || adventure.isEmpty())
            throw new RuntimeException(new IllegalAccessException("Adventure cannot be null or empty"));
    }

}
