package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.model.InvalidGraphException;
import de.mayer.backendspringpostgres.graph.model.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class GraphService {

    private ChapterRepository chapterRepository;
    private ChapterLinkRepository chapterLinkRepository;

    public GraphService(ChapterRepository chapterRepository, ChapterLinkRepository inMemoryChapterLinkRepository) {
        this.chapterRepository = chapterRepository;
        chapterLinkRepository = inMemoryChapterLinkRepository;
    }

    public Graph createGraph(String adventure) throws NoChaptersForAdventureException, InvalidGraphException {
        var chapters = chapterRepository
                .findByAdventure(adventure)
                .orElseThrow(() -> new NoChaptersForAdventureException("No Chapters found for adventure %s!"
                        .formatted(adventure)));
        var chapterLinks = chapterLinkRepository
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
            throw new InvalidGraphException("Graph is invalid. There are only Paths with circles.");

        var allPaths = new HashSet<Path>();

        startingPoints
                .forEach(startingPoint -> {
                    var currentChapter = startingPoint;
                    var unusedLinks = new HashSet<>(graph.chapterLinks());

                    if (endingPoints.contains(startingPoint)) {
                        allPaths.add(new PathBuilder(startingPoint).build());
                    }

                    while (unusedLinks
                            .stream()
                            .anyMatch(link ->
                                    link.from().equals(startingPoint))) {
                        PathBuilder pathBuilder = new PathBuilder(startingPoint);
                        currentChapter = startingPoint;
                        while (!endingPoints.contains(currentChapter)) {
                            Chapter finalCurrentChapter = currentChapter;
                            var nextLink = unusedLinks
                                    .stream()
                                    .filter(unusedLink -> finalCurrentChapter.equals(unusedLink.from()))
                                    .findAny()

                                    // If there are no unused links to go to the next chapter,
                                    // and the currentChapter is not a possible ending point of the adventure,
                                    // only already used links are left as a possibility.
                                    // When a link has to be used more than once, the path at least contains a circle
                                    // and is therefore invalid.
                                    // A single circle path is enough to invalidate the whole Graph.
                                    .orElseThrow(() -> new RuntimeException(
                                            new InvalidGraphException(
                                                    "Graph is invalid. Circle detected on Path %s."
                                                            .formatted(pathBuilder
                                                                    .build()
                                                                    .toString()))));
                            unusedLinks.remove(nextLink);
                            currentChapter = nextLink.to();
                            pathBuilder.addChapter(currentChapter);
                        }
                        allPaths.add(pathBuilder.build());
                    }
                });
        return allPaths;
    }


}
