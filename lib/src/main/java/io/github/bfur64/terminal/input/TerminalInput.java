package io.github.bfur64.terminal.input;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Closeable;

public interface TerminalInput extends Closeable {
    @NonNull KeyStroke readInput();

    @Nullable KeyStroke pollInput();
}