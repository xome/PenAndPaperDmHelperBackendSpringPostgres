package de.mayer.backendspringpostgres.graph.model;

public class InvalidGraphException extends Throwable {
    public InvalidGraphException(String message) {
        super(message);
    }
}
