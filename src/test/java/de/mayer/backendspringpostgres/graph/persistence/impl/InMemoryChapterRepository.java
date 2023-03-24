package de.mayer.backendspringpostgres.graph.persistence.impl;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryChapterRepository implements ChapterRepository {

    private HashMap<String, Chapter> database;

    public void save(String adventure, Chapter chapter) {
        if (database == null) {
            database = new HashMap<>();
        }

        database.put(adventure + chapter.name(), chapter);
    }

    public void deleteByAdventure(String adventure) {
        if (database == null || database.isEmpty()) return;
        database
                .keySet()
                .stream()
                .filter(id -> id.startsWith(adventure))
                .forEach(id -> database.remove(id));
    }

    @Override
    public Set<Chapter> findByAdventure(String adventure) {
        if (database == null || database.isEmpty())
            return Collections.emptySet();

        return database
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(adventure))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Chapter> findById(String adventure, String chapter) {
        if (this.database == null || this.database.isEmpty())
            return Optional.empty();

        if (!this.database.containsKey(adventure + chapter))
            return Optional.empty();

        return Optional.of(this.database.get(adventure + chapter));

    }

    @Override
    public void invalidateCache() {

    }

}
