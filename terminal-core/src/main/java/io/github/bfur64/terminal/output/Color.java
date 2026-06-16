package io.github.bfur64.terminal.output;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Color(int r, int g, int b) {
    public static Color of(int r, int g, int b) {
        return new Color(r, g, b);
    }
}
