package de.mayer.backendspringpostgres.adventure.story.graph;

import de.mayer.backendspringpostgres.adventure.story.chapters.Chapter;
import de.mayer.backendspringpostgres.adventure.story.records.model.ChapterLink;

import java.util.List;

public record Graph(String adventure, List<Chapter> chapters, List<ChapterLink> chapterLinks) {
}
