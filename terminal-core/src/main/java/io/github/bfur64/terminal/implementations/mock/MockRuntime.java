package io.github.bfur64.terminal.implementations.mock;

import io.github.bfur64.terminal.input.KeyEvent;
import io.github.bfur64.terminal.interfaces.RendererBackend;
import io.github.bfur64.terminal.render.*;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class MockRuntime implements TerminalRuntime, TerminalEnvironment {
    private final Terminal terminal;
    private final MockInputSource mockInputSource;
    private int xSize;
    private int ySize;

    public MockRuntime() {
        this.mockInputSource = new MockInputSource();

        RendererBackend rendererBackend = new MockBackend();

        this.terminal = new Terminal(this, new FrameBuilder(rendererBackend), this.mockInputSource);
    }

    @Override
    @SuppressFBWarnings("EI_EXPOSE_REP") // Terminal is intentionally exposed by contract
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

    public void addKeyStroke(@Nullable KeyEvent keyEvent) {
        mockInputSource.addKeyStroke(keyEvent);
    }

    public void addKeyStroke(@Nullable KeyEvent... keyEvents) {
        for (KeyEvent keyEvent : keyEvents) {
            mockInputSource.addKeyStroke(keyEvent);
        }
    }

    public void addKeyStroke(List<@Nullable KeyEvent> keyEvents) {
        for (KeyEvent keyEvent : keyEvents) {
            mockInputSource.addKeyStroke(keyEvent);
        }
    }
}
