package de.mayer.backendspringpostgres.adventure.model;

public record ChapterLink(String chapterNameTo) implements RecordInAChapter {

    public ChapterLink {
        if (chapterNameTo == null || chapterNameTo.isEmpty())
            throw new IllegalModelAccessException("ChapterNameTo cannot be null or empty.");
    }

}
