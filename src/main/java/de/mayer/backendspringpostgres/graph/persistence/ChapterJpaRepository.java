package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ChapterJpaRepository extends JpaRepository<ChapterJpa, ChapterJpaId> {
    @Query("select c from ChapterJpa c where c.adventure = ?1")
    Set<ChapterJpa> findByAdventure(String adventure);
}
