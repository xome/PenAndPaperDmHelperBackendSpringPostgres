package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.EnvironmentLightningJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnvironmentLightningJpaRepository extends JpaRepository<EnvironmentLightningJpa, Long> {
    Optional<EnvironmentLightningJpa> findByRecordJpa(RecordJpa recordJpaId);

    @Transactional
    void deleteByRecordJpa(RecordJpa recordJpa);
}
