package de.mayer.backendspringpostgres.graph.persistence;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class ChapterJpaId implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private String adventure;
    private String name;

    public ChapterJpaId(){

    }

    public ChapterJpaId(String adventure, String name) {
        this.adventure = adventure;
        this.name = name;
    }

    public String adventure() {
        return adventure;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ChapterJpaId) obj;
        return Objects.equals(this.adventure, that.adventure) &&
                Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adventure, name);
    }

    @Override
    public String toString() {
        return "ChapterJpaId[" +
                "adventure=" + adventure + ", " +
                "name=" + name + ']';
    }

}
