package de.mayer.backendspringpostgres.graph.api;

import de.mayer.penandpaperdmhelperjcore.graph.domainservice.GraphService;
import de.mayer.penandpaperdmhelperjcore.graph.model.InvalidGraphException;
import de.mayer.penandpaperdmhelperjcore.graph.model.NoChaptersForAdventureException;
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
        } catch (InvalidGraphException | NoChaptersForAdventureException e) {
            return getResponseFromException(e);
        }
    }

    @Override
    public ResponseEntity<?> getShortestPaths(String adventureName) {
        try {
            return ResponseEntity.ok(graphService.getShortestPaths(adventureName));
        } catch (InvalidGraphException | NoChaptersForAdventureException e) {
            return getResponseFromException(e);
        }
    }

    @Override
    public ResponseEntity<?> getLongestPaths(String adventureName) {
        try {
            return ResponseEntity.ok(graphService.getLongestPath(adventureName));
        } catch (InvalidGraphException | NoChaptersForAdventureException e) {
            return getResponseFromException(e);
        }
    }

    private ResponseEntity<?> getResponseFromException(Throwable exception) {
        if (exception instanceof InvalidGraphException) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(exception);
        } else if (exception instanceof NoChaptersForAdventureException) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        throw new RuntimeException(exception);
    }

    @Override
    public ResponseEntity<?> getNextPaths(String adventureName, String startingPoint) {
        try {
            return ResponseEntity.ok(graphService.getNextPaths(adventureName, startingPoint));
        } catch (InvalidGraphException | NoChaptersForAdventureException e) {
            return getResponseFromException(e);
        }
    }

    @Override
    public ResponseEntity<Void> modelHasChanged() {
        graphService.invalidateCaches();
        return ResponseEntity.ok().build();
    }


}
