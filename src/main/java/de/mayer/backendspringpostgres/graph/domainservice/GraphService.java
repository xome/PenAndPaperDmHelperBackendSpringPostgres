package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.model.InvalidGraphException;
import de.mayer.backendspringpostgres.graph.model.Path;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class GraphService {

    private final ChapterDomainRepository chapterDomainRepository;
    private final ChapterLinkDomainRepository chapterLinkDomainRepository;

    public GraphService(ChapterDomainRepository chapterDomainRepository, ChapterLinkDomainRepository inMemoryChapterLinkDomainRepository) {
        this.chapterDomainRepository = chapterDomainRepository;
        chapterLinkDomainRepository = inMemoryChapterLinkDomainRepository;
    }

    public Graph createGraph(String adventure) throws NoChaptersForAdventureException, InvalidGraphException {
        var chapters = chapterDomainRepository
                .findByAdventure(adventure)
                .orElseThrow(() -> new NoChaptersForAdventureException("No Chapters found for adventure %s!"
                        .formatted(adventure)));
        var chapterLinks = chapterLinkDomainRepository
                .findByAdventure(adventure)
                .orElseGet(Collections::emptySet);

        var graph = new Graph(chapters, chapterLinks);

        // A Graph is valid if and only if there are no circle Paths.
        // If Paths are not valid, an InvalidGraphException is thrown here.
        generatePaths(graph);

        return graph;
    }

    public Set<Path> generatePaths(Graph graph) throws InvalidGraphException {

        // compute all possible complete Paths and check for a circle
        var startingPoints = graph.chapters()
                .stream()
                .filter(chapter ->
                        graph.chapterLinks()
                                .stream()
                                .noneMatch((link) -> link.to().equals(chapter)))
                .collect(Collectors.toSet());

        var endingPoints = graph.chapters()
                .stream()
                .filter(chapter ->
                        graph.chapterLinks()
                                .stream()
                                .noneMatch(chapterLink -> chapterLink.from().equals(chapter)))
                .collect(Collectors.toSet());

        if (startingPoints.isEmpty() || endingPoints.isEmpty())
            throw new InvalidGraphException("Graph is invalid. There are only Paths with circles.", Collections.emptySet());

        var allPaths = new HashSet<Path>();
        var problematicPaths = new HashSet<Path>();

        startingPoints
                .forEach(startingPoint -> {
                    AtomicReference<Chapter> currentChapter = new AtomicReference<>(startingPoint);
                    var unusedLinks = new HashSet<>(graph.chapterLinks());

                    if (endingPoints.contains(startingPoint)) {
                        allPaths.add(new PathBuilder(startingPoint).build());
                    }

                    while (unusedLinks
                            .stream()
                            .anyMatch(link ->
                                    link.from().equals(startingPoint))) {
                        PathBuilder pathBuilder = new PathBuilder(startingPoint);
                        currentChapter.set(startingPoint);
                        var cycleDetected = new AtomicBoolean(false);

                        // if there is a cycle for this starting point, we stop.
                        while (!endingPoints.contains(currentChapter.get())
                                && !cycleDetected.get()) {
                            unusedLinks
                                    .stream()
                                    .filter(unusedLink -> currentChapter.get().equals(unusedLink.from()))
                                    .findAny()
                                    .ifPresentOrElse((nextLink) -> {
                                        unusedLinks.remove(nextLink);
                                        currentChapter.set(nextLink.to());
                                        pathBuilder.addChapter(currentChapter.get());
                                        // If there are no unused links to go to the next chapter,
                                        // and the currentChapter is not a possible ending point of the adventure,
                                        // only already used links are left as a possibility.
                                        // When a link has to be used more than once, the path at least contains a circle
                                        // and is therefore invalid.
                                        // A single circle path is enough to invalidate the whole Graph.
                                    }, () -> {
                                        cycleDetected.set(true);
                                        if (problematicPaths
                                                .stream()
                                                .noneMatch(path -> path.chapters().contains(currentChapter.get()))) {
                                            // Otherwise we would describe the same Path twice.
                                            problematicPaths.add(pathBuilder.build());
                                        }
                                    });
                        }
                        allPaths.add(pathBuilder.build());
                    }
                });
        if (!problematicPaths.isEmpty()) {
            throw new InvalidGraphException("There are problematic Graphs.", problematicPaths);
        }
        return allPaths;
    }


}
