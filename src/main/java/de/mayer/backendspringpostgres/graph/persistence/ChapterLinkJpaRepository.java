package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ChapterLinkJpaRepository extends JpaRepository<ChapterLinkJpa, ChapterLinkJpaId> {
    @Query("select c from ChapterLinkJpa c where c.adventure = ?1")
    Set<ChapterLinkJpa> findByAdventure(String adventure);
}
