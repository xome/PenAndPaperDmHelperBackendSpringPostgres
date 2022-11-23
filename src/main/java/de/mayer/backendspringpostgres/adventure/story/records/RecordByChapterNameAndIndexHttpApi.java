package de.mayer.backendspringpostgres.adventure.story.records;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController("/record/{chapterName}/{index}")
public class RecordByChapterNameAndIndexHttpApi {

    @GetMapping
    public RecordInAChapter getRecordByNameAndIndex(@PathVariable("chapterName") String chapterName,
                                                    @PathVariable("index") Integer index,
                                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PutMapping
    public void putRecordByChapterNameAndIndex(@PathVariable("chapterName") String chapterName,
                                                  @PathVariable("index") Integer index,
                                                  HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    public void patchRecordByChapterNameAndIndex(@PathVariable("chapterName") String chapterName,
                                                    @PathVariable("index") Integer index,
                                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping
    public void deleteRecordByChapterNameAndIndex(@PathVariable("chapterName") String chapterName,
                                                  @PathVariable("index") Integer index,
                                                  HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }
    
    

}
