package io.github.bfur64.terminal.v3.lanterna;

import com.googlecode.lanterna.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.v3.interfaces.InputSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class LanternaInputSource implements InputSource {
    private final Terminal terminal;

    public LanternaInputSource(Terminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public KeyStroke read() {
        return terminal.readInput();
    }

    @Override
    public @Nullable KeyStroke poll() {
        return terminal.pollInput();
    }
}
