package de.mayer.backendspringpostgres;

public class IllegalModelAccessException extends RuntimeException {

    public IllegalModelAccessException(String message){
        super(new IllegalAccessException(message));
    }

}
