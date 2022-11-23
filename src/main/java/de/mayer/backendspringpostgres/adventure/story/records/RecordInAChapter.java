package de.mayer.backendspringpostgres.adventure.story.records;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public abstract class RecordInAChapter {

    protected String chapterName;
    protected Integer index;


    @JsonIgnore
    public String getChapterName() {
        return chapterName;
    }
    @JsonIgnore
    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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
        return chapterName.equals(that.chapterName) && index.equals(that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chapterName, index);
    }
}
