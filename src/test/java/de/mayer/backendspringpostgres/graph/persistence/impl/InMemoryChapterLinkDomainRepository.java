package de.mayer.backendspringpostgres.graph.persistence.impl;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.*;

public class InMemoryChapterLinkDomainRepository implements ChapterLinkDomainRepository {
    private HashMap<String, Set<ChapterLink>> database;

    public void save(String adventure, ChapterLink chapterLink) {
        if (this.database == null) {
            this.database = new HashMap<>();
        }
        var links = this.database.getOrDefault(adventure, new HashSet<>());
        links.add(chapterLink);
        this.database.put(adventure, links);

    }

    @Override
    public Set<ChapterLink> findByAdventure(String adventure) {
        if (this.database == null) return new HashSet<>();
       return database.getOrDefault(adventure, new HashSet<>());
    }

}
