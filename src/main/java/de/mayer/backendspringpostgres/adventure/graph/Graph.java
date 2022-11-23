package de.mayer.backendspringpostgres.adventure.graph;

import de.mayer.backendspringpostgres.adventure.story.chapters.Chapter;
import de.mayer.backendspringpostgres.adventure.story.records.ChapterLink;

import java.util.List;

public record Graph(List<Chapter> chapters, List<ChapterLink> chapterLinks) {
}
