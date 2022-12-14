package de.mayer.backendspringpostgres.graph.model;

import de.mayer.backendspringpostgres.IllegalModelAccessException;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public record Graph(Set<Chapter> chapters, Set<ChapterLink> chapterLinks) {

    public Graph {
        if (chapters == null || chapters.isEmpty())
            throw new IllegalModelAccessException("Chapters cannot be null or empty.");

        if (chapterLinks == null)
            throw new IllegalModelAccessException("ChapterLinks cannot be null.");

    }


}
