package io.github.bfur64.terminal.lanterna;

import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.InputHandler;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

class LanternaInputHandler implements InputHandler {
    private final Terminal terminal;

    public LanternaInputHandler(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public void start() {

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
            case Home -> KeyType.HOME;
            case End -> KeyType.END;
            default -> KeyType.UNKNOWN;
        };
    }

    @Override
    public void close() {

    }
}
