package io.github.bfur64.terminal.v3.interfaces;

import io.github.bfur64.terminal.v3.Terminal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TerminalRuntime extends AutoCloseable, TerminalEnvironment {
    Terminal terminal();
}
