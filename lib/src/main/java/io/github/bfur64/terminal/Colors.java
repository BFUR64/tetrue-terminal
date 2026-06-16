package io.github.bfur64.terminal;

public enum Colors {
    RED(16711680),
    BLUE(255),
    GREEN(65280),
    WHITE(16777215),
    BLACK(0);

    private final int rgb;

    Colors(int rgb) {
        this.rgb = rgb;
    }

    public Color get() {
        return Color.of(rgb);
    }
}
