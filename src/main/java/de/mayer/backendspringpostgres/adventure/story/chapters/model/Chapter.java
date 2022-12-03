package de.mayer.backendspringpostgres.adventure.story.chapters.model;

import de.mayer.backendspringpostgres.adventure.story.records.model.RecordInAChapter;

import java.util.List;

public record Chapter(String adventure, String name, String subheader, Double approximateDurationInMinutes, List<RecordInAChapter> recordInAChapters) {
}
