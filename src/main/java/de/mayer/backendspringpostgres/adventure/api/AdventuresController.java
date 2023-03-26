package de.mayer.backendspringpostgres.adventure.api;

import de.mayer.backendspringpostgres.adventure.domainservice.AdventureRepository;
import de.mayer.backendspringpostgres.adventure.model.Adventure;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AdventuresController implements AdventuresHttpApi {

    private final AdventureRepository adventureRepository;

    public AdventuresController(AdventureRepository adventureRepository) {
        this.adventureRepository = adventureRepository;
    }

    @Override
    public ResponseEntity<List<Adventure>> getAdventures() {
        return ResponseEntity.ok(adventureRepository.findAll(Sort.by(Sort.Order.asc("name"))));

    }
}
