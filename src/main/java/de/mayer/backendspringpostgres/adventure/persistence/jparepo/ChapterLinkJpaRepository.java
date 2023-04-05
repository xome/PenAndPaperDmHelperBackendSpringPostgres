package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.ChapterLinkJpa;
import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapterLinkJpaRepository extends JpaRepository<ChapterLinkJpa, Long> {
    Optional<ChapterLinkJpa> findByRecordJpa(RecordJpa recordJpa);

    @Transactional
    void deleteByRecordJpa(RecordJpa recordJpa);

    List<ChapterLinkJpa> findByChapterTo(Long id);
}