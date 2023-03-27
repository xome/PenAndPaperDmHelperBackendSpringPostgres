package de.mayer.backendspringpostgres.graph.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "chapter")
public final class ChapterJpa {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "adventure")
    private Long adventure;

    @Column(name = "name")
    private String name;
    @Column(name = "approximate_duration_in_minutes")
    private Long approximateDurationInMinutes;

    public ChapterJpa(Long adventure,
                      String name,
                      Long approximateDurationInMinutes
    ) {
        this.adventure = adventure;
        this.name = name;
        this.approximateDurationInMinutes = approximateDurationInMinutes;
    }

    public ChapterJpa() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAdventure(Long adventure) {
        this.adventure = adventure;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApproximateDurationInMinutes(Long approximateDurationInMinutes) {
        this.approximateDurationInMinutes = approximateDurationInMinutes;
    }

    @Column(name = "adventure")
    public Long getAdventure() {
        return adventure;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    @Column(name = "approximate_duration_in_minutes")
    public Long getApproximateDurationInMinutes() {
        return approximateDurationInMinutes;
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

    @Override
    public String toString() {
        return "ChapterJpa{" +
                "id=" + id +
                ", adventure=" + adventure +
                ", name='" + name + '\'' +
                ", approximateDurationInMinutes=" + approximateDurationInMinutes +
                '}';
    }
}