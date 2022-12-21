package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryChapterRepository implements ChapterRepository {

    private HashMap<ChapterId, Chapter> database;

    @Override
    public void save(ChapterId chapterId, Chapter chapter) {
        if (database == null) {
            database = new HashMap<>();
        }

        database.put(chapterId, chapter);
    }

    @Override
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
}
