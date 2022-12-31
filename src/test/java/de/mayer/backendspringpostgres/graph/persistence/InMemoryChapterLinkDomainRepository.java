package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.adventure.model.Adventure;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.*;
import java.util.stream.Collectors;

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
    public Optional<Set<ChapterLink>> findByAdventure(String adventure) {
        var chapterLinks = database.getOrDefault(adventure, new HashSet<>());
        if (chapterLinks.isEmpty())
            return Optional.empty();
        else
            return Optional.of(chapterLinks);
    }

    @Override
    public void deleteAll() {
        database.clear();
    }
}
