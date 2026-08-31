package io.github.bfur64.terminal.implementations.mock;

import io.github.bfur64.terminal.input.InputEvent;
import io.github.bfur64.terminal.input.Key;
import io.github.bfur64.terminal.input.KeyEvent;
import io.github.bfur64.terminal.interfaces.InputSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

@NullMarked
public final class MockInputSource implements InputSource {
    private final List<@Nullable KeyEvent> keyEvents = new LinkedList<>();

    @Override
    public InputEvent read() {
        if (keyEvents.isEmpty()) {
            return new KeyEvent(Key.UNKNOWN);
        }

        KeyEvent keyEvent = keyEvents.getFirst();
        keyEvents.removeFirst();

        return keyEvent != null ? keyEvent : new KeyEvent(Key.UNKNOWN);
    }

    @Override
    public @Nullable InputEvent poll() {
        if (keyEvents.isEmpty()) {
            return null;
        }

        KeyEvent keyEvent = keyEvents.getFirst();
        keyEvents.removeFirst();

        return keyEvent;
    }

    void addKeyStroke(@Nullable KeyEvent keyEvent) {
        keyEvents.add(keyEvent);
    }
}
