package de.mayer.backendspringpostgres.graph.api;

import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.model.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public interface GraphHttpApi {

    @GetMapping("/graph/{adventureName}")
    default ResponseEntity<Graph> getGraph(@PathVariable("adventureName") String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/shortest/{adventureName}")
    default ResponseEntity<List<Path>> getShortestPaths(@PathVariable("adventureName") String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/longest/{adventureName}")
    default ResponseEntity<List<Path>> getLongestPaths(@PathVariable("adventureName") String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/next/{adventureName}/{startingPoint}")
    default ResponseEntity<List<Path>> getNextPaths(@PathVariable("adventureName") String adventureName,
                                    @PathVariable("startingPoint") String startingPoint){
        throw new RuntimeException("Not yet implemented!");
    }

}
