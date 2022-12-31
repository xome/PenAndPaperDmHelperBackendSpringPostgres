package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import de.mayer.backendspringpostgres.graph.persistence.ChapterLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ChapterLinkRepository {
    void save(ChapterLinkId chapterLinkId, ChapterLink chapterLink);
    Optional<Set<ChapterLink>> findByAdventure(String adventure);
    void deleteAll();
}
