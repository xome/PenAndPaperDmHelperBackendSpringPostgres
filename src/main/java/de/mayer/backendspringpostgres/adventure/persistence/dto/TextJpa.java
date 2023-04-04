package de.mayer.backendspringpostgres.adventure.persistence.dto;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "text")
public class TextJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "record_id")
    @OneToOne(cascade = CascadeType.REMOVE)
    private RecordJpa recordJpa;

    @Column(name = "text")
    private String text;

    public TextJpa() {
    }

    public TextJpa(RecordJpa recordJpa, String text) {
        this.recordJpa = recordJpa;
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextJpa textJpa = (TextJpa) o;
        return Objects.equals(id, textJpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TextJpa{" +
                "id=" + id +
                ", recordJpa=" + recordJpa +
                ", text='" + text + '\'' +
                '}';
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
