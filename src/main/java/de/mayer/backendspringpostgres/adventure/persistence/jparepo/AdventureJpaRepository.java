package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.AdventureJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdventureJpaRepository extends JpaRepository<AdventureJpa, Long> {

        Optional<AdventureJpa> findByName(String name);

}
