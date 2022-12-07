package de.mayer.backendspringpostgres.adventure.chapter.record.model;

public class Picture extends RecordInAChapter {

    public Picture(String adventure, String chapter, Integer index, String base64, String fileFormat, boolean isShareableWithGroup) {
        super(adventure, chapter, index);
        if (base64 == null) throw new RuntimeException(new IllegalAccessException("base64 cannot be null or empty."));
        this.base64 = base64;
        this.fileFormat = fileFormat;
        this.isShareableWithGroup = isShareableWithGroup;
    }

    private final String base64;
    private final String fileFormat;
    private final boolean isShareableWithGroup;

    public String getBase64() {
        return base64;
    }


    public String getFileFormat() {
        return fileFormat;
    }


    public boolean isShareableWithGroup() {
        return isShareableWithGroup;
    }

    @Override
    public String toString() {
        return "Picture{base64='%s...', fileFormat='%s', isShareableWithGroup=%s, chapterName='%s', index=%d}"
                .formatted(base64.substring(1, 10), fileFormat, isShareableWithGroup, chapter, index);
    }
}
