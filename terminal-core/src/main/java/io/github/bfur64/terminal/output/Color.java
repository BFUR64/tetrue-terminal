package io.github.bfur64.terminal.output;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Color(int rgb) {
    public static Color of(int rgb) {
        return new Color(rgb);
    }

    public static Color of(int r, int g, int b) {
        return new Color((0xFF << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF));
    }

    public int r() {
        return (rgb >>> 16) & 0xFF;
    }

    public int g() {
        return (rgb >>> 8) & 0xFF;
    }

    public int b() {
        return rgb & 0xFF;
    }
}
