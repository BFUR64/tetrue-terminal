package io.github.bfur64.terminal.lanterna;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.github.bfur64.terminal.Config;
import io.github.bfur64.terminal.pipeline.RenderType;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalEnvironment;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;
import io.github.bfur64.terminal.pipeline.BufferedMode;
import io.github.bfur64.terminal.pipeline.ImmediateMode;
import io.github.bfur64.terminal.pipeline.RenderStrategy;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;

@NullMarked
public final class LanternaRuntime implements TerminalRuntime, TerminalEnvironment {
    private static final int DEFAULT_X = 0;
    private static final int DEFAULT_Y = 0;

    private final Terminal terminal;
    private final com.googlecode.lanterna.terminal.Terminal lanternaTerminal;

    public LanternaRuntime(RenderType renderType) throws IOException {
        this.lanternaTerminal = new DefaultTerminalFactory().createTerminal();

        RenderStrategy renderStrategy = renderType == RenderType.BUFFERED ?
                new BufferedMode(new LanternaBackend(lanternaTerminal, lanternaTerminal.newTextGraphics())) :
                new ImmediateMode(new LanternaBackend(lanternaTerminal, lanternaTerminal.newTextGraphics()));

        this.terminal = new Terminal(this, renderStrategy, new LanternaInputSource(lanternaTerminal));
    }

    private void start() {
        try {
            lanternaTerminal.enterPrivateMode();
            lanternaTerminal.setCursorVisible(false);
        }
        catch (IOException ignored) {}
    }

    @Override
    public Terminal terminal() {
        return terminal;
    }

    @Override
    public void close() {
        try {
            lanternaTerminal.exitPrivateMode();
            lanternaTerminal.close();
        }
        catch (IOException ignored) {}
    }

    @Override
    public int xSize() {
        try {
            return lanternaTerminal.getTerminalSize().getColumns();
        }
        catch (IOException ignored) {
            return DEFAULT_X;
        }
    }

    @Override
    public int ySize() {
        try {
            return lanternaTerminal.getTerminalSize().getRows();
        }
        catch (IOException ignored) {
            return DEFAULT_Y;
        }
    }

    @Override
    public String terminalInfo() {
        return "Lanterna: " + Config.lanternaVersion;
    }
}
