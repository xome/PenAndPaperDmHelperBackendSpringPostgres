package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChapterLinkJpaRepository extends JpaRepository<ChapterLinkJpa, ChapterLinkJpaId> {
    @Query("select c from ChapterLinkJpa c where c.adventure = ?1 and c.chapterFrom = ?2 and c.to = ?3")
    List<ChapterLinkJpa> findByAdventureAndChapterFromAndChapterTo(String adventure, String chapterFrom, String to);
}
