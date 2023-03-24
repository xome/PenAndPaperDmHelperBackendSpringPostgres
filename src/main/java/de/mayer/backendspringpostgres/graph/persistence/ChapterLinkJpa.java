package de.mayer.backendspringpostgres.graph.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "chapter_link")
public class ChapterLinkJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne()
    @JoinColumn(name = "record_id")
    private RecordJpa record;

    @Column(name = "chapter_to")
    private Long to;

    public ChapterLinkJpa() {
    }

    public ChapterLinkJpa(RecordJpa record, Long to) {
        this.record = record;
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChapterLinkJpa) obj;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ChapterLinkId[" +
                "id=" + id + ", " +
                "record=" + record.toString() + ", " +
                "to=" + to + "]";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecordJpa getRecord() {
        return record;
    }

    public void setRecord(RecordJpa record) {
        this.record = record;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
