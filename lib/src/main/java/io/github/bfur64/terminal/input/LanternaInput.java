package io.github.bfur64.terminal.input;

import com.googlecode.lanterna.terminal.Terminal;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

public class LanternaInput implements TerminalInput {
    private final Terminal terminal;

    public LanternaInput(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public @NonNull KeyStroke readInput() {
        try {
            com.googlecode.lanterna.input.KeyStroke lanternaKeyStroke = terminal.readInput();

            if (lanternaKeyStroke.getKeyType() == com.googlecode.lanterna.input.KeyType.Character) {
                return new KeyStroke(lanternaKeyStroke.getCharacter());
            }

            return new KeyStroke(getKeyType(lanternaKeyStroke));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @Nullable KeyStroke pollInput() {
        try {
            com.googlecode.lanterna.input.KeyStroke lanternaKeyStroke = terminal.pollInput();

            if (lanternaKeyStroke == null) {
                return null;
            }

            if (lanternaKeyStroke.getKeyType() == com.googlecode.lanterna.input.KeyType.Character) {
                return new KeyStroke(lanternaKeyStroke.getCharacter());
            }

            return new KeyStroke(getKeyType(lanternaKeyStroke));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private @NonNull KeyType getKeyType(com.googlecode.lanterna.input.KeyStroke keyStroke) {
        return switch (keyStroke.getKeyType()) {
            case Escape -> KeyType.ESCAPE;
            case Backspace -> KeyType.BACKSPACE;
            case Enter -> KeyType.ENTER;
            case ArrowUp -> KeyType.ARROW_UP;
            case ArrowDown -> KeyType.ARROW_DOWN;
            case ArrowLeft -> KeyType.ARROW_LEFT;
            case ArrowRight -> KeyType.ARROW_RIGHT;
            default -> KeyType.UNKNOWN;
        };
    }
}
