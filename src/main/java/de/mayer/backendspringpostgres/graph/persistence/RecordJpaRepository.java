package de.mayer.backendspringpostgres.graph.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecordJpaRepository extends JpaRepository<RecordJpa, RecordJpa> {

    @Query("select max(r.index) from RecordJpa r where r.adventure = ?1 and r.chapter = ?2")
    Integer findMaxIndexByAdventureAndChapter(String adventure, String chapter);
}
