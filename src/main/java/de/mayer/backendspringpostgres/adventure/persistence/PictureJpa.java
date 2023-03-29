package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "picture")
public class PictureJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "base64")
    private String base64;

    @Column(name = "file_format")
    private String fileFormat;

    @Column(name = "is_shareable_with_group")
    private Boolean isShareableWithGroup;

    public PictureJpa() {
    }

    public PictureJpa(Long recordId, String base64, String fileFormat, Boolean isShareableWithGroup) {
        this.recordId = recordId;
        this.base64 = base64;
        this.fileFormat = fileFormat;
        this.isShareableWithGroup = isShareableWithGroup;
    }

    @Override
    public String toString() {
        return "PictureJpa{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", base64='" + base64 + '\'' +
                ", fileFormat='" + fileFormat + '\'' +
                ", isShareableWithGroup=" + isShareableWithGroup +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureJpa that = (PictureJpa) o;
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

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public Boolean getShareableWithGroup() {
        return isShareableWithGroup;
    }

    public void setShareableWithGroup(Boolean shareableWithGroup) {
        isShareableWithGroup = shareableWithGroup;
    }
}
