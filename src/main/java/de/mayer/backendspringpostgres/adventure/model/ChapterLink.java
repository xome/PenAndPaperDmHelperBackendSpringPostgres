package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.IllegalModelAccessException;

public record ChapterLinkRecord(String chapterNameTo) implements RecordInAChapter {

    public ChapterLinkRecord {
        if (chapterNameTo == null || chapterNameTo.isEmpty())
            throw new IllegalModelAccessException("ChapterNameTo cannot be null or empty.");
    }

}
