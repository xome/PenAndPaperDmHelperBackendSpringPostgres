package de.mayer.backendspringpostgres.adventure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface AdventureByNameHttpApi {

    @DeleteMapping("/adventure/{adventureName}")
    default ResponseEntity<Void> deleteAdventureByName(@PathVariable("adventureName") String adventure) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PutMapping("/adventure/{adventureName}")
    default ResponseEntity<Void> putAdventureByName(@PathVariable("adventureName") String adventure) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping(value = "/adventure/{adventureName}")
    default ResponseEntity<Void> patchAdventureByName(@PathVariable("adventureName") String adventure,
                                                      @RequestBody String newAdventureName) {
        throw new RuntimeException("Not yet implemented!");
    }

}
