package de.mayer.backendspringpostgres.adventure.graph.service;

import de.mayer.backendspringpostgres.adventure.graph.model.Graph;
import de.mayer.backendspringpostgres.adventure.graph.model.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public interface GraphHttpApi {

    @GetMapping("/graph/{adventureName}")
    default Graph getGraph(@PathVariable String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/shortest/{adventureName}")
    default List<Path> getShortestPaths(@PathVariable String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/longest/{adventureName}")
    default List<Path> getLongestPaths(@PathVariable String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/next/{adventureName}/{startingPoint}")
    default List<Path> getNextPaths(@PathVariable String adventureName, @PathVariable("startingPoint") String startingPoint){
        throw new RuntimeException("Not yet implemented!");
    }

}
