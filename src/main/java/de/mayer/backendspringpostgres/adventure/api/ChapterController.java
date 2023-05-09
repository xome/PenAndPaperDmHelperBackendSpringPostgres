package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.domainservice.*;
import de.mayer.backendspringpostgres.adventure.model.Chapter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChapterController implements ChapterHttpApi {

    private final ChapterRepository chapterRepository;

    public ChapterController(ChapterRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public ResponseEntity<Void> putChapters(String adventure, List<Chapter> chapters) {
        for (Chapter chapter : chapters) {
            try {
                chapterRepository.create(adventure, chapter);
            } catch (AdventureNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (ChapterAlreadyExistsException | ChapterToNotFoundException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } catch (ChapterNotFoundException e) {
                throw new RuntimeException(e); // this should never happen
            }
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> patchChapters(String adventure, List<Chapter> chapters) {
        for (Chapter chapter : chapters) {
            try {
                chapterRepository.update(adventure, chapter.name(), chapter);
            } catch (ChapterNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            } catch (ChapterAlreadyExistsException | ChapterToNotFoundException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }
        return ResponseEntity.ok().build();
    }
}
