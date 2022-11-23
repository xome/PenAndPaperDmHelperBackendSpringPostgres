package de.mayer.backendspringpostgres.adventure.graph;

import de.mayer.backendspringpostgres.adventure.chapters.Chapter;
import de.mayer.backendspringpostgres.adventure.chapters.ChapterLink;

import java.util.List;

public record Graph(List<Chapter> chapters, List<ChapterLink> chapterLinks) {
}
