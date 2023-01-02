package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChapterLinkRepository implements ChapterLinkDomainRepository {

    private final ChapterLinkJpaRepository chapterLinkJpaRepository;
    private final RecordJpaRepository recordJpaRepository;
    private final ChapterRepository chapterJpaRepository;

    @Autowired
    public ChapterLinkRepository(ChapterLinkJpaRepository chapterLinkJpaRepository, RecordJpaRepository recordJpaRepository, ChapterRepository chapterJpaRepository) {
        this.chapterLinkJpaRepository = chapterLinkJpaRepository;
        this.recordJpaRepository = recordJpaRepository;
        this.chapterJpaRepository = chapterJpaRepository;
    }


    @Override
    public void save(String adventure, ChapterLink chapterLink) {
        Integer maxIndex = recordJpaRepository.findMaxIndexByAdventureAndChapter(adventure, chapterLink.from().name());
        int newIndex;
        if (maxIndex == null)
            newIndex = 0;
        else
            newIndex = maxIndex + 1;

        recordJpaRepository.save(new RecordJpa(adventure, chapterLink.from().name(), newIndex));
        chapterLinkJpaRepository.save(new ChapterLinkJpa(adventure,
                chapterLink.from().name(),
                newIndex,
                chapterLink.to().name()));
    }

    @Override
    public Set<ChapterLink> findByAdventure(String adventure) {
        var links = chapterLinkJpaRepository.findByAdventure(adventure);
        if (links.isEmpty()) return Collections.emptySet();
        return links
                .stream()
                .map(chapterLinkJpa -> {
                    var chapterFrom = chapterJpaRepository.findById(adventure, chapterLinkJpa.chapterFrom());
                    var chapterTo = chapterJpaRepository.findById(adventure, chapterLinkJpa.to());

                    if (chapterFrom.isEmpty() || chapterTo.isEmpty()) {
                        return null;
                    }
                    return new ChapterLink(chapterFrom.get(), chapterTo.get());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {
        chapterLinkJpaRepository.deleteAll();
    }
}
