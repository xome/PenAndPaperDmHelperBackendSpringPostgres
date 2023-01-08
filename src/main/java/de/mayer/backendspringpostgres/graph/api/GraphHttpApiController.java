package de.mayer.backendspringpostgres.graph.api;

import de.mayer.backendspringpostgres.graph.domainservice.GraphService;
import de.mayer.backendspringpostgres.graph.model.NoChaptersForAdventureException;
import de.mayer.backendspringpostgres.graph.model.InvalidGraphException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class GraphHttpApiController implements GraphHttpApi {
    private final GraphService graphService;

    @Autowired
    public GraphHttpApiController(GraphService graphService) {
        this.graphService = graphService;
    }

    @Override
    public ResponseEntity<?> getGraph(String adventureName) {
        try {
            return ResponseEntity
                    .ok(graphService.createGraph(adventureName));
        } catch (NoChaptersForAdventureException noChaptersForAdventureException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        } catch (InvalidGraphException invalidGraphException) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(invalidGraphException);
        }

    }
}
