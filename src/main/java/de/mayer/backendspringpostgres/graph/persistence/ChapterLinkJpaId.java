package de.mayer.backendspringpostgres.graph.persistence;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class ChapterLinkJpaId implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;
    private String adventure;
    private String chapterFrom;
    private Integer index;

    public ChapterLinkJpaId() {
    }

    public ChapterLinkJpaId(String adventure, String chapterFrom, Integer index) {
        this.adventure = adventure;
        this.chapterFrom = chapterFrom;
        this.index = index;
    }

    public String getAdventure() {
        return adventure;
    }

    public void setAdventure(String adventure) {
        this.adventure = adventure;
    }

    public String getChapterFrom() {
        return chapterFrom;
    }

    public void setChapterFrom(String chapterFrom) {
        this.chapterFrom = chapterFrom;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChapterLinkJpaId that = (ChapterLinkJpaId) o;
        return Objects.equals(adventure, that.adventure) && Objects.equals(chapterFrom, that.chapterFrom) && Objects.equals(index, that.index);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adventure, chapterFrom, index);
    }

    @Override
    public String toString() {
        return "ChapterLinkJpaId{" +
                "adventure='" + adventure + '\'' +
                ", chapterFrom='" + chapterFrom + '\'' +
                ", index=" + index +
                '}';
    }
}
