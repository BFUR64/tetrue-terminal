package io.github.bfur64.terminal.v3;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum TextColor {
    WHITE(Color.of(255, 255, 255)),
    SILVER(Color.of(211, 211, 211)),
    LIGHTGREY(Color.of(192, 192, 192)),
    MEDIUMGREY(Color.of(169, 169, 169)),
    DARKGREY(Color.of(128, 128, 128)),
    BLACK(Color.of(0, 0, 0)),

    RED(Color.of(255, 0, 0)),
    LIME(Color.of(0, 255, 0)),
    BLUE(Color.of(0, 0, 255)),

    YELLOW(Color.of(255, 255, 0)),
    MAGENTA(Color.of(255, 0, 255)),
    CYAN(Color.of(0, 255, 255)),

    DARKRED(Color.of(139, 0, 0)),
    MAROON(Color.of(128, 0, 0)),

    GREEN(Color.of(0, 128, 0)),
    DARKGREEN(Color.of(0, 100, 0)),
    OLIVE(Color.of(128, 128, 0)),

    DARKBLUE(Color.of(0, 0, 139)),
    NAVY(Color.of(0, 0, 128)),

    TEAL(Color.of(0, 128, 128)),

    PURPLE(Color.of(128, 0, 128)),
    INDIGO(Color.of(75, 0, 130)),

    ORANGE(Color.of(255, 165, 0)),
    GOLD(Color.of(255, 215, 0)),
    PINK(Color.of(255, 192, 203)),
    CORAL(Color.of(255, 127, 80)),
    TOMATO(Color.of(255, 99, 71)),

    SKYBLUE(Color.of(135, 206, 235)),
    LIGHTBLUE(Color.of(173, 216, 230)),
    WHEAT(Color.of(245, 222, 179)),
    BEIGE(Color.of(245, 245, 220));

    private final Color color;

    TextColor(Color color) {
        this.color = color;
    }

    public Color color() {
        return color;
    }
}
