package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkDomainRepository;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class ChapterLinkRepository implements ChapterLinkDomainRepository {

    private ChapterLinkJpaRepository chapterLinkJpaRepository;
    private RecordJpaRepository recordJpaRepository;

    @Autowired
    public ChapterLinkRepository(ChapterLinkJpaRepository chapterLinkJpaRepository, RecordJpaRepository recordJpaRepository) {
        this.chapterLinkJpaRepository = chapterLinkJpaRepository;
        this.recordJpaRepository = recordJpaRepository;
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
    public Optional<Set<ChapterLink>> findByAdventure(String adventure) {
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
