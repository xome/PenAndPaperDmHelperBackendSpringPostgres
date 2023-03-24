package de.mayer.backendspringpostgres.graph.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "adventure")
public class AdventureJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public AdventureJpa(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public AdventureJpa() {
    }

    public AdventureJpa(String adventure) {
        this.name = adventure;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdventureJpa that = (AdventureJpa) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }


    @Override
    public String toString() {
        return "AdventureJpa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
