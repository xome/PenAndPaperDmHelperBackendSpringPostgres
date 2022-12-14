package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.*;

public class InMemoryChapterLinkDomainRepository implements ChapterLinkDomainRepository {
    private HashMap<String, Set<ChapterLink>> database;

    @Override
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
       return database.getOrDefault(adventure, new HashSet<>());
    }

    @Override
    public void deleteAll() {
        database.clear();
    }
}
