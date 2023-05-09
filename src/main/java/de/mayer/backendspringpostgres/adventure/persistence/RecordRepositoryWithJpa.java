package de.mayer.backendspringpostgres.adventure.persistence;

import de.mayer.backendspringpostgres.adventure.domainservice.ChapterNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.ChapterToNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.RecordRepository;
import de.mayer.backendspringpostgres.adventure.model.*;
import de.mayer.backendspringpostgres.adventure.persistence.dto.*;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;
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
                       RecordInAChapter record,
                       Integer index)
            throws ChapterNotFoundException, ChapterToNotFoundException {
        var adventureJpa = adventureJpaRepository.findByName(adventure.name());
        if (adventureJpa.isEmpty()) {
            throw new ChapterNotFoundException();
        }

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapter.name());
        if (chapterJpa.isEmpty()) {
            throw new ChapterNotFoundException();
        }
        if (index == null) {
            index = chapter.records() != null ? chapter.records().size() : 0;
        }

        int indexToPutRecord;
        var currentMaxIndex = recordJpaRepository.findMaxIndexByChapterId(chapterJpa.get().getId());
        if (currentMaxIndex == null) {
            indexToPutRecord = 0;
        } else {
            if (index < currentMaxIndex){
                var recordsToUpdate = recordJpaRepository
                        .findByChapterIdAndIndexGreaterThanEqual(chapterJpa.get().getId(), index)
                        .stream()
                        .peek(recordJpa -> recordJpa.setIndex(recordJpa.getIndex() + 1)).toList();

                recordJpaRepository.saveAll(recordsToUpdate);
                indexToPutRecord = index;
            } else {
                indexToPutRecord = currentMaxIndex.intValue() + 1;
            }
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
                indexToPutRecord,
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
                        link.chapterNameTo())
                        .orElseThrow(ChapterToNotFoundException::new);
                chapterLinkJpaRepository.save(new ChapterLinkJpa(recordJpa, chapterTo.getId()));
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

        allJpaRecords
                .stream()
                .sorted(Comparator.comparing(RecordJpa::getIndex))
                .map(this::mapRecordJpaToIndexAndModelDto)
                .forEachOrdered(pairOfIndexAndRecordInAChapter -> {
                    if (pairOfIndexAndRecordInAChapter.getSecond().isPresent())
                        records.add(pairOfIndexAndRecordInAChapter.getFirst(),
                                pairOfIndexAndRecordInAChapter.getSecond().get());
                    else
                        throw new RuntimeException("Inconsistency at Record with index %d!"
                                .formatted(pairOfIndexAndRecordInAChapter.getFirst()));
                });


        return records;

    }

    private Pair<Integer, Optional<RecordInAChapter>> mapRecordJpaToIndexAndModelDto(RecordJpa recordJpa) {
        return switch (recordJpa.getType()) {
            case Text -> {
                var optionalText = textJpaRepository
                        .findByRecordJpa(recordJpa);
                if (optionalText.isPresent())
                    yield Pair.of(recordJpa.getIndex(),
                            Optional.of(new Text(optionalText.get().getText())));
                yield Pair.of(recordJpa.getIndex(), Optional.empty());
            }


            case Picture -> {
                var optionalPicture =
                        pictureJpaRepository
                                .findByRecordJpa(recordJpa);
                if (optionalPicture.isPresent()) {
                    var pictureJpa = optionalPicture.get();
                    yield Pair.of(recordJpa.getIndex(),
                            Optional.of(new Picture(pictureJpa.getBase64(),
                                    pictureJpa.getFileFormat(),
                                    pictureJpa.getShareableWithGroup())));
                }
                yield Pair.of(recordJpa.getIndex(), Optional.empty());
            }


            case ChapterLink -> {
                var optionalLink = chapterLinkJpaRepository.findByRecordJpa(recordJpa);
                if (optionalLink.isPresent()) {
                    var chapterTo = chapterJpaRepository.findById(optionalLink.get().getChapterTo());
                    if (chapterTo.isPresent())
                        yield Pair.of(recordJpa.getIndex(),
                                Optional.of(new ChapterLink(chapterTo.get().getName())));

                    log.error("ChapterTo {} not found. Deleting ChapterLink.",
                            optionalLink.get().getChapterTo());
                    chapterLinkJpaRepository.delete(optionalLink.get());
                }
                yield Pair.of(recordJpa.getIndex(), Optional.empty());
            }

            case EnvironmentLightning -> {
                var optionalLightning = environmentLightningJpaRepository
                        .findByRecordJpa(recordJpa);

                if (optionalLightning.isPresent()) {
                    var environmentLightningJpa = optionalLightning.get();
                    yield Pair.of(recordJpa.getIndex(), Optional.of(
                            new EnvironmentLightning(environmentLightningJpa.getBrightness(),
                                    new int[]{environmentLightningJpa.getRgb1(),
                                            environmentLightningJpa.getRgb2(),
                                            environmentLightningJpa.getRgb3()})));
                }
                yield Pair.of(recordJpa.getIndex(), Optional.empty());
            }

            case Music -> {
                var optionalMusic = backgroundMusicJpaRepository
                        .findByRecordJpa(recordJpa);

                if (optionalMusic.isPresent()) {
                    var backgroundMusicJpa = optionalMusic.get();
                    yield Pair.of(recordJpa.getIndex(),
                            Optional.of(new BackgroundMusic(backgroundMusicJpa.getName(),
                                    backgroundMusicJpa.getBase64())));
                }
                yield Pair.of(recordJpa.getIndex(), Optional.empty());
            }
        };
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
    public void createMultiple(Adventure adventure, Chapter chapter, List<RecordInAChapter> records) throws ChapterNotFoundException, ChapterToNotFoundException {
        for (var record : records) {
            create(adventure, chapter, record, null);
        }
    }

    @Override
    public void deleteAllChapterLinksReferencing(String adventureName, String chapterName) {
        var adventureJpa = adventureJpaRepository.findByName(adventureName);
        if (adventureJpa.isEmpty()){
            return; // There is nothing to delete
        }

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapterName);
        if (chapterJpa.isEmpty()) {
            return; // Neither is in this case
        }

        var chapterLinkJpas = chapterLinkJpaRepository.findByChapterTo(chapterJpa.get().getId());
        var recordJpasToDelete = chapterLinkJpas.stream().map(ChapterLinkJpa::getRecordJpa).collect(Collectors.toList());

        chapterLinkJpaRepository.deleteAll(chapterLinkJpas);
        recordJpaRepository.deleteAll(recordJpasToDelete);
    }

    @Override
    public Optional<RecordInAChapter> readByAdventureAndChapterAndIndex(String adventure, String chapter, Integer index) {
        var adventureJpa = adventureJpaRepository.findByName(adventure);
        if (adventureJpa.isEmpty()) return Optional.empty();

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapter);
        if (chapterJpa.isEmpty()) return Optional.empty();

        var recordJpa = recordJpaRepository.findByChapterIdAndIndex(chapterJpa.get().getId(), index);
        return recordJpa.flatMap(jpa -> mapRecordJpaToIndexAndModelDto(jpa).getSecond());
    }

    @Override
    public void create(String adventure, String chapterName, Integer index, RecordInAChapter record)
            throws ChapterNotFoundException, ChapterToNotFoundException {
        var adventureJpa = adventureJpaRepository.findByName(adventure);
        if (adventureJpa.isEmpty()) throw new ChapterNotFoundException();

        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.get().getId(), chapterName);
        if (chapterJpa.isEmpty()) throw new ChapterNotFoundException();

        var currentRecords = readByAdventureAndChapter(adventure, chapterName);

        create(new Adventure(adventure, null),
                new Chapter(chapterJpa.get().getName(),
                        null,
                        null,
                        currentRecords),
                record,
                index);

    }

    @Override
    public void update(String adventure, String chapterName, Integer index, RecordInAChapter record)
            throws RecordNotFoundException, ChapterNotFoundException, ChapterToNotFoundException {
        var adventureJpa = adventureJpaRepository.findByName(adventure)
                .orElseThrow(ChapterNotFoundException::new);
        var chapterJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.getId(), chapterName)
                .orElseThrow(ChapterNotFoundException::new);
        var recordJpa = recordJpaRepository.findByChapterIdAndIndex(chapterJpa.getId(), index)
                .orElseThrow(RecordNotFoundException::new);
        switch (recordJpa.getType()) {
            case ChapterLink -> {
                var linkModel = (ChapterLink) record;
                var linkJpa = chapterLinkJpaRepository.findByRecordJpa(recordJpa)
                        .orElseThrow(RecordNotFoundException::new);
                var chapterToJpa = chapterJpaRepository.findByAdventureAndName(adventureJpa.getId(),
                        linkModel.chapterNameTo())
                        .orElseThrow(ChapterToNotFoundException::new);
                linkJpa.setId(chapterToJpa.getId());
                chapterLinkJpaRepository.save(linkJpa);
            }
            case EnvironmentLightning -> {
                var envModel = (EnvironmentLightning) record;
                var envJpa = environmentLightningJpaRepository.findByRecordJpa(recordJpa)
                        .orElseThrow(RecordNotFoundException::new);
                envJpa.setBrightness(envModel.brightness());
                envJpa.setRgb1(envModel.rgb()[0]);
                envJpa.setRgb2(envModel.rgb()[1]);
                envJpa.setRgb3(envModel.rgb()[2]);
                environmentLightningJpaRepository.save(envJpa);
            }

            case Picture -> {
                var picModel = (Picture) record;
                var picJpa = pictureJpaRepository.findByRecordJpa(recordJpa)
                        .orElseThrow(RecordNotFoundException::new);
                picJpa.setShareableWithGroup(picModel.isShareableWithGroup());
                picJpa.setBase64(picModel.base64());
                picJpa.setFileFormat(picModel.fileFormat());
                pictureJpaRepository.save(picJpa);

            }
            case Text -> {
                var textModel = (Text) record;
                var textJpa = textJpaRepository.findByRecordJpa(recordJpa)
                        .orElseThrow(RecordNotFoundException::new);
                textJpa.setText(textModel.text());
                textJpaRepository.save(textJpa);
            }
            case Music -> {
                var musicModel = (BackgroundMusic) record;
                var musicJpa = backgroundMusicJpaRepository.findByRecordJpa(recordJpa)
                        .orElseThrow(RecordNotFoundException::new);
                musicJpa.setBase64(musicModel.base64());
                musicJpa.setName(musicModel.name());
                backgroundMusicJpaRepository.save(musicJpa);
            }
        }
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
