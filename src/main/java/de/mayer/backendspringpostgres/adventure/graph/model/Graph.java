package de.mayer.backendspringpostgres.adventure.graph.model;

import de.mayer.backendspringpostgres.adventure.chapter.Chapter;
import de.mayer.backendspringpostgres.adventure.chapter.record.model.ChapterLink;

import java.util.List;

public record Graph(String adventure, List<Chapter> chapters, List<ChapterLink> chapterLinks) {
}
