package de.mayer.backendspringpostgres.adventure.graph.model;

import de.mayer.backendspringpostgres.adventure.chapter.Chapter;

import java.util.LinkedList;

public record Path(String adventure, LinkedList<Chapter> chapters, Double approximateDurationInMinutes) {
}
