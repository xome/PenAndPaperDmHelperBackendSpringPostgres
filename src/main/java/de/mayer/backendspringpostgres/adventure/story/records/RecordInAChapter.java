package de.mayer.backendspringpostgres.adventure.story.records;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public abstract class RecordInAChapter {

    protected String adventure;
    protected String chapter;
    protected Integer index;


    @JsonIgnore
    public String getChapter() {
        return chapter;
    }
    @JsonIgnore
    public void setChapter(String chapter) {
        this.chapter = chapter;
    }

    @JsonIgnore
    public Integer getIndex() {
        return index;
    }

    @JsonIgnore
    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordInAChapter that = (RecordInAChapter) o;
        return chapter.equals(that.chapter) && index.equals(that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapter, index);
    }
}
