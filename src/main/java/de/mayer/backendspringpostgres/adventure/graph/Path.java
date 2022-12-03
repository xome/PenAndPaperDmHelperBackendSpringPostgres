package de.mayer.backendspringpostgres.adventure.graph;

import de.mayer.backendspringpostgres.adventure.story.chapters.model.Chapter;

import java.util.LinkedList;

public record Path(String adventure, LinkedList<Chapter> chapters, Double approximateDurationInMinutes) {
}
