package de.mayer.backendspringpostgres.adventure.domainservice;

import de.mayer.backendspringpostgres.adventure.model.Adventure;
import de.mayer.backendspringpostgres.adventure.model.Chapter;
import de.mayer.backendspringpostgres.adventure.model.RecordInAChapter;

import java.util.LinkedList;
import java.util.List;

public interface RecordRepository {

    void saveRecord(Adventure adventure, Chapter chapter, RecordInAChapter record) throws ChapterNotFoundException;

    LinkedList<RecordInAChapter> findRecordsByAdventureAndChapter(String adventureName, String chapterName) throws ChapterNotFoundException;

    void deleteAllByAdventureAndChapter(String adventure, String chapter) throws ChapterNotFoundException;

    void saveRecords(Adventure adventure, Chapter chapter, List<RecordInAChapter> records) throws ChapterNotFoundException;

    void deleteAllChapterLinksReferencing(String adventureName, String chapterName);
}
