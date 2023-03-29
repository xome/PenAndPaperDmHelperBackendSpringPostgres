package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnvironmentLightningJpaRepository extends JpaRepository<EnvironmentLightningJpa, Long> {
    Optional<EnvironmentLightningJpa> findByRecordId(Long recordJpaId);
}
