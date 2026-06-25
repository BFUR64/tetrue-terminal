package io.github.bfur64.terminal.implementations.lanterna;

import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.InputSource;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;

@NullMarked
public final class LanternaInputSource implements InputSource {
    private final Terminal terminal;

    /**
     * Creates a {@link LanternaInputSource} that reads input from a given {@link Terminal}
     *
     * <p>This class does <b>not</b> own the {@link Terminal} and its lifecycle. The
     * {@link Terminal} is used to get input from the user. The caller, {@link LanternaRuntime},
     * is responsible for managing the terminal's lifecycle.</p>
     *
     * @param terminal The terminal to read and poll input from
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
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
