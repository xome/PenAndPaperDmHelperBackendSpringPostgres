package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChapterLinkRepository implements ChapterLinkDomainRepository {

    private final ChapterLinkJpaRepository chapterLinkJpaRepository;
    private final ChapterDomainRepository chapterDomainRepository;
    private final ConcurrentMapCacheManager jpaCache;

    @Autowired
    public ChapterLinkRepository(ChapterLinkJpaRepository chapterLinkJpaRepository,
                                 ChapterDomainRepository chapterDomainRepository, ConcurrentMapCacheManager jpaCache) {
        this.chapterLinkJpaRepository = chapterLinkJpaRepository;
        this.chapterDomainRepository = chapterDomainRepository;
        this.jpaCache = jpaCache;
    }

    @Override
    public Set<ChapterLink> findByAdventure(String adventure) {
        var links = chapterLinkJpaRepository.findByAdventure(adventure);
        if (links.isEmpty()) return Collections.emptySet();
        return links
                .stream()
                .map(chapterLinkJpa -> {
                    var chapterFrom = chapterDomainRepository.findById(adventure, chapterLinkJpa.chapterFrom());
                    var chapterTo = chapterDomainRepository.findById(adventure, chapterLinkJpa.to());

                    if (chapterFrom.isEmpty() || chapterTo.isEmpty()) {
                        return null;
                    }
                    return new ChapterLink(chapterFrom.get(), chapterTo.get());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public void invalidateCache() {
        var chapterCache = jpaCache.getCache("graphChapterLinkCache");
        if (chapterCache != null){
            chapterCache.invalidate();
        }
    }

}
