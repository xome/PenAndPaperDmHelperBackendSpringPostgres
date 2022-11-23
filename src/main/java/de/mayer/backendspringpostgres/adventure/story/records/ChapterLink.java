package de.mayer.backendspringpostgres.adventure.story.records;

public class ChapterLink extends RecordInAChapter {

    private String chapterNameFrom;
    private String chapterNameTo;

    public String getChapterNameFrom() {
        return chapterNameFrom;
    }

    public void setChapterNameFrom(String chapterNameFrom) {
        this.chapterNameFrom = chapterNameFrom;
    }

    public String getChapterNameTo() {
        return chapterNameTo;
    }

    public void setChapterNameTo(String chapterNameTo) {
        this.chapterNameTo = chapterNameTo;
    }


    @Override
    public String toString() {
        return "ChapterLink{chapterNameFrom='%s', chapterNameTo='%s', chapterName='%s', index=%d}"
                .formatted(chapterNameFrom, chapterNameTo, chapterName, index);
    }
}
