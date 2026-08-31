package io.github.bfur64.terminal.implementations.lanterna;

import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.input.CharacterEvent;
import io.github.bfur64.terminal.input.InputEvent;
import io.github.bfur64.terminal.input.Key;
import io.github.bfur64.terminal.input.KeyEvent;
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
    public InputEvent read() {
        try {
            com.googlecode.lanterna.input.KeyStroke lanternaKeyStroke = terminal.readInput();

            if (lanternaKeyStroke.getKeyType() == com.googlecode.lanterna.input.KeyType.Character) {
                return new CharacterEvent(lanternaKeyStroke.getCharacter());
            }

            return new KeyEvent(getKeyType(lanternaKeyStroke));
        }
        catch (IOException ignored) {
            return new KeyEvent(Key.UNKNOWN);
        }
    }

    @Override
    public @Nullable InputEvent poll() {
        try {
            com.googlecode.lanterna.input.KeyStroke lanternaKeyStroke = terminal.pollInput();

            if (lanternaKeyStroke == null) {
                return null;
            }

            if (lanternaKeyStroke.getKeyType() == com.googlecode.lanterna.input.KeyType.Character) {
                return new CharacterEvent(lanternaKeyStroke.getCharacter());
            }

            return new KeyEvent(getKeyType(lanternaKeyStroke));
        }
        catch (IOException ignored) {
            return new KeyEvent(Key.UNKNOWN);
        }
    }

    private Key getKeyType(com.googlecode.lanterna.input.KeyStroke keyStroke) {
        return switch (keyStroke.getKeyType()) {
            case Escape -> Key.ESCAPE;
            case Backspace -> Key.BACKSPACE;
            case Enter -> Key.ENTER;
            case ArrowUp -> Key.ARROW_UP;
            case ArrowDown -> Key.ARROW_DOWN;
            case ArrowLeft -> Key.ARROW_LEFT;
            case ArrowRight -> Key.ARROW_RIGHT;
            case Home -> Key.HOME;
            case End -> Key.END;
            case PageUp -> Key.PAGE_UP;
            case PageDown -> Key.PAGE_DOWN;
            default -> Key.UNKNOWN;
        };
    }
}
