package de.mayer.backendspringpostgres.adventure.persistence;


import de.mayer.backendspringpostgres.adventure.domainservice.ChapterAlreadyExistsException;
import de.mayer.backendspringpostgres.adventure.domainservice.ChapterNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.adventure.domainservice.RecordRepository;
import de.mayer.backendspringpostgres.adventure.model.Adventure;
import de.mayer.backendspringpostgres.adventure.model.Chapter;
import de.mayer.backendspringpostgres.adventure.persistence.dto.ChapterJpa;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.AdventureJpaRepository;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.ChapterJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChapterRepositoryWithJpa implements ChapterRepository {

    private final ChapterJpaRepository chapterJpaRepository;
    private final AdventureJpaRepository adventureJpaRepository;
    private final RecordRepository recordRepository;

    public ChapterRepositoryWithJpa(ChapterJpaRepository chapterJpaRepository,
                                    AdventureJpaRepository adventureJpaRepository,
                                    RecordRepository recordRepository) {
        this.chapterJpaRepository = chapterJpaRepository;
        this.adventureJpaRepository = adventureJpaRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public Optional<Chapter> findById(String adventureName, String chapterName) {
        var adventureOptional = adventureJpaRepository.findByName(adventureName);
        if (adventureOptional.isEmpty())
            return Optional.empty();

        var optionalChapter = chapterJpaRepository
                .findByAdventureAndName(adventureOptional.get().getId(), chapterName);

        if (optionalChapter.isPresent()) {
            try {
                return Optional.of(mapJpaToDomain(adventureName, optionalChapter.get()));
            } catch (ChapterNotFoundException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    @Override
    public void updateChapter(String adventure, String nameOfChapterToBeUpdated, Chapter chapterWithNewData)
            throws ChapterNotFoundException, ChapterAlreadyExistsException {

        var optionalAdventureJpa = adventureJpaRepository.findByName(adventure);
        if (optionalAdventureJpa.isEmpty()) {
            throw new ChapterNotFoundException();
        }
        var adventureJpa = optionalAdventureJpa.get();

        var optionalChapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.getId(),
                nameOfChapterToBeUpdated);

        if (optionalChapterJpa.isEmpty()) {
            throw new ChapterNotFoundException();
        }
        var chapterJpa = optionalChapterJpa.get();

        if (chapterWithNewData.name() != null) {
            var possibleChapterWithConflictingName =
                    chapterJpaRepository.findByAdventureAndName(adventureJpa.getId(), chapterWithNewData.name());

            if (possibleChapterWithConflictingName.isPresent()
                    && !possibleChapterWithConflictingName.get().getId().equals(chapterJpa.getId()))
                throw new ChapterAlreadyExistsException();

            chapterJpa.setName(chapterWithNewData.name());
        }

        if (chapterWithNewData.subheader() != null) {
            chapterJpa.setSubheader(chapterWithNewData.subheader());
        }

        if (chapterWithNewData.approximateDurationInMinutes() != null) {
            chapterJpa.setApproximateDurationInMinutes(Long.valueOf(chapterWithNewData.approximateDurationInMinutes()));
        }

        var chapter = new Chapter(chapterJpa.getName(),
                null,
                null,
                null);

        if (chapterWithNewData.records() != null) {
            recordRepository.deleteAllByAdventureAndChapter(adventure, nameOfChapterToBeUpdated);
            recordRepository.saveRecords(
                    new Adventure(adventureJpa.getName(), null),
                    chapter,
                    chapterWithNewData.records());
        }

        chapterJpaRepository.save(chapterJpa);
    }

    private Chapter mapJpaToDomain(String adventureName, ChapterJpa chapterJpa) throws ChapterNotFoundException {

        var records = recordRepository.findRecordsByAdventureAndChapter(adventureName, chapterJpa.getName());

        return new Chapter(chapterJpa.getName(),
                chapterJpa.getSubheader(),
                chapterJpa.getApproximateDurationInMinutes().intValue(),
                records);
    }
}
