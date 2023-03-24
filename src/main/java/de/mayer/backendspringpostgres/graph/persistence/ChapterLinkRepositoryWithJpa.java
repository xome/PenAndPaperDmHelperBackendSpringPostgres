package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class ChapterLinkRepositoryWithJpa implements ChapterLinkRepository {

    private final ChapterLinkJpaRepository chapterLinkJpaRepository;
    private final ChapterRepositoryWithJpa chapterJpaRepository;
    private final RecordJpaRepository recordJpaRepository;
    private final ConcurrentMapCacheManager jpaCache;

    @Autowired
    public ChapterLinkRepositoryWithJpa(ChapterLinkJpaRepository chapterLinkJpaRepository, ChapterRepositoryWithJpa chapterDomainRepository, RecordJpaRepository recordJpaRepository, ConcurrentMapCacheManager jpaCache) {
        this.chapterLinkJpaRepository = chapterLinkJpaRepository;
        this.chapterJpaRepository = chapterDomainRepository;
        this.recordJpaRepository = recordJpaRepository;
        this.jpaCache = jpaCache;
    }

    @Override
    public Set<ChapterLink> findByAdventure(String adventure) {
        Set<ChapterJpa> chapters = chapterJpaRepository.findJpasByAdventureName(adventure);
        var chapterLinks = new HashSet<ChapterLink>();
        chapters.forEach(chapter -> {
            var jpaRecords = recordJpaRepository.findByChapterId(chapter.getId());
            jpaRecords.stream().map(recordJpa -> {
                var chapterJpaLink = chapterLinkJpaRepository.findByRecordId(recordJpa.getId());
                if (chapterJpaLink.isEmpty()) return null;
                var chapterTo = chapterJpaRepository.findById(chapterJpaLink.get().getTo()).map(ChapterRepositoryWithJpa::mapJpaToDomain);
                if (chapterTo.isEmpty()) return null;
                var chapterFrom = ChapterRepositoryWithJpa.mapJpaToDomain(chapter);
                return new ChapterLink(chapterFrom, chapterTo.get());
            }).filter(Objects::nonNull).forEach(chapterLinks::add);
        });
        return chapterLinks;
    }

    @Override
    public void invalidateCache() {
        var chapterCache = jpaCache.getCache("graphChapterLinkCache");
        if (chapterCache != null){
            chapterCache.invalidate();
        }
    }

}
