package de.mayer.backendspringpostgres.graph.model;

public class IllegalModelAccessException extends RuntimeException {

    public IllegalModelAccessException(String message){
        super(new IllegalAccessException(message));
    }

}
