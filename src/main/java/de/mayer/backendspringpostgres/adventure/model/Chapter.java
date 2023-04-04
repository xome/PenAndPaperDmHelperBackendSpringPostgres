package de.mayer.backendspringpostgres.adventure.model;

import java.util.List;

public record Chapter(String name,
                      String subheader,
                      Integer approximateDurationInMinutes,
                      List<RecordInAChapter> records
){}
