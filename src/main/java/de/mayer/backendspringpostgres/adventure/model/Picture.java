package de.mayer.backendspringpostgres.adventure.model;

public record Picture(String base64, String fileFormat, Boolean isShareableWithGroup) implements RecordInAChapter {
    public Picture {
        if (base64 == null || base64.isEmpty())
            throw new IllegalModelAccessException("base64 cannot be null or empty.");
        if (fileFormat == null || fileFormat.isEmpty())
            throw new IllegalModelAccessException("File format cannot be null or empty.");
        if (isShareableWithGroup == null)
            throw new IllegalModelAccessException("isShareableWithGroup cannot be null.");
    }
}
