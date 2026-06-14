package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.Terminal;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TerminalRuntime extends AutoCloseable, TerminalEnvironment {
    Terminal terminal();
}
