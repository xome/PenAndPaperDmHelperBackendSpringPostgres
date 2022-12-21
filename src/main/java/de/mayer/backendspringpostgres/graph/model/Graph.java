package de.mayer.backendspringpostgres.graph.model;

import java.util.Set;

public record Graph(Set<Chapter> chapters, Set<ChapterLink> chapterLinks) {

    public Graph {
        if (chapters == null || chapters.isEmpty())
            throw new IllegalModelAccessException("Chapters cannot be null or empty.");

        if (chapterLinks == null)
            throw new IllegalModelAccessException("ChapterLinks cannot be null.");

    }


}
