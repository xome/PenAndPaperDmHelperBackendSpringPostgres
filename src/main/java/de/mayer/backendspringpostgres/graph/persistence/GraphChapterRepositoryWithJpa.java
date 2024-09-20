package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.penandpaperdmhelperjcore.graph.domainservice.ChapterRepository;
import de.mayer.penandpaperdmhelperjcore.graph.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GraphChapterRepositoryWithJpa implements ChapterRepository {

    private final GraphChapterJpaRepository jpaRepository;

    private final ConcurrentMapCacheManager jpaCache;

    private final GraphAdventureJpaRepository adventureJpaRepository;

    @Autowired
    public GraphChapterRepositoryWithJpa(GraphChapterJpaRepository jpaRepository, ConcurrentMapCacheManager jpaCache, GraphAdventureJpaRepository adventureJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.jpaCache = jpaCache;
        this.adventureJpaRepository = adventureJpaRepository;
    }

    public static Chapter mapJpaToDomain(ChapterJpa chapterJpa) {
        return new Chapter(chapterJpa.getName(), chapterJpa.getApproximateDurationInMinutes().intValue());
    }


    @Override
    public Set<Chapter> findByAdventure(String adventure) {
        return findJpasByAdventureName(adventure)
                .stream()
                .map(GraphChapterRepositoryWithJpa::mapJpaToDomain)
                .collect(Collectors.toSet());

    }

    public Set<ChapterJpa> findJpasByAdventureName(String adventure){
        var adventureJpa = adventureJpaRepository.findByName(adventure);
        if (adventureJpa.isEmpty()){
            return Collections.emptySet();
        }
        return jpaRepository.findByAdventure(adventureJpa.get().getId());
    }

    @Override
    public Optional<Chapter> findById(String adventure, String chapter) {
        var adventureJpa = adventureJpaRepository.findByName(adventure);
        return adventureJpa.flatMap(jpa -> jpaRepository
                .findByAdventureAndName(jpa.getId(), chapter)
                .map(GraphChapterRepositoryWithJpa::mapJpaToDomain));
    }

    public Optional<ChapterJpa> findById(Long id){
        return jpaRepository.findById(id);
    }

    @Override
    public void invalidateCache() {
        var chapterCache = jpaCache.getCache("graphChaptersByAdventure");
        if (chapterCache != null){
            chapterCache.invalidate();
        }
    }
}
