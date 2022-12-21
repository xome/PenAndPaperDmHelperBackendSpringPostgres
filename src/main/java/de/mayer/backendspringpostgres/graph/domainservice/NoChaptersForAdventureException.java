package de.mayer.backendspringpostgres.graph.domainservice;

public class NoChaptersForAdventureException extends Throwable {
    public NoChaptersForAdventureException(String adventure) {
        super(adventure);
    }
}
