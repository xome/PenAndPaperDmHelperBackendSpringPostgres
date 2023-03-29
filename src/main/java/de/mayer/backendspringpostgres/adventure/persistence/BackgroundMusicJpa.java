package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "background_music")
public class BackgroundMusicJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "name")
    private String name;

    @Column(name = "base64")
    private String base64;

    public BackgroundMusicJpa() {
    }

    public BackgroundMusicJpa(Long recordId, String name, String base64) {
        this.recordId = recordId;
        this.name = name;
        this.base64 = base64;
    }

    @Override
    public String toString() {
        return "BackgroundMusicJpa{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", name='" + name + '\'' +
                ", base64='" + base64 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackgroundMusicJpa that = (BackgroundMusicJpa) o;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
