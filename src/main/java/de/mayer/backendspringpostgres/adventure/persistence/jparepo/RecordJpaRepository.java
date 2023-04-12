package de.mayer.backendspringpostgres.adventure.persistence.jparepo;

import de.mayer.backendspringpostgres.adventure.persistence.dto.RecordJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecordJpaRepository extends JpaRepository<RecordJpa, Long> {
    List<RecordJpa> findByChapterIdOrderByIndex(Long id);
    @Query("select max(t.index) from RecordJpa t where t.chapterId = :chapterId")
    Long findMaxIndexByChapterId(@Param("chapterId") Long chapterId);
    Optional<RecordJpa> findByChapterIdAndIndex(Long id, Integer index);

    List<RecordJpa> findByChapterIdAndIndexGreaterThanEqual(Long chapterId, Integer index);
}
