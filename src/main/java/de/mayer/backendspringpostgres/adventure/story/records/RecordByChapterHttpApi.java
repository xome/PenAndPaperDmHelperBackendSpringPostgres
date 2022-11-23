package de.mayer.backendspringpostgres.adventure.story.records;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/record/{chapterName}")
public class RecordByChapterHttpApi {

    @GetMapping
    public List<RecordInAChapter> getRecordsByChapterName(@PathVariable("chapterName") String chapterName,
                                                          HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @DeleteMapping
    public void deleteRecordsByChapterName(@PathVariable("chapterName") String chapterName,
                                            HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

}
