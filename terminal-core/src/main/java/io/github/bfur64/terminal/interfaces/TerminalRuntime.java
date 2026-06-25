package io.github.bfur64.terminal.interfaces;

import io.github.bfur64.terminal.Terminal;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TerminalRuntime extends AutoCloseable, TerminalEnvironment {
    /**
     * Returns the {@link Terminal} associated with this runtime.
     *
     * <p>The returned {@link Terminal} is owned by this runtime and remains valid until
     * the runtime is closed.</p>
     *
     * <p>The same instance is returned on every invocation. Callers are expected
     * to interact with the {@link Terminal} directly.</p>
     *
     * @return The {@link Terminal} managed by this runtime
     */
    Terminal terminal();
}
