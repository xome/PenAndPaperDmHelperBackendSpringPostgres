package de.mayer.backendspringpostgres.adventure;

import de.mayer.backendspringpostgres.adventure.chapter.Chapter;

import java.util.List;

public record Adventure(String name, List<Chapter> chapters) {
    public Adventure {
        if (name == null || name.isEmpty())
            throw new RuntimeException(new IllegalAccessException("Name cannot be null or empty."));
    }
}
