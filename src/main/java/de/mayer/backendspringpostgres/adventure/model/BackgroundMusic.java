package de.mayer.backendspringpostgres.adventure.model;

import de.mayer.backendspringpostgres.IllegalModelAccessException;

public record BackgroundMusic(String name, byte[] data) implements RecordInAChapter {

    public BackgroundMusic {
        if (name == null || name.isEmpty())
            throw new IllegalModelAccessException("Name cannot be null or empty.");
        if (data == null || data.length == 0)
            throw new IllegalModelAccessException("Data cannot be null or empty.");
    }


    //TODO: later for JPA implementation:
    //      https://stackoverflow.com/questions/35505424/how-to-read-bytea-image-data-from-postgresql-with-jpa
}
