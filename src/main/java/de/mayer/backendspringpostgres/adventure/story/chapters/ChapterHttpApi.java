package de.mayer.backendspringpostgres.adventure.story.chapters;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/chapter")
public class ChapterHttpApi {

    @PutMapping
    public void putChapter(@RequestBody Chapter chapter,
                            HttpServletResponse httResponse){
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    public void patchChapter(@RequestBody Chapter chapter,
                              HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }




}
