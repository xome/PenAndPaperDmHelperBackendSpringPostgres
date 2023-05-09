package de.mayer.backendspringpostgres.adventure.domainservice;

import de.mayer.backendspringpostgres.adventure.model.Chapter;

import java.util.Optional;

public interface ChapterRepository {
    void create(String adventure, Chapter chapter) throws AdventureNotFoundException, ChapterNotFoundException, ChapterAlreadyExistsException, ChapterToNotFoundException;
    Optional<Chapter> read(String adventureName, String chapterName);

    void update(String adventure, String nameOfChapterToBeUpdated, Chapter chapterWithNewData) throws ChapterNotFoundException, ChapterAlreadyExistsException, ChapterToNotFoundException;

    void delete(String adventureName, String chapterName) throws ChapterNotFoundException;
}
