package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryChapterDomainRepository implements ChapterDomainRepository {

    private HashMap<ChapterJpaId, Chapter> database;

    @Override
    public void save(String adventure, Chapter chapter) {
        if (database == null) {
            database = new HashMap<>();
        }

        database.put(new ChapterJpaId(adventure, chapter.name()), chapter);
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

    @Override
    public void deleteAll() {
        database.clear();
    }

    @Override
    public Optional<Chapter> findById(String adventure, String chapter) {
        return Optional.empty();
    }
}
