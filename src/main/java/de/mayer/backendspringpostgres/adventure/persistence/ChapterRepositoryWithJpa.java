package de.mayer.backendspringpostgres.adventure.persistence;


import de.mayer.backendspringpostgres.adventure.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.adventure.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ChapterRepositoryWithJpa implements ChapterRepository {

    private final ChapterJpaRepository chapterJpaRepository;
    private final AdventureJpaRepository adventureJpaRepository;
    private final RecordJpaRepository recordJpaRepository;
    private final TextJpaRepository textJpaRepository;
    private final PictureJpaRepository pictureJpaRepository;
    private final BackgroundMusicRepository backgroundMusicRepository;
    private final ChapterLinkJpaRepository chapterLinkJpaRepository;
    private final EnvironmentLightningJpaRepository environmentLightningJpaRepository;

    private final Logger log = LoggerFactory.getLogger(ChapterRepositoryWithJpa.class);

    public ChapterRepositoryWithJpa(ChapterJpaRepository chapterJpaRepository,
                                    AdventureJpaRepository adventureJpaRepository, RecordJpaRepository recordJpaRepository,
                                    TextJpaRepository textJpaRepository,
                                    PictureJpaRepository pictureJpaRepository,
                                    BackgroundMusicRepository backgroundMusicRepository,
                                    ChapterLinkJpaRepository chapterLinkJpaRepository,
                                    EnvironmentLightningJpaRepository environmentLightningJpaRepository) {
        this.chapterJpaRepository = chapterJpaRepository;
        this.adventureJpaRepository = adventureJpaRepository;
        this.recordJpaRepository = recordJpaRepository;
        this.textJpaRepository = textJpaRepository;
        this.pictureJpaRepository = pictureJpaRepository;
        this.backgroundMusicRepository = backgroundMusicRepository;
        this.chapterLinkJpaRepository = chapterLinkJpaRepository;
        this.environmentLightningJpaRepository = environmentLightningJpaRepository;
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

        var allJpaRecords = recordJpaRepository.findByChapterIdOrderByIndex(chapterJpa.getId());
        var records = new LinkedList<RecordInAChapter>();

        var recordsToDelete = new ArrayList<RecordJpa>();

        allJpaRecords
                .forEach(recordJpa -> {

                    AtomicBoolean subTypeFound = new AtomicBoolean(true);

                    switch (recordJpa.getType()) {
                        case Text -> textJpaRepository
                                .findByRecordId(recordJpa.getId())
                                .ifPresentOrElse(textJpa -> records.add(recordJpa.getIndex(),
                                                new Text(textJpa.getText())),
                                        () -> subTypeFound.set(false));

                        case Picture -> pictureJpaRepository
                                .findByRecordId(recordJpa.getId())
                                .ifPresentOrElse(pictureJpa -> records.add(recordJpa.getIndex(),
                                                new Picture(pictureJpa.getBase64(),
                                                        pictureJpa.getFileFormat(),
                                                        pictureJpa.getShareableWithGroup())),
                                        () -> subTypeFound.set(false));

                        case ChapterLink -> {
                            var optionalLink = chapterLinkJpaRepository.findByRecordId(recordJpa.getId());
                            if (optionalLink.isPresent()) {
                                var chapterTo = chapterJpaRepository.findById(optionalLink.get().getChapterTo());
                                chapterTo
                                        .ifPresentOrElse(
                                                chapterToJpa -> records.add(recordJpa.getIndex(),
                                                        new ChapterLink(chapterToJpa.getName())),
                                                () -> {
                                                    throw new RuntimeException("ChapterTo not found.");
                                                });
                            } else {
                                subTypeFound.set(false);
                            }
                        }

                        case EnvironmentLightning -> environmentLightningJpaRepository
                                .findByRecordId(recordJpa.getId())
                                .ifPresentOrElse(environmentLightningJpa -> records.add(
                                                new EnvironmentLightning(environmentLightningJpa.getBrightness(),
                                                        new int[]{environmentLightningJpa.getRgb1(),
                                                                environmentLightningJpa.getRgb2(),
                                                                environmentLightningJpa.getRgb3()})),
                                        () -> subTypeFound.set(false));

                        case Music -> backgroundMusicRepository
                                .findByRecordId(recordJpa.getId())
                                .ifPresentOrElse(backgroundMusicJpa -> records.add(recordJpa.getIndex(),
                                                new BackgroundMusic(backgroundMusicJpa.getName(),
                                                        backgroundMusicJpa.getBase64())),
                                        () -> subTypeFound.set(false));

                        default -> {
                            log.error("Subtype not implemented: %s".formatted(recordJpa.getType()));
                            subTypeFound.set(false);
                        }
                    }

                    if (!subTypeFound.get()) {
                        log.error("No subtype record found for Record. It will be deleted: %s"
                                .formatted(recordJpa));
                        recordsToDelete.add(recordJpa);
                    }
                });

        recordJpaRepository.deleteAll(recordsToDelete);


        return new Chapter(chapterJpa.getName(),
                chapterJpa.getSubheader(),
                chapterJpa.getApproximateDurationInMinutes().intValue(),
                records);
    }
}
