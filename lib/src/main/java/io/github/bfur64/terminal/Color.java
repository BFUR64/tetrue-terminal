package io.github.bfur64.terminal;

public class Color {

    private final int rgb;

    private Color(int rgb) {
        this.rgb = rgb;
    }

    public static Color of(int rgb) {
        return new Color(rgb);
    }

    public static Color of(int red, int blue, int green) {
        final int rgb = (0xFF << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | (blue & 0xFF);
        return new Color(rgb);
    }

    public int getRgb() {
        return rgb;
    }

    public int getRed() {
        return (rgb >>> 16) & 0xFF;
    }

    public int getGreen() {
        return (rgb >>> 8) & 0xFF;
    }

    public int getBlue() {
        return rgb & 0xFF;
    }
}
