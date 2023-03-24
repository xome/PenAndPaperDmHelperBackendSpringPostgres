package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RecordJpaRepository extends JpaRepository<RecordJpa, Long> {
    Set<RecordJpa> findByChapterId(Long chapterId);
}
