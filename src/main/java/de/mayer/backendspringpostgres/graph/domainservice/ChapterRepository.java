package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.persistence.ChapterId;

import java.util.Optional;
import java.util.Set;

public interface ChapterRepository {
    void save(ChapterId chapterId, Chapter chapter);

    void deleteByAdventure(String adventure);

    Optional<Set<Chapter>> findByAdventure(String adventure);
}
