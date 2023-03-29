package de.mayer.backendspringpostgres.adventure.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name = "environment_lightning")
public class EnvironmentLightningJpa {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_id")
    private Long recordId;

    @Column(name = "rgb_1")
    private Integer rgb1;

    @Column(name = "rgb_2")
    private Integer rgb2;

    @Column(name = "rgb_3")
    private Integer rgb3;

    @Column(name = "brightness")
    private Double brightness;

    public EnvironmentLightningJpa() {
    }

    public EnvironmentLightningJpa(Long recordId, Integer rgb1, Integer rgb2, Integer rgb3, Double brightness) {
        this.recordId = recordId;
        this.rgb1 = rgb1;
        this.rgb2 = rgb2;
        this.rgb3 = rgb3;
        this.brightness = brightness;
    }

    @Override
    public String toString() {
        return "EnvironmentLightningJpa{" +
                "id=" + id +
                ", recordId=" + recordId +
                ", rgb1=" + rgb1 +
                ", rgb2=" + rgb2 +
                ", rgb3=" + rgb3 +
                ", brightness=" + brightness +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvironmentLightningJpa that = (EnvironmentLightningJpa) o;
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

    public Integer getRgb1() {
        return rgb1;
    }

    public void setRgb1(Integer rgb1) {
        this.rgb1 = rgb1;
    }

    public Integer getRgb2() {
        return rgb2;
    }

    public void setRgb2(Integer rgb2) {
        this.rgb2 = rgb2;
    }

    public Integer getRgb3() {
        return rgb3;
    }

    public void setRgb3(Integer rgb3) {
        this.rgb3 = rgb3;
    }

    public Double getBrightness() {
        return brightness;
    }

    public void setBrightness(Double brightness) {
        this.brightness = brightness;
    }
}
