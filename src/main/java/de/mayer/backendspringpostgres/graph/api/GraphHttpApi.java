package de.mayer.backendspringpostgres.graph.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public interface GraphHttpApi {

    @GetMapping("/graph/{adventureName}")
    default ResponseEntity<?> getGraph(@PathVariable("adventureName") String adventureName){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/paths/shortest/{adventureName}")
    default ResponseEntity<?> getShortestPaths(@PathVariable("adventureName") String adventureName) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/paths/longest/{adventureName}")
    default ResponseEntity<?> getLongestPaths(@PathVariable("adventureName") String adventureName){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/paths/next/{adventureName}/{startingPoint}")
    default ResponseEntity<?> getNextPaths(@PathVariable("adventureName") String adventureName,
                                           @PathVariable("startingPoint") String startingPoint){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/graph/modelHasChanged")
    default ResponseEntity<Void> modelHasChanged(){
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

}
