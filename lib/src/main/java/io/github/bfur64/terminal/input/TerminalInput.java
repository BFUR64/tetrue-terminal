package io.github.bfur64.terminal.input;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface TerminalInput {
    @NonNull KeyStroke readInput();

    @Nullable KeyStroke pollInput();
}