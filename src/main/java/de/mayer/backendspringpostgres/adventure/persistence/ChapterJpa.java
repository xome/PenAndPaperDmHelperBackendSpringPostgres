package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "chapter")
public class ChapterJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adventure")
    private Long adventure;

    @Column(name = "name")
    private String name;

    @Column(name = "subheader")
    private String subheader;

    @Column(name = "approximate_duration_in_minutes")
    private Long approximateDurationInMinutes;

    public ChapterJpa() {
    }

    public ChapterJpa(Long adventure, String name, String subheader, Long approximateDurationInMinutes) {
        this.adventure = adventure;
        this.name = name;
        this.subheader = subheader;
        this.approximateDurationInMinutes = approximateDurationInMinutes;
    }

    @Override
    public String toString() {
        return "ChapterJpa{" +
                "id=" + id +
                ", adventure=" + adventure +
                ", name='" + name + '\'' +
                ", subheader='" + subheader + '\'' +
                ", approximateDurationInMinutes=" + approximateDurationInMinutes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterJpa that = (ChapterJpa) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdventure() {
        return adventure;
    }

    public void setAdventure(Long adventure) {
        this.adventure = adventure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubheader() {
        return subheader;
    }

    public void setSubheader(String subheader) {
        this.subheader = subheader;
    }

    public Long getApproximateDurationInMinutes() {
        return approximateDurationInMinutes;
    }

    public void setApproximateDurationInMinutes(Long approximateDurationInMinutes) {
        this.approximateDurationInMinutes = approximateDurationInMinutes;
    }
}
