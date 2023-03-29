package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PictureJpaRepository extends JpaRepository<PictureJpa, Long> {
    Optional<PictureJpa> findByRecordId(Long recordJpaId);
}
