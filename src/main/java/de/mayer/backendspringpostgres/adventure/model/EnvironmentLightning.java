package de.mayer.backendspringpostgres.adventure.model;


import de.mayer.backendspringpostgres.IllegalModelAccessException;

public record EnvironmentLightning(Double brightness, int[] rgb) implements RecordInAChapter {

    public EnvironmentLightning {
        if (brightness == null)
            brightness = 0.0d;

        if (rgb == null) {
            throw new RuntimeException(new IllegalAccessException("RGB cannot be null!"));
        }

        if (rgb.length != 3) {
            throw new RuntimeException(new IllegalAccessException("RGB has to consist of three values!"));
        }

        int index;
        for (index = 0; index < 3; index++) {
            Character rOrGorB = switch (index) {
                case 0 -> 'R';
                case 1 -> 'G';
                case 2 -> 'B';
                default -> throw new IndexOutOfBoundsException();
            };

            if (rgb[index] < 0) {
                throw new IllegalModelAccessException(
                        "%s value has to be positive."
                                .formatted(rOrGorB));
            }
            if (rgb[index] > 255) {
                throw new IllegalModelAccessException(
                        "%s value has to be less than or equal to 255."
                                .formatted(rOrGorB));
            }
        }
    }
}
