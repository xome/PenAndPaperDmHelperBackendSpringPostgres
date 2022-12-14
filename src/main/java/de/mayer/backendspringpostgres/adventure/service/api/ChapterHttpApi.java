package de.mayer.backendspringpostgres.adventure.service.api;

import de.mayer.backendspringpostgres.adventure.model.Chapter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController("/chapter/{adventureName}")
public interface ChapterHttpApi {

    @PutMapping
    default void putChapter(@PathVariable("adventureName") String adventure,
                           @RequestBody Chapter chapter,
                            HttpServletResponse httResponse){
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    default void patchChapter(@PathVariable("adventureName") String adventure,
                             @RequestBody Chapter chapter,
                              HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }




}
