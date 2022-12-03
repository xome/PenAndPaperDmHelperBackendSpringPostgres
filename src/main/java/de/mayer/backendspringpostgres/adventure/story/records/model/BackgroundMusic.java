package de.mayer.backendspringpostgres.adventure.story.records.model;

public class BackgroundMusic extends RecordInAChapter {

    private String name;
    private byte[] data;
    //TODO: https://stackoverflow.com/questions/35505424/how-to-read-bytea-image-data-from-postgresql-with-jpa


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BackgroundMusic{" +
                "name='" + name + '\'' +
                ", chapterName='" + chapter + '\'' +
                ", index=" + index +
                '}';
    }
}
