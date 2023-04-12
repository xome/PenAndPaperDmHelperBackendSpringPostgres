package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.domainservice.RecordRepository;
import de.mayer.backendspringpostgres.adventure.model.RecordInAChapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class RecordByChapterNameAndIndexController implements RecordByChapterNameAndIndexHttpApi {

    private final RecordRepository recordRepository;

    public RecordByChapterNameAndIndexController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public ResponseEntity<RecordInAChapter> getRecordByNameAndIndex(String adventure, String chapterName, Integer index) {
        var optionalRecord = recordRepository.readByAdventureAndChapterAndIndex(adventure, chapterName, index);
        return optionalRecord
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @Override
    public ResponseEntity<Void> putRecordByChapterNameAndIndex(String adventure, String chapterName, Integer index) {
        return RecordByChapterNameAndIndexHttpApi.super.putRecordByChapterNameAndIndex(adventure, chapterName, index);
    }

    @Override
    public ResponseEntity<Void> patchRecordByChapterNameAndIndex(String adventure, String chapterName, Integer index) {
        return RecordByChapterNameAndIndexHttpApi.super.patchRecordByChapterNameAndIndex(adventure, chapterName, index);
    }

    @Override
    public ResponseEntity<Void> deleteRecordByChapterNameAndIndex(String adventure, String chapterName, Integer index) {
        return RecordByChapterNameAndIndexHttpApi.super.deleteRecordByChapterNameAndIndex(adventure, chapterName, index);
    }
}
