package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.IllegalModelAccessException;

import java.util.List;

public record Chapter(
        String name,
        String subheader,
        Double approximateDurationInMinutes,
        List<RecordInAChapter> records
) {

    public Chapter {
        if (name == null || name.isEmpty())
            throw new IllegalModelAccessException("Name cannot be null or empty");
    }

}
