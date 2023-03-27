package de.mayer.backendspringpostgres.adventure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChapterJpaRepository extends JpaRepository<ChapterJpa, Long> {
    Optional<ChapterJpa> findByAdventureAndName(Long id, String chapterName);
}
