package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.Set;

public interface ChapterLinkRepository {
    Set<ChapterLink> findByAdventure(String adventure);
    void invalidateCache();

}
