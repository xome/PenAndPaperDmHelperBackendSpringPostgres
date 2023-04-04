package de.mayer.backendspringpostgres.adventure.domainservice;

import de.mayer.backendspringpostgres.adventure.model.Chapter;

import java.util.Optional;

public interface ChapterRepository {
    Optional<Chapter> findById(String adventureName, String chapterName);

    void updateChapter(String adventure, String nameOfChapterToBeUpdated, Chapter chapterWithNewData) throws ChapterNotFoundException;
}
