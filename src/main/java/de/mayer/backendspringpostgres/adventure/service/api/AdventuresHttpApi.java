package de.mayer.backendspringpostgres.adventure.service.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public interface AdventuresHttpApi {

    @GetMapping("/adventures")
    default List<String> getAdventures(){
        throw new RuntimeException("Not yet implemented!");
    }


}
