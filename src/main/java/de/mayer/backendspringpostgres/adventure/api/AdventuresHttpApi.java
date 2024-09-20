package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public interface AdventuresHttpApi {

    @GetMapping("/adventures")
    default ResponseEntity<List<Adventure>> getAdventures(){
        throw new RuntimeException("Not yet implemented!");
    }


}
