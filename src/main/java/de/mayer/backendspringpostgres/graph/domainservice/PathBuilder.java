package de.mayer.backendspringpostgres.graph.domainservice;

import de.mayer.backendspringpostgres.graph.model.Chapter;
import de.mayer.backendspringpostgres.graph.model.Path;

import java.util.LinkedList;

public class PathBuilder {

    private Path path;

    public PathBuilder(){
    }
    public PathBuilder(Chapter startingPoint) {
        this.addChapter(startingPoint);
    }

    public PathBuilder initiateFrom(Path path){
        this.path = path;
        return this;
    }

    public PathBuilder addChapter(Chapter chapter) {
        var path = this.build();
        var newChapters = path.chapters();
        newChapters.add(chapter);
        var newDuration = path.approximateDurationInMinutes() + chapter.approximateDurationInMinutes();
        this.path = new Path(newChapters, newDuration);
        return this;
    }

    public Path build(){
        if (path == null){
            return new Path(new LinkedList<>(), 0);
        }
        return this.path;
    }


    public boolean isEmpty() {
        return this.path == null || this.path.chapters().isEmpty();
    }
}
