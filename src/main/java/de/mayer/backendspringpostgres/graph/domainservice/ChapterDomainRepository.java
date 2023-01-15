package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;

import java.util.Optional;
import java.util.Set;

public interface ChapterDomainRepository {
    Optional<Set<Chapter>> findByAdventure(String adventure);
    Optional<Chapter> findById(String adventure, String chapter);

    void invalidateCache();

}
