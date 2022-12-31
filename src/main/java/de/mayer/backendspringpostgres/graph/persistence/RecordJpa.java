package de.mayer.backendspringpostgres.graph.persistence;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "record")
@IdClass(RecordJpa.class)
public class RecordJpa implements Serializable {

    @Id
    @Column(name = "adventure")
    private String adventure;

    @Id
    @Column(name = "chapter")
    private String chapter;

    @Id
    @Column(name = "index")
    private Integer index;

    public RecordJpa() {
    }

    public RecordJpa(String adventure, String chapter, Integer index) {
        this.adventure = adventure;
        this.chapter = chapter;
        this.index = index;
    }

    public String getAdventure() {
        return adventure;
    }

    public void setAdventure(String adventure) {
        this.adventure = adventure;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordJpa recordJpa = (RecordJpa) o;
        return Objects.equals(adventure, recordJpa.adventure) && Objects.equals(chapter, recordJpa.chapter) && Objects.equals(index, recordJpa.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adventure, chapter, index);
    }


    @Override
    public String toString() {
        return "RecordJpa{" +
                "adventure='" + adventure + '\'' +
                ", chapter='" + chapter + '\'' +
                ", index=" + index +
                '}';
    }
}
