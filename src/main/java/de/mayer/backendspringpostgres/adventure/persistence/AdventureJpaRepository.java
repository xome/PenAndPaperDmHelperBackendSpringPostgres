package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdventureJpaRepository extends JpaRepository<AdventureJpa, Long> {

        Optional<AdventureJpa> findByName(String name);

}
