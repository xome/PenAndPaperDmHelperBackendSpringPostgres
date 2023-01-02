package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;

import java.util.Optional;
import java.util.Set;

public interface ChapterDomainRepository {
    void save(String adventure, Chapter chapter);

    void deleteByAdventure(String adventure);

    Optional<Set<Chapter>> findByAdventure(String adventure);

    void deleteAll();

    Optional<Chapter> findById(String adventure, String chapter);
}
