package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.domainservice.ChapterDomainRepository;
import de.mayer.backendspringpostgres.graph.model.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChapterRepository implements ChapterDomainRepository {

    private final ChapterJpaRepository jpaRepository;

    @Autowired
    public ChapterRepository(ChapterJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public void save(String adventure, Chapter chapter) {
        jpaRepository.save(ChapterJpa.fromModel(adventure, chapter));
    }

    @Override
    public void deleteByAdventure(String adventure) {

    }

    @Override
    public Optional<Set<Chapter>> findByAdventure(String adventure) {

        var jpaChapters = jpaRepository.findByAdventure(adventure);
        if (jpaChapters.isEmpty()) return Optional.empty();
        return Optional.of(jpaChapters
                .stream()
                .map(jpaChapter -> new Chapter(jpaChapter.getName(), jpaChapter.getApproximateDurationInMinutes()))
                .collect(Collectors.toSet()));

    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public Optional<Chapter> findById(String adventure, String chapter) {
        return jpaRepository
                .findById(new ChapterJpaId(adventure, chapter))
                .map(chapterJpa ->
                        new Chapter(chapterJpa.getName(), chapterJpa.getApproximateDurationInMinutes()));
    }
}
