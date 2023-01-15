package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChapterRepository implements ChapterDomainRepository {

    private final ChapterJpaRepository jpaRepository;

    private final ConcurrentMapCacheManager jpaCache;

    @Autowired
    public ChapterRepository(ChapterJpaRepository jpaRepository, ConcurrentMapCacheManager jpaCache) {
        this.jpaRepository = jpaRepository;
        this.jpaCache = jpaCache;
    }

    private static Chapter mapJpaToDomain(ChapterJpa chapterJpa) {
        return new Chapter(chapterJpa.getName(), chapterJpa.getApproximateDurationInMinutes());
    }


    @Override
    public Optional<Set<Chapter>> findByAdventure(String adventure) {

        var jpaChapters = jpaRepository.findByAdventure(adventure);
        if (jpaChapters.isEmpty()) return Optional.empty();
        return Optional.of(jpaChapters
                .stream()
                .map(ChapterRepository::mapJpaToDomain)
                .collect(Collectors.toSet()));

    }

    @Override
    public Optional<Chapter> findById(String adventure, String chapter) {
        return jpaRepository
                .findById(new ChapterJpaId(adventure, chapter))
                .map(ChapterRepository::mapJpaToDomain);
    }

    @Override
    public void invalidateCache() {
        var chapterCache = jpaCache.getCache("graphChaptersByAdventure");
        if (chapterCache != null){
            chapterCache.invalidate();
        }
    }
}
