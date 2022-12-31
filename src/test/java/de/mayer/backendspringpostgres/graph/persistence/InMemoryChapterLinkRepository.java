package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryChapterLinkRepository implements ChapterLinkRepository {
    private HashMap<ChapterLinkId, ChapterLink> database;

    @Override
    public void save(ChapterLinkId chapterLinkId, ChapterLink chapterLink) {
        if (this.database == null) {
            this.database = new HashMap<>();
        }

        this.database.put(chapterLinkId, chapterLink);

    }

    @Override
    public Optional<Set<ChapterLink>> findByAdventure(String adventure) {
        var chapterLinks = database
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().adventure().equals(adventure))
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
