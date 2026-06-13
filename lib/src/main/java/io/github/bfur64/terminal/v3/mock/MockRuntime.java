package io.github.bfur64.terminal.v3.mock;

import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.v3.pipeline.ImmediatePipeline;
import io.github.bfur64.terminal.v3.pipeline.Pipeline;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MockRuntime implements TerminalRuntime {
    private final Terminal terminal;

    public MockRuntime() {
        Pipeline pipeline = new ImmediatePipeline(new MockBackend());
        this.terminal = new Terminal(pipeline, new MockInputSource());
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() {}

    @Override
    public int xSize() {
        return 0;
    }

    @Override
    public int ySize() {
        return 0;
    }
}
