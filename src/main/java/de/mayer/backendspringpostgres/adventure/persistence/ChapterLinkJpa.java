package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "chapter_link")
public class ChapterLinkJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "chapter_to")
    private Long chapterTo;

    public ChapterLinkJpa() {
    }

    public ChapterLinkJpa(Long recordId, Long chapterTo) {
        this.recordId = recordId;
        this.chapterTo = chapterTo;
    }

    @Override
    public String toString() {
        return "ChapterLinkJpa{" +
                "id=" + id +
                ", recordId=" + recordId +
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

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getChapterTo() {
        return chapterTo;
    }

    public void setChapterTo(Long chapterTo) {
        this.chapterTo = chapterTo;
    }
}
