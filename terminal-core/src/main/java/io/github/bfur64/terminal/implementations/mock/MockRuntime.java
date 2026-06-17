package io.github.bfur64.terminal.implementations.mock;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.render.RenderType;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.render.BufferedMode;
import io.github.bfur64.terminal.render.ImmediateMode;
import io.github.bfur64.terminal.render.RenderStrategy;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class MockRuntime implements TerminalRuntime, TerminalEnvironment {
    private final Terminal terminal;
    private final MockInputSource mockInputSource;
    private int xSize;
    private int ySize;

    public MockRuntime(RenderType renderType) {
        RenderStrategy renderStrategy = renderType == RenderType.BUFFERED ?
            new BufferedMode(new MockBackend()) :
            new ImmediateMode(new MockBackend());

        this.mockInputSource = new MockInputSource();

        this.terminal = new Terminal(this, renderStrategy, this.mockInputSource);
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

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
    }

    public void addKeyStroke(@Nullable KeyStroke keyStroke) {
        mockInputSource.addKeyStroke(keyStroke);
    }

    public void addKeyStroke(@Nullable KeyStroke... keyStrokes) {
        for (KeyStroke keyStroke : keyStrokes) {
            mockInputSource.addKeyStroke(keyStroke);
        }
    }

    public void addKeyStroke(List<@Nullable KeyStroke> keyStrokes) {
        for (KeyStroke keyStroke : keyStrokes) {
            mockInputSource.addKeyStroke(keyStroke);
        }
    }
}
