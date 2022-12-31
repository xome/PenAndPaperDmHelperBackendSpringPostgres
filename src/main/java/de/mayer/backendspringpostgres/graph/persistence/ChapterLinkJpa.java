package de.mayer.backendspringpostgres.graph.persistence;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "chapter_link")
@IdClass(ChapterLinkJpaId.class)
public class ChapterLinkJpa {
    @Id
    @Column(name = "adventure")
    private String adventure;

    @Id
    @Column(name="chapter_from")
    private String chapterFrom;

    @Id
    @Column(name = "index")
    private Integer index;

    @Column(name = "chapter_to")
    private String to;

    public ChapterLinkJpa() {
    }

    public ChapterLinkJpa(String adventure, String chapterFrom, Integer index, String to) {
        this.adventure = adventure;
        this.chapterFrom = chapterFrom;
        this.to = to;
        this.index = index;
    }

    public String adventure() {
        return adventure;
    }

    public String chapterFrom() {
        return chapterFrom;
    }

    public String to() {
        return to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChapterLinkJpa) obj;
        return Objects.equals(this.adventure, that.adventure) &&
                Objects.equals(this.chapterFrom, that.chapterFrom) &&
                Objects.equals(this.to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adventure, chapterFrom, to);
    }

    @Override
    public String toString() {
        return "ChapterLinkId[" +
                "adventure=" + adventure + ", " +
                "chapterFrom=" + chapterFrom + ", " +
                "to=" + to + ']';
    }

}
