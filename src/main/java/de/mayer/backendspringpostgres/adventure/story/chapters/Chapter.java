package de.mayer.backendspringpostgres.adventure.story.chapters;

import de.mayer.backendspringpostgres.adventure.story.records.RecordInAChapter;

import java.util.List;

public record Chapter(String name, String subheader, Double approximateDurationInMinutes, List<RecordInAChapter> recordInAChapters) {
}
