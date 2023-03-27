package de.mayer.backendspringpostgres.adventure.persistence;


import de.mayer.backendspringpostgres.adventure.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.adventure.model.Chapter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class ChapterRepositoryWithJpa implements ChapterRepository {

    private final ChapterJpaRepository chapterJpaRepository;
    private final AdventureJpaRepository adventureJpaRepository;

    public ChapterRepositoryWithJpa(ChapterJpaRepository chapterJpaRepository,
                                    AdventureJpaRepository adventureJpaRepository) {
        this.chapterJpaRepository = chapterJpaRepository;
        this.adventureJpaRepository = adventureJpaRepository;
    }

    @Override
    public Optional<Chapter> findById(String adventureName, String chapterName) {
        var adventureOptional = adventureJpaRepository.findByName(adventureName);
        if (adventureOptional.isEmpty())
            return Optional.empty();

        var optionalChapter = chapterJpaRepository
                .findByAdventureAndName(adventureOptional.get().getId(), chapterName);

        return optionalChapter.map(this::mapJpaToDomain);

    }

    private Chapter mapJpaToDomain(ChapterJpa chapterJpa) {
        return new Chapter(chapterJpa.getName(),
                chapterJpa.getSubheader(),
                chapterJpa.getApproximateDurationInMinutes().intValue(),
                Collections.emptyList());
    }
}
