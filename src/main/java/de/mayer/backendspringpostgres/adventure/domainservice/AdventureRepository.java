package de.mayer.backendspringpostgres.adventure.domainservice;

import de.mayer.backendspringpostgres.adventure.model.Adventure;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface AdventureRepository {
    Optional<Adventure> findByName(String adventureName);

    void save(Adventure adventure);

    void changeName(Adventure adventure, String newAdventureName);

    void delete(Adventure adventure);

    List<Adventure> findAll(Sort name);
}
