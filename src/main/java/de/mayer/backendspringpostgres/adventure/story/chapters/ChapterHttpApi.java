package de.mayer.backendspringpostgres.adventure.story.chapters;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController("/chapter/{adventureName}")
public class ChapterHttpApi {

    @PutMapping
    public void putChapter(@PathVariable("adventureName") String adventure,
                           @RequestBody Chapter chapter,
                            HttpServletResponse httResponse){
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    public void patchChapter(@PathVariable("adventureName") String adventure,
                             @RequestBody Chapter chapter,
                              HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }




}
