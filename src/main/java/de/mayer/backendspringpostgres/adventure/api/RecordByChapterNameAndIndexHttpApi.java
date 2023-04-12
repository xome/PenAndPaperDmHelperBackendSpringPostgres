package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.model.RecordInAChapter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public interface RecordByChapterNameAndIndexHttpApi {

    String PATH = "/record/{adventureName}/{chapterName}/{index}";

    @GetMapping(PATH)
    default ResponseEntity<RecordInAChapter> getRecordByNameAndIndex(@PathVariable("adventureName") String adventure,
                                                                    @PathVariable("chapterName") String chapterName,
                                                                    @PathVariable("index") Integer index) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PutMapping(PATH)
    default ResponseEntity<Void> putRecordByChapterNameAndIndex(@PathVariable("adventureName") String adventure,
                                        @PathVariable("chapterName") String chapterName,
                                        @PathVariable("index") Integer index) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping(PATH)
    default ResponseEntity<Void> patchRecordByChapterNameAndIndex(@PathVariable("adventureName") String adventure,
                                                 @PathVariable("chapterName") String chapterName,
                                                    @PathVariable("index") Integer index) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping(PATH)
    default ResponseEntity<Void> deleteRecordByChapterNameAndIndex(@PathVariable("adventureName") String adventure,
                                                  @PathVariable("chapterName") String chapterName,
                                                  @PathVariable("index") Integer index) {
        throw new RuntimeException("Not yet implemented!");
    }
    
    

}
