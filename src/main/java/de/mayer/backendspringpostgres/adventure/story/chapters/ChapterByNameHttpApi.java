package de.mayer.backendspringpostgres.adventure.story.chapters;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController("/chapter/{adventureName}/{chapterName}")
public class ChapterByNameHttpApi {

    @GetMapping
    public Chapter getChapterByName(@PathVariable("adventureName") String adventure,
                                    @PathVariable("chapterName") String chapterName,
                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    public void patchChapterByName(@PathVariable("adventureName") String adventure,
                                   @PathVariable("chapterName") String chapterName,
                                   @RequestBody Chapter chapter,
                                   HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping
    public void deleteChapterByName(@PathVariable("adventureName") String adventure,
                                    @PathVariable("chapterName") String chapterName,
                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

}
