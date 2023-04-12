package de.mayer.backendspringpostgres.adventure.persistence;

import de.mayer.backendspringpostgres.adventure.domainservice.ChapterNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.RecordRepository;
import de.mayer.backendspringpostgres.adventure.model.*;
import de.mayer.backendspringpostgres.adventure.persistence.dto.*;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class RecordRepositoryWithJpa implements RecordRepository {

    private final AdventureJpaRepository adventureJpaRepository;
    private final ChapterJpaRepository chapterJpaRepository;
    private final RecordJpaRepository recordJpaRepository;
    private final TextJpaRepository textJpaRepository;
    private final BackgroundMusicJpaRepository backgroundMusicJpaRepository;
    private final PictureJpaRepository pictureJpaRepository;
    private final EnvironmentLightningJpaRepository environmentLightningJpaRepository;
    private final ChapterLinkJpaRepository chapterLinkJpaRepository;
    private static final Logger log = LoggerFactory.getLogger(RecordRepositoryWithJpa.class);


    public RecordRepositoryWithJpa(AdventureJpaRepository adventureJpaRepository, RecordJpaRepository recordJpaRepository,
                                   ChapterJpaRepository chapterRepository,
                                   TextJpaRepository textJpaRepository,
                                   BackgroundMusicJpaRepository backgroundMusicJpaRepository,
                                   PictureJpaRepository pictureJpaRepository,
                                   EnvironmentLightningJpaRepository environmentLightningJpaRepository,
                                   ChapterLinkJpaRepository chapterLinkJpaRepository) {
        this.adventureJpaRepository = adventureJpaRepository;
        this.recordJpaRepository = recordJpaRepository;
        this.chapterJpaRepository = chapterRepository;
        this.textJpaRepository = textJpaRepository;
        this.backgroundMusicJpaRepository = backgroundMusicJpaRepository;
        this.pictureJpaRepository = pictureJpaRepository;
        this.environmentLightningJpaRepository = environmentLightningJpaRepository;
        this.chapterLinkJpaRepository = chapterLinkJpaRepository;
    }

    @Override
    public void create(Adventure adventure,
                       Chapter chapter,
                       RecordInAChapter record)
            throws ChapterNotFoundException {
        var adventureJpa = adventureJpaRepository.findByName(adventure.name());
        if (adventureJpa.isEmpty()) {
            throw new ChapterNotFoundException();
        }

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapter.name());
        if (chapterJpa.isEmpty()) {
            throw new ChapterNotFoundException();
        }

        var currentMaxIndex = recordJpaRepository.findMaxIndexByChapterId(chapterJpa.get().getId());
        if (currentMaxIndex == null) {
            currentMaxIndex = -1L;
        }

        RecordType type;
        if (record instanceof Text) {
            type = RecordType.Text;
        } else if (record instanceof BackgroundMusic) {
            type = RecordType.Music;
        } else if (record instanceof Picture) {
            type = RecordType.Picture;
        } else if (record instanceof EnvironmentLightning) {
            type = RecordType.EnvironmentLightning;
        } else if (record instanceof ChapterLink) {
            type = RecordType.ChapterLink;
        } else {
            throw new RuntimeException("%s not implemented!".formatted(record.getClass().getSimpleName()));
        }

        var recordJpa = recordJpaRepository.save(new RecordJpa(chapterJpa.get().getId(),
                currentMaxIndex.intValue() + 1,
                type
        ));

        switch (type) {
            case Text -> textJpaRepository.save(new TextJpa(recordJpa, ((Text) record).text()));
            case Picture -> {
                var picture = (Picture) record;
                pictureJpaRepository.save(new PictureJpa(recordJpa,
                        picture.base64(),
                        picture.fileFormat(),
                        picture.isShareableWithGroup()));
            }
            case ChapterLink -> {
                var link = (ChapterLink) record;
                var chapterTo = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(),
                        link.chapterNameTo());
                chapterTo.ifPresent(chapterToJpa ->
                        chapterLinkJpaRepository.save(new ChapterLinkJpa(recordJpa, chapterToJpa.getId())));
            }
            case EnvironmentLightning -> {
                var envLight = (EnvironmentLightning) record;
                environmentLightningJpaRepository.save(new EnvironmentLightningJpa(recordJpa,
                        envLight.rgb()[0],
                        envLight.rgb()[1],
                        envLight.rgb()[2],
                        envLight.brightness()));
            }
            case Music -> {
                var music = (BackgroundMusic) record;
                backgroundMusicJpaRepository.save(new BackgroundMusicJpa(recordJpa, music.name(), music.base64()));
            }
        }
    }

    @Override
    public LinkedList<RecordInAChapter> readByAdventureAndChapter(String adventureName, String chapterName) throws ChapterNotFoundException {
        var allJpaRecords = findRecordsByAdventureNameAndChapterName(adventureName, chapterName);

        var records = new LinkedList<RecordInAChapter>();
        var recordsToDelete = new ArrayList<RecordJpa>();

        allJpaRecords
                .forEach(recordJpa -> {

                    AtomicBoolean subTypeFound = new AtomicBoolean(true);

                    switch (recordJpa.getType()) {
                        case Text -> textJpaRepository
                                .findByRecordJpa(recordJpa)
                                .ifPresentOrElse(textJpa -> records.add(recordJpa.getIndex(),
                                                new Text(textJpa.getText())),
                                        () -> subTypeFound.set(false));

                        case Picture -> pictureJpaRepository
                                .findByRecordJpa(recordJpa)
                                .ifPresentOrElse(pictureJpa -> records.add(recordJpa.getIndex(),
                                                new Picture(pictureJpa.getBase64(),
                                                        pictureJpa.getFileFormat(),
                                                        pictureJpa.getShareableWithGroup())),
                                        () -> subTypeFound.set(false));

                        case ChapterLink -> {
                            var optionalLink = chapterLinkJpaRepository.findByRecordJpa(recordJpa);
                            if (optionalLink.isPresent()) {
                                var chapterTo = chapterJpaRepository.findById(optionalLink.get().getChapterTo());
                                chapterTo
                                        .ifPresentOrElse(
                                                chapterToJpa -> records.add(recordJpa.getIndex(),
                                                        new ChapterLink(chapterToJpa.getName())),
                                                () -> {
                                                    log.error("ChapterTo {} not found. Deleting ChapterLink.",
                                                            optionalLink.get().getChapterTo());
                                                    chapterLinkJpaRepository.delete(optionalLink.get());
                                                    subTypeFound.set(false);
                                                });
                            } else {
                                subTypeFound.set(false);
                            }
                        }

                        case EnvironmentLightning -> environmentLightningJpaRepository
                                .findByRecordJpa(recordJpa)
                                .ifPresentOrElse(environmentLightningJpa -> records.add(
                                                new EnvironmentLightning(environmentLightningJpa.getBrightness(),
                                                        new int[]{environmentLightningJpa.getRgb1(),
                                                                environmentLightningJpa.getRgb2(),
                                                                environmentLightningJpa.getRgb3()})),
                                        () -> subTypeFound.set(false));

                        case Music -> backgroundMusicJpaRepository
                                .findByRecordJpa(recordJpa)
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

        return records;

    }

    @Override
    public void deleteByAdventureAndChapter(String adventure, String chapter) throws ChapterNotFoundException {
        var records = findRecordsByAdventureNameAndChapterName(adventure, chapter);

        records.forEach(recordJpa -> {
            switch (recordJpa.getType()) {
                case Text -> textJpaRepository.deleteByRecordJpa(recordJpa);
                case Picture -> pictureJpaRepository.deleteByRecordJpa(recordJpa);
                case ChapterLink -> chapterLinkJpaRepository.deleteByRecordJpa(recordJpa);
                case EnvironmentLightning -> environmentLightningJpaRepository.deleteByRecordJpa(recordJpa);
                case Music -> backgroundMusicJpaRepository.deleteByRecordJpa(recordJpa);
            }
        });

        recordJpaRepository.deleteAll(records);

    }

    @Override
    public void createMultiple(Adventure adventure, Chapter chapter, List<RecordInAChapter> records) throws ChapterNotFoundException {
        for (var record : records) {
            create(adventure, chapter, record);
        }
    }

    @Override
    public void deleteAllChapterLinksReferencing(String adventureName, String chapterName) {
        var adventureJpa = adventureJpaRepository.findByName(adventureName);
        if (adventureJpa.isEmpty()){
            return; // There is nothing to delete
        }

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapterName);
        if (chapterJpa.isEmpty()){
            return; // Neither is in this case
        }

        var chapterLinkJpas = chapterLinkJpaRepository.findByChapterTo(chapterJpa.get().getId());
        var recordJpasToDelete = chapterLinkJpas.stream().map(ChapterLinkJpa::getRecordJpa).collect(Collectors.toList());

        chapterLinkJpaRepository.deleteAll(chapterLinkJpas);
        recordJpaRepository.deleteAll(recordJpasToDelete);
    }

    private List<RecordJpa> findRecordsByAdventureNameAndChapterName(String adventure, String chapter)
            throws ChapterNotFoundException {
        var adventureJpa = adventureJpaRepository.findByName(adventure);
        if (adventureJpa.isEmpty())
            throw new ChapterNotFoundException();

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapter);
        if (chapterJpa.isEmpty())
            throw new ChapterNotFoundException();

        return recordJpaRepository.findByChapterIdOrderByIndex(chapterJpa.get().getId());
    }
}
