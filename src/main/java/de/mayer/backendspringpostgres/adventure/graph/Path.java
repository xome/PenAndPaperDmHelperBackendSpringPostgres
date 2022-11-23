package de.mayer.backendspringpostgres.adventure.graph;

import de.mayer.backendspringpostgres.adventure.story.chapters.Chapter;

import java.util.LinkedList;

public record Path(LinkedList<Chapter> chapters, Double approximateDurationInMinutes) {
}
