package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ChapterJpaRepository extends JpaRepository<ChapterJpa, Long> {
    @Query("select c from ChapterJpa c where c.adventure = ?1")
    @Cacheable("graphChaptersByAdventure")
    Set<ChapterJpa> findByAdventure(Long adventure);

    Optional<ChapterJpa> findByAdventureAndName(Long adventureId, String name);
}
