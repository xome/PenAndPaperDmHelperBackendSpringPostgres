package de.mayer.backendspringpostgres.adventure.chapters;

import java.util.List;

public record Chapter(String name, String subheader, Double approximateDurationInMinutes, List<Record> records) {
}
