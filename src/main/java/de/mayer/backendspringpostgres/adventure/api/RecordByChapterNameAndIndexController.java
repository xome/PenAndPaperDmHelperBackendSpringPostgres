package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.domainservice.ChapterNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.ChapterToNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.RecordRepository;
import de.mayer.backendspringpostgres.adventure.model.RecordInAChapter;
import de.mayer.backendspringpostgres.adventure.model.RecordNotFoundException;
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
        var optionalRecord = recordRepository.read(adventure, chapterName, index);
        return optionalRecord
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @Override
    public ResponseEntity<Void> putRecordByChapterNameAndIndex(String adventure,
                                                               String chapterName,
                                                               Integer index,
                                                               RecordInAChapter record) {
        try {
            recordRepository.create(adventure, chapterName, index, record);
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ChapterToNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> patchRecordByChapterNameAndIndex(String adventure,
                                                                 String chapterName,
                                                                 Integer index,
                                                                 RecordInAChapter record) {
        try {
            recordRepository.update(adventure, chapterName, index, record);
        } catch (RecordNotFoundException | ChapterNotFoundException  e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ChapterToNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteRecordByChapterNameAndIndex(String adventure, String chapterName, Integer index) {
        try {
            recordRepository.delete(adventure, chapterName, index);
        } catch (ChapterNotFoundException | RecordNotFoundException e) {
            return ResponseEntity.status((HttpStatus.NOT_FOUND)).build();
        }
        return ResponseEntity.ok().build();
    }
}
