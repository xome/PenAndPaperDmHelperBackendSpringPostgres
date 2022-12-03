package de.mayer.backendspringpostgres.adventure.story.records;

public class Picture extends RecordInAChapter{

    private String base64;
    private String fileFormat;
    private boolean isShareableWithGroup;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public boolean isShareableWithGroup() {
        return isShareableWithGroup;
    }

    public void setShareableWithGroup(boolean shareableWithGroup) {
        isShareableWithGroup = shareableWithGroup;
    }

    @Override
    public String toString() {
        return "Picture{base64='%s...', fileFormat='%s', isShareableWithGroup=%s, chapterName='%s', index=%d}"
                .formatted(base64.substring(1, 10), fileFormat, isShareableWithGroup, chapter, index);
    }
}
