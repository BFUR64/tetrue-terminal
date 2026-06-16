package io.github.bfur64.terminal;

public record Symbol(char value, Color foregroundColor, Color backgroundColor) {

    public Symbol(char value, int foregroundColor, int backgroundColor) {
        this(value, Color.of(foregroundColor), Color.of(backgroundColor));
    }

    public Symbol(char value, Colors foregroundColor, Colors backgroundColor) {
        this(value, foregroundColor.get(), backgroundColor.get());
    }
}
