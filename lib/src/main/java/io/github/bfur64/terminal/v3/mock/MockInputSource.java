package io.github.bfur64.terminal.v3.mock;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.v3.interfaces.InputSource;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class MockInputSource implements InputSource {
    @Override
    public KeyStroke read() {
        return new KeyStroke(KeyType.UNKNOWN);
    }

    @Override
    public @Nullable KeyStroke poll() {
        return null;
    }
}
