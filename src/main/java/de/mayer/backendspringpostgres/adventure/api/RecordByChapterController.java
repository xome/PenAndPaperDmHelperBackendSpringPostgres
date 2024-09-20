package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.ChapterNotFoundException;
import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.RecordRepository;
import de.mayer.penandpaperdmhelperjcore.adventure.model.RecordInAChapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RecordByChapterController implements RecordByChapterHttpApi {

    private final RecordRepository recordRepository;

    public RecordByChapterController(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public ResponseEntity<List<RecordInAChapter>> getRecordsByChapterName(String adventure, String chapterName) {
        try {
            return ResponseEntity.ok(recordRepository.read(adventure, chapterName));
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteRecordsByChapterName(String adventure, String chapterName) {
        try {
            recordRepository.delete(adventure, chapterName);
            return ResponseEntity.ok().build();
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
