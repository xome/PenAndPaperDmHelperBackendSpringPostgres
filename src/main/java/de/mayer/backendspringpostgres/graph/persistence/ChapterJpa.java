package de.mayer.backendspringpostgres.graph.persistence;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "chapter")
@IdClass(ChapterJpaId.class)
public final class ChapterJpa {


    @Column(name = "adventure")
    @Id
    private String adventure;
    @Column(name = "name")
    @Id
    private String name;
    @Column(name = "approximate_duration_in_minutes")
    private Double approximateDurationInMinutes;

    public ChapterJpa(String adventure,
                      String name,
                      Double approximateDurationInMinutes
    ) {
        this.adventure = adventure;
        this.name = name;
        this.approximateDurationInMinutes = approximateDurationInMinutes;
    }

    public ChapterJpa() {

    }

    public static ChapterJpa fromModel(String adventure, Chapter chapterRecord) {
        return new ChapterJpa(adventure, chapterRecord.name(), chapterRecord.approximateDurationInMinutes());
    }

    @Column(name = "adventure")
    public String getAdventure() {
        return adventure;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "approximate_duration_in_minutes")
    public Double getApproximateDurationInMinutes() {
        return approximateDurationInMinutes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChapterJpa) obj;
        return Objects.equals(this.adventure, that.adventure) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.approximateDurationInMinutes, that.approximateDurationInMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adventure, name, approximateDurationInMinutes);
    }

    @Override
    public String toString() {
        return "ChapterJpa[" +
                "adventure=" + adventure + ", " +
                "name=" + name + ", " +
                "approximateDurationInMinutes=" + approximateDurationInMinutes + ']';
    }

}