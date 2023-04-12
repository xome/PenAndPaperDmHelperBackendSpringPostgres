package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.domainservice.ChapterAlreadyExistsException;
import de.mayer.backendspringpostgres.adventure.domainservice.ChapterNotFoundException;
import de.mayer.backendspringpostgres.adventure.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.adventure.model.Chapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class ChapterByNameController implements ChapterByNameHttpApi {

    private final ChapterRepository chapterRepository;

    public ChapterByNameController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public ResponseEntity<Chapter> getChapterByName(String adventureName, String chapterName) {
        var chapter = chapterRepository.read(adventureName, chapterName);
        return chapter
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build());
    }

    @Override
    public ResponseEntity<Void> deleteChapterByName(String adventureName, String chapterName) {
        try {
            chapterRepository.delete(adventureName, chapterName);
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> patchChapterByName(String adventureName, String chapterName, Chapter chapter) {
        try {
            chapterRepository.update(adventureName, chapterName, chapter);
        } catch (ChapterNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (ChapterAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();

    }
}
