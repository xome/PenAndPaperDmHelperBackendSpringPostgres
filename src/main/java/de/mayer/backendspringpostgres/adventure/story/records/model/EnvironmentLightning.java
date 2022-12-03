package de.mayer.backendspringpostgres.adventure.story.records.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EnvironmentLightning extends RecordInAChapter {

    private Float brightness;
    private Integer rgb_1;
    private Integer rgb_2;
    private Integer rgb_3;

    public Integer[] getRgb() {
        return new Integer[]{rgb_1, rgb_2, rgb_3};
    }

    public Float getBrightness() {
        return brightness;
    }

    public void setBrightness(Float brightness) {
        this.brightness = brightness;
    }

    @JsonIgnore
    public Integer getRgb_1() {
        return rgb_1;
    }

    @JsonIgnore
    public void setRgb_1(Integer rgb_1) {
        this.rgb_1 = rgb_1;
    }

    @JsonIgnore
    public Integer getRgb_2() {
        return rgb_2;
    }

    @JsonIgnore
    public void setRgb_2(Integer rgb_2) {
        this.rgb_2 = rgb_2;
    }

    @JsonIgnore
    public Integer getRgb_3() {
        return rgb_3;
    }

    @JsonIgnore
    public void setRgb_3(Integer rgb_3) {
        this.rgb_3 = rgb_3;
    }

    @Override
    public String toString() {
        return "EnvironmentLightning{brightness=%s, rgb_1=%d, rgb_2=%d, rgb_3=%d, chapterName='%s', index=%d}"
                .formatted(brightness, rgb_1, rgb_2, rgb_3, chapter, index);
    }
}
