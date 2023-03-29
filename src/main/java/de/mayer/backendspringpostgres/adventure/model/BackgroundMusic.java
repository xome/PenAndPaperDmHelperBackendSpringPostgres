package de.mayer.backendspringpostgres.adventure.model;

public record BackgroundMusic(String name, String base64) implements RecordInAChapter {

    public BackgroundMusic {
        if (name == null || name.isEmpty())
            throw new IllegalModelAccessException("Name cannot be null or empty.");
        if (base64 == null || base64.isEmpty())
            throw new IllegalModelAccessException("Data cannot be null or empty.");
    }
}
