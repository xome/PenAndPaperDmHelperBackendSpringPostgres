package de.mayer.backendspringpostgres.graph.model;

import java.util.Set;

public class InvalidGraphException extends Throwable {
    private final Set<Path> problematicPaths;

    public InvalidGraphException(String message, Set<Path> problematicPaths) {
        super(message);
        this.problematicPaths = problematicPaths;
    }

    public Set<Path> getProblematicPaths() {
        return problematicPaths;
    }
}
