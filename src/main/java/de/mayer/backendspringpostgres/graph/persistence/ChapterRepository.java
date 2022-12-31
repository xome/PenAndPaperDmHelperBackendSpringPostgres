package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;

@Component
public class ChapterRepository implements ChapterDomainRepository {

    private final ChapterJpaRepository jpaRepository;

    @Autowired
    public ChapterRepository(ChapterJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public void save(ChapterJpaId chapterJpaId, Chapter chapter) {

    }

    @Override
    public void deleteByAdventure(String adventure) {

    }

    @Override
    public Optional<Set<Chapter>> findByAdventure(String adventure) {
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
