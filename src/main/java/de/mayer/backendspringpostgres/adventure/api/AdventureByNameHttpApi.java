package de.mayer.backendspringpostgres.adventure.service.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController("/adventure/{adventureName}")
public interface AdventureByNameHttpApi {

    @DeleteMapping
    default void deleteAdventureByName(@PathVariable("adventureName") String adventure,
                                       HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PutMapping
    default void putAdventureByName(@PathVariable("adventureName") String adventure,
                                    HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

    @PatchMapping
    default void patchAdventureByName(@PathVariable("adventureName") String adventure,
                                      @RequestBody String newAdventureName,
                                      HttpServletResponse httpResponse) {
        throw new RuntimeException("Not yet implemented!");
    }

}
