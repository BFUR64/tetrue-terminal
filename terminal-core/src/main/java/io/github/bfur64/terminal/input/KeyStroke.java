package io.github.bfur64.terminal.input;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record KeyStroke(KeyType keyType, @Nullable Character character) {
    public KeyStroke(Character character) {
        this(KeyType.CHARACTER, character);
    }

    public KeyStroke(KeyType keyType) {
        this(keyType, null);
    }

    @Override
    public String toString() {
        return switch (keyType) {
            case CHARACTER -> {
                if (character != null && character == ' ') {
                    yield "Space";
                }

                yield String.valueOf(character);
            }
            case ESCAPE -> "Escape";
            case BACKSPACE -> "Backspace";
            case ENTER -> "Enter";
            case ARROW_UP -> "Arrow Up";
            case ARROW_DOWN -> "Arrow Down";
            case ARROW_LEFT -> "Arrow Left";
            case ARROW_RIGHT -> "Arrow Right";
            case HOME -> "Home";
            case END -> "End";
            case PAGE_UP -> "Page Up";
            case PAGE_DOWN -> "Page Down";
            case UNKNOWN -> "Unknown";
        };
    }
}
