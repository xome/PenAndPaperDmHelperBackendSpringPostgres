package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "record")
public class RecordJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "index")
    private Integer index;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private RecordType type;

    public RecordJpa() {
    }

    public RecordJpa(Long chapterId, Integer index, RecordType type) {
        this.chapterId = chapterId;
        this.index = index;
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecordJpa{" +
                "id=" + id +
                ", chapterId=" + chapterId +
                ", index=" + index +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordJpa recordJpa = (RecordJpa) o;
        return Objects.equals(id, recordJpa.id);
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

    public RecordType getType() {
        return type;
    }

    public void setType(RecordType type) {
        this.type = type;
    }
}
