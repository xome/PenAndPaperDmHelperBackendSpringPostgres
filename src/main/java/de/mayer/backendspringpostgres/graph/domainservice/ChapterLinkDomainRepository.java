package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.Set;

public interface ChapterLinkDomainRepository {
    Set<ChapterLink> findByAdventure(String adventure);

}
