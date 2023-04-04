package de.mayer.backendspringpostgres.adventure.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;


class EnvironmentLightningTest {

    @Test
    @DisplayName("""
            Given brightness is null,
            when an EnvironmentLightning record is created,
            then brightness is 0
            """)
    void defaultBrightnessIsZero() {
        var lightning = new EnvironmentLightning(null, new int[]{0, 0, 0});
        assertThat(lightning.brightness(), is(0.0d));
    }

    @Test
    @DisplayName("""
            Given rgb is null,
            when an EnvironmentLightning record is created,
            then an exception is raised
            """)
    void rgbCannotBeNull() {
        var exc = assertThrows(RuntimeException.class, () -> new EnvironmentLightning(null, null));
        assertThat(exc.getCause(), is(instanceOf(IllegalAccessException.class)));
        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("rgb"));
    }

    @Test
    @DisplayName("""
            Given rgb has a size of less than 3,
            when an EnvironmentLightning record is created,
            then an exception is raised
            """)
    void rgbHasLessThanThreeElements() {
        var exc = assertThrows(RuntimeException.class,
                () ->
                        new EnvironmentLightning(null, new int[]{0, 0}));

        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("RGB has to consist of three values!"));
    }

    @Test
    @DisplayName("""
            Given rgb has a size of more than 3,
            when an EnvironmentLightning record is created,
            then an exception is raised
            """)
    void rgbHasMoreThanThreeElements() {
        var exc = assertThrows(RuntimeException.class,
                () ->
                        new EnvironmentLightning(null, new int[]{0, 0, 0, 0}));

        assertThat(exc.getCause().getMessage(), containsStringIgnoringCase("RGB has to consist of three values!"));
    }

    @Test
    @DisplayName("""
            Given a negative r value,
            when an EnvironmentLightning record is created,
            then an exception is raised
            """)
    void rValueCannotBeNegative() {
        var exc = assertThrows(RuntimeException.class,
                () ->
                        new EnvironmentLightning(null, new int[]{-1, 0, 0})
        );

        assertThat(exc.getMessage(), containsStringIgnoringCase("r value has to be positive"));
    }

    @Test
    @DisplayName("""
            Given a r value above 255,
            when an EnvironmentLightning record is created,
            then an exception is raised
            """)
    void rValueCannotBeGreaterThan255() {
        var exc = assertThrows(RuntimeException.class,
                () ->
                        new EnvironmentLightning(null, new int[]{256, 0, 0})
        );

        assertThat(exc.getMessage(), containsStringIgnoringCase("r value has to be less than or equal to 255"));
    }


}