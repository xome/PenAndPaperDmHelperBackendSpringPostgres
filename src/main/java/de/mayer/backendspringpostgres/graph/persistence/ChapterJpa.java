package de.mayer.backendspringpostgres.graph.persistence;

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
    @Column(name = "subheader")
    private String subheader;
    @Column(name = "approximate_duration_in_minutes")
    private Long approximateDurationInMinutes;

    public ChapterJpa(String adventure,
                      String name,
                      String subheader,
                      Long approximateDurationInMinutes
    ) {
        this.adventure = adventure;
        this.name = name;
        this.subheader = subheader;
        this.approximateDurationInMinutes = approximateDurationInMinutes;
    }

    public ChapterJpa() {

    }

    @Column(name = "adventure")
    public String adventure() {
        return adventure;
    }

    @Column(name = "name")
    public String name() {
        return name;
    }

    @Column(name = "subheader")
    public String subheader() {
        return subheader;
    }

    @Column(name = "approximate_duration_in_minutes")
    public Long approximateDurationInMinutes() {
        return approximateDurationInMinutes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChapterJpa) obj;
        return Objects.equals(this.adventure, that.adventure) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.subheader, that.subheader) &&
                Objects.equals(this.approximateDurationInMinutes, that.approximateDurationInMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adventure, name, subheader, approximateDurationInMinutes);
    }

    @Override
    public String toString() {
        return "ChapterJpa[" +
                "adventure=" + adventure + ", " +
                "name=" + name + ", " +
                "subheader=" + subheader + ", " +
                "approximateDurationInMinutes=" + approximateDurationInMinutes + ']';
    }

}