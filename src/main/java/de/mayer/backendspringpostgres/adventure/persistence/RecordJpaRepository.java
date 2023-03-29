package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordJpaRepository extends JpaRepository<RecordJpa, Long> {
    List<RecordJpa> findByChapterIdOrderByIndex(Long id);
}
