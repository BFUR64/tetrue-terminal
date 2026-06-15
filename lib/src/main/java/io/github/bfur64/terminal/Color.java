package io.github.bfur64.terminal;

public enum Color {
    RED(16711680),
    BLUE(255),
    GREEN(65280),
    WHITE(16777215),
    BLACK(0);

    private final int rgb;

    Color(int rgb) {
        this.rgb = rgb;
    }

    public int getRgb() {
        return rgb;
    }
}
