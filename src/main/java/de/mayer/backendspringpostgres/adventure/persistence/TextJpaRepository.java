package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextJpaRepository extends JpaRepository<TextJpa, Long> {
    Optional<TextJpa> findByRecordId(Long recordJpaId);
}
