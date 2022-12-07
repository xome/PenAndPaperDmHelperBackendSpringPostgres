package de.mayer.backendspringpostgres.adventure.chapter.record.model;

public class Text extends RecordInAChapter {

    private final String text;

    public Text(String adventure, String chapter, Integer index, String text) {
        super(adventure, chapter, index);
        if (text == null || text.isEmpty())
            throw new RuntimeException(new IllegalAccessException("Text cannot be null or empty."));
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Text{text='%s', chapterName='%s', index=%d}"
                .formatted(text, chapter, index);
    }
}
