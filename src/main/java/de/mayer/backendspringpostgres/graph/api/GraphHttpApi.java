package de.mayer.backendspringpostgres.graph.api;

import de.mayer.backendspringpostgres.graph.model.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public interface GraphHttpApi {

    @GetMapping("/graph/{adventureName}")
    default ResponseEntity<?> getGraph(@PathVariable("adventureName") String adventureName){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/paths/shortest/{adventureName}")
    default ResponseEntity<List<Path>> getShortestPaths(@PathVariable("adventureName") String adventureName){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/paths/longest/{adventureName}")
    default ResponseEntity<List<Path>> getLongestPaths(@PathVariable("adventureName") String adventureName){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/paths/next/{adventureName}/{startingPoint}")
    default ResponseEntity<List<Path>> getNextPaths(@PathVariable("adventureName") String adventureName,
                                    @PathVariable("startingPoint") String startingPoint){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
