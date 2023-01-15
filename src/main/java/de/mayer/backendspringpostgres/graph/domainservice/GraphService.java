package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class GraphService {

    private final ChapterDomainRepository chapterDomainRepository;
    private final ChapterLinkDomainRepository chapterLinkDomainRepository;
    private final Cache cache;

    @Autowired
    public GraphService(ChapterDomainRepository chapterDomainRepository,
                        ChapterLinkDomainRepository inMemoryChapterLinkDomainRepository,
                        Cache cache) {
        this.chapterDomainRepository = chapterDomainRepository;
        this.chapterLinkDomainRepository = inMemoryChapterLinkDomainRepository;
        this.cache = cache;
    }

    public Graph createGraph(String adventure) throws NoChaptersForAdventureException, InvalidGraphException {
        var cachedGraph = cache.get(adventure, Graph.class);

        if (cachedGraph.isPresent()){
            return cachedGraph.get();
        }

        var chapters = chapterDomainRepository
                .findByAdventure(adventure)
                .orElseThrow(() -> new NoChaptersForAdventureException("No Chapters found for adventure %s!"
                        .formatted(adventure)));
        var chapterLinks = chapterLinkDomainRepository
                .findByAdventure(adventure);

        var graph = new Graph(chapters, chapterLinks);

        // A Graph is valid if and only if there are no circle Paths.
        // If Paths are not valid, an InvalidGraphException is thrown here.
        generatePaths(adventure, graph);

        cache.put(adventure, graph);
        return graph;
    }

    public Set<Path> generatePaths(String adventure, Graph graph) throws InvalidGraphException {
        var cacheKey = "AllPaths - %s".formatted(adventure);
        var cachedObject = cache.get(cacheKey, HashSet.class);

        if (cachedObject.isPresent()){
            if (cachedObject
                    .get()
                    .stream()
                    .anyMatch(obj -> !(obj instanceof Path))){
                throw new RuntimeException("Cache Failure.");
            }
            return (HashSet<Path>) cachedObject.get();
        }

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

        if (startingPoints.isEmpty() || endingPoints.isEmpty()) {
            throw new InvalidGraphException("Graph is invalid. There are only Paths with circles.", Collections.emptySet());
        }

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

        cache.put(cacheKey, allPaths);
        return allPaths;
    }

    public List<Path> getShortestPaths(String adventureName)
            throws InvalidGraphException, NoChaptersForAdventureException {

        var allPaths = generatePaths(adventureName, createGraph(adventureName));
        var optionalMin = allPaths
                .stream()
                .mapToDouble(Path::approximateDurationInMinutes)
                .min();

        if (optionalMin.isEmpty()) {
            return Collections.emptyList();
        }

        double minDuration = optionalMin.getAsDouble();

        return getAllPathsWithDuration(allPaths, minDuration);
    }

    private static List<Path> getAllPathsWithDuration(Set<Path> allPaths, double duration) {
        return allPaths
                .stream()
                .filter(path -> path.approximateDurationInMinutes().equals(duration))
                .collect(Collectors.toList());
    }


    public List<Path> getLongestPath(String adventureName) throws InvalidGraphException, NoChaptersForAdventureException {
        var allPaths = generatePaths(adventureName, createGraph(adventureName));
        var optionalMax = allPaths
                .stream()
                .mapToDouble(Path::approximateDurationInMinutes)
                .max();

        if (optionalMax.isEmpty()) {
            return Collections.emptyList();
        }
        return getAllPathsWithDuration(allPaths, optionalMax.getAsDouble());
    }


    public void invalidateCaches() {
        cache.invalidateAll();
        chapterDomainRepository.invalidateCache();
        chapterLinkDomainRepository.invalidateCache();
    }
}
