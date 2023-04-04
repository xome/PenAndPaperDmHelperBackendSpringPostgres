package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.BackgroundMusicJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BackgroundMusicJpaRepository extends JpaRepository<BackgroundMusicJpa, Long> {
    Optional<BackgroundMusicJpa> findByRecordJpa(RecordJpa recordJpa);

    @Transactional
    void deleteByRecordJpa(RecordJpa recordJpa);
}
