package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.penandpaperdmhelperjcore.adventure.model.Chapter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public interface ChapterHttpApi {
    String PATH = "chapter/{adventureName}";

    @PutMapping(PATH)
    default ResponseEntity<Void> putChapters(@PathVariable("adventureName") String adventure,
                                             @RequestBody List<Chapter> chapters){
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping(PATH)
    default ResponseEntity<Void> patchChapters(@PathVariable("adventureName") String adventure,
                                               @RequestBody List<Chapter> chapters) {
        throw new RuntimeException("Not yet implemented!");
    }




}
