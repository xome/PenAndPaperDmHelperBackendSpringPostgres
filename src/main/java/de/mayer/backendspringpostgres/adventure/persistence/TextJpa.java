package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "text")
public class TextJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "text")
    private String text;

    public TextJpa() {
    }

    public TextJpa(Long recordId, String text) {
        this.recordId = recordId;
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextJpa{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", text='" + text + '\'' +
                '}';
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
