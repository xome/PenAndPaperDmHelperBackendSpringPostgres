package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChapterLinkJpaRepository extends JpaRepository<ChapterLinkJpa, Long> {
    Optional<ChapterLinkJpa> findByRecordId(Long recordId);
}
