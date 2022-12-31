package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.persistence.ChapterJpaId;

import java.util.Optional;
import java.util.Set;

public interface ChapterDomainRepository {
    void save(ChapterJpaId chapterJpaId, Chapter chapter);

    void deleteByAdventure(String adventure);

    Optional<Set<Chapter>> findByAdventure(String adventure);

    void deleteAll();
}
