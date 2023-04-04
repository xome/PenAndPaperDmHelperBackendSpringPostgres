package de.mayer.backendspringpostgres.adventure.model;

public sealed interface RecordInAChapter permits BackgroundMusic,
        ChapterLink,
        EnvironmentLightning,
        Picture,
        Text {
}
