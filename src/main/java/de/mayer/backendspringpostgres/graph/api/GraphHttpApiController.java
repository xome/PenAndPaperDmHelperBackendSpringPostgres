package de.mayer.backendspringpostgres.graph.api;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Graph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class GraphHttpApiController implements GraphHttpApi {

    private final ChapterDomainRepository chapterRepository;

    @Autowired
    public GraphHttpApiController(ChapterDomainRepository chapterRepository) {
        this.chapterRepository = chapterRepository;
    }

    @Override
    public ResponseEntity<Graph> getGraph(String adventureName) {

        if (chapterRepository.findByAdventure(adventureName).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return null;
    }
}
