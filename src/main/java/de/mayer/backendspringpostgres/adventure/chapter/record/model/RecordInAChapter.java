package de.mayer.backendspringpostgres.adventure.chapter.record.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public abstract class RecordInAChapter {

    protected String adventure;
    protected String chapter;
    protected Integer index;

    protected RecordInAChapter(String adventure, String chapter, Integer index) {
        if (adventure == null || adventure.isEmpty())
            throw new RuntimeException(new IllegalAccessException("adventure cannot be null or empty."));
        this.adventure = adventure;
        if (chapter == null || chapter.isEmpty())
            throw new RuntimeException(new IllegalAccessException("chapter cannot be null or empty."));
        this.chapter = chapter;
        if (index == null) throw new RuntimeException(new IllegalAccessException("index cannot be null"));
        if (index < 0)
            throw new RuntimeException(new IllegalAccessException("index cannot be negative. Was %d.".formatted(index)));
        this.index = index;
    }


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
