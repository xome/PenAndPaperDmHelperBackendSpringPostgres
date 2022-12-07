package de.mayer.backendspringpostgres.adventure.chapter.record.model;

public class ChapterLink extends RecordInAChapter {

    private String chapterNameFrom;
    private String chapterNameTo;

    protected ChapterLink(String adventure, String chapter, Integer index) {
        super(adventure, chapter, index);
    }

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
                .formatted(chapterNameFrom, chapterNameTo, chapter, index);
    }
}
