package de.mayer.backendspringpostgres.adventure.story.records.apiservice;

import de.mayer.backendspringpostgres.adventure.story.records.model.RecordInAChapter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController("/record/{adventureName}/{chapterName}/{index}")
public interface RecordByChapterNameAndIndexHttpApi {

    @GetMapping
    default RecordInAChapter getRecordByNameAndIndex(@PathVariable("adventureName") String adventure,
                                                    @PathVariable("chapterName") String chapterName,
                                                    @PathVariable("index") Integer index,
                                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PutMapping
    default void putRecordByChapterNameAndIndex(@PathVariable("adventureName") String adventure,
                                               @PathVariable("chapterName") String chapterName,
                                                  @PathVariable("index") Integer index,
                                                  HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    default void patchRecordByChapterNameAndIndex(@PathVariable("adventureName") String adventure,
                                                 @PathVariable("chapterName") String chapterName,
                                                    @PathVariable("index") Integer index,
                                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping
    default void deleteRecordByChapterNameAndIndex(@PathVariable("adventureName") String adventure,
                                                  @PathVariable("chapterName") String chapterName,
                                                  @PathVariable("index") Integer index,
                                                  HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }
    
    

}
