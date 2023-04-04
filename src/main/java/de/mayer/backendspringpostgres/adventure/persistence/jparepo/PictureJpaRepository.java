package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.PictureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PictureJpaRepository extends JpaRepository<PictureJpa, Long> {
    Optional<PictureJpa> findByRecordJpa(RecordJpa recordJpa);

    @Transactional
    void deleteByRecordJpa(RecordJpa recordJpa);
}
