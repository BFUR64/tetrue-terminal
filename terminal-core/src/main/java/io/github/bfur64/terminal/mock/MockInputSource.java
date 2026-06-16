package io.github.bfur64.terminal.mock;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.InputSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

@NullMarked
public final class MockInputSource implements InputSource {
    private final List<@Nullable KeyStroke> keyStrokes = new LinkedList<>();

    @Override
    public KeyStroke read() {
        if (keyStrokes.isEmpty()) {
            return new KeyStroke(KeyType.UNKNOWN);
        }

        KeyStroke keyStroke = keyStrokes.getFirst();
        keyStrokes.removeFirst();

        return keyStroke != null ? keyStroke : new KeyStroke(KeyType.UNKNOWN);
    }

    @Override
    public @Nullable KeyStroke poll() {
        if (keyStrokes.isEmpty()) {
            return null;
        }

        KeyStroke keyStroke = keyStrokes.getFirst();
        keyStrokes.removeFirst();

        return keyStroke;
    }

    void addKeyStroke(@Nullable KeyStroke keyStroke) {
        keyStrokes.add(keyStroke);
    }
}
