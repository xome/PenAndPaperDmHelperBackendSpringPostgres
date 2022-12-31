package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.adventure.model.Adventure;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryChapterLinkDomainRepository implements ChapterLinkDomainRepository {
    private HashMap<String, ChapterLink> database;

    @Override
    public void save(String adventure, ChapterLink chapterLink) {
        if (this.database == null) {
            this.database = new HashMap<>();
        }

        this.database.put(adventure, chapterLink);

    }

    @Override
    public Optional<Set<ChapterLink>> findByAdventure(String adventure) {
        var chapterLinks = database
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(adventure))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
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
