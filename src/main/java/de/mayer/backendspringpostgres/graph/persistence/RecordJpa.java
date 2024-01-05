package de.mayer.backendspringpostgres.graph.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "record")
public class RecordJpa  {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "index")
    private Integer index;

    @Column(name = "type")
    private String type = "ChapterLink";

    public RecordJpa() {
    }

    public RecordJpa(Long chapterId, Integer index) {
        this.chapterId = chapterId;
        this.index = index;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
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
        return Objects.equals(id, recordJpa.id) && Objects.equals(chapterId, recordJpa.chapterId) && Objects.equals(index, recordJpa.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chapterId, index);
    }

    @Override
    public String toString() {
        return "RecordJpa{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", index=" + index +
                '}';
    }
}
