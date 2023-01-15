package de.mayer.backendspringpostgres.graph.persistence.impl;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.persistence.ChapterJpaId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryChapterDomainRepository implements ChapterRepository {

    private HashMap<ChapterJpaId, Chapter> database;

    public void save(String adventure, Chapter chapter) {
        if (database == null) {
            database = new HashMap<>();
        }

        database.put(new ChapterJpaId(adventure, chapter.name()), chapter);
    }

    public void deleteByAdventure(String adventure) {
        if (database == null || database.isEmpty()) return;
        database
                .keySet()
                .stream()
                .filter(id -> id.adventure().equals(adventure))
                .forEach(id -> database.remove(id));
    }

    @Override
    public Optional<Set<Chapter>> findByAdventure(String adventure) {
        if (database == null || database.isEmpty())
            return Optional.empty();

        var chapters = database
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().adventure().equals(adventure))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());

        if (chapters.isEmpty())
            return Optional.empty();
        else
            return Optional.of(chapters);
    }

    @Override
    public Optional<Chapter> findById(String adventure, String chapter) {
        return Optional.empty();
    }

    @Override
    public void invalidateCache() {

    }

}
