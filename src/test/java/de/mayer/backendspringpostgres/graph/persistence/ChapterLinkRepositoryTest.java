package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.ChapterLink;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.persistence.impl.InMemoryChapterDomainRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChapterLinkRepositoryTest {

    @Test
    @DisplayName("""
            Given a Graph is cached,
            When a link is saved,
            then the cached Graph is invalidated
            """)
    void invalidateGraphCache() {
        var cache = new InMemoryCache();
        var chapterLinkJpaRepo = mock(ChapterLinkJpaRepository.class);
        var recordJpaRepo = mock(RecordJpaRepository.class);
        var chapterDomainRepository = new InMemoryChapterDomainRepository();

        var chapterLinkRepo = new ChapterLinkRepository(chapterLinkJpaRepo,
                recordJpaRepo,
                chapterDomainRepository,
                cache);
        var adventure = "Adventure";
        var chapter1 = new Chapter("1", 1d);
        var chapter2 = new Chapter("2", 1d);
        var link = new ChapterLink(chapter1, chapter2);

        cache.put(adventure, new Graph(Set.of(chapter1, chapter2), Collections.emptySet()));

        chapterLinkRepo.save(adventure, link);

        assertThat(cache.get(adventure, Graph.class), is(Optional.empty()));

    }


}