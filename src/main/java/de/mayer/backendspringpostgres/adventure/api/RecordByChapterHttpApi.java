package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.model.RecordInAChapter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public interface RecordByChapterHttpApi {
    String PATH = "/record/{adventureName}/{chapterName}";
    @GetMapping(PATH)
    default ResponseEntity<List<RecordInAChapter>> getRecordsByChapterName(@PathVariable("adventureName") String adventure,
                                                                          @PathVariable("chapterName") String chapterName) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping(PATH)
    default ResponseEntity<Void> deleteRecordsByChapterName(@PathVariable("adventureName") String adventure,
                                           @PathVariable("chapterName") String chapterName) {
        throw new RuntimeException("Not yet implemented!");
    }

}
