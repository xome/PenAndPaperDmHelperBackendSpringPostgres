package de.mayer.backendspringpostgres.adventure.graph;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GraphHttpApi {

    @GetMapping("/graph/{adventureName}")
    public Graph getGraph(@PathVariable String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/shortest/{adventureName}")
    public List<Path> getShortestPaths(@PathVariable String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/longest/{adventureName}")
    public List<Path> getLongestPaths(@PathVariable String adventureName){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/next/{adventureName}/{startingPoint}")
    public List<Path> getNextPaths(@PathVariable String adventureName, @PathVariable("startingPoint") String startingPoint){
        throw new RuntimeException("Not yet implemented!");
    }

}
