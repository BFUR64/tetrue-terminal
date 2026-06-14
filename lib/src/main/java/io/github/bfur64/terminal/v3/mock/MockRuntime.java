package io.github.bfur64.terminal.v3.mock;

import io.github.bfur64.terminal.v3.pipeline.RenderType;
import io.github.bfur64.terminal.v3.Terminal;
import io.github.bfur64.terminal.v3.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.v3.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.v3.pipeline.BufferedMode;
import io.github.bfur64.terminal.v3.pipeline.ImmediateMode;
import io.github.bfur64.terminal.v3.pipeline.RenderStrategy;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MockRuntime implements TerminalRuntime, TerminalEnvironment {
    private static final int DEFAULT_X = 0;
    private static final int DEFAULT_Y = 0;

    private final Terminal terminal;
    private final int xSize;
    private final int ySize;

    public MockRuntime(RenderType renderType, int xSize, int ySize, boolean sizeOverride) {
        RenderStrategy renderStrategy = renderType == RenderType.BUFFERED ?
            new BufferedMode(new MockBackend()) :
            new ImmediateMode(new MockBackend());

        this.terminal = new Terminal(this, renderStrategy, new MockInputSource());

        int localXSize = DEFAULT_X;
        int localYSize = DEFAULT_Y;

        if (sizeOverride) {
            localXSize = xSize;
            localYSize = ySize;
        }

        this.xSize = localXSize;
        this.ySize = localYSize;
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() {}

    @Override
    public int xSize() {
        return xSize;
    }

    @Override
    public int ySize() {
        return ySize;
    }

    @Override
    public String terminalInfo() {
        return "Mock Terminal";
    }
}
