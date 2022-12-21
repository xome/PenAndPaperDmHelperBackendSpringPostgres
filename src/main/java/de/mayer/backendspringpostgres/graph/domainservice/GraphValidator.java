package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Graph;
import de.mayer.backendspringpostgres.graph.model.InvalidGraphException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class GraphValidator {

    public void validateGraph(Graph graph) throws InvalidGraphException {

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

        startingPoints
                .forEach(startingPoint -> {
                    var currentChapter = startingPoint;
                    var unUsedLinks = new HashSet<>(graph.chapterLinks());
                    PathBuilder pathBuilder = new PathBuilder(startingPoint);
                    while (!endingPoints.contains(currentChapter)) {
                        Chapter finalCurrentChapter = currentChapter;
                        var nextLink = unUsedLinks
                                .stream()
                                .filter(unusedLink -> finalCurrentChapter.equals(unusedLink.from()))
                                .findAny()
                                .orElseThrow(() -> new RuntimeException(
                                        new InvalidGraphException(
                                                "Graph is invalid. Circle detected on Path %s."
                                                        .formatted(pathBuilder
                                                                .build()
                                                                .toString()))));
                        unUsedLinks.remove(nextLink);
                        currentChapter = nextLink.to();
                        pathBuilder.addChapter(currentChapter);
                    }
                });

    }


}
