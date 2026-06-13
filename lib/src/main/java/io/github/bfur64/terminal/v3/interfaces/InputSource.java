package io.github.bfur64.terminal.v3.interfaces;

import io.github.bfur64.terminal.input.KeyStroke;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface InputSource {
    KeyStroke read();
    @Nullable KeyStroke poll();
}
