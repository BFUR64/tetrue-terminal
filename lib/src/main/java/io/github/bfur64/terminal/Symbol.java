package io.github.bfur64.terminal;

public record Symbol(char value, int foregroundColor, int backgroundColor) {

    public static final Symbol DEFAULT = new Symbol(' ', Color.BLACK, Color.WHITE);

    public Symbol(char value, Color foregroundColor, Color backgroundColor) {
        this(value, foregroundColor.getRgb(), backgroundColor.getRgb());
    }
}
