package de.mayer.backendspringpostgres.adventure.domainservice;

import de.mayer.backendspringpostgres.adventure.model.Adventure;

import java.util.Optional;

public interface AdventureRepository {
    Optional<Adventure> findByName(String adventureName);

    void save(Adventure adventure);

    void changeName(Adventure adventure, String newAdventureName);
}
