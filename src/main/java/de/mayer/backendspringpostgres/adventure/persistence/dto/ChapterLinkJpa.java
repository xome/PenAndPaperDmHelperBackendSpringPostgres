package de.mayer.backendspringpostgres.adventure.persistence.dto;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "chapter_link")
public class ChapterLinkJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "record_id")
    @OneToOne(cascade = CascadeType.REMOVE)
    private RecordJpa recordJpa;

    @Column(name = "chapter_to")
    private Long chapterTo;

    public ChapterLinkJpa() {
    }

    public ChapterLinkJpa(RecordJpa recordJpa, Long chapterTo) {
        this.recordJpa = recordJpa;
        this.chapterTo = chapterTo;
    }

    @Override
    public String toString() {
        return "ChapterLinkJpa{" +
                "id=" + id +
                ", recordJpa=" + recordJpa +
                ", chapterTo=" + chapterTo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterLinkJpa that = (ChapterLinkJpa) o;
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

    public RecordJpa getRecordJpa() {
        return recordJpa;
    }

    public void setRecordJpa(RecordJpa recordJpa) {
        this.recordJpa = recordJpa;
    }

    public Long getChapterTo() {
        return chapterTo;
    }

    public void setChapterTo(Long chapterTo) {
        this.chapterTo = chapterTo;
    }
}
