package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.domainservice.AdventureRepository;
import de.mayer.backendspringpostgres.adventure.model.Adventure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class AdventureByNameController implements AdventureByNameHttpApi {

    private final AdventureRepository adventureRepository;
    private final ObjectMapper jsonMapper;
    private static final Logger log = LoggerFactory.getLogger(AdventureByNameController.class);

    public AdventureByNameController(AdventureRepository adventureRepository, ObjectMapper jsonMapper) {
        this.adventureRepository = adventureRepository;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public ResponseEntity<Void> putAdventureByName(String adventure) {
        if (adventureRepository.findByName(adventure).isEmpty()) {
            adventureRepository.save(new Adventure(adventure, null));
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> patchAdventureByName(String adventure, String newAdventureName) {
        boolean errorParsingBody = false;
        var optionalAdventure = adventureRepository.findByName(adventure);
        try {
            newAdventureName = jsonMapper.readValue(newAdventureName.getBytes(StandardCharsets.UTF_8), String.class);
        } catch (IOException e) {
            log.error("Error parsing JSON-String for new Adventure. String: {}", newAdventureName, e);
            errorParsingBody = true;
        }

        if (optionalAdventure.isEmpty() || errorParsingBody) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        adventureRepository.changeName(optionalAdventure.get(), newAdventureName);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteAdventureByName(String adventure){
        var optionalAdventure = adventureRepository.findByName(adventure);
        if (optionalAdventure.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        adventureRepository.delete(optionalAdventure.get());

        return ResponseEntity.ok().build();
    }
}
