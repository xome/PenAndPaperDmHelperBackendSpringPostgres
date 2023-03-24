package de.mayer.backendspringpostgres.adventure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.mayer.backendspringpostgres.adventure.domainservice.AdventureRepository;
import de.mayer.backendspringpostgres.adventure.model.Adventure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;

@Controller
public class AdventureByNameController implements AdventureByNameHttpApi {

    private final AdventureRepository adventureRepository;
    private final ObjectMapper jsonMapper;

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
    public ResponseEntity<Void> deleteAdventureByName(String adventure) {
        return AdventureByNameHttpApi.super.deleteAdventureByName(adventure);
    }

    @Override
    public ResponseEntity<Void> patchAdventureByName(String adventure, String newAdventureName) {
        var optionalAdventure = adventureRepository.findByName(adventure);
        try {
            newAdventureName = jsonMapper.readValue(newAdventureName.getBytes(StandardCharsets.UTF_8), String.class);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (optionalAdventure.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            adventureRepository.changeName(optionalAdventure.get(), newAdventureName);
        }
        return ResponseEntity.ok().build();
    }
}
