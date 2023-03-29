package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackgroundMusicRepository extends JpaRepository<BackgroundMusicJpa, Long> {
    Optional<BackgroundMusicJpa> findByRecordId(Long recordJpaId);
}
