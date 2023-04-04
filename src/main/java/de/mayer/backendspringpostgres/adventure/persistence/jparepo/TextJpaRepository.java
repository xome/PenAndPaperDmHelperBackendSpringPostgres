package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.TextJpa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextJpaRepository extends JpaRepository<TextJpa, Long> {
    Optional<TextJpa> findByRecordJpa(RecordJpa recordJpa);

    @Transactional
    void deleteByRecordJpa(RecordJpa recordJpa);
}
