package io.github.bfur64.terminal.input;

import org.jspecify.annotations.NonNull;

public record KeyStroke(KeyType keyType, Character character) {
    public KeyStroke(Character character) {
        this(KeyType.CHARACTER, character);
    }

    public KeyStroke(KeyType keyType) {
        this(keyType, null);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof KeyStroke(KeyType otherType, Character otherChar))) return false;
        if (this.keyType != otherType) return false;
        if (this.keyType == KeyType.CHARACTER) {
            return this.character == otherChar;
        }
        return true;
    }

    @Override @NonNull
    public String toString() {
        return switch (keyType) {
            case CHARACTER -> {
                if (character == ' ') {
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
            case UNKNOWN -> "Unknown";
        };
    }
}
