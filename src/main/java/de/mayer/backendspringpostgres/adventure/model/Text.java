package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.IllegalModelAccessException;

public record Text(String text) implements RecordInAChapter {
    public Text {
        if (text == null || text.isEmpty())
            throw new IllegalModelAccessException("Text cannot be null or empty.");
    }
}
