package io.github.bfur64.terminal.v3.lanterna;

import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.v3.interfaces.InputSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@NullMarked
public final class LanternaInputSource implements InputSource {
    private final Terminal terminal;

    public LanternaInputSource(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public KeyStroke read() {
        try {
            com.googlecode.lanterna.input.KeyStroke lanternaKeyStroke = terminal.readInput();

            if (lanternaKeyStroke.getKeyType() == com.googlecode.lanterna.input.KeyType.Character) {
                return new KeyStroke(lanternaKeyStroke.getCharacter());
            }

            return new KeyStroke(getKeyType(lanternaKeyStroke));
        }
        catch (IOException ignored) {
            return new KeyStroke(KeyType.UNKNOWN);
        }
    }

    @Override
    public @Nullable KeyStroke poll() {
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
        catch (IOException ignored) {
            return new KeyStroke(KeyType.UNKNOWN);
        }
    }

    private KeyType getKeyType(com.googlecode.lanterna.input.KeyStroke keyStroke) {
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
            case PageUp -> KeyType.PAGE_UP;
            case PageDown -> KeyType.PAGE_DOWN;
            default -> KeyType.UNKNOWN;
        };
    }
}
