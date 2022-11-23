package de.mayer.backendspringpostgres.adventure.story.records;

public class Text extends RecordInAChapter {

    private String text;

    public Text(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Text{text='%s', chapterName='%s', index=%d}"
                .formatted(text, chapterName, index);
    }
}
