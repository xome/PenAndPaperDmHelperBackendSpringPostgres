package de.mayer.backendspringpostgres.graph.model;

import java.util.LinkedList;
import java.util.stream.Collectors;

public record Path(LinkedList<Chapter> chapters, Double approximateDurationInMinutes) {

    @Override
    public String toString() {
        return "%s (%.2f Minutes)".formatted(
                chapters.stream().map(Chapter::name).collect(Collectors.joining(" -> ")),
                approximateDurationInMinutes);
    }
}
