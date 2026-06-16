package io.github.bfur64.terminal.mock;

import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.commands.Command;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MockBackend implements RendererBackend {
    @Override
    public void execute(Command command) {}
}
