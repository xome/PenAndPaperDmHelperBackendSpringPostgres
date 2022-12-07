package de.mayer.backendspringpostgres.adventure.chapter.record.service.apiservice;

import de.mayer.backendspringpostgres.adventure.chapter.record.model.RecordInAChapter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/record/{adventureName}/{chapterName}")
public interface RecordByChapterHttpApi {

    @GetMapping
    default List<RecordInAChapter> getRecordsByChapterName(@PathVariable("adventureName") String adventure,
                                                           @PathVariable("chapterName") String chapterName,
                                                           HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping
    default void deleteRecordsByChapterName(@PathVariable("adventureName") String adventure,
                                           @PathVariable("chapterName") String chapterName,
                                            HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

}
