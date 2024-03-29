package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GraphAdventureJpaRepository extends JpaRepository<AdventureJpa, Long> {

    Optional<AdventureJpa> findByName(String name);

}
