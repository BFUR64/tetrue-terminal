package io.github.bfur64.terminal;

public record Size(int height, int width) {

    public static Size of(int height, int width) {
        return new Size(height, width);
    }
}
