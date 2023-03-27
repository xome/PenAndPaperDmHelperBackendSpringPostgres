package de.mayer.backendspringpostgres.graph.model;

import java.util.LinkedList;
import java.util.stream.Collectors;

public record Path(LinkedList<Chapter> chapters, Integer approximateDurationInMinutes) {

    @Override
    public String toString() {
        return "%s (%d Minutes)".formatted(
                chapters.stream().map(Chapter::name).collect(Collectors.joining(" -> ")),
                approximateDurationInMinutes);
    }
}
