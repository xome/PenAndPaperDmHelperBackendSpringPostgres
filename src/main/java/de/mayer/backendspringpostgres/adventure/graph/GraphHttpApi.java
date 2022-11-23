package de.mayer.backendspringpostgres.adventure.graph;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class GraphHttpApi {

    @GetMapping("/graph")
    public Graph getGraph(){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/shortest")
    public List<Path> getShortestPaths(){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/longest")
    public List<Path> getLongestPaths(){
        throw new RuntimeException("Not yet implemented!");
    }

    @GetMapping("/paths/next/{startingPoint}")
    public List<Path> getNextPaths(@PathVariable("startingPoint") String startingPoint){
        throw new RuntimeException("Not yet implemented!");
    }

}
