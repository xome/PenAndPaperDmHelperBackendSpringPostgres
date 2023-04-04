package de.mayer.backendspringpostgres.adventure.persistence.dto;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "adventure")
public class AdventureJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public AdventureJpa(String name) {
        this.name = name;
    }

    public AdventureJpa() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdventureJpa that = (AdventureJpa) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AdventureJpa{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
