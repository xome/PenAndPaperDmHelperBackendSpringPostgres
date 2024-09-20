package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.penandpaperdmhelperjcore.adventure.model.Chapter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public interface ChapterByNameHttpApi {

    @GetMapping("/chapter/{adventureName}/{chapterName}")
    default ResponseEntity<Chapter> getChapterByName(@PathVariable("adventureName") String adventureName,
                                                     @PathVariable("chapterName") String chapterName) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping(value = "/chapter/{adventureName}/{chapterName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    default ResponseEntity<Void> patchChapterByName(@PathVariable("adventureName") String adventureName,
                                   @PathVariable("chapterName") String chapterName,
                                   @RequestBody Chapter chapter) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping("/chapter/{adventureName}/{chapterName}")
    default ResponseEntity<Void> deleteChapterByName(@PathVariable("adventureName") String adventureName,
                                    @PathVariable("chapterName") String chapterName) {
        throw new RuntimeException("Not yet implemented!");
    }

}
