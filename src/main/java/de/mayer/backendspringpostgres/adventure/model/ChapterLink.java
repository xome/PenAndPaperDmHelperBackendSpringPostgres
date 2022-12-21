package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.IllegalModelAccessException;

public record ChapterLink(String chapterNameTo) implements RecordInAChapter {

    public ChapterLink {
        if (chapterNameTo == null || chapterNameTo.isEmpty())
            throw new IllegalModelAccessException("ChapterNameTo cannot be null or empty.");
    }

}
