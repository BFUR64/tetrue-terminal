package io.github.bfur64.terminal.input;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record CharacterKey(char character) implements KeyStroke {
    @Override
    public String toString() {
        if (character == ' ') {
            return "Space";
        }

        return String.valueOf(character);
    }
}
