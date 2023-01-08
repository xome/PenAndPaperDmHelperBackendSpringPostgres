package de.mayer.backendspringpostgres.graph.model;

public class NoChaptersForAdventureException extends Throwable {
    public NoChaptersForAdventureException(String adventure) {
        super(adventure);
    }
}
