package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.Set;

public interface ChapterLinkDomainRepository {
    void save(String adventure, ChapterLink chapterLink);
    Set<ChapterLink> findByAdventure(String adventure);
    void deleteAll();
}
