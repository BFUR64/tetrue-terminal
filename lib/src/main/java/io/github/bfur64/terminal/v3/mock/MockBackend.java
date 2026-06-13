package io.github.bfur64.terminal.v3.mock;

import io.github.bfur64.terminal.v3.interfaces.RendererBackend;
import io.github.bfur64.terminal.v3.commands.Command;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MockBackend implements RendererBackend {
    @Override
    public void execute(Command command) {}
}
